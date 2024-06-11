package com.oceanbase.odc.plugin.connect.oboracle;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Test;
import org.junit.Before;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

public class OBOracleSessionExtensionTest {
    private OBOracleSessionExtension extension;
    private Connection mockConnection;

    @Before
    public void setUp() {
        extension = new OBOracleSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryWithNullConnection() {
        extension.killQuery(null, "1001");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwitchSchemaWithNullSchemaName() {
        extension.switchSchema(mockConnection, null);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaSameAsCurrent() throws SQLException {
        when(mockConnection.getSchema()).thenReturn("testSchema");
        extension.switchSchema(mockConnection, "testSchema");
    }

    @Test
    public void testGetConnectionIdWithException() {
        String result = extension.getConnectionId(mockConnection);
        verify(mockConnection).getServerThreadId();
        assertEquals(result, "0");
    }

}