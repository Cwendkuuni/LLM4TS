import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
        obmysqlSessionExtension.killQuery(connection, "123456");
        
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY 123456");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
        String currentSchema = "schema1";
        String newSchema = "schema2";
        
        obmysqlSessionExtension.switchSchema(connection, newSchema);
        assertEquals(newSchema, connection.getCatalog());
        
        obmysqlSessionExtension.switchSchema(connection, currentSchema);
        assertEquals(currentSchema, connection.getCatalog());
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
        
        String mockSchema = "schema1";
        JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class);
        assertEquals(mockSchema, obmysqlSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
        
        String mockConnectionId = "123456";
        JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class);
        assertEquals(mockConnectionId, obmysqlSessionExtension.getConnectionId(connection));
    }
}