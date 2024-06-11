package com.oceanbase.odc.plugin.connect.obmysql;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;

public class OBMySQLSessionExtensionTest {

  OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

  @Test
  public void testKillQuery() {
    Connection connection = mock(Connection.class);
    String connectionId = "12345";
    obMySQLSessionExtension.killQuery(connection, connectionId);
    // Add assertions here
  }

  @Test
  public void testSwitchSchema() throws SQLException {
    Connection connection = mock(Connection.class);
    String schemaName = "testSchema";
    // Add mock behavior for getCurrentSchema
    when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("oldSchema");
    obMySQLSessionExtension.switchSchema(connection, schemaName);
    // Add assertions here
  }

  @Test
  public void testGetCurrentSchema() {
    Connection connection = mock(Connection.class);
    // Add mock behavior for queryForObject
    // when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("testSchema");
    String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
    // Add assertions here
  }

  @Test
  public void testGetConnectionId() {
    Connection connection = mock(Connection.class);
    // Add mock behavior for getServerThreadId
    // when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(54321);
    String connectionId = obMySQLSessionExtension.getConnectionId(connection);
    // Add assertions here
  }
}