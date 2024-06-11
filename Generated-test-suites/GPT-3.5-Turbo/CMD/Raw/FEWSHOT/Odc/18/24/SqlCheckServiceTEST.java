//Import Statements
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

//Test Class
public class SqlCheckServiceTest {

    //Mock Objects
    private RuleService ruleService = mock(RuleService.class);
    private EnvironmentService environmentService = mock(EnvironmentService.class);
    private ConnectionSession session = mock(ConnectionSession.class);
    private SqlCheckReq req = new SqlCheckReq();
    private ConnectionConfig config = new ConnectionConfig();
    private JdbcTemplate jdbc = mock(JdbcTemplate.class);

    //Test Cases for check(ConnectionSession session, SqlCheckReq req)
    @Test
    public void testCheckSessionReqRuleSetIdNull() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        List<CheckResult> results = new SqlCheckService().check(session, req);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckSessionReqEmptyRules() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());
        List<CheckResult> results = new SqlCheckService().check(session, req);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    //Additional test cases for check(ConnectionSession session, SqlCheckReq req) can be added here

    //Test Cases for check(Long environmentId, String databaseName, List<OffsetString> sqls, ConnectionConfig config)
    @Test
    public void testCheckEnvironmentIdEmptySqls() {
        List<OffsetString> sqls = Collections.emptyList();
        List<CheckViolation> violations = new SqlCheckService().check(1L, "testDB", sqls, config);
        assertNotNull(violations);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testCheckEnvironmentIdValid() {
        List<Rule> rules = Collections.singletonList(new Rule());
        Environment env = new Environment();
        env.setRulesetId(1L);
        when(environmentService.detail(1L)).thenReturn(env);
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        when(jdbc.update(any(String.class))).thenReturn(1);
        List<CheckViolation> violations = new SqlCheckService().check(1L, "testDB", Collections.singletonList(new OffsetString()), config);
        assertNotNull(violations);
        assertFalse(violations.isEmpty());
    }

    //Additional test cases for check(Long environmentId, String databaseName, List<OffsetString> sqls, ConnectionConfig config) can be added here

    //Test Cases for getRules(List<Rule> rules, ConnectionSession session)
    @Test
    public void testGetRulesSessionEmptyRules() {
        List<SqlCheckRule> checkRules = new SqlCheckService().getRules(Collections.emptyList(), session);
        assertNotNull(checkRules);
        assertTrue(checkRules.isEmpty());
    }

    //Additional test cases for getRules(List<Rule> rules, ConnectionSession session) can be added here

    //Test Cases for getRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbc)
    @Test
    public void testGetRulesDialectTypeJdbc() {
        List<Rule> rules = Collections.singletonList(new Rule());
        List<SqlCheckRule> checkRules = new SqlCheckService().getRules(rules, DialectType.MYSQL, jdbc);
        assertNotNull(checkRules);
        assertFalse(checkRules.isEmpty());
    }

    //Additional test cases for getRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbc) can be added here

    //Test Cases for fullFillRiskLevel(List<Rule> rules, List<CheckViolation> violations)
    @Test
    public void testFullFillRiskLevelEmptyRulesViolations() {
        List<Rule> violatedRules = new SqlCheckService().fullFillRiskLevel(Collections.emptyList(), Collections.emptyList());
        assertNotNull(violatedRules);
        assertTrue(violatedRules.isEmpty());
    }

    //Additional test cases for fullFillRiskLevel(List<Rule> rules, List<CheckViolation> violations) can be added here

}