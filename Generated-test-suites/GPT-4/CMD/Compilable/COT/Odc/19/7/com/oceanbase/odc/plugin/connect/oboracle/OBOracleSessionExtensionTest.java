package com.oceanbase.odc.plugin.connect.oboracle;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {
    OBOracleSessionExtension extensionsUnderTest;

    @Before
    public void setUp() {
        extensionsUnderTest = new OBOracleSessionExtension();
    }

    public Connection getMockedConnection() {
        return mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        // Test normal functionality
        Connection connection = getMockedConnection();
        extensionsUnderTest.killQuery(connection, "12345");

        // Test behavior when connection is null
        try {
            extensionsUnderTest.killQuery(null, "12345");
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            // Exception expected
        }
    }

    @Test
    public void testSwitchSchema() {
        // Test normal functionality
        Connection connection = getMockedConnection();
        try {
            extensionsUnderTest.switchSchema(connection, "NewSchema");
        } catch (SQLException e) {
            fail(e.getMessage());
        }

        // Test behavior when connection is null
        try {
            extensionsUnderTest.switchSchema(null, "NewSchema");
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            // Exception expected
        }
    }

    @Test
    public void testGetCurrentSchema() {
        // Test normal functionality
        Connection connection = getMockedConnection();
        String schema = extensionsUnderTest.getCurrentSchema(connection);
        assertNotNull(schema);

        // Test behavior when connection is null
        try {
            extensionsUnderTest.getCurrentSchema(null);
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            // Exception expected
        }
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = getMockedConnection();
        String connectionId = extensionsUnderTest.getConnectionId(connection);
        assertNotNull(connectionId);

        // Test behavior when connection is null
        try {
            extensionsUnderTest.getConnectionId(null);
            fail("Expected an exception to be thrown");
        } catch(Exception e) {
            // Exception expected
        }
    }
}