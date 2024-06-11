package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;

import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.Test;

public class SingleConnectionDataSourceTest {
  
  @Test
  public void testAddInitializer() {
    SingleConnectionDataSource ds = new SingleConnectionDataSource();
    ds.addInitializer(connection -> connection.setAutoCommit(false));
    assertNotNull(ds.initializerList);
    assertFalse(ds.initializerList.isEmpty());
  }
  
  @Test
  public void testGetterAndSetter() {
    SingleConnectionDataSource ds = new SingleConnectionDataSource();

    ds.setAutoCommit(true);
    assertTrue(ds.getAutoCommit());

    ds.setTimeOutMillis(5000);
    assertEquals(5000, ds.getTimeOutMillis());

    ds.setUsername("foo");
    assertEquals("foo", ds.getUsername());

    ds.setPassword("bar");
    assertEquals("bar", ds.getPassword());
  }
  
  @Test
  public void testGetConnection() throws Exception {
    SingleConnectionDataSource ds = new SingleConnectionDataSource();
    ds.setUsername("foo");
    ds.setPassword("bar");
  
    Connection connection = ds.getConnection();
    assertNotNull(connection);
    assertFalse(connection.isClosed());
  }

  @Test(expected = SQLException.class)
  public void testGetConnectionInvalidCredentials() throws Exception {
    SingleConnectionDataSource ds = new SingleConnectionDataSource();
    ds.setUsername("foo");
    ds.setPassword("bar");

    ds.getConnection("baz", "qux");
  }

  @Test
  public void testClose() throws Exception {
    SingleConnectionDataSource ds = new SingleConnectionDataSource();
    ds.setUsername("foo");
    ds.setPassword("bar");

    Connection connection = ds.getConnection();
    assertNotNull(connection);

    ds.close();

    assertTrue(ds.connection.isClosed());
  }
  
  @Test(expected = ConflictException.class)
  public void testGetConnectionProxy_Timeout() throws Exception {
    SingleConnectionDataSource ds = new SingleConnectionDataSource();
    ds.setTimeOutMillis(500);

    Connection connection = ds.getConnection();
    Connection anotherConnection = ds.getConnection();
  }

  @Test(expected = SQLException.class)
  public void testResetConnection() throws Exception {
    SingleConnectionDataSource ds = new SingleConnectionDataSource(); 
    ds.resetConnection();
  }
}