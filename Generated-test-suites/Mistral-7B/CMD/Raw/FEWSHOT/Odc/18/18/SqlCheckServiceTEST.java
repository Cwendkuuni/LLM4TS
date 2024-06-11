package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.annotation.Validated;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckContext;

@Validated
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcOperations jdbcOperations;
    @Mock
    private ConnectionSession session;
    @Mock
    private SingleConnectionDataSource dataSource;
    @Mock
    private SqlChecker sqlChecker;
    @Mock
    private OBConsoleDataSourceFactory factory;
    @Mock
    private SqlCheckContext checkContext;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(session.getDialectType()).thenReturn(DialectType.MySQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);
        when(dataSource.getJdbcTemplate()).thenReturn(jdbcOperations);
        when(factory.getDataSource()).thenReturn(dataSource);
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(Collections.emptyList());
    }

    @Test
    public void testCheck_emptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(0, result.size());
    }

    @Test
    public void testCheck_emptySql() {
        List<Rule> rules = new ArrayList<>();
        List<CheckResult> result = sqlCheckService.check(session, new SqlCheckReq());
        assertEquals(0, result.size());
    }

    @Test
    public void testCheck_matchedRule() {
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);

        List<Rule> rules = new ArrayList<>();
        rules.add(rule);

        SqlCheckRuleFactory factory = Mockito.mock(SqlCheckRuleFactory.class);
        when(SqlCheckRules.getAllFactories(DialectType.MySQL, jdbcOperations)).thenReturn(Collections.singletonList(factory));
        when(factory.create(rule)).thenReturn(Mockito.mock(SqlCheckRule.class));

        String sql = "SELECT 1";
        String expectedError = "TestRule error message";
        CheckResult result = new CheckResult();
        result.setSql(sql);
        result.setErrorMessage(expectedError);
        result.setRiskLevel("HIGH");

        when(sqlChecker.check(anyList(), any(SqlCheckContext.class))).thenReturn(Collections.singletonList(new CheckViolation(RuleType.SQL_CHECK, "error", sql, "errorMessage")));

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(sql);
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(1, results.size());
        assertEquals(expectedError, results.get(0).getErrorMessage());
        assertEquals("HIGH", results.get(0).getRiskLevel());

        verify(factory).create(rule);
    }

    @Test
    public void testCheck_multipleMatchedRules() {
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("Rule1");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        rule1.setEnabled(true);

        Rule rule2 = new Rule();
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("Rule2");
        metadata2.setType(RuleType.SQL_CHECK);
        rule2.setMetadata(metadata2);
        rule2.setEnabled(true);

        List<Rule> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);

        SqlCheckRuleFactory factory1 = Mockito.mock(SqlCheckRuleFactory.class);
        when(SqlCheckRules.getAllFactories(DialectType.MySQL, jdbcOperations)).thenReturn(Collections.singletonList(factory1));
        when(factory1.create(rule1)).thenReturn(Mockito.mock(SqlCheckRule.class));
        SqlCheckRuleFactory factory2 = Mockito.mock(SqlCheckRuleFactory.class);
        when(SqlCheckRules.getAllFactories(DialectType.MySQL, jdbcOperations)).thenReturn(Arrays.asList(factory1, factory2));
        when(factory2.create(rule2)).thenReturn(Mockito.mock(SqlCheckRule.class));

        String sql = "SELECT 1";

        CheckResult result1 = new CheckResult();
        result1.setSql(sql);
        result1.setErrorMessage("Rule1 error message");
        result1.setRiskLevel("HIGH");

        CheckResult result2 = new CheckResult();
        result2.setSql(sql);
        result2.setErrorMessage("Rule2 error message");
        result2.setRiskLevel("MEDIUM");

        when(sqlChecker.check(anyList(), any(SqlCheckContext.class))).thenReturn(Arrays.asList(
                new CheckViolation(RuleType.SQL_CHECK, "error1", sql, "Rule1 error message"),
                new CheckViolation(RuleType.SQL_CHECK, "error2", sql, "Rule2 error message")));

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(sql);
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(2, results.size());
        assertEquals("Rule1 error message", results.get(0).getErrorMessage());
        assertEquals("HIGH", results.get(0).getRiskLevel());
        assertEquals("Rule2 error message", results.get(1).getErrorMessage());
        assertEquals("MEDIUM", results.get(1).getRiskLevel());

        verify(factory1).create(rule1);
        verify(factory2).create(rule2);
    }

    @Test
    public void testCheck_emptySqlsWithEnvironment() {
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        String[] sqls = {};
        List<CheckResult> result = sqlCheckService.check(1L, "test", Arrays.asList(SqlCheckReq.newBuilder().build()), new ConnectionConfig());
        assertEquals(0, result.size());
    }

    @Test
    public void testCheck_matchedRuleWithEnvironment() {
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);

        List<Rule> rules = new ArrayList<>();
        rules.add(rule);

        String sql = "SELECT 1";
        String expectedError = "TestRule error message";
        CheckResult result = new CheckResult();
        result.setSql(sql);
        result.setErrorMessage(expectedError);
        result.setRiskLevel("HIGH");

        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.singletonList(rule));
        when(SqlCheckRules.getAllFactories(DialectType.MySQL, jdbcOperations)).thenReturn(Collections.singletonList(Mockito.mock(SqlCheckRuleFactory.class)));
        when(SqlCheckRules.createByRule(Collections.singletonList(rule), DialectType.MySQL, jdbcOperations)).thenReturn(Mockito.mock(SqlCheckRule.class));
        when(sqlChecker.check(Collections.singletonList(sql), checkContext)).thenReturn(Collections.singletonList(new CheckViolation(RuleType.SQL_CHECK, "error", sql, "errorMessage")));

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(sql);
        List<CheckResult> results = sqlCheckService.check(1L, "test", Arrays.asList(req), new ConnectionConfig());
        assertEquals(1, results.size());
        assertEquals(expectedError, results.get(0).getErrorMessage());
        assertEquals("HIGH", results.get(0).getRiskLevel());
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, DialectType.MySQL, jdbcOperations);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetRulesWithMatchedRules() {
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);

        List<Rule> rules = new ArrayList<>();
        rules.add(rule);

        SqlCheckRuleFactory factory = Mockito.mock(SqlCheckRuleFactory.class);
        when(SqlCheckRules.getAllFactories(DialectType.MySQL, jdbcOperations)).thenReturn(Collections.singletonList(factory));
        when(factory.create(rule)).thenReturn(Mockito.mock(SqlCheckRule.class));

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, DialectType.MySQL, jdbcOperations);
        assertEquals(1, result.size());

        verify(factory).create(rule);
    }

    @Test
    public void testFullFillRiskLevelWithEmptyCheckViolations() {
        List<CheckViolation> violations = new ArrayList<>();
        List<Rule> rules = sqlCheckService.fullFillRiskLevel(Collections.emptyList(), violations);
        assertEquals(0, rules.size());
    }

    @Test
    public void testFullFillRiskLevelWithMatchedCheckViolation() {
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);

        List<Rule> rules = new ArrayList<>();
        rules.add(rule);

        CheckViolation violation = new CheckViolation();
        violation.setRuleType(RuleType.SQL_CHECK);
        violation.setType("TestType");
        violation.setViolation("TestViolation");
        violation.setErrorMessage("Test error message");
        violation.setLevel("HIGH");

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(violation);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(1, result.size());
        assertEquals(rule, result.get(0));

        verifyNoMoreInteractions(ruleService, environmentService, jdbcOperations, dataSource, factory, sqlChecker, checkContext);
    }
}