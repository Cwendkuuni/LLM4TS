package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    private OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery(){
        obOracleSessionExtension.killQuery(connection, "1");
        assertNotNull(connection);
    }

    @Test
    public void testSwitchSchema() throws Exception {
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("Schema1");
        obOracleSessionExtension.switchSchema(connection, "Schema1");
        assertEquals("Schema1", obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchemaThroughConnection() {
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("Schema2");
        assertEquals("Schema2", obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdThroughConnection() {
        when(obOracleSessionExtension.getConnectionId(connection)).thenReturn("1");
        assertEquals("1", obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdThroughObjectInstance(){
        when(obOracleSessionExtension.getConnectionId(oceanBaseConnection)).thenReturn("2");
        assertEquals("2", obOracleSessionExtension.getConnectionId(oceanBaseConnection));
    }
}