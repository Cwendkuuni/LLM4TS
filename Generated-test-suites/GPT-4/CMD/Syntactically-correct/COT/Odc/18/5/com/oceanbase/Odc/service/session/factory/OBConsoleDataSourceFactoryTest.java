package com.oceanbase.odc.service.session.factory;

import org.junit.Assert;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    // Initialize ConnectionConfig to be used for the tests
    // .. 

    @Test
    public void testGetJdbcUrl_NotNull() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        Assert.assertNotNull(factory.getJdbcUrl());
    }

    // Similarly, generate tests for other public methods

    // Test potential edge cases
    @Test(expected = NullPointerException.class)
    public void testGetUsername_NullConfig() {
        OBConsoleDataSourceFactory.getUsername(null);
    }

    // Similarly, generate tests for other methods and edge cases
}

