package com.oceanbase.odc.plugin.connect.oboracle;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void killQueryTest() {
        // Setup
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);

        // Exercise
        obOracleSessionExtension.killQuery(connection, "12345");

        // Verify
        verify(jdbcOperations).execute("KILL QUERY 12345");
    }

    @Test
    public void switchSchemaTest() throws SQLException {
        // Setup
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(jdbcOperations.update(anyString())).thenReturn(1);

        // Exercise
        obOracleSessionExtension.switchSchema(connection, "test_schema");

        // Verify
        verify(jdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA='test_schema'");
    }

    @Test
    public void getCurrentSchemaTest() {
        // Setup
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(anyString(), eq(String.class))).thenReturn("test_schema");

        // Exercise
        String result = obOracleSessionExtension.getCurrentSchema(connection);

        // Verify
        assertEquals("test_schema", result);
    }

    @Test
    public void getConnectionIdTest() {
        // Setup
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(anyString(), eq(String.class))).thenReturn("12345");

        // Exercise
        String result = obOracleSessionExtension.getConnectionId(connection);

        // Verify
        assertEquals("12345", result);
    }

    @Test
    public void getConnectionIdWhenExceptionTest() {
        // This will test the case when an exception is thrown
        // and the Connection is an instance of OceanBaseConnection

        // Setup
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(JdbcOperationsUtil.getJdbcOperations(oceanBaseConnection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(54321L);

        // Exercise
        String result = obOracleSessionExtension.getConnectionId(oceanBaseConnection);

        // Verify
        assertEquals("54321", result);
    }
}