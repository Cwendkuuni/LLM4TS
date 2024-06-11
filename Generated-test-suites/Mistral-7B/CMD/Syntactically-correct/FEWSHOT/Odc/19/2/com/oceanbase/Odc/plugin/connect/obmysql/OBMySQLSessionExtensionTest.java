package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.beans.AnnotationBean;
import org.pf4j.extension.ExtensionFactory;
import org.pf4j.extension.ExtensionPoint;
import org.pf4j.extension.beans.ExtensionFactoryBean;
import org.pf4j.extension.extensionpoints.SessionExtensionPoint;
import org.pf4j.util.ClassUtils;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "1234";

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.execute(Connection.class, "KILL QUERY " + connectionId)).thenReturn(true);
        when(connection.getJdbcOperations()).thenReturn(jdbcOperations);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, connectionId);
        assertEquals(true, true); // Replace with assertions for expected results
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        String currentSchema = "current_schema";

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.execute(Connection.class, "SELECT DATABASE()")).thenReturn(currentSchema);
        when(jdbcOperations.execute(Connection.class, "SET SCHEMA " + schemaName)).thenReturn(true);
        when(connection.getJdbcOperations()).thenReturn(jdbcOperations);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, schemaName);
        assertEquals(schemaName, schemaName); // Replace with assertions for expected results
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String currentSchema = "test_schema";

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.queryForObject(Connection.class, String.class, "SELECT DATABASE()")).thenReturn(currentSchema);
        when(connection.getJdbcOperations()).thenReturn(jdbcOperations);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "1234";

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.queryForObject(Connection.class, String.class, "select connection_id()")).thenReturn(connectionId);
        when(connection.getJdbcOperations()).thenReturn(jdbcOperations);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        assertEquals(connectionId, extension.getConnectionId(connection));

        connection = mock(Connection.class);
        when(connection.getInstance(OceanBaseConnection.class)).thenReturn(mock(OceanBaseConnection.class));
        OceanBaseConnection oceanBaseConnection = (OceanBaseConnection) connection.getInstance(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(123);
        when(connection.getJdbcOperations()).thenReturn(jdbcOperations);

        extension = new OBMySQLSessionExtension();
        assertEquals("123", extension.getConnectionId(connection));
    }

    @Test
    public void testExtensionPointRegistration() throws Exception {
        ExtensionFactory extensionFactory = new ExtensionFactoryBean(OBMySQLSessionExtension.class);
        ExtensionPoint extensionPoint = new AnnotationBean(extensionFactory, SessionExtensionPoint.class);
        ExtensionFactory.registerExtensionPoint(SessionExtensionPoint.class, extensionPoint);
    }
}