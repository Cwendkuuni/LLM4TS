package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
import org.mockito.stubbing.Answer;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlCheckReq;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

/**
 * Test class for SqlCheckService
 */
@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcOperations jdbcTemplate;

    private ConnectionSession session;
    private ConnectionConfig config;
    private Environment environment;
    private SqlCheckReq sqlCheckReq;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        session = Mockito.spy(new ConnectionSession("testSessionId", "testUsername", "testPassword", "testDatabase",
                ConnectionSessionConstants.CONSOLE_DS_KEY, null, null, null, null, null, null, null, null, null));
        config = Mockito.spy(new ConnectionConfig());
        environment = Mockito.spy(new Environment());
        sqlCheckReq = Mockito.spy(new SqlCheckReq());
    }

    @Test
    public void testCheck() {
        List<Rule> rules = Mockito.mock(List.class);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        Mockito.verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        assertNotNull(sqlCheckRules);
        Mockito.verifyNoMoreInteractions(ruleService);

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckResult result = new CheckResult();
        result.setCheckViolations(checkViolations);
        when(sqlCheckService.check(session, sqlCheckReq)).thenReturn(Collections.singletonList(result));
        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);
        assertNotNull(checkResults);
        assertEquals(1, checkResults.size());
        assertEquals(result, checkResults.get(0));
        Mockito.verify(sqlCheckService, times(1)).check(session, sqlCheckReq);
        Mockito.verifyNoMoreInteractions(sqlCheckService);
    }

    @Test
    public void testCheckWithEmptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);
        assertNotNull(checkResults);
        assertEquals(0, checkResults.size());
        Mockito.verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        Mockito.verifyNoMoreInteractions(ruleService);
    }

    @Test(expected = NullPointerException.class)
    public void testCheckWithNullSession() {
        sqlCheckService.check(null, sqlCheckReq);
    }

    @Test(expected = NullPointerException.class)
    public void testCheckWithNullSqlCheckReq() {
        sqlCheckService.check(session, null);
    }

    @Test
    public void testCheckWithEnvironmentIdAndDatabaseName() {
        when(environmentService.detail(anyLong())).thenReturn(environment);
        List<Rule> rules = Mockito.mock(List.class);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRuleFactory> sqlCheckRuleFactories = SqlCheckRules.getAllFactories(DialectType.MySQL, jdbcTemplate);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MySQL, jdbcTemplate);
        when(jdbcTemplate.query(anyString(), any(Class[].class), any(Map.class))).thenAnswer(new Answer<List>() {
            @Override
            public List answer(InvocationOnMock invocation) throws Throwable {
                return Collections.emptyList();
            }
        });
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("select * from testTable"));
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testDatabase", sqls, config);
        assertNotNull(checkViolations);
        assertEquals(0, checkViolations.size());
        Mockito.verify(environmentService, times(1)).detail(anyLong());
        Mockito.verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        Mockito.verify(jdbcTemplate, times(sqls.size())).query(anyString(), any(Class[].class), any(Map.class));
        Mockito.verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    public void testCheckWithEmptySqls() {
        when(environmentService.detail(anyLong())).thenReturn(environment);
        List<Rule> rules = Mockito.mock(List.class);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRuleFactory> sqlCheckRuleFactories = SqlCheckRules.getAllFactories(DialectType.MySQL, jdbcTemplate);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MySQL, jdbcTemplate);
        when(jdbcTemplate.query(anyString(), any(Class[].class), any(Map.class))).thenAnswer(new Answer<List>() {
            @Override
            public List answer(InvocationOnMock invocation) throws Throwable {
                return Collections.emptyList();
            }
        });
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testDatabase", Collections.emptyList(), config);
        assertNotNull(checkViolations);
        assertEquals(0, checkViolations.size());
        Mockito.verify(environmentService, times(1)).detail(anyLong());
        Mockito.verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        Mockito.verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = Mockito.mock(List.class);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        Mockito.verify(ruleService, times(0)).list(anyLong(), any(QueryRuleMetadataParams.class));
        assertNotNull(sqlCheckRules);
        Mockito.verifyNoMoreInteractions(ruleService);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules2 = sqlCheckService.getRules(rules, session);
        Mockito.verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        assertSame(sqlCheckRules, sqlCheckRules2);
        Mockito.verifyNoMoreInteractions(ruleService);
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        List<Rule> rules = Collections.emptyList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        Mockito.verify(ruleService, times(0)).list(anyLong(), any(QueryRuleMetadataParams.class));
        assertNotNull(sqlCheckRules);
        assertEquals(0, sqlCheckRules.size());
        Mockito.verifyNoMoreInteractions(ruleService);
    }

    @Test
    public void testGetRulesWithNullRules() {
        List<Rule> rules = null;
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        Mockito.verify(ruleService, times(0)).list(anyLong(), any(QueryRuleMetadataParams.class));
        assertNull(sqlCheckRules);
        Mockito.verifyNoMoreInteractions(ruleService);
    }

    @Test
    public void testGetRulesWithEmptySession() {
        List<Rule> rules = Mockito.mock(List.class);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, null);
        Mockito.verify(ruleService, times(0)).list(anyLong(), any(QueryRuleMetadataParams.class));
        assertNotNull(sqlCheckRules);
        assertEquals(0, sqlCheckRules.size());
        Mockito.verifyNoMoreInteractions(ruleService);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> checkViolations = new ArrayList<>();
        Rule rule = Mockito.mock(Rule.class);
        when(rule.getMetadata()).thenReturn(new RuleMetadata());
        when(rule.getMetadata().getType()).thenReturn(RuleType.SQL_CHECK);
        checkViolations.add(Mockito.mock(CheckViolation.class));
        sqlCheckService.fullFillRiskLevel(Collections.singletonList(rule), checkViolations);
        Mockito.verify(rule, times(1)).setLevel(anyInt());
        Mockito.verifyNoMoreInteractions(rule);

        RuleMetadata ruleMetadata = new RuleMetadata();
        ruleMetadata.setName("testRule");
        rule.setMetadata(ruleMetadata);
        sqlCheckService.fullFillRiskLevel(Collections.singletonList(rule), checkViolations);
        Mockito.verify(rule, times(1)).setLevel(anyInt());
        Mockito.verify(ruleMetadata, times(1)).setName("testRule");
        Mockito.verifyNoMoreInteractions(rule);
    }

    @Test
    public void testFullFillRiskLevelWithNullRule() {
        List<CheckViolation> checkViolations = new ArrayList<>();
        Rule rule = null;
        sqlCheckService.fullFillRiskLevel(Collections.singletonList(rule), checkViolations);
        Mockito.verifyNoInteractions(rule);
    }

    @Test
    public void testFullFillRiskLevelWithEmptyCheckViolations() {
        List<CheckViolation> checkViolations = Collections.emptyList();
        Rule rule = Mockito.mock(Rule.class);
        sqlCheckService.fullFillRiskLevel(Collections.singletonList(rule), checkViolations);
        Mockito.verifyNoInteractions(rule);
    }

}