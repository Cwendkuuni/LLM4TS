package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

import static org.junit.Assert.*;

public class SqlCheckServiceTest {

    private ConnectionSession session;
    private SqlCheckService service;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        session = Mockito.mock(ConnectionSession.class);
        ruleService = Mockito.mock(RuleService.class);
        environmentService = Mockito.mock(EnvironmentService.class);
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        service = new SqlCheckService() {
            {
                this.ruleService = ruleService;
                this.environmentService = environmentService;
                this.jdbcOperations = jdbcTemplate;
            }
        };
    }

    @Test
    public void testCheckWithValidScript() {
        // Arrange
        Environment env = Mockito.mock(Environment.class);
        Mockito.when(environmentService.detail(1L)).thenReturn(env);
        List<Rule> rules = Arrays.asList(Mockito.mock(Rule.class));
        Mockito.when(ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = getRules(rules, ConnectionSessionConstants.MYSQL);
        Mockito.when(jdbcTemplate.queryForList(Mockito.anyString(), Mockito.any())).thenReturn(Collections.emptyList());

        String scriptContent = "SELECT * FROM users WHERE age > 18";
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(scriptContent);

        // Act
        List<CheckResult> checkResults = service.check(session, req);

        // Assert
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithInvalidScript() {
        // Arrange
        Environment env = Mockito.mock(Environment.class);
        Mockito.when(environmentService.detail(1L)).thenReturn(env);
        List<Rule> rules = Arrays.asList(Mockito.mock(Rule.class));
        Mockito.when(ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = getRules(rules, ConnectionSessionConstants.MYSQL);
        Mockito.when(jdbcTemplate.queryForList(Mockito.anyString(), Mockito.any())).thenThrow(new RuntimeException());

        String scriptContent = "SELECT * FROM users DROP TABLE users;";
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(scriptContent);

        // Act
        List<CheckResult> checkResults = service.check(session, req);

        // Assert
        assertTrue(checkResults.size() > 0);
        CheckViolation violation = checkResults.get(0).getViolations().get(0);
        assertEquals(RuleType.SQL_INJECTION.getName(), violation.getType().getName());
    }

    @Test
    public void testCheckWithEmptyScript() {
        // Arrange
        Environment env = Mockito.mock(Environment.class);
        Mockito.when(environmentService.detail(1L)).thenReturn(env);
        List<Rule> rules = Arrays.asList(Mockito.mock(Rule.class));
        Mockito.when(ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = getRules(rules, ConnectionSessionConstants.MYSQL);

        SqlCheckReq req = new SqlCheckReq();

        // Act
        List<CheckResult> checkResults = service.check(session, req);

        // Assert
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithNullSession() {
        // Arrange
        Environment env = Mockito.mock(Environment.class);
        List<Rule> rules = Arrays.asList(Mockito.mock(Rule.class));
        Mockito.when(ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = getRules(rules, ConnectionSessionConstants.MYSQL);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> service.check(null, new SqlCheckReq()));
    }

    @Test
    public void testCheckWithNullRequest() {
        // Arrange
        Environment env = Mockito.mock(Environment.class);
        List<Rule> rules = Arrays.asList(Mockito.mock(Rule.class));
        Mockito.when(ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = getRules(rules, ConnectionSessionConstants.MYSQL);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> service.check(session, null));
    }

    private List<SqlCheckRule> getRules(List<Rule> rules, DialectType dialectType) {
        return SqlCheckRules.getAllFactories(dialectType, jdbcTemplate)
                .stream()
                .filter(factory -> rules.stream().anyMatch(rule -> rule.getMetadata().getName().equals(factory.getName())))
                .map(factory -> factory.createRuleInstance())
                .collect(Collectors.toList());
    }
}