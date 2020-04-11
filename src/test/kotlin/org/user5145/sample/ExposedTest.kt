package org.user5145.sample

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.Test

class BugTest {
    fun getDefaultDb(): Database {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:h2:mem:test;MODE=Postgresql;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE"
        config.username = ""
        config.password = ""
        config.driverClassName = "org.h2.Driver"
        return Database.connect(HikariDataSource(config))
    }

    object Champions : IdTable<String>() {
        override val id get() = username
        val username = varchar("name", 50).entityId()
        val pass = text("pass")
    }

    object ChampionsStats : IdTable<String>("champions_stats") {
        override val id get() = username
        val username = varchar("name", 50).entityId() references Champions.username
        val hp = text("hp")
    }

    @Test
    fun testCreate() {
        transaction(getDefaultDb()) {
            SchemaUtils.createMissingTablesAndColumns(
                Champions,
                ChampionsStats
            )
        }
    }
}