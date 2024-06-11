package com.oceanbase.odc.service.session.factory;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {
  
  @Test
  public void test_GetUsername_Null() {
    ConnectionConfig conn = new ConnectionConfig();
    conn.setDialectType(DialectType.NULL);
    assertNull(OBConsoleDataSourceFactory.getUsername(conn));
  }

  // add more methods here representing all the different test cases

}