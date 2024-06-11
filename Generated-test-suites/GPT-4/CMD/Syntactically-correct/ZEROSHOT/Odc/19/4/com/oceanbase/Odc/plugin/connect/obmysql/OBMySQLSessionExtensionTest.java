package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    OceanBaseConnection oceanBaseConnection;

    @InjectMocks
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() throws SQLException {
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1234L);
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(connection, "100");
        verify(connection).execute("KILL QUERY 100");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("testSchema");
        obMySQLSessionExtension.switchSchema(connection, "testSchema");
        verify(connection, never()).setCatalog(anyString());
        obMySQLSessionExtension.switchSchema(connection, "newSchema");
        verify(connection).setCatalog("newSchema");
    }

    @Test
    public void testGetCurrentSchema() {
        obMySQLSessionExtension.getCurrentSchema(connection);
        verify(connection).queryForObject("SELECT DATABASE()", String.class);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(connection.queryForObject(anyString(), eq(String.class))).thenThrow(SQLException.class);
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        verify(connection).queryForObject("select connection_id()", String.class);
        verify(oceanBaseConnection).getServerThreadId();
    }

    @Test
    public void testGetConnectionId_NoException() {
        when(connection.queryForObject(anyString(), eq(String.class))).thenReturn("100");
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        verify(connection).queryForObject("select connection_id()", String.class);
    }
}
