package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionFactory;
import org.pf4j.ExtensionManager;
import org.pf4j.util.ClasspathScanner;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.JdbcOperationsUtilMock;

@Extension
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuerySuccess() throws SQLException {
        // Given
        Connection connection = mock(Connection.class);
        String connectionId = "test_connection_id";
        JdbcOperations jdbcOperations = new JdbcOperationsUtilMock();
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension() {
            @Override
            JdbcOperations getJdbcOperations() {
                return jdbcOperations;
            }
        };

        when(connection.createStatement()).thenReturn(JdbcOperationsUtil.getStatement(jdbcOperations));
        when(jdbcOperations.execute(connection, "KILL QUERY " + connectionId)).thenReturn(true);

        // When
        extension.killQuery(connection, connectionId);

        // Then
        assertEquals(true, extension.isKillQuerySuccess());
    }

    @Test
    public void testKillQueryFailure() throws SQLException {
        // Given
        Connection connection = mock(Connection.class);
        String connectionId = "test_connection_id";
        JdbcOperations jdbcOperations = new JdbcOperationsUtilMock();
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension() {
            @Override
            JdbcOperations getJdbcOperations() {
                return jdbcOperations;
            }
        };

        when(connection.createStatement()).thenReturn(JdbcOperationsUtil.getStatement(jdbcOperations));
        when(jdbcOperations.execute(connection, "KILL QUERY " + connectionId)).thenThrow(SQLException.class);

        // When & Then
        assertThrows(SQLException.class, () -> extension.killQuery(connection, connectionId));
    }

    @Test
    public void testSwitchSchemaSuccess() throws Exception {
        // Given
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        JdbcOperations jdbcOperations = new JdbcOperationsUtilMock();
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension() {
            @Override
            JdbcOperations getJdbcOperations() {
                return jdbcOperations;
            }
        };

        when(connection.createStatement()).thenReturn(JdbcOperationsUtil.getStatement(jdbcOperations));
        when(connection.setCatalog(schemaName)).thenReturn(true);
        when(jdbcOperations.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(null);
        when(jdbcOperations.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(schemaName);

        // When
        extension.switchSchema(connection, schemaName);

        // Then
        assertEquals(schemaName, extension.getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchemaSameSchema() throws Exception {
        // Given
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        JdbcOperations jdbcOperations = new JdbcOperationsUtilMock();
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension() {
            @Override
            JdbcOperations getJdbcOperations() {
                return jdbcOperations;
            }
        };

        when(connection.createStatement()).thenReturn(JdbcOperationsUtil.getStatement(jdbcOperations));
        when(connection.setCatalog(schemaName)).thenReturn(false);
        when(jdbcOperations.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(schemaName);

        // When & Then
        extension.switchSchema(connection, schemaName);
        assertEquals(schemaName, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchemaSuccess() throws Exception {
        // Given
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        JdbcOperations jdbcOperations = new JdbcOperationsUtilMock();
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension() {
            @Override
            JdbcOperations getJdbcOperations() {
                return jdbcOperations;
            }
        };

        when(connection.createStatement()).thenReturn(JdbcOperationsUtil.getStatement(jdbcOperations));
        when(jdbcOperations.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(schemaName);

        // When
        String result = extension.getCurrentSchema(connection);

        // Then
        assertEquals(schemaName, result);
    }

    @Test
    public void testGetCurrentSchemaFailure() throws Exception {
        // Given
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = new JdbcOperationsUtilMock();
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension() {
            @Override
            JdbcOperations getJdbcOperations() {
                return jdbcOperations;
            }
        };

        when(connection.createStatement()).thenReturn(JdbcOperationsUtil.getStatement(jdbcOperations));
        when(jdbcOperations.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(null);

        // When & Then
        assertThrows(SQLException.class, () -> extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdSuccess() throws Exception {
        // Given
        Connection connection = mock(Connection.class);
        String connectionId = "test_connection_id";
        JdbcOperations jdbcOperations = new JdbcOperationsUtilMock();
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension() {
            @Override
            JdbcOperations getJdbcOperations() {
                return jdbcOperations;
            }
        };

        when(connection.createStatement()).thenReturn(JdbcOperationsUtil.getStatement(jdbcOperations));
        when(jdbcOperations.queryForObject(connection, "select connection_id()", String.class)).thenReturn(connectionId);

        // When
        String result = extension.getConnectionId(connection);

        // Then
        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionIdFailure() throws Exception {
        // Given
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = new JdbcOperationsUtilMock();
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension() {
            @Override
            JdbcOperations getJdbcOperations() {
                return jdbcOperations;
            }
        };

        when(connection.createStatement()).thenReturn(JdbcOperationsUtil.getStatement(jdbcOperations));
        when(jdbcOperations.queryForObject(connection, "select connection_id()", String.class)).thenReturn(null);

        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.isWrapperFor(OceanBaseConnection.class)).thenReturn(true);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(12345);

        // When
        String result = extension.getConnectionId(connection);

        // Then
        assertEquals("12345", result);
    }

    static class JdbcOperationsUtilMock implements JdbcOperations {
        @Override
        public void execute(Connection connection, String sql) throws SQLException {
            // do nothing
        }

        @Override
        public <T> T queryForObject(Connection connection, String sql, Class<T> clazz) throws SQLException {
            return null;
        }
    }

    static class OBMySQLSessionExtensionTestFactory implements ExtensionFactory<SessionExtensionPoint> {
        @Override
        public SessionExtensionPoint createExtension() {
            return new OBMySQLSessionExtension();
        }
    }

    @Test
    public void testRegisterExtension() throws Exception {
        // Given
        ExtensionManager extensionManager = mock(ExtensionManager.class);
        ClasspathScanner scanner = mock(ClasspathScanner.class);

        when(extensionManager.getExtensionManager()).thenReturn(extensionManager);
        when(extensionManager.addExtension(Mockito.any(Class.class), Mockito.any(ExtensionFactory.class)))
                .thenReturn(true);
        when(scanner.scanClasspath(Mockito.any(Class.class))).thenReturn(new OBMySQLSessionExtensionTestFactory());

        // When
        ExtensionFactory extensionFactory = new ExtensionFactory<SessionExtensionPoint>() {
            @Override
            public SessionExtensionPoint createExtension() {
                return new OBMySQLSessionExtension();
            }
        };
        extensionManager.scanClasspath(OBMySQLSessionExtension.class);

        // Then
        assertEquals(true, extensionManager.isExtensionLoaded(SessionExtensionPoint.class));
    }
}