package com.oceanbase.odc.plugin.connect.obmysql;

import com.oceanbase.jdbc.OceanBaseConnection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    private OBMySQLSessionExtension mySqlSessionExtension;

    private static final String SCHEMA_NAME = "test_schema";

    @Before
    public void setUp() throws Exception {
        this.mySqlSessionExtension = new OBMySQLSessionExtension();
        this.connection = Mockito.mock(Connection.class);
        this.oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
    }

    @Test
    public void testKillQuery() {
        mySqlSessionExtension.killQuery(connection, "1234");
    }

    @Test(expected = SQLException.class)
    public void switchSchema_When_SchemaIsDifferentFromCurrent_SetsNewSchema() throws SQLException {
        when(connection.getCatalog()).thenReturn("test_catalog");

        mySqlSessionExtension.switchSchema(connection, SCHEMA_NAME);

        assertEquals(SCHEMA_NAME, connection.getCatalog());
    }

    @Test
    public void getCurrentSchema_Should_GetSchema() {
        String expectedSchema = "test_db";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(expectedSchema);

        String actualSchema = mySqlSessionExtension.getCurrentSchema(connection);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void getConnectionId_When_ErrorOccurs_IfInstanceOfOceanBaseConnection_Should_ReturnServerThreadId() {
        String expectedId = "1";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenThrow(new RuntimeException());

        when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(expectedId));

        String actualId = mySqlSessionExtension.getConnectionId(oceanBaseConnection);

        assertEquals(expectedId, actualId);
    }
}