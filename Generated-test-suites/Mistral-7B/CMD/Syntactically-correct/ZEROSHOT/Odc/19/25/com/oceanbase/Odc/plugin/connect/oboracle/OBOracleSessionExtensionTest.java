package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private OBOracleSessionExtension extension;

    @Before
    public void setUp() throws Exception {
        Class.forName("com.oceanbase.jdbc.ObDriver");
        connection = DriverManager.getConnection("jdbc:oceanbase:ds1:1527/oceanbase:root:root@123456");
        extension = new OBOracleSessionExtension();
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testKillQuery() throws Exception {
        String connectionId = "123456";
        extension.killQuery(connection, connectionId);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        assertEquals("KILL QUERY " + connectionId, sqlCaptor.getValue());
        verifyQueriesExecuted(1);
    }

    @Test
    public void testKillQuery_NullConnectionId() {
        extension.killQuery(connection, null);
        verifyQueriesExecuted(0);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        extension.switchSchema(connection, schemaName);
        verifyQueriesExecuted(1);
        assertEquals(schemaName, getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchema_SameSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = schemaName;
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(StringUtils.quoteQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"), String.class))
                .thenReturn(currentSchema);
        extension.switchSchema(connection, schemaName);
        verifyQueriesExecuted(0);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_InvalidSchema() throws SQLException {
        String schemaName = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(StringUtils.quoteQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"), String.class))
                .thenThrow(new SQLException("Invalid schema"));
        extension.switchSchema(connection, schemaName);
        verifyQueriesExecuted(1);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String schemaName = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(StringUtils.quoteQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"), String.class))
                .thenReturn(schemaName);
        assertEquals(schemaName, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchema_NullConnection() throws SQLException {
        assertNull(extension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() throws Exception {
        String connectionId = "123456";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(StringUtils.quoteQuery("select userenv('sessionid') from dual"), String.class))
                .thenReturn(connectionId);
        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_NullConnection() {
        assertNull(extension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() throws Exception {
        String connectionId = "123456";
        OceanBaseConnection obConnection = mock(OceanBaseConnection.class);
        when(obConnection.getServerThreadId()).thenReturn(123L);
        when(obConnection.getConnection()).thenReturn(connection);
        assertEquals(connectionId, extension.getConnectionId(obConnection));
    }

    private void verifyQueriesExecuted(int expectedQueries) {
        Optional<Throwable> thrown = Optional.empty();
        try {
            extension.killQuery(connection, "test_connection_id");
        } catch (Throwable e) {
            thrown = Optional.of(e);
        }
        if (expectedQueries > 0) {
            assertEquals(expectedQueries, JdbcOperationsUtil.getQueriesCount(connection));
        }
        if (thrown.isPresent()) {
            thrown.ifPresent(Throwable::printStackTrace);
            throw new RuntimeException("Unexpected exception", thrown.get());
        }
    }
}