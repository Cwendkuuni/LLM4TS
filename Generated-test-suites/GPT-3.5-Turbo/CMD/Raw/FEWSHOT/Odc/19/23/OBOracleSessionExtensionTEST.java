package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    @Test
    public void testKillQuery() {
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.killQuery(connection, "123456");
        // Add assertion here if needed
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.switchSchema(connection, "schema_name");
        // Add assertion here if needed
    }

    @Test
    public void testGetCurrentSchema() {
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
        // Add further assertions here if needed
    }

    @Test
    public void testGetConnectionId() {
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        String connectionId = sessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
        // Add further assertions here if needed
    }
}