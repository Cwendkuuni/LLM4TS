package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionPoint;
import org.pf4j.impl.DefaultExtensionPointRegistry;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.JdbcOperationsUtil;

/**
 * Test class for {@link OBMySQLSessionExtension}
 *
 * @author yaobin
 */
public class OBMySQLSessionExtensionTest {

    private static final String SCHEMA_NAME = "testSchema";
    private static final String CONNECTION_ID = "12345678";

    private Connection connection;
    private JdbcOperations jdbcOperations;
    private Statement statement;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() throws Exception {
        ExtensionManager extensionManager = new DefaultExtensionPointRegistry();
        extensionManager.startup();

        jdbcOperations = mock(JdbcOperations.class);
        connection = mock(Connection.class);
        statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        when(jdbcOperations.execute(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        obMySQLSessionExtension = new OBMySQLSessionExtension();
        ExtensionPoint<?> extensionPoint = new DefaultExtensionPoint<SessionExtensionPoint>(obMySQLSessionExtension);
        extensionManager.addExtensionPoint(SessionExtensionPoint.class, extensionPoint);

        when(connection.getCatalog()).thenReturn(null);
        when(connection.getCatalog()).thenAnswer(invocation -> SCHEMA_NAME);
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(1000)).thenReturn(true);
        when(connection.isClosed()).thenAnswer(invocation -> false);

        when(jdbcOperations.queryForObject(anyString(), eq(String.class)))
                .thenAnswer(invocation -> invocation.getArgument(0).equals("testSchema") ? SCHEMA_NAME : CONNECTION_ID);
    }

    @After
    public void tearDown() throws Exception {
        extensionManager.shutdown();
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, SCHEMA_NAME);
        assertEquals(SCHEMA_NAME, connection.getCatalog());
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaToSameSchema() throws SQLException {
        obMySQLSessionExtension.switchSchema(connection, SCHEMA_NAME);
        obMySQLSessionExtension.switchSchema(connection, SCHEMA_NAME);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(SCHEMA_NAME, connection.getCatalog());
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(CONNECTION_ID, connection.getConnectionId());
    }

    @Test
    public void testKillQuery() throws SQLException {
        when(jdbcOperations.execute(eq("KILL QUERY " + CONNECTION_ID)))
                .thenReturn("Query killed successfully.");

        obMySQLSessionExtension.killQuery(connection, CONNECTION_ID);
    }
}