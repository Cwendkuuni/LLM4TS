package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfigBuilder;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolationType;
import com.oceanbase.odc.service.regulation.ruleset.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactoryImpl;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.validator.SqlCheckReqValidator;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private SingleConnectionDataSource dataSource;
    @Mock
    private ConnectionSession session;
    @Mock
    private SkipAuthorize skipAuthorize;
    @Mock
    private Rule rule;
    @Mock
    private RuleMetadata metadata;
    @Mock
    private SqlCheckRuleFactory sqlCheckRuleFactory;
    @Mock
    private SqlCheckRuleFactoryImpl sqlCheckRuleFactoryImpl;
    @Mock
    private SqlCheckReq sqlCheckReq;
    @Mock
    private CheckResult checkResult;
    @Mock
    private CheckViolation checkViolation;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private OBConsoleDataSourceFactory factory;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private List<Rule> rules;
    private List<SqlCheckRule> sqlCheckRules;
    private List<CheckViolation> checkViolations;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(rule.getMetadata()).thenReturn(metadata);
        when(metadata.getName()).thenReturn("rule1");
        when(metadata.getType()).thenReturn(RuleType.SQL_CHECK);

        when(metadata.getLevel()).thenReturn(1);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Arrays.asList(rule));
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(skipAuthorize.isAuthorized(anyString(), any())).thenReturn(true);

        when(jdbcTemplate.query(anyString(), any()))
                .thenReturn(Collections.emptyList().toArray(new Object[0]));
        when(dataSource.getJdbcOperations()).thenReturn(jdbcTemplate);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(ConnectionSessionUtil.getSessionDialectType(session)).thenReturn(DialectType.MYSQL);

        sqlCheckRules = Arrays.asList(mock(SqlCheckRule.class), mock(SqlCheckRule.class));

        when(sqlCheckRules.get(0).check(anyString(), any(SqlCheckContext.class))).thenReturn(checkViolations);
        when(sqlCheckRules.get(1).check(anyString(), any(SqlCheckContext.class))).thenReturn(Collections.emptyList());

        when(factory.getDataSource()).thenReturn(dataSource);
        when(factory.getDataSource(eq("id"))).thenReturn(dataSource);
    }

    @Test
    public void testCheck_ConnectionSession_Success() {
        RulesBuilder rulesBuilder = new RulesBuilder();
        rules = rulesBuilder.build();

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);

        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);
        assertNotNull(checkResults);
        assertFalse(checkResults.isEmpty());

        CheckResult checkResult1 = checkResults.get(0);
        assertEquals(1, checkResult1.getCheckViolations().size());

        CheckViolation checkViolation1 = checkResult1.getCheckViolations().get(0);
        assertEquals(RuleViolationType.INJECT_SQL, checkViolation1.getViolationType());
        assertEquals("rule1", checkViolation1.getName());
        assertEquals(1, checkViolation1.getLevel());
        assertNotNull(checkViolation1.getStartLine());
        assertNotNull(checkViolation1.getEndLine());
    }

    @Test
    public void testCheck_Environment_Success() {
        RulesBuilder rulesBuilder = new RulesBuilder();
        rules = rulesBuilder.build();

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionConfig.getDialectType(), jdbcTemplate);
        assertNotNull(sqlCheckRules);

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);
        assertNotNull(checkViolations);
        assertFalse(checkViolations.isEmpty());

        CheckViolation checkViolation1 = checkViolations.get(0);
        assertEquals(RuleViolationType.INJECT_SQL, checkViolation1.getViolationType());
        assertEquals("rule1", checkViolation1.getName());
        assertEquals(1, checkViolation1.getLevel());
    }

    @Test
    public void testCheck_ConnectionSession_EmptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);
        assertNotNull(checkResults);
        assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheck_ConnectionSession_EmptyScript() {
        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("");

        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);
        assertNotNull(checkResults);
        assertTrue(checkResults.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheck_ConnectionSession_NullScript() {
        SqlCheckReq sqlCheckReq = null;

        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);
        assertNull(checkResults);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheck_ConnectionSession_NullSession() {
        SqlCheckReq sqlCheckReq = new SqlCheckReq();

        List<CheckResult> checkResults = sqlCheckService.check(null, sqlCheckReq);
        assertNull(checkResults);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheck_Environment_EmptyScripts() {
        List<OffsetString> sqls = Collections.emptyList();

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);
        assertNotNull(checkViolations);
        assertTrue(checkViolations.isEmpty());
    }

    @Test
    public void testGetRules() {
        RulesBuilder rulesBuilder = new RulesBuilder();
        rules = rulesBuilder.build();

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
        assertEquals(2, sqlCheckRules.size());

        sqlCheckRules = sqlCheckService.getRules(rules, session.getDialectType(), jdbcTemplate);
        assertNotNull(sqlCheckRules);
        assertEquals(2, sqlCheckRules.size());

        List<SqlCheckRule> sqlCheckRules1 = sqlCheckService.getRules(Collections.emptyList(), session);
        assertNull(sqlCheckRules1);

        List<SqlCheckRule> sqlCheckRules2 = sqlCheckService.getRules(Collections.emptyList(), session.getDialectType(), jdbcTemplate);
        assertNull(sqlCheckRules2);
    }

    @Test
    public void testFullFillRiskLevel() {
        RulesBuilder rulesBuilder = new RulesBuilder();
        rules = rulesBuilder.build();

        List<CheckViolation> checkViolations = new ArrayList<>();
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);
        assertNotNull(violatedRules);
        assertEquals(1, violatedRules.size());
        assertEquals("rule1", violatedRules.get(0).getMetadata().getName());

        Rule rule = new Rule();
        rule.setLevel(2);
        rule.setViolation(new RuleViolation());
        List<Rule> violatedRules1 = sqlCheckService.fullFillRiskLevel(Collections.singletonList(rule), checkViolations);
        assertNotNull(violatedRules1);
        assertEquals(1, violatedRules1.size());
        assertEquals(2, violatedRules1.get(0).getLevel());
    }
}

class RulesBuilder {
    public List<Rule> build() {
        Rule rule1 = new Rule();
        rule1.setMetadata(metadata);
        rule1.setEnabled(true);

        Rule rule2 = new Rule();
        rule2.setMetadata(metadata);
        rule2.setEnabled(false);

        List<Rule> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        return rules;
    }
}

class SqlCheckRule {
    // mock implementation
}