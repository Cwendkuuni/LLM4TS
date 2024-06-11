import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private ConnectionSession session;
    private SqlCheckReq req;
    private Long ruleSetId = 1L;
    private List<Rule> rules;
    private List<SqlCheckRule> sqlCheckRules;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        session = mock(ConnectionSession.class);
        req = new SqlCheckReq();
        rules = Arrays.asList(new Rule(), new Rule());
        sqlCheckRules = Arrays.asList(new SqlCheckRule(), new SqlCheckRule());

        when(session.getDialectType()).thenReturn(DialectType.ANSI_SQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(mock(JdbcOperations.class));
        when(session.getRuleSetId()).thenReturn(ruleSetId);
        when(sqlCheckService.getRules(rules, session)).thenReturn(sqlCheckRules);
    }

    @Test
    public void testCheck() {
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertNotNull(checkResults);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithCheckViolations() {
        List<CheckViolation> checkViolations = Arrays.asList(new CheckViolation(), new CheckViolation());
        List<CheckResult> checkResultsExpected = Collections.emptyList();

        when(sqlCheckService.check(session, req)).thenReturn(checkViolations);
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertNotNull(checkResults);
        assertEquals(checkResultsExpected.size(), checkResults.size());
    }

    @Test
    public void testGetRules() {
        List<SqlCheckRule> actualCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(actualCheckRules);
        assertEquals(sqlCheckRules.size(), actualCheckRules.size());
    }

}