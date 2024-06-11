package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation.CheckViolationType;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcOperations jdbcOperations;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private ConnectionSession session;
    @Mock
    private Environment environment;
    @Mock
    private ConnectionConfig connectionConfig;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck_EmptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        List<CheckResult> results = sqlCheckService.check(session, new SqlCheckReq());
        assertEquals(Collections.emptyList(), results);
    }

    @Test
    public void testCheck_EmptyScript() {
        List<Rule> rules = mockRules();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = mockSqlCheckRules(rules);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);

        List<CheckViolation> violations = Collections.emptyList();
        List<CheckResult> results = sqlCheckService.check(session, new SqlCheckReq(""));
        assertEquals(results, Collections.emptyList());
    }

    @Test
    public void testCheck_ValidScript() {
        List<Rule> rules = mockRules();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = mockSqlCheckRules(rules);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation("test1", CheckViolationType.TEST1, "message1", 1.0);
        violations.add(violation1);

        String script = "select 1;";
        List<CheckResult> results = sqlCheckService.check(session, new SqlCheckReq(script));
        assertEquals(1, results.size());
        CheckResult result = results.get(0);
        assertEquals(violation1, result.getViolations().get(0));
        assertEquals(script, result.getScript());
    }

    @Test
    public void testCheck_OneEnvironment() {
        List<Rule> rules = mockRules();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = mockSqlCheckRules(rules);
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(environment.getRulesetId()).thenReturn(1L);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("select 1;"));
        sqls.add(new OffsetString("select 2;"));

        List<CheckViolation> violations1 = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation("test1", CheckViolationType.TEST1, "message1", 1.0);
        violations1.add(violation1);

        List<CheckViolation> violations2 = new ArrayList<>();
        CheckViolation violation2 = new CheckViolation("test2", CheckViolationType.TEST2, "message2", 2.0);
        violations2.add(violation2);

        when(jdbcOperations.query(anyString(), anyArray())).thenReturn(violations1.stream().map(v -> v.getMessage()).collect(Collectors.toList()).toArray(new String[0]));
        when(jdbcOperations.query(eq("select 2;"), anyArray())).thenReturn(violations2.stream().map(v -> v.getMessage()).collect(Collectors.toList()).toArray(new String[0]));

        List<CheckResult> results = sqlCheckService.check(session, new SqlCheckReq(sqls, connectionConfig));
        assertEquals(sqls.size(), results.size());
        for (int i = 0; i < sqls.size(); i++) {
            CheckResult result = results.get(i);
            List<CheckViolation> violations = result.getViolations();
            assertEquals(violations.size(), 1);
            CheckViolation violation = violations.get(0);
            assertEquals(CheckViolationType.TEST1, violation.getType());
            assertEquals("message1", violation.getMessage());
            assertEquals(1.0, violation.getRiskLevel(), 0.01);
        }
    }

    @Test
    public void testCheck_MultipleEnvironments() {
        List<Rule> rules = mockRules();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = mockSqlCheckRules(rules);
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(environment.getRulesetId()).thenReturn(1L);

        List<OffsetString> sqls1 = new ArrayList<>();
        sqls1.add(new OffsetString("select 1;"));
        sqls1.add(new OffsetString("select 2;"));

        List<CheckViolation> violations1 = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation("test1", CheckViolationType.TEST1, "message1", 1.0);
        violations1.add(violation1);

        when(jdbcOperations.query(anyString(), anyArray())).thenReturn(violations1.stream().map(v -> v.getMessage()).collect(Collectors.toList()).toArray(new String[0]));

        List<OffsetString> sqls2 = new ArrayList<>();
        sqls2.add(new OffsetString("select 3;"));
        sqls2.add(new OffsetString("select 4;"));

        List<CheckViolation> violations2 = new ArrayList<>();
        CheckViolation violation2 = new CheckViolation("test2", CheckViolationType.TEST2, "message2", 2.0);
        violations2.add(violation2);

        when(jdbcOperations.query(eq("select 3;"), anyArray())).thenReturn(violations2.stream().map(v -> v.getMessage()).collect(Collectors.toList()).toArray(new String[0]));

        List<Environment> environments = new ArrayList<>();
        environments.add(environment);

        ConnectionConfig config1 = new ConnectionConfig();
        config1.setName("config1");
        when(environment.getConfigs()).thenReturn(Collections.singletonList(config1));

        ConnectionConfig config2 = new ConnectionConfig();
        config2.setName("config2");
        when(environment.getConfigs()).thenReturn(Arrays.asList(config1, config2));

        List<CheckResult> results = sqlCheckService.check(session, new SqlCheckReq(sqls1, config1));
        assertEquals(sqls1.size(), results.size());
        for (int i = 0; i < sqls1.size(); i++) {
            CheckResult result = results.get(i);
            List<CheckViolation> violations = result.getViolations();
            assertEquals(violations.size(), 1);
            CheckViolation violation = violations.get(0);
            assertEquals(CheckViolationType.TEST1, violation.getType());
            assertEquals("message1", violation.getMessage());
            assertEquals(1.0, violation.getRiskLevel(), 0.01);
        }

        results = sqlCheckService.check(session, new SqlCheckReq(sqls2, config2));
        assertEquals(sqls2.size(), results.size());
        for (int i = 0; i < sqls2.size(); i++) {
            CheckResult result = results.get(i);
            List<CheckViolation> violations = result.getViolations();
            assertEquals(violations.size(), 1);
            CheckViolation violation = violations.get(0);
            assertEquals(CheckViolationType.TEST2, violation.getType());
            assertEquals("message2", violation.getMessage());
            assertEquals(2.0, violation.getRiskLevel(), 0.01);
        }
    }

    @Test
    public void testGetRules_EmptyRules() {
        List<Rule> rules = Collections.emptyList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcOperations);
        assertEquals(Collections.emptyList(), sqlCheckRules);
    }

    @Test
    public void testGetRules_ValidRules() {
        List<Rule> rules = mockRules();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcOperations);
        assertEquals(rules.size(), sqlCheckRules.size());
        for (int i = 0; i < rules.size(); i++) {
            Rule rule = rules.get(i);
            SqlCheckRule sqlCheckRule = sqlCheckRules.get(i);
            assertEquals(rule.getName(), sqlCheckRule.getName());
            assertEquals(rule.getLevel(), sqlCheckRule.getLevel());
        }
    }

    @Test
    public void testFullFillRiskLevel_EmptyRules() {
        List<Rule> rules = Collections.emptyList();
        List<CheckViolation> violations = new ArrayList<>();
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testFullFillRiskLevel_ValidViolations() {
        List<Rule> rules = mockRules();
        List<CheckViolation> violations = new ArrayList<>();

        CheckViolation violation1 = new CheckViolation("test1", CheckViolationType.TEST1, "message1", 1.0);
        CheckViolation violation2 = new CheckViolation("test2", CheckViolationType.TEST2, "message2", 2.0);

        violations.add(violation1);
        violations.add(violation2);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(rules.size() + violations.size(), result.size());
        for (int i = 0; i < rules.size(); i++) {
            Rule rule = rules.get(i);
            Rule newRule = result.get(i + rules.size());
            assertEquals(rule.getName(), newRule.getName());
            assertEquals(rule.getLevel(), newRule.getLevel());
        }

        for (int i = rules.size(); i < result.size(); i++) {
            Rule newRule = result.get(i);
            CheckViolation violation = violations.get(i - rules.size());
            assertEquals(violation.getType().getName(), newRule.getMetadata().getName());
            assertEquals(violation.getRiskLevel(), newRule.getLevel(), 0.01);
        }
    }

    private List<Rule> mockRules() {
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("test1");
        metadata1.setLevel(1.0);
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);

        Rule rule2 = new Rule();
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("test2");
        metadata2.setLevel(2.0);
        metadata2.setType(RuleType.SQL_CHECK);
        rule2.setMetadata(metadata2);

        return Arrays.asList(rule1, rule2);
    }

    private List<SqlCheckRule> mockSqlCheckRules(List<Rule> rules) {
        List<SqlCheckRuleFactory> candidates = Collections.singletonList(mock(SqlCheckRuleFactory.class));
        return rules.stream().map(rule -> SqlCheckRules.createByRule(candidates, DialectType.MYSQL, rule)).collect(Collectors.toList());
    }
}