package com.oceanbase.odc.service.session.factory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory testInstance;

    @Before
    public void setUp() {
        // Assuming a valid ConnectionConfig object
        ConnectionConfig connectionConfig = new ConnectionConfig();
        Boolean autoCommit = true;
        testInstance = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
    }

    @Test
    public void testgetJdbcUrl() {
        assertNotNull(this.testInstance.getJdbcUrl());
    }

    @Test
    public void testgetUsername() {
        assertNotNull(OBConsoleDataSourceFactory.getUsername(connectionConfig));
    }

    @Test
    public void testgetPassword() {
        assertNotNull(OBConsoleDataSourceFactory.getPassword(connectionConfig));
    }

    @Test
    public void testgetJdbcParams() {
        assertNotNull(OBConsoleDataSourceFactory.getJdbcParams(connectionConfig));
    }

    @Test
    public void testgetDataSource() {
        assertNotNull(this.testInstance.getDataSource());
    }

    @Test
    public void testgetDialectType() {
        assertNotNull(this.testInstance.getDialectType());
    }

    @Test
    public void testdeepCopy() {
        assertNotNull(this.testInstance.deepCopy());
    }

    @Test
    public void testresetUsername() {
        this.testInstance.resetUsername(value -> value);
        assertEquals(connectionConfig.getUsername(), this.testInstance.getUsername());
    }

    @Test
    public void testresetPassword() {
        this.testInstance.resetPassword(value -> value);
        assertEquals(connectionConfig.getPassword(), this.testInstance.getPassword());
    }

    // Continue with other test cases for reset methods...

    @Test(expected = IllegalArgumentException.class)
    public void testResetPortNegative() {
        this.testInstance.resetPort(value -> -1);
    }
}
