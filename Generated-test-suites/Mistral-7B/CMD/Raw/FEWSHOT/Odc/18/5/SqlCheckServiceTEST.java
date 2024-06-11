package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import com.oceanbase.odc.service.regulation.ruleset.Model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.DefaultSqlChecker;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlCheckServiceTest {
    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private OBConsoleDataSourceFactory factory;
    @Mock
    private SingleConnectionDataSource dataSource;
    @InjectMocks
    private SqlCheckService sqlCheckService;
    private ConnectionSession session;
    private ConnectionConfig config;
    private Environment environment;
    private List<Rule> rules;
    private List<SqlCheckRule> sqlCheckRules;
    private List<CheckViolation> checkViolations;
    private List<CheckResult> checkResults;
    private List<OffsetString> sqls;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        config = mock(ConnectionConfig.class);
        environment = mock(Environment.class);
        rules = Collections.singletonList(mock(Rule.class));
        sqlCheckRules = Collections.singletonList(mock(SqlCheckRule.class));
        checkViolations = Collections.singletonList(mock(CheckViolation.class));
        checkResults = Collections.singletonList(mock(CheckResult.class));
        sqls = Collections.singletonList(new OffsetString("testSql"));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(jdbcTemplate.queryTemplate(anyString(), anyArray())).thenReturn(new Object()[0]);
        when(factory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);
    }

    @Test
    public void testCheck() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(getRules(rules, eq(DialectType.MYSQL), any(JdbcOperations.class))).thenReturn(sqlCheckRules);
        when(sqlCheckRules.check(anyString())).thenReturn(checkViolations);
        when(fullFillRiskLevel(rules, anyList())).thenReturn(rules);
        when(SqlCheckUtil.buildCheckResults(anyList())).thenReturn(checkResults);

        assertEquals(checkResults, sqlCheckService.check(session, new SqlCheckReq()));

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(getRules(anyList(), eq(DialectType.MYSQL), any(JdbcOperations.class))).once();
        verify(sqlCheckRules, times(1)).check(anyString());
        verify(fullFillRiskLevel(anyList(), anyList())).once();
        verify(SqlCheckUtil, times(1)).buildCheckResults(anyList());
    }

    @Test
    public void testCheckWithEnvironment() {
        when(environment.getRulesetId()).thenReturn(1L);
        when(factory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);

        assertEquals(Collections.emptyList(), sqlCheckService.check(null, null, Collections.emptyList(), config));

        verify(factory, times(1)).getDataSource();
        verify(jdbcTemplate, times(0)).queryTemplate(anyString(), anyArray());
        verify(ruleService, times(0)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheckWithEmptyRules() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        assertEquals(Collections.emptyList(), sqlCheckService.check(session, new SqlCheckReq()));

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(getRules(anyList(), eq(DialectType.MYSQL), any(JdbcOperations.class))).never();
    }

    @Test
    public void testCheckWithNullEnvironment() {
        when(environmentService.detail(anyLong())).thenReturn(null);

        assertNull(sqlCheckService.check(null, null, Collections.emptyList(), config));

        verify(environmentService, times(1)).detail(anyLong());
        verify(ruleService, times(0)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testGetRules() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> rules1 = sqlCheckService.getRules(session);
        List<SqlCheckRule> rules2 = sqlCheckService.getRules(session, DialectType.MYSQL, jdbcTemplate);

        assertEquals(rules1, rules2);
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        assertTrue(CollectionUtils.isEmpty(sqlCheckService.getRules(session)));
        assertTrue(CollectionUtils.isEmpty(sqlCheckService.getRules(session, DialectType.MYSQL, jdbcTemplate)));
    }

    @Test
    public void testGetRulesWithEmptyJdbcTemplate() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(null);

        assertTrue(CollectionUtils.isEmpty(sqlCheckService.getRules(session)));
        assertTrue(CollectionUtils.isEmpty(sqlCheckService.getRules(session, DialectType.MYSQL, jdbcTemplate)));
    }

    @Test
    public void testGetRulesWithNullSession() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> rules1 = sqlCheckService.getRules(null);
        List<SqlCheckRule> rules2 = sqlCheckService.getRules(null, DialectType.MYSQL, jdbcTemplate);

        assertEquals(0, rules1.size());
        assertEquals(0, rules2.size());
    }

    @Test
    public void testFullFillRiskLevelWithEmptyCheckViolations() {
        List<CheckViolation> emptyCheckViolations = Collections.emptyList();
        when(fullFillRiskLevel(rules, emptyCheckViolations)).thenReturn(rules);

        assertEquals(rules, sqlCheckService.fullFillRiskLevel(rules, emptyCheckViolations));
    }

    @Test
    public void testFullFillRiskLevelWithCheckViolations() {
        when(fullFillRiskLevel(rules, checkViolations)).thenReturn(rules);

        assertEquals(rules, sqlCheckService.fullFillRiskLevel(rules, checkViolations));
    }
}