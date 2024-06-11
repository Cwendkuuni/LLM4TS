import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;

    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
    }

    @Test
    public void testCheckWithSession() {
        // Test case for check method with session
        // Typical use case
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq("SELECT * FROM table", ",");
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertNotNull(result);
    }

    @Test
    public void testCheckWithEnvironmentId() {
        // Test case for check method with environment id
        // Typical use case
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString(0, "SELECT * FROM table"));
        ConnectionConfig config = new ConnectionConfig();
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(violations);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithEmptySqls() {
        // Test case for check method with empty sqls
        // Edge case
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Collections.emptyList();
        ConnectionConfig config = new ConnectionConfig();
        sqlCheckService.check(environmentId, databaseName, sqls, config);
    }

    @Test
    public void testGetRulesWithSession() {
        // Test case for getRules method with session
        // Typical use case
        ConnectionSession session = new ConnectionSession();
        List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.emptyList(), session);
        assertNotNull(rules);
    }

    @Test
    public void testGetRulesWithJdbc() {
        // Test case for getRules method with JdbcOperations
        // Typical use case
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = new JdbcTemplate();
        List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.emptyList(), dialectType, jdbc);
        assertNotNull(rules);
    }

    @Test
    public void testFullFillRiskLevel() {
        // Test case for fullFillRiskLevel method
        // Typical use case
        List<Rule> rules = Collections.singletonList(new Rule());
        List<CheckViolation> violations = Collections.singletonList(new CheckViolation());
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(violatedRules);
    }
}