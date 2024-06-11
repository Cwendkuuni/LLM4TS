package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import javax.sql.DataSource;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testuser");
        connectionConfig.setPassword("testpass");
        connectionConfig.setDefaultSchema("testdb");

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        DataSource dataSource = dataSourceFactory.getDataSource();

        assertNotNull(dataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testuser");
        connectionConfig.setPassword("testpass");
        connectionConfig.setDefaultSchema("testdb");

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        String jdbcUrl = dataSourceFactory.getJdbcUrl();

        assertEquals("jdbc:mysql://localhost:3306/testdb", jdbcUrl);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        DialectType dialectType = dataSourceFactory.getDialectType();

        assertEquals(DialectType.MYSQL, dialectType);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testuser");
        connectionConfig.setPassword("testpass");
        connectionConfig.setDefaultSchema("testdb");

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        CloneableDataSourceFactory clone = dataSourceFactory.deepCopy();

        assertNotNull(clone);
    }

}