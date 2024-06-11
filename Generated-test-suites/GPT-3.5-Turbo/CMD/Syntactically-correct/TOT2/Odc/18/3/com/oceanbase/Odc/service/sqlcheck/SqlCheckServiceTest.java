import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private RuleService ruleServiceMock;
    private EnvironmentService environmentServiceMock;

    @Before
    public void setup() {
        sqlCheckService = new SqlCheckService();
        ruleServiceMock = mock(RuleService.class);
        environmentServiceMock = mock(EnvironmentService.class);

        sqlCheckService.ruleService = ruleServiceMock;
        sqlCheckService.environmentService = environmentServiceMock;
    }

    @Test
    public void testCheckMethodWithValidSessionAndReq() {
        // Setup
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        // Mocking
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        List<Rule> rules = Collections.singletonList(new Rule());
        when(ruleServiceMock.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(rules);

        // Test
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckMethodWithEnvironmentIdAndSqls() {
        // Setup
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM table"));

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);

        // Mocking
        Environment env = new Environment();
        env.setRulesetId(1L);
        when(environmentServiceMock.detail(environmentId)).thenReturn(env);

        List<Rule> rules = Collections.singletonList(new Rule());
        when(ruleServiceMock.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build())).thenReturn(rules);

        // Test
        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRulesMethodWithValidSession() {
        // Setup
        ConnectionSession session = new ConnectionSession();
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = mock(JdbcOperations.class);

        List<Rule> rules = Collections.singletonList(new Rule());

        // Test
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Add more test cases for other methods in SqlCheckService

}