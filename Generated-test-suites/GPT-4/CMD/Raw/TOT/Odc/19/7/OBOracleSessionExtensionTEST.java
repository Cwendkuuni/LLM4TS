
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import org.mockito.Mockito;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

public class OBOracleSessionExtensionTest {
  Connection mockConnection;
  OBOracleSessionExtension sessionExtension;
  
  @Before
  public void setUp() throws Exception {
    mockConnection = Mockito.mock(Connection.class);
    sessionExtension = new OBOracleSessionExtension();
  }

  @Test
  public void testKillQuery() {
    String connectionId = "con123";
    sessionExtension.killQuery(mockConnection, connectionId);
    verify(mockConnection).execute("KILL QUERY " + connectionId);
  }

  @Test(expected = SQLException.class)
  public void testSwitchSchema() throws SQLException {
    String currentSchema = "schema1";
    String newSchema = "schema2";
    doThrow(new SQLException()).when(mockConnection).setSchema(newSchema);
    sessionExtension.switchSchema(mockConnection, newSchema);
  }

  @Test
  public void testGetCurrentSchema() {
    String expectedSchema = "schema1";
    sessionExtension.getCurrentSchema(mockConnection);
    verify(mockConnection).getSchema(expectedSchema);
  }

  @Test
  public void testGetConnectionId() {
    String expectedId = "con123";
    sessionExtension.getConnectionId(mockConnection);
    verify(mockConnection).getMetaData();
  }

  @Test
  public void testGetConnectionIdWithException() throws Exception {
    String expectedId = "con123";
    doThrow(new SQLException()).when(mockConnection).getMetaData();
    sessionExtension.getConnectionId(mockConnection);
    verify(mockConnection).getMetaData();
  }
}
