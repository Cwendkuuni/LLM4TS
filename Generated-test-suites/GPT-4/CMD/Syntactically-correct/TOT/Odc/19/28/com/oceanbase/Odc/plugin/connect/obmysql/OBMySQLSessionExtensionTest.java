package com.oceanbase.odc.plugin.connect.obmysql;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private JdbcOperationsUtil mockJdbcOperationsUtil;

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(mockConnection, "anyConnectionId");
        verify(mockJdbcOperationsUtil).execute("KILL QUERY " + "anyConnectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(mockConnection))
                .thenReturn("oldSchema");
        obMySQLSessionExtension.switchSchema(mockConnection, "newSchema");
        verify(mockConnection).setCatalog("newSchema");
    }

    @Test
    public void testGetCurrentSchema() {
        obMySQLSessionExtension.getCurrentSchema(mockConnection);
        verify(mockJdbcOperationsUtil).queryForObject("SELECT DATABASE()", String.class);
    }

    @Test
    public void testGetConnectionId() {
        obMySQLSessionExtension.getConnectionId(mockConnection);
        verify(mockJdbcOperationsUtil).queryForObject("select connection_id()", String.class);
    }
}