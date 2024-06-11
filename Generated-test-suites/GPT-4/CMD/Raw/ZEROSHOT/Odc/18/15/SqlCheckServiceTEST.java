
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SqlCheckServiceTest {

    @MockBean
    private RuleService ruleService;

    @MockBean
    private EnvironmentService environmentService;

    @MockBean
    private ConnectionSession session;

    @MockBean
    private SqlCheckReq req;

    private SqlCheckService sqlCheckService;

    @Before
    public void initialize() {
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
    }

    @Test
    public void testCheckWithSessionAndReq() {
        Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        List<Rule> mockRules = new ArrayList<>();
        Mockito.when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class)))
            .thenReturn(mockRules);

        List<CheckResult> results = sqlCheckService.check(session, req);
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    public void testCheckWithEnvIdDatabaseNameSqlsAndConfig() {
        Environment mockEnvironment = mock(Environment.class);
        Mockito.when(mockEnvironment.getRulesetId()).thenReturn(1L);
        Mockito.when(environmentService.detail(any(Long.class))).thenReturn(mockEnvironment);
        List<Rule> mockRules = new ArrayList<>();
        Mockito.when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class)))
            .thenReturn(mockRules);

        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users", 1));
        ConnectionConfig mockConfig = mock(ConnectionConfig.class);

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testDatabase", sqls, mockConfig);
        assertNotNull(checkViolations);
        assertFalse(checkViolations.isEmpty());
    }

    @Test
    public void testGetRulesWithRulesSession() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());
        JdbcOperations mockJdbc = mock(JdbcOperations.class);
        Mockito.when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(mockJdbc);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
        assertFalse(sqlCheckRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(violatedRules);
        assertFalse(violatedRules.isEmpty());
    }
}
