
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;

    @Before
    public void setUp() {
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }

    @Test
    public void testCheckWithConnectionSession() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        when(ruleService.list(anyLong(), any())).thenReturn(new ArrayList<>());
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString());
        ConnectionConfig config = new ConnectionConfig();
        Environment env = new Environment();
        env.setRulesetId(1L);
        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(anyLong(), any())).thenReturn(new ArrayList<>());
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(violations);
        assertEquals(0, violations.size());
    }

    @Test
    public void testGetRulesWithSession() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(anyString())).thenReturn(mock(JdbcOperations.class));
        List<SqlCheckRule> rules = sqlCheckService.getRules(new ArrayList<>(), session);
        assertNotNull(rules);
        assertEquals(0, rules.size());
    }

    @Test
    public void testGetRulesWithConfig() {
        DialectType dialectType = DialectType.POSTGRESQL;
        JdbcOperations jdbc = mock(JdbcOperations.class);
        List<SqlCheckRule> rules = sqlCheckService.getRules(new ArrayList<>(), dialectType, jdbc);
        assertNotNull(rules);
        assertEquals(0, rules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setEnabled(true);
        rule.setLevel(1);
        List<Rule> rules = Collections.singletonList(rule);
        CheckViolation violation = new CheckViolation();
        violation.setType(new RuleViolation());
        List<CheckViolation> violations = Collections.singletonList(violation);
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(violatedRules);
        assertEquals(1, violatedRules.size());
        assertEquals(1, violatedRules.get(0).getLevel());
    }
}
