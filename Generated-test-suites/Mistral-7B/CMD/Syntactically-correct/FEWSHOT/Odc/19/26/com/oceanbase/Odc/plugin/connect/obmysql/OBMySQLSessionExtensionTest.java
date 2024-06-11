package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.impl.BaseExtension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.jdbc.mock.MockConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    private SessionExtensionPoint obMySQLSessionExtension;
    private OceanBaseConnection mockConnection;

    @Before
    public void init() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        mockConnection = new MockConnection();
        mockConnection.setJdbcOperationsUtil(new JdbcOperationsUtil());
    }

    @Test
    public void testKillQuery() throws SQLException {
        obMySQLSessionExtension.killQuery(mockConnection, "connectionId");
        assertEquals("KILL QUERY connectionId", mockConnection.getLastExecutedStatement());
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obMySQLSessionExtension.switchSchema(mockConnection, "testSchema");
        assertEquals("testSchema", mockConnection.getCatalog());
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String currentSchema = mockConnection.getCatalog();
        obMySQLSessionExtension.switchSchema(mockConnection, currentSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        obMySQLSessionExtension.switchSchema(mockConnection, "testSchema");
        assertEquals("testSchema", obMySQLSessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() {
        assertEquals("connectionId", obMySQLSessionExtension.getConnectionId(mockConnection));
        mockConnection.setJdbcOperationsUtil(null);
        assertNull(obMySQLSessionExtension.getConnectionId(mockConnection));
        mockConnection.setJdbcOperationsUtil(new JdbcOperationsUtil());
        if (mockConnection instanceof OceanBaseConnection) {
            assertEquals(((OceanBaseConnection) mockConnection).getServerThreadId() + "", obMySQLSessionExtension.getConnectionId(mockConnection));
        }
    }
}

@Test
public class OBMySQLSessionExtensionIntegrationTest {

    @Test
    public void testSessionExtensionPoint() throws Exception {
        Connection connection = new MockConnection();
        SessionExtensionPoint sessionExtensionPoint = (SessionExtensionPoint) com.pf4j.ExtensionLoader.getExtension(OBMySQLSessionExtension.class);
        sessionExtensionPoint.killQuery(connection, "connectionId");
        sessionExtensionPoint.switchSchema(connection, "testSchema");
        assertEquals("testSchema", sessionExtensionPoint.getCurrentSchema(connection));
        assertEquals("connectionId", sessionExtensionPoint.getConnectionId(connection));
    }
}