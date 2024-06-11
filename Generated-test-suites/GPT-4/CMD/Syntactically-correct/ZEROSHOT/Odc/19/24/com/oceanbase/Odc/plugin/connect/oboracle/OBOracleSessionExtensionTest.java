
package com.oceanbase.odc.plugin.connect.oboracle;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {
    @Test
    public void testKillQuery() {
        SessionExtensionPoint sep = new OBOracleSessionExtension();
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "123";

        try {
            sep.killQuery(connection, connectionId);
        } catch(Exception e) {
            Assert.fail("Should not have thrown any exception");
        }
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        SessionExtensionPoint sep = new OBOracleSessionExtension();

        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "mySchema";

        try {
            sep.switchSchema(connection, schemaName);
        } catch(Exception e) {
            Assert.fail("Should not have thrown any exception");
        }
    }

    @Test
    public void testGetCurrentSchema() {
        SessionExtensionPoint sep = new OBOracleSessionExtension();
        Connection connection = Mockito.mock(Connection.class);

        try {
            sep.getCurrentSchema(connection);
        } catch(Exception e) {
            Assert.fail("Should not have thrown any exception");
        }
    }

    @Test
    public void testGetConnectionId() {
        SessionExtensionPoint sep = new OBOracleSessionExtension();
        Connection connection = Mockito.mock(Connection.class);
        String expectedConnectionId = "123";

        try {
            String connectionId = sep.getConnectionId(connection);
            assert expectedConnectionId.equals(connectionId): "Connection ID mismatch";
        } catch(Exception e) {
            Assert.fail("Should not have thrown any exception");
        }
    }
}
