package com.oceanbase.odc.service.sqlcheck.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;
    private ConnectionConfig config;
    private Environment env;
    private SingleConnectionDataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        config = new ConnectionConfig();
        env = new Environment();
        dataSource = mock(SingleConnectionDataSource.class);
        jdbcTemplate = new JdbcTemplate(dataSource);
        when(dataSource.getJdbcOperations()).thenReturn(jdbcTemplate);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        when(environmentService.detail(1L)).thenReturn(env);
    }

    @Test
    public void testCheckMethodWithConnectionSession() {
        List<Rule> rules = Arrays.asList(new Rule());
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertNotNull(checkResults);
        assertEquals(0, checkResults.size());

        List<Rule> violatedRules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata("test_rule", RuleType.SQL_CHECK, null, null));
        rule.setEnabled(true);
        rule.setLevel(1);
        violatedRules.add(rule);

        when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertEquals(0, sqlCheckRules.size());

        CheckViolation violation = new CheckViolation("test_violation", null, RuleViolation.SEMANTIC_ERROR, null);
        List<CheckViolation> violations = Arrays.asList(violation);
        List<Rule> newRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(1, newRules.size());
        assertEquals(1, newRules.get(0).getLevel());

        List<CheckResult> results = SqlCheckUtil.buildCheckResults(violations);
        List<CheckResult> expectedResults = Arrays.asList(new CheckResult(results.get(0), 1));
        assertEquals(expectedResults, checkResults);
    }

    @Test
    public void testCheckMethodWithEnvironment() {
        String databaseName = "test_database";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("test_sql"));
        List<CheckViolation> violations = Arrays.asList(new CheckViolation("test_violation", null, RuleViolation.SEMANTIC_ERROR, null));

        List<Rule> rules = Arrays.asList(new Rule());
        when(ruleService.list(eq(env.getRulesetId()), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);
        assertEquals(0, sqlCheckRules.size());

        when(environmentService.detail(1L)).thenReturn(env);
        List<CheckResult> checkResults = sqlCheckService.check(1L, databaseName, sqls, config);
        assertNotNull(checkResults);
        assertEquals(1, checkResults.size());
        assertEquals(violations.get(0).getMessage(), checkResults.get(0).getViolations().get(0).getMessage());
        assertEquals(violations.get(0).getRiskLevel(), checkResults.get(0).getRiskLevel());
    }

    @Test
    public void testGetRulesMethod() {
        List<Rule> rules = Arrays.asList(new Rule());
        SqlCheckRuleFactory factory = mock(SqlCheckRuleFactory.class);
        List<SqlCheckRule> sqlCheckRules = Arrays.asList(new SqlCheckRule());

        when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(SqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class))).thenReturn(Arrays.asList(factory));
        List<SqlCheckRule> expectedRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);
        assertEquals(sqlCheckRules, expectedRules);
    }
}