
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private ConnectionSession session;

    private SqlCheckService sqlCheckService = new SqlCheckService();

    @Test
    public void testCheck() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table;");
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        
        List<CheckResult> results = sqlCheckService.check(session, req);
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testEmptyCheck() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("");
        when(session.getDialectType()).thenReturn(DialectType.ORACLE);
        
        List<CheckResult> results = sqlCheckService.check(session, req);
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithViolations() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table WHERE condition=5;");
        when(session.getDialectType()).thenReturn(DialectType.POSTGRES);
        when(ruleService.list(anyLong(), any())).thenReturn(Arrays.asList(new Rule()));
        
        List<CheckResult> results = sqlCheckService.check(session, req);
        
        assertNotNull(results);
        assertTrue(!results.isEmpty());
    }

    @Test
    public void testCheckWithInvalidRuleSetId() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table;");
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getRuleSetId()).thenReturn(null);
        
        List<CheckResult> results = sqlCheckService.check(session, req);
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
}
