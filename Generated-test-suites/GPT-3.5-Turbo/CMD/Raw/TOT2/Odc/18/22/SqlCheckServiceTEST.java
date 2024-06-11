import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    
    @Before
    public void setup() {
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = Mockito.mock(RuleService.class);
        sqlCheckService.environmentService = Mockito.mock(EnvironmentService.class);
    }

    @Test
    public void testCheck_SessionAndReq_ValidInput() {
        // Test case for valid input with ConnectionSession and SqlCheckReq
        // Mocking the dependencies
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        when(sqlCheckService.ruleService.list(anyLong(), any())).thenReturn(Collections.singletonList(new Rule()));
        
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertNotNull(checkResults);
        assertEquals(1, checkResults.size());
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testCheck_EnvIdAndSqls_ValidInput() {
        // Test case for valid input with environmentId, databaseName, sqls, and connection config
        // Mocking the dependencies
        Long environmentId = 1L;
        String databaseName = "db";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM table", 0));
        ConnectionConfig config = new ConnectionConfig();
        when(sqlCheckService.ruleService.list(anyLong(), any())).thenReturn(Collections.singletonList(new Rule()));
        Environment env = new Environment();
        when(sqlCheckService.environmentService.detail(environmentId)).thenReturn(env);
        
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(checkViolations);
        assertEquals(1, checkViolations.size());
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testGetRules_Session_ValidInput() {
        // Test case for getting rules based on ConnectionSession
        // Mocking the dependencies
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(anyString())).thenReturn(Mockito.mock(JdbcOperations.class));

        List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.singletonList(new Rule()), session);

        assertNotNull(rules);
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testGetRules_DialectTypeAndJdbc_ValidInput() {
        // Test case for getting rules based on DialectType and JdbcOperations
        // Mocking the dependencies
        DialectType dialectType = DialectType.POSTGRES;
        JdbcOperations jdbc = Mockito.mock(JdbcOperations.class);

        List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.singletonList(new Rule()), dialectType, jdbc);

        assertNotNull(rules);
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testFullFillRiskLevel_ValidInput() {
        // Test case for filling risk levels based on rules and violations
        // Mocking the dependencies
        List<Rule> rules = Collections.singletonList(new Rule());
        List<CheckViolation> violations = Collections.singletonList(new CheckViolation());

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertNotNull(violatedRules);
        // Add more assertions based on the expected behavior
    }
}