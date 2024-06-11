
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    
    @Mock
    private EnvironmentService environmentService;
  
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // Test for check(ConnectionSession session, SqlCheckReq req)
    @Test
    public void testCheckWithValidSessionAndReq() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    // Test for check(Long environmentId, String databaseName, List<OffsetString> sqls, ConnectionConfig config)
    @Test
    public void testCheckWithValidParams() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(violations);
        assertFalse(violations.isEmpty());
    }

    // Test for getRules(List<Rule> rules, ConnectionSession session)
    @Test
    public void testGetRulesWithValidSession() {
        List<Rule> rules = new ArrayList<>();
        ConnectionSession session = new ConnectionSession();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
        assertFalse(sqlCheckRules.isEmpty());
    }

    // Test for getRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbc)
    @Test
    public void testGetRulesWithValidParams() {
        List<Rule> rules = new ArrayList<>();
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = new JdbcTemplate();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);
        assertNotNull(sqlCheckRules);
        assertFalse(sqlCheckRules.isEmpty());
    }

    // Test for fullFillRiskLevel(List<Rule> rules, List<CheckViolation> violations)
    @Test
    public void testFullFillRiskLevelWithValidParams() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(violatedRules);
        assertFalse(violatedRules.isEmpty());
    }

}
