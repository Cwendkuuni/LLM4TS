package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;

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
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.DefaultSqlChecker;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckContext;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcOperations jdbcOperations;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;

    @Mock
    private OBConsoleDataSourceFactory factory;

    @Mock
    private Environment environment;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private SqlChecker sqlChecker;

    @Mock
    private SqlCheckContext checkContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class)))
                .thenReturn(Arrays.asList(new Rule()));
        Mockito.when(environmentService.detail(Mockito.anyLong())).thenReturn(environment);
        Mockito.when(environment.getRulesetId()).thenReturn(1L);
        Mockito.when(factory.getDataSource()).thenReturn(singleConnectionDataSource);
        Mockito.when(singleConnectionDataSource.getJdbcTemplate()).thenReturn(jdbcOperations);
        Mockito.when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        Mockito.when(connectionSession.getSyncJdbcExecutor(Mockito.anyString())).thenReturn(jdbcOperations);
        Mockito.when(connectionSession.getRuleSetId()).thenReturn(1L);
        Mockito.when(jdbcOperations.queryForList(Mockito.anyString(), Mockito.any())).thenReturn(Collections.emptyList());
        Mockito.when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(null);
        Mockito.when(sqlCheckService.getRules(Mockito.anyList(), Mockito.eq(DialectType.MYSQL), Mockito.eq(jdbcOperations)))
                .thenReturn(Collections.emptyList());
        Mockito.when(sqlChecker.check(Mockito.anyList(), Mockito.eq(checkContext))).thenReturn(Collections.emptyList());
    }

    @Test
    public void testCheck() {
        // Given
        SqlCheckReq req = new SqlCheckReq();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Mockito.when(session.getRuleSetId()).thenReturn(1L);

        // When
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Then
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertFalse(result.isEmpty());
    }

    @Test
    public void testCheckWithEmptyRules() {
        // Given
        SqlCheckReq req = new SqlCheckReq();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Mockito.when(session.getRuleSetId()).thenReturn(null);
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.emptyList());

        // When
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithEmptyScript() {
        // Given
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Mockito.when(session.getRuleSetId()).thenReturn(1L);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(null);

        // When
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Then
        assertTrue(CollectionUtils.isEmpty(result));
    }

    @Test
    public void testCheckWithEmptySqls() {
        // Given
        Environment environment = Mockito.mock(Environment.class);
        Mockito.when(environment.getRulesetId()).thenReturn(1L);
        SingleConnectionDataSource singleConnectionDataSource = Mockito.mock(SingleConnectionDataSource.class);
        Mockito.when(singleConnectionDataSource.getJdbcTemplate()).thenReturn(jdbcOperations);
        OBConsoleDataSourceFactory factory = Mockito.mock(OBConsoleDataSourceFactory.class);
        Mockito.when(factory.getDataSource()).thenReturn(singleConnectionDataSource);
        SqlCheckContext checkContext = Mockito.mock(SqlCheckContext.class);
        List<OffsetString> sqls = Collections.emptyList();

        // When
        List<CheckViolation> violations = sqlCheckService.check(environment.getId(), "databaseName", sqls, connectionConfig);

        // Then
        assertTrue(CollectionUtils.isEmpty(violations));
    }

    @Test
    public void testGetRules() {
        // Given
        List<Rule> rules = Arrays.asList(new Rule());

        // When
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcOperations);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        // Given
        List<Rule> rules = Collections.emptyList();

        // When
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcOperations);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        // Given
        List<CheckViolation> violations = Arrays.asList(new CheckViolation());
        Rule rule = new Rule();
        rule.setLevel(1);
        Rule newRule = new Rule();
        newRule.setLevel(1);

        // When
        List<Rule> result = sqlCheckService.fullFillRiskLevel(Arrays.asList(rule), violations);

        // Then
        assertEquals(1, result.size());
        assertEquals(rule, result.get(0));
    }

    @Test
    public void testFullFillRiskLevelWithEmptyViolations() {
        // Given
        List<Rule> rules = Arrays.asList(new Rule());
        List<CheckViolation> violations = Collections.emptyList();

        // When
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        // Then
        assertEquals(0, result.size());
    }
}