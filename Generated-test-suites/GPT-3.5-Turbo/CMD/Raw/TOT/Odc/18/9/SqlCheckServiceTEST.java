import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private ConnectionSession session;

    @Mock
    private SqlCheckReq req;

    @Mock
    private ConnectionConfig config;

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }

    @Test
    public void testCheckWithSession() {
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertNotNull(results);
        assertFalse(results.isEmpty());
        // Add more assertions as needed
    }

    @Test
    public void testCheckWithEnvironmentId() {
        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql = new OffsetString("SELECT * FROM table", 0, 0);
        sqls.add(sql);
        List<CheckViolation> violations = sqlCheckService.check(1L, "database_name", sqls, config);
        assertNotNull(violations);
        assertFalse(violations.isEmpty());
        // Add more assertions as needed
    }

    @Test
    public void testGetRulesWithSession() {
        List<Rule> rules = new ArrayList<>();
        // Add rules to the list
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
        assertFalse(sqlCheckRules.isEmpty());
        // Add more assertions as needed
    }

    @Test
    public void testGetRulesWithJdbc() {
        List<Rule> rules = new ArrayList<>();
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = new JdbcTemplate();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);
        assertNotNull(sqlCheckRules);
        assertFalse(sqlCheckRules.isEmpty());
        // Add more assertions as needed
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        // Add violations
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(violatedRules);
        assertFalse(violatedRules.isEmpty());
        // Add more assertions as needed
    }
}