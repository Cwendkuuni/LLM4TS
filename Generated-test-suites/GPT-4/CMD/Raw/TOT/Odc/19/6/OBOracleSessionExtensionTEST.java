import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;

public class OBOracleSessionExtensionTest {
    OBOracleSessionExtension extension = new OBOracleSessionExtension();

    @Test
    public void testGetConnectionId() {
        Connection connection = Mockito.mock(Connection.class);
        String expected = "12345";
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenReturn(expected);
        String result = extension.getConnectionId(connection);
        
        assertEquals(expected, result);
    }
}