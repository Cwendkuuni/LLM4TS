package com.oceanbase.odc.core.datasource;

import org.jmock.Mockery;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Connection;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest{

  // Use 'mockery' to manage all mocks.
  private Mockery mockery = new ContextedMockery();
 
  // Simulates connection
  private Connection sqlOpenConnection = mockery.mock(Connection.class);
  private Connection sqlClosedConnection = mockery.mock(Connection.class);
   
  // Valid and Invalid entries
  public static final String VALID_USERNAME = "validUser";
  public static final String VALID_PASSWORD = "validPass";
  public static final String INVALID_USERNAME = "invalidUser";
  public static final String INVALID_PASSWORD = "invalidPass";

  // Initialize our SingleConnectionDataSource we will be testing on.
  private SingleConnectionDataSource testObj = new SingleConnectionDataSource();

  @Test
  public void testGetConnectionWhenConnectionIsNull() throws SQLException {
      testObj.setConnection(null);
      assertNotNull(testObj.getConnection());
  }

  @Test
  public void testGetConnectionWhenConnectionIsClosed() throws SQLException {
      testObj.setConnection(sqlClosedConnection);
      assertNotNull(testObj.getConnection());
  }

  @Test
  public void testGetConnectionWhenConnectionIsValid() throws SQLException {
      testObj.setConnection(sqlOpenConnection);
      assertEquals(sqlOpenConnection, testObj.getConnection());
  }

  @Test
  public void testGetConnectionWithUsernameAndPasswordWhenCredentialsAreValid() throws SQLException {
      assertEquals(sqlOpenConnection, testObj.getConnection(VALID_USERNAME, VALID_PASSWORD));
  }

  @Test(expected = SQLException.class)
  public void testGetConnectionWithUsernameAndPasswordWhenCredentialsAreInvalid() throws SQLException {
      testObj.getConnection(INVALID_USERNAME, INVALID_PASSWORD);
  }

  @Test(expected = SQLException.class)
  public void testGetConnectionWithUsernameAndPasswordWhenUsernameIsValidAndPasswordIsInvalid() throws SQLException {
      testObj.getConnection(VALID_USERNAME, INVALID_PASSWORD);
  }

  @Test
  public void testResetConnection() throws SQLException {
      sqlOpenConnection.setAutoCommit(false);
      testObj.setConnection(sqlOpenConnection);
      testObj.resetConnection();
      assertTrue(testObj.getConnection().getAutoCommit());
  }

  @Test
  public void testResetConnectionWhenConnectionIsNull() throws SQLException {
      testObj.setConnection(null);
      testObj.resetConnection();
      assertNotNull(testObj.getConnection());
  }

  @Test
  public void testResetConnectionWhenConnectionIsClosed() throws SQLException {
      testObj.setConnection(sqlClosedConnection);
      testObj.resetConnection();
      assertNotEquals(sqlClosedConnection, testObj.getConnection());
  }

  @Test
  public void testAddInitializer() {
      ConnectionInitializer initializer = mock(ConnectionInitializer.class);
      testObj.addInitializer(initializer);
      assertArrayEquals(new ConnectionInitializer[]{initializer}, testObj.getInitializerList().toArray());
  }

  @Test(expected = NullPointerException.class)
  public void testAddInitializerWithNull() {
      testObj.addInitializer(null);
  }

  @Test
  public void testAddInitializerWhenAlreadyExists() {
      ConnectionInitializer initializer = mock(ConnectionInitializer.class);
      testObj.addInitializer(initializer);
      testObj.addInitializer(initializer);
      assertArrayEquals(new ConnectionInitializer[]{initializer, initializer}, testObj.getInitializerList().toArray());
  }

  @Test
  public void testCloseWhenConnectionIsNull() throws SQLException {
      testObj.close();
      assertNull(testObj.getConnection());
  }

  @Test
  public void testCloseWhenConnectionIsNotNull() throws SQLException {
      testObj.setConnection(sqlOpenConnection);
      testObj.close();
      assertTrue(testObj.getConnection().isClosed());
  }

  @Test
  public void testCloseWhenConnectionIsAlreadyClosed() throws SQLException {
      testObj.setConnection(sqlClosedConnection);
      testObj.close();
      assertTrue(testObj.getConnection().isClosed());
  }
}
