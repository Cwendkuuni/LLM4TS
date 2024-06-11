import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.core.session.ConnectionSession;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private ConnectionSession mockSession;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        mockSession = mock(ConnectionSession.class);
    }

    @Test
    public void testCheckWithValidRequest() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table;");
        
        when(mockSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(mockSession.getRuleSetId()).thenReturn(1L);
        
        List<CheckResult> results = sqlCheckService.check(mockSession, req);
        
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    public void testCheckWithEmptyRequest() {
        SqlCheckReq req = new SqlCheckReq();
        
        when(mockSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(mockSession.getRuleSetId()).thenReturn(null);
        
        List<CheckResult> results = sqlCheckService.check(mockSession, req);
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithEmptySqls() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, Collections.emptyList(), new ConnectionConfig());
        
        assertNotNull(violations);
        assertTrue(violations.isEmpty());
    }

    // Add more test cases as needed

}