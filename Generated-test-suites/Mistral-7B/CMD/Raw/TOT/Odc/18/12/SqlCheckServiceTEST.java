package com.oceanbase.odc.service.sqlcheck;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayName("SqlCheckService Test")
class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcOperations jdbcTemplate;
    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;
    @Mock
    private OBConsoleDataSourceFactory factory;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @MockitoSession
    private MockitoSession mockitoSession;

    private ConnectionSession session;
    private ConnectionConfig connectionConfig;
    private Environment env;
    private List<Rule> rules;
    private List<SqlCheckRule> sqlCheckRules;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        connectionConfig = mock(ConnectionConfig.class);
        env = mock(Environment.class);
        rules = Collections.singletonList(mock(Rule.class));
        sqlCheckRules = mock(List.class);
        when(ruleService.list(eq(1L), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ruleService.list(eq(env.getRulesetId()), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(jdbcTemplate.queryForList(anyString(), any(Map.class))).thenReturn(Collections.emptyList());
        when(factory.getDataSource()).thenReturn(singleConnectionDataSource);
        when(singleConnectionDataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);
    }

    @Test
    @DisplayName("Test check method with valid session and req")
    void testCheckWithValidSessionAndReq() {
        // Given
        SqlCheckReq req = new SqlCheckReq();
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        // When
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Then
        verify(ruleService, times(1)).list(eq(1L), any(QueryRuleMetadataParams.class));
        verifyNoMoreInteractions(ruleService);
        verify(sqlCheckService, times(1)).getRules(eq(rules), eq(DialectType.MySQL), eq(jdbcTemplate));
        verify(sqlChecker, times(1)).check(eq(Collections.singletonList(req.getScriptContent())), any(SqlCheckContext.class));
        verifyNoMoreInteractions(sqlChecker);
        verifyNoMoreInteractions(factory);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test check method with empty sqls")
    void testCheckWithEmptySqls() {
        // Given
        List<OffsetString> sqls = Collections.emptyList();
        when(environmentService.detail(eq(1L))).thenReturn(env);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(env.getRulesetId());

        // When
        List<CheckViolation> result = sqlCheckService.check(1L, "testDbName", sqls, connectionConfig);

        // Then
        verify(ruleService, times(1)).list(eq(env.getRulesetId()), any(QueryRuleMetadataParams.class));
        verifyNoMoreInteractions(ruleService);
        verifyNoMoreInteractions(factory);
        assertTrue(result.isEmpty());
    }

    @DisplayName("Test getRules method with valid rules and jdbcTemplate")
    @Test
    void testGetRulesWithValidRulesAndJdbcTemplate() {
        // Given
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRuleName");
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        List<SqlCheckRuleFactory> candidates = mock(List.class);
        SqlCheckRuleFactory factoryMock = mock(SqlCheckRuleFactory.class);
        when(SqlCheckRules.getAllFactories(eq(DialectType.MySQL), any(JdbcOperations.class))).thenReturn(candidates);
        when(candidates.get(0)).thenReturn(factoryMock);
        when(SqlCheckRules.createByRule(eq(candidates), eq(DialectType.MySQL), eq(rule))).thenReturn(new SqlCheckRule());

        // When
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, DialectType.MySQL, jdbcTemplate);

        // Then
        verify(SqlCheckRules, times(1)).getAllFactories(eq(DialectType.MySQL), eq(jdbcTemplate));
        verify(rule, times(1)).getMetadata();
        verify(candidates, times(1)).get(0);
        verify(factoryMock, times(1)).createByRule(eq(candidates), eq(DialectType.MySQL), eq(rule));
        assertNotNull(result);
        assertTrue(result.contains(new SqlCheckRule()));
    }

    @Test
    @DisplayName("Test fullFillRiskLevel method with valid checkViolations and rules")
    void testFullFillRiskLevelWithValidCheckViolationsAndRules() {
        // Given
        String ruleName = "testRuleName";
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName(ruleName);
        metadata.setLevel(1);
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setLevel(1);
        CheckViolation violation = new CheckViolation();
        violation.setRuleName(ruleName);

        // When
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);
        List<CheckViolation> result = sqlCheckService.fullFillRiskLevel(rules, Collections.singletonList(violation));

        // Then
        assertEquals(1, result.size());
        assertEquals(rule.getLevel(), result.get(0).getLevel());
    }
}