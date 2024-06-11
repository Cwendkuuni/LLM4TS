import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;

import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {
    private OBMySQLSessionExtension extension;
    private Connection connection;

    @Before
    public void setUp() {
        extension = new OBMySQLSessionExtension();
        connection = Mockito.mock(Connection.class);
    }

    @Test
    public void killQueryTest() {
        String connectionId = "1";
        extension.killQuery(connection, connectionId);
        
        // As the method is a void type and does not return anything, 
        // A typical test case will include verifying that the execute method was called with the right query.
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void switchSchemaTest() throws Exception {
        String schemaName = "newSchema";
        when(extension.getCurrentSchema(connection)).thenReturn("oldSchema");

        extension.switchSchema(connection, schemaName);
        
        // Verify whether the setCatalog method was called with the right schemaName.
        verify(connection).setCatalog(schemaName);
    }

    @Test
    public void getCurrentSchemaTest() {
        String querySql = "SELECT DATABASE()";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenReturn("oldSchema");

        Assert.assertEquals("oldSchema", extension.getCurrentSchema(connection));
    }

    @Test
    public void getConnectionIdWhenErrorThrownTest() {
        String querySql = "select connection_id()";

        // Simulate a situation when an exception is thrown. 
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenThrow(new RuntimeException());

        try {
            extension.getConnectionId(connection);
            Assert.fail("Expected an Exception to be thrown");
        } catch (Exception e) {
            Assert.assertEquals(RuntimeException.class, e.getClass());
        }
    }

    @Test
    public void getConnectionIdTest() {
        String querySql = "select connection_id()";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenReturn("1");
        
        Assert.assertEquals("1", extension.getConnectionId(connection));
    }
}