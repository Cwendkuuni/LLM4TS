package com.oceanbase.odc.service.session.factory;

import org.junit.Test;
import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

    // Test null or empty ConnectionConfig in constructor
    @Test(expected = IllegalArgumentException.class)
    public void testNullEmptyConnectionConfigConstructor() {
        new OBConsoleDataSourceFactory(null, true);
    }

    // Test negative port number in resetPort method
    @Test(expected = IllegalArgumentException.class)
    public void testNegativePortResetPort() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        factory.resetPort(value -> -1);
    }

    // Test null ValueMapper in reset methods
    @Test(expected = IllegalArgumentException.class)
    public void testNullValueMapperInReset() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        factory.resetUsername(null);
    }

    // Test empty defaultSchema in getDefaultSchema method
    @Test
    public void testEmptyDefaultSchemaInGetDefaultSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String defaultSchema = factory.getDefaultSchema();
        assertNull(defaultSchema);
    }

    // Additional test cases for other methods can be added here

}

