package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.DialectType;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.Model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.Model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.Model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

/**
 * Test class for {@link SqlCheckService}
 */
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private SingleConnectionDataSource dataSource;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ConnectionSession session;

    @Mock
    private Environment env;

    @Mock
    private ConnectionConfig config;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(dataSource.getJdbcOperations()).thenReturn(jdbcTemplate);
        when(config.getDialectType()).thenReturn(DialectType.MYSQL);
        when(env.getRulesetId()).thenReturn(1L);
    }

    @Test
    public void testCheck() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setType(RuleType.SQL_CHECK);
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);

        List<Rule> rules = Arrays.asList(rule);

        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
        assertEquals(1, sqlCheckRules.size());

        SqlCheckReq req = new SqlCheckReq();
        String scriptContent = "SELECT * FROM users WHERE age < 18";
        req.setScriptContent(scriptContent);
        List<CheckViolation> checkViolations = sqlCheckService.check(session, req).get(0).getViolations();
        assertNotNull(checkViolations);
        assertEquals(1, checkViolations.size());
        assertEquals("Age restriction violation: The query 'SELECT * FROM users WHERE age < 18' contains a clause which may expose minors.",
                checkViolations.get(0).getMessage());
        assertEquals(RuleType.SQL_CHECK.getName(), checkViolations.get(0).getType().getName());
        assertEquals(CheckResult.Level.CRITICAL, checkViolations.get(0).getLevel());
    }

    @Test
    public void testCheckWithEmptyRules() {
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());
        SqlCheckReq req = new SqlCheckReq();
        String scriptContent = "SELECT * FROM users WHERE age < 18";
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertNull(checkResults);
    }

    @Test
    public void testCheckEmptySql() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setType(RuleType.SQL_CHECK);
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);

        List<Rule> rules = Arrays.asList(rule);

        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertNotNull(checkResults);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEnvironment() {
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users WHERE age < 18"));
        sqls.add(new OffsetString("SELECT * FROM employees WHERE salary < 2000"));

        Environment env = new Environment();
        when(environmentService.detail(1L)).thenReturn(env);
        when(env.getRulesetId()).thenReturn(1L);

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        config.setUrl("jdbc:mysql://localhost:3306/odbtest");
        config.setUsername("root");
        config.setPassword("password");

        when(jdbcTemplate.queryForList(Mockito.anyString(), Mockito.any())).thenReturn(Collections.emptyList());
        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(null);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true, false);
        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) factory.getDataSource();

        SqlCheckContext checkContext = new SqlCheckContext((long) sqls.size());
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "dbname", sqls, config).stream()
                .map(r -> r.getViolations().get(0)).collect(Collectors.toList());
        assertEquals(2, checkViolations.size());
        assertEquals("Age restriction violation: The query 'SELECT * FROM users WHERE age < 18' contains a clause which may expose minors.",
                checkViolations.get(0).getMessage());
        assertEquals("Salary restriction violation: The query 'SELECT * FROM employees WHERE salary < 2000' may expose sensitive data.",
                checkViolations.get(1).getMessage());
    }

    @Test
    public void testGetRules() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setType(RuleType.SQL_CHECK);
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);

        List<Rule> rules = Arrays.asList(rule);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertEquals(1, sqlCheckRules.size());
        assertEquals("Rule1", sqlCheckRules.get(0).getName());

        rules = Collections.emptyList();
        sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithEmptyRuleset() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setType(RuleType.SQL_CHECK);
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);

        List<Rule> rules = Arrays.asList(rule);
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithNullRuleset() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setType(RuleType.SQL_CHECK);
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);

        List<Rule> rules = Arrays.asList(rule);
        when(ruleService.list(null, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithNullSession() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setType(RuleType.SQL_CHECK);
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);

        List<Rule> rules = Arrays.asList(rule);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, null);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithEmptyJdbcOperations() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setType(RuleType.SQL_CHECK);
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);

        List<Rule> rules = Arrays.asList(rule);
        when(jdbcTemplate.queryForList(Mockito.anyString(), Mockito.any())).thenReturn(Collections.emptyList());
        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(null);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setType(RuleType.SQL_CHECK);
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(new RuleViolation());
        violation.setLevel(CheckResult.Level.CRITICAL);
        violations.add(violation);

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        SqlCheckService sqlCheckService = new SqlCheckService();
        List<Rule> rules = Arrays.asList(rule);
        List<CheckViolation> checkViolations = sqlCheckService.fullFillRiskLevel(rules, violations).stream()
                .map(r -> r).collect(Collectors.toList());

        assertEquals(1, checkViolations.size());
        assertEquals(CheckResult.Level.CRITICAL, checkViolations.get(0).getLevel());
        assertEquals("Rule1", checkViolations.get(0).getRuleName());
    }

    @Test
    public void testFullFillRiskLevelWithEmptyRules() {
        List<CheckViolation> violations = new ArrayList<>();

        SqlCheckService sqlCheckService = new SqlCheckService();
        List<Rule> rules = Collections.emptyList();
        List<CheckViolation> checkViolations = sqlCheckService.fullFillRiskLevel(rules, violations).stream()
                .map(r -> r).collect(Collectors.toList());

        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testFullFillRiskLevelWithNullRules() {
        List<CheckViolation> violations = new ArrayList<>();

        SqlCheckService sqlCheckService = new SqlCheckService();
        List<Rule> rules = null;
        List<CheckViolation> checkViolations = sqlCheckService.fullFillRiskLevel(rules, violations).stream()
                .map(r -> r).collect(Collectors.toList());

        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testFullFillRiskLevelWithNullViolations() {
        List<Rule> rules = Arrays.asList(new Rule());

        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckViolation> checkViolations = sqlCheckService.fullFillRiskLevel(rules, null).stream()
                .map(r -> r).collect(Collectors.toList());

        assertEquals(0, checkViolations.size());
    }
}