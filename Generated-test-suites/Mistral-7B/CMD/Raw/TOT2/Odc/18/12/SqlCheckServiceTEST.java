package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;

/**
 * Test class for {@link SqlCheckService}
 */
public class SqlCheckServiceTest {

    private static final String RULE_SET_ID = "ruleSetId";
    private static final String ENVIRONMENT_ID = "environmentId";
    private static final String DATABASE_NAME = "databaseName";
    private static final List<OffsetString> SQLS = Collections.singletonList(new OffsetString("SELECT * FROM tableName;"));
    private static final ConnectionConfig CONFIG = new ConnectionConfig();

    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        sqlCheckService = new SqlCheckService();
        ruleService = Mockito.mock(RuleService.class);
        environmentService = Mockito.mock(EnvironmentService.class);
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);

        when(ruleService.list(RULE_SET_ID, QueryRuleMetadataParams.builder().build()))
                .thenReturn(Collections.singletonList(new Rule()));
        when(environmentService.detail(ENVIRONMENT_ID)).thenReturn(new Environment());

        SqlCheckService sqlCheckService = spy(new SqlCheckService());
        doReturn(ruleService).when(sqlCheckService).ruleService;
        doReturn(environmentService).when(sqlCheckService).environmentService;
        doReturn(jdbcTemplate).when(sqlCheckService).getDataSource().getJdbcTemplate();
    }

    /**
     * Test method for {@link SqlCheckService#check(ConnectionSession, SqlCheckReq)}
     */
    @Test
    public void testCheck() {
        SqlCheckReq req = new SqlCheckReq();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);

        List<CheckResult> result = sqlCheckService.check(session, req);
        verify(ruleService, times(1)).list(RULE_SET_ID, QueryRuleMetadataParams.builder().build());
        verify(environmentService, times(1)).detail(ENVIRONMENT_ID);
        verify(jdbcTemplate, never()).check(anyString(), anyList(), any(SqlCheckContext.class));
        assertEquals(Collections.emptyList(), result);
    }

    /**
     * Test method for {@link SqlCheckService#check(Long, String, List, ConnectionConfig)}
     */
    @Test
    public void testCheckWithEnvironment() {
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        when(jdbcTemplate.check(anyString(), anyList(), any(SqlCheckContext.class)))
                .thenReturn(violations);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setMetadata().setLevel(1);
        rule.setMetadata().setType(RuleType.SQL_CHECK);
        rules.add(rule);
        when(ruleService.list(ENVIRONMENT_ID, QueryRuleMetadataParams.builder().build()))
                .thenReturn(rules);

        List<CheckResult> result = sqlCheckService.check(ENVIRONMENT_ID, DATABASE_NAME, SQLS, CONFIG);
        verify(ruleService, times(1)).list(ENVIRONMENT_ID, QueryRuleMetadataParams.builder().build());
        verify(environmentService, times(1)).detail(ENVIRONMENT_ID);
        verify(jdbcTemplate, times(1)).check(anyString(), anyList(), any(SqlCheckContext.class));
        assertEquals(1, result.size());
        CheckResult checkResult = result.get(0);
        assertEquals(RuleViolation.fromCheckViolation(violations.get(0)), checkResult.getViolations().get(0));
    }

    /**
     * Test method for {@link SqlCheckService#getRules(List, ConnectionSession)}
     */
    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setMetadata().setLevel(1);
        rule.setMetadata().setType(RuleType.SQL_CHECK);
        rules.add(rule);
        SqlCheckRuleFactory ruleFactory = Mockito.mock(SqlCheckRuleFactory.class);
        when(ruleFactory.create(any(Rule.class), any(DialectType.class), any(JdbcOperations.class)))
                .thenReturn(new SqlCheckRule());
        List<SqlCheckRuleFactory> candidates = Collections.singletonList(ruleFactory);

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, Mockito.mock(ConnectionSession.class));
        verify(ruleFactory, times(1)).create(rule, Mockito.any(DialectType.class), Mockito.any(JdbcOperations.class));
        assertEquals(1, result.size());
    }

    /**
     * Test method for {@link SqlCheckService#getRules(List, DialectType, JdbcOperations)}
     */
    @Test
    public void testGetRulesWithDialectTypeAndJdbcOperations() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setMetadata().setLevel(1);
        rule.setMetadata().setType(RuleType.SQL_CHECK);
        rules.add(rule);
        SqlCheckRuleFactory ruleFactory = Mockito.mock(SqlCheckRuleFactory.class);
        when(ruleFactory.create(any(Rule.class), any(DialectType.class), any(JdbcOperations.class)))
                .thenReturn(new SqlCheckRule());
        List<SqlCheckRuleFactory> candidates = Collections.singletonList(ruleFactory);

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);
        verify(ruleFactory, times(1)).create(rule, Mockito.eq(DialectType.MYSQL), Mockito.any(JdbcOperations.class));
        assertEquals(1, result.size());
    }

    /**
     * Test method for {@link SqlCheckService#fullFillRiskLevel(List, List)}
     */
    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = Collections.singletonList(new CheckViolation());
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setMetadata().setLevel(1);
        rule.setMetadata().setType(RuleType.SQL_CHECK);
        rules.add(rule);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(1, result.size());
        CheckViolation violation = violations.get(0);
        Rule ruleFromResult = result.get(0);
        assertEquals(violation.getLevel(), ruleFromResult.getLevel());
        assertEquals(RuleViolation.fromCheckViolation(violation), ruleFromResult.getViolation());
    }
}