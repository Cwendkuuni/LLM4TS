package com.oceanbase.odc.service.sqlcheck.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.service.DefaultSqlChecker;

class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcOperations jdbc;
    @Mock
    private OBConsoleDataSourceFactory factory;
    @Mock
    private SingleConnectionDataSource dataSource;
    @Mock
    private ConnectionSession session;
    @Mock
    private ConnectionConfig config;
    @Mock
    private Environment env;
    @Mock
    private SqlChecker sqlChecker;
    @Mock
    private DefaultSqlChecker defaultSqlChecker;
    @Mock
    private SqlCheckContext checkContext;
    @Mock
    private List<Rule> rules;
    @Mock
    private List<CheckViolation> checkViolations;
    @Mock
    private List<SqlCheckRule> sqlCheckRules;
    @Mock
    private SqlCheckRuleFactory sqlCheckRuleFactory;
    @Mock
    private SqlCheckRule sqlCheckRule;

    private SqlCheckService sqlCheckService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }

    @Test
    public void testCheck() {
        when(ruleService.list(anyLong(), ArgumentMatchers.eq(QueryRuleMetadataParams.builder().build())))
                .thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(env);
        when(config.getDialectType()).thenReturn(DialectType.MySQL);
        when(factory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getJdbcTemplate()).thenReturn(jdbc);
        when(jdbc.queryForList(anyString(), ArgumentMatchers.any()))
                .thenReturn(Collections.emptyList());
        when(sqlCheckRules.stream()).thenReturn(Arrays.asList(sqlCheckRule).stream());
        when(sqlCheckRuleFactory.create(any(), any(), any()))
                .thenReturn(sqlCheckRule);
        when(sqlChecker.check(anyList(), ArgumentMatchers.eq(checkContext)))
                .thenReturn(checkViolations);
        when(defaultSqlChecker.check(anyList(), ArgumentMatchers.eq(checkContext))).thenCallRealMethod();

        CheckResult result = sqlCheckService.check(session,
                new SqlCheckReq("scriptContent", "delimiter", 1L, Collections.singletonList("ruleId")));
        assertEquals(1, result.getCheckViolations().size());
        verify(ruleService).list(1L, ArgumentMatchers.eq(QueryRuleMetadataParams.builder().build()));
        verify(environmentService).detail(1L);
        verify(factory).getDataSource();
        verify(dataSource).getJdbcTemplate();
        verify(jdbc).queryForList(anyString(), ArgumentMatchers.any());
        verify(sqlCheckRules).stream();
        verify(sqlCheckRuleFactory).create(any(), any(), any());
        verify(sqlChecker).check(ArgumentMatchers.eq(Collections.singletonList("scriptContent")),
                ArgumentMatchers.eq(checkContext));
    }

    @Test
    public void testCheckWithEmptyRules() {
        when(ruleService.list(anyLong(), ArgumentMatchers.eq(QueryRuleMetadataParams.builder().build())))
                .thenReturn(Collections.emptyList());

        assertNull(sqlCheckService.check(session, new SqlCheckReq("scriptContent", "delimiter", 1L,
                Collections.singletonList("ruleId"))));
        verify(ruleService, never())
                .list(ArgumentMatchers.eq(ConnectionSessionUtil.getRuleSetId(session)),
                        ArgumentMatchers.eq(QueryRuleMetadataParams.builder().build()));
    }

    @Test
    public void testCheckWithEmptySqls() {
        when(config.getDialectType()).thenReturn(DialectType.MySQL);
        when(factory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getJdbcTemplate()).thenReturn(jdbc);
        when(ruleService.list(env.getRulesetId(), ArgumentMatchers.eq(QueryRuleMetadataParams.builder().build())))
                .thenReturn(rules);

        assertEmpty(sqlCheckService.check(1L, "databaseName", Collections.emptyList(), config));
        verify(factory).getDataSource();
        verify(dataSource).getJdbcTemplate();
        verify(ruleService, never())
                .list(ArgumentMatchers.eq(env.getRulesetId()), ArgumentMatchers.eq(QueryRuleMetadataParams.builder().build()));
    }

    @Test
    public void testCheckWithEmptyCheckViolations() {
        when(config.getDialectType()).thenReturn(DialectType.MySQL);
        when(factory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getJdbcTemplate()).thenReturn(jdbc);
        when(ruleService.list(env.getRulesetId(), ArgumentMatchers.eq(QueryRuleMetadataParams.builder().build())))
                .thenReturn(rules);
        when(sqlChecker.check(anyList(), ArgumentMatchers.eq(checkContext)))
                .thenReturn(Collections.emptyList());

        assertEmpty(sqlCheckService.check(1L, "databaseName", Arrays.asList("sql1", "sql2"), config));
        verify(factory).getDataSource();
        verify(dataSource).getJdbcTemplate();
        verify(ruleService).list(ArgumentMatchers.eq(env.getRulesetId()),
                ArgumentMatchers.eq(QueryRuleMetadataParams.builder().build()));
        verify(sqlChecker).check(ArgumentMatchers.anyList(), ArgumentMatchers.eq(checkContext));
    }

    @Test
    public void testGetRules() {
        when(config.getDialectType()).thenReturn(DialectType.MySQL);
        when(jdbc.queryForList(anyString(), ArgumentMatchers.any()))
                .thenReturn(Collections.emptyList());
        when(ruleService.list(rules, DialectType.MySQL, jdbc))
                .thenReturn(sqlCheckRules);

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);
        assertEquals(1, result.size());
        verify(ruleService).list(rules, DialectType.MySQL, jdbc);
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        when(config.getDialectType()).thenReturn(DialectType.MySQL);
        when(ruleService.list(rules, DialectType.MySQL, jdbc)).thenReturn(Collections.emptyList());

        assertEmpty(sqlCheckService.getRules(rules, session));
        verify(ruleService, never())
                .list(ArgumentMatchers.isA(List.class), DialectType.MySQL, jdbc);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = new ArrayList<>();
        RuleMetadata metadata1 = new RuleMetadata("rule1", RuleType.SQL_CHECK, 1, 1, 1);
        RuleMetadata metadata2 = new RuleMetadata("rule2", RuleType.SQL_CHECK, 1, 1, 1);
        Rule rule1 = new Rule();
        Rule rule2 = new Rule();
        CheckViolation violation1 = new CheckViolation("violation1", 1, "type1", RuleViolation.RISK_LOW);
        CheckViolation violation2 = new CheckViolation("violation2", 1, "type2", RuleViolation.RISK_MEDIUM);
        violation1.setRuleMetadata(metadata1);
        violation2.setRuleMetadata(metadata2);
        violations.add(violation1);
        violations.add(violation2);

        when(rules.get(0).getMetadata()).thenReturn(metadata1);
        when(rules.get(1).getMetadata()).thenReturn(metadata2);

        sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(2, violations.size());
        verify(rules, atLeast(2)).getMetadata();
    }

    @Test
    public void testFullFillRiskLevelWithEmptyRules() {
        List<CheckViolation> violations = new ArrayList<>();

        sqlCheckService.fullFillRiskLevel(Collections.emptyList(), violations);
        assertEmpty(violations);
        verify(rules, never()).getMetadata();
    }
}