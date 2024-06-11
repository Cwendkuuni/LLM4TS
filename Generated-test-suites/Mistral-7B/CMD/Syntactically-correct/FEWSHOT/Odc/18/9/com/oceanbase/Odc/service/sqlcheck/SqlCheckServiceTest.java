package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckContext;

@Slf4j
public class SqlCheckServiceTest {

    private RuleService ruleService;
    private EnvironmentService environmentService;
    private ConnectionSession session;
    private SingleConnectionDataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private SqlCheckService sqlCheckService;
    private List<Rule> rules;
    private List<SqlCheckRule> sqlCheckRules;
    private List<CheckViolation> checkViolations;
    private List<CheckResult> checkResults;
    private List<SqlCheckRuleFactory> sqlCheckRuleFactories;

    @Before
    public void setUp() {
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        session = mock(ConnectionSession.class);
        dataSource = mock(SingleConnectionDataSource.class);
        jdbcTemplate = new JdbcTemplate(dataSource);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);

        rules = new ArrayList<>();
        sqlCheckRules = new ArrayList<>();
        sqlCheckRuleFactories = SqlCheckRules.getAllFactories(DialectType.MySQL, jdbcTemplate);

        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setType(RuleType.SQL_CHECK);
        rule.getMetadata().setName("TestRule");
        rule.getMetadata().setEnabled(true);
        rules.add(rule);

        SqlCheckRuleFactory factory = mock(SqlCheckRuleFactory.class);
        when(factory.create(rule)).thenReturn(new SqlCheckRule());

        sqlCheckRules.add(new SqlCheckRule());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(session.getSyncJdbcExecutor(eq(ConnectionSessionConstants.CONSOLE_DS_KEY))).thenReturn(jdbcTemplate);
        when(session.getDialectType()).thenReturn(DialectType.MySQL);
        when(session.getJdbcOperations()).thenReturn(jdbcTemplate);
    }

    @Test
    public void testCheck() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        SqlCheckReq req = new SqlCheckReq();
        CheckResult result = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), result.getCheckViolations());
    }

    @Test
    public void testCheckWithViolation() {
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setType(RuleType.SQL_CHECK);
        rule.getMetadata().setName("TestRule");
        rule.getMetadata().setEnabled(true);
        rules.add(rule);

        SqlCheckRuleFactory factory = mock(SqlCheckRuleFactory.class);
        when(factory.create(rule)).thenReturn(new SqlCheckRule());

        rule.setSqlCheckRule(factory.create(rule));

        CheckViolation violation = new CheckViolation();
        violation.setLevel("HIGH");
        violation.setType("TestType");
        violation.setMessage("TestMessage");
        checkViolations = new ArrayList<>();
        checkViolations.add(violation);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("TestScript");
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(1, results.size());
        assertEquals(violation, results.get(0).getCheckViolations().get(0));
    }

    @Test
    public void testGetRules() {
        assertEquals(Collections.emptyList(), sqlCheckService.getRules(session));

        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setType(RuleType.SQL_CHECK);
        rule.getMetadata().setName("TestRule");
        rule.getMetadata().setEnabled(true);
        rules.add(rule);

        List<SqlCheckRule> rules = sqlCheckService.getRules(session);
        assertEquals(1, rules.size());
        assertEquals(sqlCheckRules.get(0), rules.get(0));
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        assertEquals(Collections.emptyList(), sqlCheckService.getRules(session));
    }

    @Test
    public void testCheckWithEmptySqls() {
        List<OffsetString> sqls = new ArrayList<>();
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "TestDBName", sqls, new ConnectionConfig());
        assertEquals(Collections.emptyList(), checkViolations);
    }

    @Test
    public void testCheckWithSqls() {
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setType(RuleType.SQL_CHECK);
        rule.getMetadata().setName("TestRule");
        rule.getMetadata().setEnabled(true);
        rules.add(rule);

        SqlCheckRuleFactory factory = mock(SqlCheckRuleFactory.class);
        when(factory.create(rule)).thenReturn(new SqlCheckRule());

        rule.setSqlCheckRule(factory.create(rule));

        String sql = "TestSql";
        CheckViolation violation = new CheckViolation();
        violation.setLevel("HIGH");
        violation.setType("TestType");
        violation.setMessage("TestMessage");
        checkViolations = new ArrayList<>();
        checkViolations.add(violation);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString(0, sql));
        List<CheckViolation> checkResults = sqlCheckService.check(1L, "TestDBName", sqls, new ConnectionConfig());
        assertEquals(1, checkResults.size());
        assertEquals(violation, checkResults.get(0));
    }
}