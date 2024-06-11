import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithValidSessionAndRequest() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithInvalidSession() {
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> results = sqlCheckService.check(null, req);
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithNullRequest() {
        ConnectionSession session = mock(ConnectionSession.class);
        List<CheckResult> results = sqlCheckService.check(session, null);
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testGetRulesWithValidSession() {
        ConnectionSession session = mock(ConnectionSession.class);
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevelWithValidViolations() {
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType("Type1");
        violations.add(violation1);
        List<Rule> rules = new ArrayList<>();
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(violatedRules);
        assertEquals(1, violatedRules.size());
    }

}