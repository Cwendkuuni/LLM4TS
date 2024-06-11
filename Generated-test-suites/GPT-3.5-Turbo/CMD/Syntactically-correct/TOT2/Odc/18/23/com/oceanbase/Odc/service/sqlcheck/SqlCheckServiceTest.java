import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheck_validSessionAndSqlCheckReq_returnCheckResults() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        
        List<CheckResult> results = sqlCheckService.check(session, req);
        
        assertNotNull(results);
        // Add more assertions as needed
    }

    @Test
    public void testCheck_invalidSession_returnEmptyList() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getRuleSetId()).thenReturn(null);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        
        List<CheckResult> results = sqlCheckService.check(session, req);
        
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheck_environmentIdAndSqls_returnCheckViolations() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM table", 0));
        ConnectionConfig config = mock(ConnectionConfig.class);
        
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertNotNull(violations);
        // Add more assertions as needed
    }

    // Add more test cases for other methods in the SqlCheckService class

}