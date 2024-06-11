package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import java.sql.SQLException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class OBMySQLSessionExtensionTest {

  @Mock
  Connection connectionMock;

  OBMySQLSessionExtension obMySQLSessionExtension;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    obMySQLSessionExtension = new OBMySQLSessionExtension();
  }

  @Test
  public void testKillQuery() throws SQLException {
    obMySQLSessionExtension.killQuery(connectionMock, "1");
    Mockito.verify(connectionMock).execute("KILL QUERY 1");
  }

  @Test
  public void testSwitchSchema() throws SQLException {
    obMySQLSessionExtension.switchSchema(connectionMock, "newSchema");
    Mockito.verify(connectionMock).setCatalog("newSchema");
  }

  @Test
  public void testGetCurrentSchema() throws SQLException {
    obMySQLSessionExtension.getCurrentSchema(connectionMock);
    Mockito.verify(connectionMock).queryForObject("SELECT DATABASE()", String.class);
  }

  @Test
  public void testGetConnectionId() throws SQLException {
    obMySQLSessionExtension.getConnectionId(connectionMock);
    Mockito.verify(connectionMock).queryForObject("select connection_id()", String.class);
  }

}
