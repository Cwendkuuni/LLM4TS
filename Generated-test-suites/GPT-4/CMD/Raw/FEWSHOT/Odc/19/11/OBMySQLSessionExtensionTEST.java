package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.jdbc.OceanBaseConnection;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.killQuery(connection, "123");
        verify(connection).prepareStatement("KILL QUERY 123");
    }
    
    @Test
    public void testSwitchSameSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        when(connection.getCatalog()).thenReturn("schema1");
        sessionExtension.switchSchema(connection, "schema1");
        verify(connection, Mockito.times(0)).setCatalog(Mockito.anyString());
    }

    @Test
    public void testSwitchDifferentSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        when(connection.getCatalog()).thenReturn("schema1");
        sessionExtension.switchSchema(connection, "schema2");
        verify(connection).setCatalog("schema2");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        verify(connection).prepareStatement("SELECT DATABASE()");
        assertEquals("schema1", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String connectionId = sessionExtension.getConnectionId(connection);
        verify(connection).prepareStatement("select connection_id()");
        assertEquals("123", connectionId);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() {
        Connection connection = Mockito.mock(OceanBaseConnection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String connectionId = sessionExtension.getConnectionId(connection);
        verify(connection).prepareStatement("select connection_id()");
        assertEquals("123", connectionId);
    }
}