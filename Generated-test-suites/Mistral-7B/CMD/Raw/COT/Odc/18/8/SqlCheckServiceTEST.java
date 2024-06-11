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
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.service.DefaultSqlChecker;

/**
 * {@link SqlCheckService} Test Class
 *
 * @author yh263208
 * @date 2023-03-20 14:45
 */
@Slf4j
public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private SingleConnectionDataSource dataSource;
    private JdbcOperations jdbcOperations;
    private ConnectionSession connectionSession;

    @Before
    public void setup() {
        ruleService = Mockito.mock(RuleService.class);
        environmentService = Mockito.mock(EnvironmentService.class);
        dataSource = Mockito.mock(SingleConnectionDataSource.class);
        jdbcOperations = new JdbcTemplate(dataSource);
        connectionSession = Mockito.mock(ConnectionSession.class);

        SqlCheckRules sqlCheckRules = Mockito.mock(SqlCheckRules.class);
        when(sqlCheckRules.getAllFactories(Mockito.any(DialectType.class), Mockito.any(JdbcOperations.class)))
                .thenReturn(Collections.singletonList(Mockito.mock(SqlCheckRuleFactory.class)));

        sqlCheckService = new SqlCheckService() {
            {
                this.ruleService = ruleService;
                this.environmentService = environmentService;
                this.ruleService = ruleService;
                this.ruleCheckRules = sqlCheckRules;
            }
        };
        when(connectionSession.getDialectType()).thenReturn(DialectType.MySQL);
        when(connectionSession.getSyncJdbcExecutor(Mockito.anyString())).thenReturn(jdbcOperations);
    }

    @Test
    public void testCheck() {
        Long ruleSetId = 1L;
        List<Rule> rules = Collections.singletonList(Mockito.mock(Rule.class));

        when(ruleService.list(ruleSetId, Mockito.any(QueryRuleMetadataParams.class))).thenReturn(rules);

        SqlCheckReq req = new SqlCheckReq();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        List<CheckResult> actualCheckResults = sqlCheckService.check(session, req);

        verify(ruleService, times(1)).list(ruleSetId, Mockito.any(QueryRuleMetadataParams.class));
        verify(ruleService, times(1)).list(eq(session.getRuleSetId()), Mockito.any(QueryRuleMetadataParams.class));
        verifyNoMoreInteractions(ruleService);
        verifyNoMoreInteractions(environmentService);
        assertEquals(0, actualCheckResults.size());
    }

    @Test
    public void testCheckWithEmptySqls() {
        Long environmentId = 1L;
        String databaseName = "test";
        ConnectionConfig config = Mockito.mock(ConnectionConfig.class);
        List<OffsetString> sqls = Collections.emptyList();

        Environment env = Mockito.mock(Environment.class);
        when(environmentService.detail(environmentId)).thenReturn(env);
        when(env.getRulesetId()).thenReturn(1L);

        List<CheckViolation> actualCheckViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        verify(environmentService, times(1)).detail(environmentId);
        verify(env, times(1)).getRulesetId();
        verifyNoMoreInteractions(ruleService);
        verifyNoMoreInteractions(environmentService);
        assertEquals(0, actualCheckViolations.size());
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        List<Rule> rules = Collections.emptyList();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);

        List<SqlCheckRule> actualCheckRules = sqlCheckService.getRules(rules, session);

        verify(ruleService, times(0)).list(eq(session.getRuleSetId()), Mockito.any(QueryRuleMetadataParams.class));
        assertEquals(0, actualCheckRules.size());
    }

    @Test
    public void testGetRulesWithRules() {
        List<Rule> rules = Collections.singletonList(Mockito.mock(Rule.class));
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        ConnectionSession session1 = Mockito.mock(ConnectionSession.class);
        Rule rule = Mockito.mock(Rule.class);
        RuleMetadata metadata = Mockito.mock(RuleMetadata.class);

        when(rule.getMetadata()).thenReturn(metadata);
        when(metadata.getType()).thenReturn(RuleType.SQL_CHECK);

        when(ruleService.list(eq(session.getRuleSetId()), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ruleService.list(eq(session1.getRuleSetId()), Mockito.any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.emptyList());

        List<SqlCheckRule> actualCheckRules = sqlCheckService.getRules(rules, session);

        verify(ruleService, times(1)).list(eq(session.getRuleSetId()), Mockito.any(QueryRuleMetadataParams.class));
        verifyNoMoreInteractions(ruleService);
        assertEquals(1, actualCheckRules.size());
    }

    @Test(expected = NullPointerException.class)
    public void testGetRulesWithNullRules() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);

        List<SqlCheckRule> actualCheckRules = sqlCheckService.getRules(null, session);

        verifyNoInteractions(ruleService);
        verifyNoInteractions(environmentService);
        throw new AssertionError("Expected null pointer exception but was not thrown.");
    }

    @Test
    public void testFullFillRiskLevelWithEmptyViolations() {
        List<Rule> rules = Collections.singletonList(Mockito.mock(Rule.class));
        List<CheckViolation> violations = Collections.emptyList();

        List<Rule> actualViolatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        verify(ruleService, times(0)).list(eq(session.getRuleSetId()), Mockito.any(QueryRuleMetadataParams.class));
        assertEquals(0, actualViolatedRules.size());
    }

    @Test
    public void testFullFillRiskLevelWithViolations() {
        List<Rule> rules = Collections.singletonList(Mockito.mock(Rule.class));
        List<CheckViolation> violations = new ArrayList<>();
        RuleMetadata metadata = Mockito.mock(RuleMetadata.class);
        Rule rule = Mockito.mock(Rule.class);
        RuleViolation violation = Mockito.mock(RuleViolation.class);

        when(metadata.getType()).thenReturn(RuleType.SQL_CHECK);
        when(rule.getMetadata()).thenReturn(metadata);
        when(violation.getType()).thenReturn(RuleViolation.Type.SQL_CHECK);

        SqlCheckRule sqlCheckRule = Mockito.mock(SqlCheckRule.class);
        when(sqlCheckRule.getRule()).thenReturn(rule);
        when(sqlCheckRule.getViolation(Mockito.any(CheckViolation.class))).thenReturn(violation);

        Rule rule1 = Mockito.mock(Rule.class);
        when(rule1.getMetadata()).thenReturn(metadata);

        violations.add(new CheckViolation());
        violations.add(new CheckViolation());

        List<Rule> actualViolatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        verify(ruleService, times(1)).list(eq(session.getRuleSetId()), Mockito.any(QueryRuleMetadataParams.class));
        assertEquals(2, actualViolatedRules.size());
    }
}