import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {
  
  private OBMySQLSessionExtension sessionExtension;
  private Connection mockConnection;

  @Before
  public void setUp() {
    sessionExtension = new OBMySQLSessionExtension();
    mockConnection = mock(Connection.class);
  }

  @Test
  public void testKillQuery() {
    // Test killQuery method
    // Mocking JdbcOperationsUtil
    JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
    when(jdbcOperationsUtil.execute(anyString())).thenReturn(true);
    // Mocking OceanBaseConnection
    OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
    when(mockConnection.setCatalog(anyString())).thenAnswer(invocation -> {
      assertEquals("Expected schema name", "test_schema", invocation.getArgument(0));
      return null;
    });

    // Testing killQuery
    sessionExtension.killQuery(mockConnection, "12345");

    verify(jdbcOperationsUtil).execute("KILL QUERY 12345");
  }

  @Test
  public void testSwitchSchema() throws SQLException {
    // Test switchSchema method
    // Mocking JdbcOperationsUtil
    JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
    when(jdbcOperationsUtil.queryForObject(anyString(), eq(String.class))).thenReturn("current_schema");

    // Testing switchSchema
    assertEquals("current_schema", sessionExtension.getCurrentSchema(mockConnection));
  }

  @Test
  public void testGetCurrentSchema() {
    // Test getCurrentSchema method
    // Mocking JdbcOperationsUtil
    JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
    when(jdbcOperationsUtil.queryForObject(anyString(), eq(String.class))).thenReturn("12345");

    // Testing getCurrentSchema
    assertEquals("12345", sessionExtension.getConnectionId(mockConnection));
  }

  @Test
  public void testGetConnectionId() {
    // Test getConnectionId method
    // Mocking OceanBaseConnection
    OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
    when(oceanBaseConnection.getServerThreadId()).thenReturn(67890);

    // Testing getConnectionId
    assertEquals("67890", sessionExtension.getConnectionId(mockConnection));
  }

}