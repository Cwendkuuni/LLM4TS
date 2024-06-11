package com.oceanbase.odc.plugin.connect.obmysql;
import org.junit.*;
import org.mockito.*;
import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {
  
    @Mock
    private Connection connection;
  
    private OBMySQLSessionExtension obMySqlSessionExtension;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obMySqlSessionExtension = new OBMySQLSessionExtension();
    }
  
    @Test
    public void testKillQuery() {
      obMySqlSessionExtension.killQuery(connection, "12345");
    }
  
    @Test
    public void testSwitchSchema() throws SQLException {
        obMySqlSessionExtension.switchSchema(connection, "testSchema");
    }
  
    @Test
    public void testGetCurrentSchema() {
        obMySqlSessionExtension.getCurrentSchema(connection);
    }
  
    @Test
    public void testGetConnectionId() {
        obMySqlSessionExtension.getConnectionId(connection);
    }    

    //test case for switchSchema when the current schema is the same as the new one 
    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        Mockito.when(connection.getCatalog()).thenReturn("testSchema");
        obMySqlSessionExtension.switchSchema(connection, "testSchema");
    }

    //test case for getConnectionId when exception is thrown 
    @Test
    public void testGetConnectionIdException() {
        Connection oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
        Mockito.when(oceanBaseConnection.getServerThreadId()).thenReturn(12345L);
        obMySqlSessionExtension.getConnectionId(oceanBaseConnection);
    }
}