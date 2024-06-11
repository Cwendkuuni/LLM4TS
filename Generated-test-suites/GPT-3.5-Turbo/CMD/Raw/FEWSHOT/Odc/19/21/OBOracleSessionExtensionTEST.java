package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension sessionExtension;
    private Connection mockConnection;

    @Before
    public void setUp() {
        sessionExtension = new OBOracleSessionExtension();
        mockConnection = mock(OceanBaseConnection.class);
    }

    @Test
    public void testKillQuery() {
        doReturn(JdbcOperationsUtil.getJdbcOperations(mockConnection)).when(mockConnection).getConnection();
        sessionExtension.killQuery(mockConnection, "12345");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        doReturn(JdbcOperationsUtil.getJdbcOperations(mockConnection)).when(mockConnection).getConnection();
        sessionExtension.switchSchema(mockConnection, "schema_name");
    }

    @Test
    public void testGetCurrentSchema() {
        doReturn(JdbcOperationsUtil.getJdbcOperations(mockConnection)).when(mockConnection).getConnection();
        String currentSchema = sessionExtension.getCurrentSchema(mockConnection);
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = sessionExtension.getConnectionId(mockConnection);
        assertNull(connectionId);
    }
}