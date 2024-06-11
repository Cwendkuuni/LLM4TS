package com.oceanbase.odc.service.sqlcheck;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.jdbc.JdbcTemplate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
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
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckContext;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcOperations jdbc;

    @Mock
    private ConnectionSession session;

    @Mock
    private SingleConnectionDataSource dataSource;

    @Mock
    private OBConsoleDataSourceFactory factory;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck_withValidSessionAndRequest_returnsCheckResults() {
        // Arrange
        Long ruleSetId = 1L;
        List<Rule> rules = Arrays.asList(mock(Rule.class));
        when(ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = Arrays.asList(mock(SqlCheckRule.class));
        when(sqlCheckService.getRules(rules, eq(DialectType.MYSQL), any())).thenReturn(sqlCheckRules);
        List<CheckViolation> checkViolations = Arrays.asList(mock(CheckViolation.class));
        when(sqlChecker.check(eq("test script"), anyList())).thenReturn(checkViolations);

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getConnectionConfig()).thenReturn(connectionConfig);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbc);

        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> expectedCheckResults = Arrays.asList(new CheckResult());

        // Act
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Assert
        verify(ruleService, times(1)).list(ruleSetId, QueryRuleMetadataParams.builder().build());
        verify(sqlCheckService, times(1)).getRules(rules, eq(DialectType.MYSQL), any());
        verify(sqlChecker, times(1)).check(eq("test script"), anyList());
        verifyZeroInteractions(ruleService, environmentService);
        assertEquals(expectedCheckResults, checkResults);
    }

    @Test
    public void testCheck_withEmptyRules_returnsEmptyCheckResults() {
        // Arrange
        Long ruleSetId = 1L;
        List<Rule> rules = Collections.emptyList();
        when(ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = Collections.emptyList();

        // Act
        List<CheckResult> checkResults = sqlCheckService.check(session, new SqlCheckReq());

        // Assert
        verify(ruleService, times(1)).list(ruleSetId, QueryRuleMetadataParams.builder().build());
        verifyZeroInteractions(sqlCheckService, sqlChecker, jdbc, ruleService, environmentService);
        assertEquals(Collections.emptyList(), checkResults);
    }

    @Test
    public void testCheck_withEnvironmentIdAndValidSqls_returnsCheckViolations() {
        // Arrange
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Arrays.asList(mock(OffsetString.class));
        ConnectionConfig config = mock(ConnectionConfig.class);
        DialectType dialectType = DialectType.MYSQL;
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        JdbcTemplate jdbc = mock(JdbcTemplate.class);

        when(environmentService.detail(environmentId)).thenReturn(new Environment());
        when(ruleService.list(anyLong(), any())).thenReturn(Collections.emptyList());
        when(SqlCheckRules.getAllFactories(dialectType, jdbc)).thenReturn(Collections.emptyList());

        List<CheckViolation> checkViolations = Arrays.asList(mock(CheckViolation.class));
        when(sqlChecker.check(eq(sqls), any())).thenReturn(checkViolations);

        when(factory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(mock(java.sql.Connection.class));
        when(dataSource.getJdbcTemplate()).thenReturn(jdbc);

        // Act
        List<CheckViolation> actualCheckViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // Assert
        verify(environmentService, times(1)).detail(environmentId);
        verify(ruleService, times(1)).list(anyLong(), any());
        verify(SqlCheckRules, times(1)).getAllFactories(dialectType, jdbc);
        verify(sqlChecker, times(1)).check(eq(sqls), any());
        verifyZeroInteractions(ruleService, environmentService, sqlCheckService, jdbc, factory);
        assertEquals(checkViolations, actualCheckViolations);
    }

    @Test
    public void testGetRules_withValidSessionAndJdbcOperations_returnsSqlCheckRules() {
        // Arrange
        Long ruleSetId = 1L;
        List<Rule> rules = Arrays.asList(mock(Rule.class));
        when(ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRuleFactory> sqlCheckRuleFactories = Arrays.asList(mock(SqlCheckRuleFactory.class));
        List<SqlCheckRule> sqlCheckRules = Arrays.asList(mock(SqlCheckRule.class));

        when(ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        when(SqlCheckRules.getAllFactories(any(), any())).thenReturn(sqlCheckRuleFactories);

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getConnectionConfig()).thenReturn(connectionConfig);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        // Act
        List<SqlCheckRule> actualSqlCheckRules = sqlCheckService.getRules(session);

        // Assert
        verify(ruleService, times(1)).list(ruleSetId, QueryRuleMetadataParams.builder().build());
        verify(SqlCheckRules, times(1)).getAllFactories(eq(DialectType.MYSQL), any());
        verifyZeroInteractions(ruleService, sqlCheckRules, sqlChecker, jdbc, factory);
        assertEquals(sqlCheckRules, actualSqlCheckRules);
    }

    @Test
    public void testGetRules_withEmptyRules_returnsEmptySqlCheckRules() {
        // Arrange
        Long ruleSetId = 1L;
        List<Rule> rules = Collections.emptyList();

        // Act
        List<SqlCheckRule> actualSqlCheckRules = sqlCheckService.getRules(session);

        // Assert
        verify(ruleService, times(1)).list(ruleSetId, QueryRuleMetadataParams.builder().build());
        verifyZeroInteractions(ruleService, sqlCheckRules, sqlChecker, jdbc, factory);
        assertEquals(Collections.emptyList(), actualSqlCheckRules);
    }

    private SqlChecker sqlChecker = new DefaultSqlChecker(any(), any(), Collections.emptyList());

    private static class DefaultSqlChecker implements SqlChecker {
        private DialectType dialectType;
        private String delimiter;
        private List<SqlCheckRule> sqlCheckRules;

        public DefaultSqlChecker(DialectType dialectType, String delimiter, List<SqlCheckRule> sqlCheckRules) {
            this.dialectType = dialectType;
            this.delimiter = delimiter;
            this.sqlCheckRules = sqlCheckRules;
        }

        @Override
        public List<CheckViolation> check(List<String> scripts, SqlCheckContext context) {
            return Collections.emptyList();
        }

        @Override
        public List<CheckViolation> check(String script, SqlCheckContext context) {
            return Collections.emptyList();
        }
    }

    private static class SqlCheckContext implements SqlCheckContext {
        private final int batchSize;

        public SqlCheckContext(int batchSize) {
            this.batchSize = batchSize;
        }

        @Override
        public void addViolation(CheckViolation violation) {
        }

        @Override
        public void addViolations(List<CheckViolation> violations) {
        }

        @Override
        public int getBatchSize() {
            return batchSize;
        }
    }
}