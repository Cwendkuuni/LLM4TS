
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    
    @Before
    public void setup() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithValidSessionAndSqlCheckReq() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        
        List<CheckResult> results = sqlCheckService.check(session, req);
        
        assertNotNull(results);
        assertEquals(0, results.size());
    }
    
    @Test
    public void testCheckWithInvalidSession() {
        ConnectionSession session = null;
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        
        List<CheckResult> results = sqlCheckService.check(session, req);
        
        assertNotNull(results);
        assertEquals(0, results.size());
    }
    
    @Test
    public void testCheckWithValidEnvironmentAndSqls() {
        Long environmentId = 1L;
        String databaseName = "test_database";
        List<OffsetString> sqls = new ArrayList<>();
        
        ConnectionConfig config = new ConnectionConfig();
        
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertNotNull(violations);
        assertEquals(0, violations.size());
    }
    
    @Test
    public void testCheckWithEmptySqls() {
        Long environmentId = 1L;
        String databaseName = "test_database";
        List<OffsetString> sqls = new ArrayList<>();
        
        ConnectionConfig config = new ConnectionConfig();
        
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertNotNull(violations);
        assertEquals(0, violations.size());
    }
}
