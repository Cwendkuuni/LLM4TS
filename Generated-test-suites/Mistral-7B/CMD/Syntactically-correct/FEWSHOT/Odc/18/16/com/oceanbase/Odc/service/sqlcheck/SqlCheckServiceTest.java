package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.jdbc.JdbcTemplate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.validation.annotation.Validated;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.ModelTestUtils;
import com.oceanbase.odc.service.regulation.ruleset.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.factory.SqlCheckRuleFactory;

@Slf4j
@Validated
@SkipAuthorize("inside connect session")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;
    @Autowired
    private SqlCheckRules sqlCheckRules;
    @Autowired
    private SqlCheckService sqlCheckService;
    @Autowired
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    @Autowired
    private SqlCheckUtil sqlCheckUtil;

    private ConnectionSession connectionSession;
    private ConnectionConfig connectionConfig;
    private Environment environment;
    private List<Rule> rules;
    private List<SqlCheckRule> sqlCheckRulesList;
    private List<CheckViolation> checkViolations;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        connectionSession = mock(ConnectionSession.class);
        connectionConfig = mock(ConnectionConfig.class);
        environment = new Environment();
        rules = new ArrayList<>();
        sqlCheckRulesList = new ArrayList<>();
        checkViolations = new ArrayList<>();
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(obConsoleDataSourceFactory.getDataSource()).thenReturn(singleConnectionDataSource);
        when(singleConnectionDataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);
    }

    @Test
    public void testCheck() {
        Rule rule = ModelTestUtils.generateRule();
        SqlCheckRule sqlCheckRule = SqlCheckRules.createByRule(sqlCheckRules.getAllFactories(DialectType.MySQL, jdbcTemplate), DialectType.MySQL, rule);
        sqlCheckRulesList.add(sqlCheckRule);
        when(ruleService.list(ConnectionSessionUtil.getRuleSetId(connectionSession), QueryRuleMetadataParams.builder().build())).thenReturn(rules);

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MySQL, ",", sqlCheckRulesList);
        CheckViolation checkViolation = new CheckViolation("testViolation", "testMessage", RuleType.SQL_CHECK, RuleViolation.WARNING);
        checkViolations.add(checkViolation);

        when(jdbcTemplate.queryForObject("SELECT 1", String.class)).thenReturn("1");
        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, new SqlCheckReq("SELECT 1", ","));

        verify(ruleService, times(1)).list(ConnectionSessionUtil.getRuleSetId(connectionSession), QueryRuleMetadataParams.builder().build());
        verify(jdbcTemplate, times(1)).queryForObject("SELECT 1", String.class);
        assertEquals(1, checkResults.size());
        assertEquals(checkViolation, checkResults.get(0).getCheckViolations().get(0));
        verify(sqlChecker, times(1)).check(Collections.singletonList("SELECT 1"));
    }

    @Test
    public void testCheckWithEmptyScriptContent() {
        when(jdbcTemplate.queryForObject("SELECT 1", String.class)).thenReturn("1");
        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, new SqlCheckReq("", ""));
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEmptyRules() {
        List<Rule> emptyRules = Collections.emptyList();
        RuleMetadata metadata = new RuleMetadata();
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rules.add(rule);
        when(ruleService.list(ConnectionSessionUtil.getRuleSetId(connectionSession), QueryRuleMetadataParams.builder().build())).thenReturn(emptyRules);
        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, new SqlCheckReq("SELECT 1", ","));
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEmptySqlCheckRules() {
        RuleMetadata metadata = new RuleMetadata();
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rules.add(rule);
        when(ruleService.list(ConnectionSessionUtil.getRuleSetId(connectionSession), QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        when(jdbcTemplate.queryForObject("SELECT 1", String.class)).thenReturn("1");
        List<SqlCheckRuleFactory> candidates = mock(List.class);
        when(sqlCheckRules.getAllFactories(DialectType.MySQL, jdbcTemplate)).thenReturn(candidates);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MySQL, jdbcTemplate);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testCheckWithSqlCheckRules() {
        RuleMetadata metadata = new RuleMetadata();
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rules.add(rule);
        RuleMetadata metadata2 = new RuleMetadata();
        Rule rule2 = new Rule();
        rule2.setMetadata(metadata2);
        rules.add(rule2);
        when(ruleService.list(ConnectionSessionUtil.getRuleSetId(connectionSession), QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        when(jdbcTemplate.queryForObject("SELECT 1", String.class)).thenReturn("1");
        when(sqlCheckRules.getAllFactories(DialectType.MySQL, jdbcTemplate)).thenReturn(sqlCheckRulesList);

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MySQL, ",", sqlCheckRulesList);
        CheckViolation checkViolation = new CheckViolation("testViolation", "testMessage", RuleType.SQL_CHECK, RuleViolation.WARNING);
        checkViolations.add(checkViolation);

        List<CheckViolation> violations = sqlChecker.check(Collections.singletonList("SELECT 1"));
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        List<CheckResult> checkResults = sqlCheckUtil.buildCheckResults(violations);

        verify(ruleService, times(1)).list(ConnectionSessionUtil.getRuleSetId(connectionSession), QueryRuleMetadataParams.builder().build());
        verify(jdbcTemplate, times(1)).queryForObject("SELECT 1", String.class);
        assertEquals(1, violatedRules.size());
        assertEquals(checkViolation.getViolation(), violatedRules.get(0).getViolation());
        assertEquals(RuleType.SQL_CHECK, violatedRules.get(0).getType());
        assertEquals(1, checkResults.size());
        assertEquals(checkViolation, checkResults.get(0).getCheckViolations().get(0));
        verify(sqlChecker, times(1)).check(Collections.singletonList("SELECT 1"));
    }

    @Test
    public void testCheckWithEnvironment() {
        RuleMetadata metadata = new RuleMetadata();
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rules.add(rule);
        RuleMetadata metadata2 = new RuleMetadata();
        Rule rule2 = new Rule();
        rule2.setMetadata(metadata2);
        rules.add(rule2);
        when(ruleService.list(ConnectionSessionUtil.getRuleSetId(connectionSession), QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(jdbcTemplate.queryForObject("SELECT 1", String.class)).thenReturn("1");
        when(sqlCheckRules.getAllFactories(DialectType.MySQL, jdbcTemplate)).thenReturn(sqlCheckRulesList);

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MySQL, null, sqlCheckRulesList);
        CheckViolation checkViolation = new CheckViolation("testViolation", "testMessage", RuleType.SQL_CHECK, RuleViolation.WARNING);
        checkViolations.add(checkViolation);

        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT 1"));
        List<CheckViolation> violations = sqlChecker.check(sqls, new SqlCheckContext(1));
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        List<CheckResult> checkResults = sqlCheckUtil.buildCheckResults(violations);

        verify(ruleService, times(1)).list(ConnectionSessionUtil.getRuleSetId(connectionSession), QueryRuleMetadataParams.builder().build());
        verify(jdbcTemplate, times(1)).queryForObject("SELECT 1", String.class);
        assertEquals(1, violatedRules.size());
        assertEquals(checkViolation.getViolation(), violatedRules.get(0).getViolation());
        assertEquals(RuleType.SQL_CHECK, violatedRules.get(0).getType());
        assertEquals(1, checkResults.size());
        assertEquals(checkViolation, checkResults.get(0).getCheckViolations().get(0));
        verify(sqlChecker, times(1)).check(sqls, new SqlCheckContext(1));
    }

    @Test
    public void testCheckWithSqlCheckRulesAndEmptyInput() {
        RuleMetadata metadata = new RuleMetadata();
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rules.add(rule);
        RuleMetadata metadata2 = new RuleMetadata();
        Rule rule2 = new Rule();
        rule2.setMetadata(metadata2);
        rules.add(rule2);
        when(ruleService.list(ConnectionSessionUtil.getRuleSetId(connectionSession), QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        when(jdbcTemplate.queryForObject("SELECT 1", String.class)).thenReturn("1");
        when(sqlCheckRules.getAllFactories(DialectType.MySQL, jdbcTemplate)).thenReturn(sqlCheckRulesList);

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MySQL, null, sqlCheckRulesList);
        List<CheckViolation> violations = sqlChecker.check(Collections.emptyList(), new SqlCheckContext(0));
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        List<CheckResult> checkResults = sqlCheckUtil.buildCheckResults(Collections.emptyList());

        verify(ruleService, times(1)).list(ConnectionSessionUtil.getRuleSetId(connectionSession), QueryRuleMetadataParams.builder().build());
        verify(jdbcTemplate, times(1)).queryForObject("SELECT 1", String.class);
        assertEquals(0, checkResults.size());
        assertEquals(0, violatedRules.size());
        verify(sqlChecker, times(1)).check(Collections.emptyList(), new SqlCheckContext(0));
    }
}