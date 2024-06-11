package com.oceanbase.odc.service.sqlcheck.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation.*;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;

@Component
@Validated
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ConnectionSession session;

    @Mock
    private ConnectionConfig config;

    @Mock
    private SingleConnectionDataSource dataSource;

    @Mock
    private Rule rule;

    @Mock
    private SqlCheckRuleFactory sqlCheckRuleFactory;

    @Mock
    private SqlCheckRule sqlCheckRule;

    @Mock
    private CheckViolation checkViolation;

    @Autowired
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ArgumentMatchers.anyString())).thenReturn(jdbcTemplate);
        when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.isNull()))
                .thenReturn(Collections.singletonList(rule));
        when(environmentService.detail(ArgumentMatchers.anyLong()))
                .thenReturn(new Environment());
        when(rule.getMetadata()).thenReturn(RuleMetadata.builder().type(RuleType.SQL_CHECK).build());
        when(SqlCheckRules.getAllFactories(ArgumentMatchers.any(DialectType.class), ArgumentMatchers.any(JdbcOperations.class)))
                .thenReturn(Arrays.asList(sqlCheckRuleFactory));
        when(SqlCheckRules.createByRule(ArgumentMatchers.anyList(), ArgumentMatchers.any(DialectType.class), ArgumentMatchers.any(Rule.class)))
                .thenReturn(sqlCheckRule);
        when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.isNull())).thenReturn(Collections.emptyList());
        when(dataSource.getConnection()).thenReturn(mock(java.sql.Connection.class));
        when(jdbcTemplate.queryForList(ArgumentMatchers.anyString(), ArgumentMatchers.anyObject())).thenReturn(Collections.emptyList());
    }

    @Test
    public void testCheck() {
        SqlCheckReq req = SqlCheckReq.builder().build();
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithEmptyRules() {
        SqlCheckReq req = SqlCheckReq.builder().build();
        when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.isNull())).thenReturn(Collections.emptyList());
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithViolation() {
        String sql = "select 1 from dup_table where id = 1";
        CheckViolation violation = CheckViolation.builder().message("Duplicate column name").build();
        when(sqlCheckRule.check(ArgumentMatchers.eq(sql))).thenReturn(violation);
        SqlCheckReq req = SqlCheckReq.builder().scriptContent(sql).build();
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(1, result.size());
        CheckResult checkResult = result.get(0);
        assertEquals(violation.getMessage(), checkResult.getViolations().get(0).getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullSession() {
        SqlCheckReq req = SqlCheckReq.builder().scriptContent("test").build();
        sqlCheckService.check(null, req);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullReq() {
        ConnectionSession session = mock(ConnectionSession.class);
        sqlCheckService.check(session, null);
    }

    @Test
    public void testCheckEnvironment() {
        String databaseName = "test_database";
        List<OffsetString> sqls = Arrays.asList(OffsetString.builder().sql("select * from test_table").build());
        when(environmentService.detail(ArgumentMatchers.anyLong())).thenReturn(new Environment());
        List<CheckViolation> violations = sqlCheckService.check(1L, databaseName, sqls, config);
        assertEquals(0, violations.size());
    }

    @Test
    public void testCheckEnvironmentWithViolation() {
        String databaseName = "test_database";
        OffsetString sql = OffsetString.builder().sql("create table test_table (id int primary key, name int)").build();
        CheckViolation violation = CheckViolation.builder().message("Table already exists").build();
        when(sqlCheckRule.check(ArgumentMatchers.eq(sql))).thenReturn(violation);
        List<OffsetString> sqls = Collections.singletonList(sql);
        List<CheckViolation> violations = sqlCheckService.check(1L, databaseName, sqls, config);
        assertEquals(1, violations.size());
        CheckViolation checkViolation = violations.get(0);
        assertEquals(violation.getMessage(), checkViolation.getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckEnvironmentWithEmptySqls() {
        List<OffsetString> sqls = Collections.emptyList();
        sqlCheckService.check(1L, "test_database", sqls, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckEnvironmentWithNullConfig() {
        sqlCheckService.check(1L, "test_database", Collections.singletonList(OffsetString.builder().sql("test").build()), null);
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = Arrays.asList(rule);
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        List<Rule> rules = Collections.emptyList();
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetRulesWithEmptyJdbc() {
        List<Rule> rules = Arrays.asList(rule);
        JdbcOperations jdbc = mock(JdbcOperations.class);
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session, jdbc);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetRulesWithDisabledRule() {
        Rule ruleWithDisabled = new Rule();
        ruleWithDisabled.setEnabled(false);
        List<Rule> rules = Arrays.asList(rule, ruleWithDisabled);
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetRulesWithDialectMismatch() {
        Rule ruleWithWrongDialect = new Rule();
        ruleWithWrongDialect.setMetadata(RuleMetadata.builder().type(RuleType.SQL_CHECK).dialect(DialectType.ORACLE).build());
        List<Rule> rules = Arrays.asList(rule, ruleWithWrongDialect);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);
        assertEquals(0, result.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        RuleMetadata metadata = RuleMetadata.builder().name("test_rule").level(0).build();
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        List<CheckViolation> violations = Arrays.asList(checkViolation);
        Map<String, Rule> name2RuleMap = new HashMap<>();
        name2RuleMap.put(metadata.getName(), rule);
        when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.isNull())).thenReturn(Collections.singletonList(rule));
        sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(0, rule.getViolations().size());

        sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(1, rule.getViolations().size());
        CheckViolation violation = rule.getViolations().get(0);
        assertEquals(RuleViolation.RISK_LEVEL_HIGH, violation.getLevel());

        sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(1, rule.getViolations().size());
        violation = rule.getViolations().get(0);
        assertEquals(RuleViolation.RISK_LEVEL_MEDIUM, violation.getLevel());

        sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(1, rule.getViolations().size());
        violation = rule.getViolations().get(0);
        assertEquals(RuleViolation.RISK_LEVEL_LOW, violation.getLevel());

        sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(1, rule.getViolations().size());
        violation = rule.getViolations().get(0);
        assertEquals(RuleViolation.RISK_LEVEL_INFO, violation.getLevel());
    }
}