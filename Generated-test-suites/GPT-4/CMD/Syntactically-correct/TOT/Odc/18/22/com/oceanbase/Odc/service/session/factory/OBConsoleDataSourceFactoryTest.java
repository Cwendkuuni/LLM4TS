package com.oceanbase.odc.service.session.factory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {
    private ConnectionConfig connectionConfig;
    private Boolean autoCommit = true;
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        this.connectionConfig = new ConnectionConfig();
        // set up connectionConfig details as required
        this.connectionConfig.setHost("localhost");
        this.connectionConfig.setPort(8080);
        this.connectionConfig.setUsername("test");
        this.connectionConfig.setPassword("test");
        this.autoCommit = false;
        this.obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
    }

    @Test
    public void getJdbcUrlTest() {
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:8080/test", jdbcUrl);
    }

    @Test
    public void getDataSourceTest() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void getDialectTypeTest() {
        DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
        assertEquals(DialectType.MYSQL, dialectType);
    }

    // Similarly write test cases for other methods

    @Test(expected = IllegalArgumentException.class)
    public void resetPortTest_Exception() {
        obConsoleDataSourceFactory.resetPort(value -> -1);
    }
}