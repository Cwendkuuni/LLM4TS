import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.ArrayList;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.junit.Test;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithValidSessionAndRequest() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        
        List<CheckResult> results = sqlCheckService.check(session, req);
        
        assertEquals(1, results.size());
    }

    @Test
    public void testCheckWithNullRuleSetId() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        
        when(session.getRuleSetId()).thenReturn(null);
        
        List<CheckResult> results = sqlCheckService.check(session, req);
        
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithEmptyRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(session.getRuleSetId(), QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        
        List<CheckResult> results = sqlCheckService.check(session, req);
        
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithValidEnvironmentIdAndSqls() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertEquals(0, violations.size());
    }

    @Test
    public void testCheckWithEmptySqls() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertEquals(0, violations.size());
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        List<Rule> rules = new ArrayList<>();
        ConnectionSession session = mock(ConnectionSession.class);
        
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithEmptyCheckViolations() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        
        assertEquals(0, violatedRules.size());
    }

    @Test
    public void testFullFillRiskLevelWithValidViolations() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType("Violation Type");
        violations.add(violation);
        
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        
        assertEquals(1, violatedRules.size());
    }

}