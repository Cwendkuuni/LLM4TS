import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {
    
    @Mock
    private Connection connection;
    
    private JdbcTemplate jdbcTemplate;
    
    private OBOracleSessionExtension obOracleSessionExtension;
    
    @Before
    public void setup() {
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }
    
    @Test
    public void testKillQuery() {
        String connectionId = "123";
        
        obOracleSessionExtension.killQuery(connection, connectionId);
        
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "current_schema";
        
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
        
        obOracleSessionExtension.switchSchema(connection, schemaName);
        
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }
    
    @Test
    public void testGetCurrentSchema() {
        String expectedResult = "test_schema";
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(expectedResult);
        
        String result = obOracleSessionExtension.getCurrentSchema(connection);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testGetConnectionId_success() {
        String sessionid = "332";
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(sessionid);
        
        String result = obOracleSessionExtension.getConnectionId(connection);
        
        assertEquals(sessionid, result);
    }
    
    @Test
    public void testGetConnectionId_exception() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        mockOceanBaseConnection();
        
        String result = obOracleSessionExtension.getConnectionId(connection);
        
        assertNotNull(result);
    }
    
    private void mockOceanBaseConnection() {
        OceanBaseConnection oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(123L);
    }
}