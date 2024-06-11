package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.ModelUnitTest;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = SqlCheckService.class)
public class SqlCheckServiceTest extends ModelUnitTest {

    private static final String RULE_SET_ID = "testRuleSetId";
    private static final String ENVIRONMENT_ID = "testEnvironmentId";
    private static final String DATABASE_NAME = "testDatabaseName";
    private static final ConnectionConfig CONNECTION_CONFIG = new ConnectionConfig();
    private static final DialectType DIALECT_TYPE = DialectType.MySQL;
    private static final String SCRIPT_CONTENT = "testScriptContent";
    private static final String SQL = "testSql";

    @Autowired
    private RuleService ruleService;
    @Autowired
    private EnvironmentService environmentService;
    @Autowired
    private SqlCheckService sqlCheckService;
    @Autowired
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<Rule> rules;
    private ConnectionSession session;
    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() throws Exception {
        rules = Arrays.asList(
                Mockito.spy(new Rule()),
                Mockito.spy(new Rule())
        );
        session = Mockito.mock(ConnectionSession.class);
        when(session.getId()).thenReturn(1L);
        when(session.getDialectType()).thenReturn(DIALECT_TYPE);
        when(session.getRuleSetId()).thenReturn(RULE_SET_ID);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        dataSource = Mockito.mock(SingleConnectionDataSource.class);
        when(obConsoleDataSourceFactory.getDataSource()).thenReturn(dataSource);

        when(ruleService.list(RULE_SET_ID, ArgumentMatchers.any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(environmentService.detail(ENVIRONMENT_ID)).thenReturn(new Environment());
    }

    @After
    public void tearDown() throws Exception {
        reset(ruleService, environmentService, sqlCheckService, obConsoleDataSourceFactory, jdbcTemplate);
    }

    @Test
    public void testCheck() throws Exception {
        // Given
        List<CheckViolation> expectedCheckViolations = Arrays.asList(
                Mockito.mock(CheckViolation.class),
                Mockito.mock(CheckViolation.class)
        );
        when(sqlCheckService.getRules(rules, session)).thenReturn(getRules(rules));
        when(sqlCheckService.fullFillRiskLevel(rules, expectedCheckViolations)).thenReturn(Collections.emptyList());
        when(sqlCheckService.check(session, Mockito.any(SqlCheckReq.class))).thenAnswer(invocation -> {
            SqlCheckReq req = invocation.getArgument(0);
            List<CheckResult> checkResults = sqlCheckService.check(session, req);
            List<CheckViolation> actualCheckViolations = checkResults.get(0).getCheckViolations();
            assertThat(actualCheckViolations, is(sameInstance(expectedCheckViolations)));
            return checkResults;
        });

        // When
        SqlCheckReq req = new SqlCheckReq();
        CheckResult checkResult = sqlCheckService.check(session, req);

        // Then
        assertThat(checkResult, notNullValue());
        assertThat(checkResult.getCheckViolations(), hasSize(2));
    }

    @Test
    public void testCheckWithEmptyRules() throws Exception {
        // Given
        when(ruleService.list(RULE_SET_ID, ArgumentMatchers.any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        // When
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Then
        assertThat(checkResults, notNullValue());
        assertThat(checkResults, empty());
    }

    @Test(expected = NullPointerException.class)
    public void testCheckWithNullSession() throws Exception {
        // Given
        SqlCheckReq req = new SqlCheckReq();

        // When
        sqlCheckService.check(null, req);

        // Then
        // NullPointerException is expected
    }

    @Test
    public void testCheckWithNullCheckReq() throws Exception {
        // Given
        ConnectionSession session = Mockito.mock(ConnectionSession.class);

        // When
        SqlCheckReq req = null;
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Then
        assertThat(checkResults, notNullValue());
        assertThat(checkResults, empty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithEmptyScriptContent() throws Exception {
        // Given
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        when(session.getId()).thenReturn(1L);
        when(session.getDialectType()).thenReturn(DIALECT_TYPE);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(null);

        // When & Then
        throwException(IllegalArgumentException.class, () -> sqlCheckService.check(session, req));
    }

    @Test
    public void testCheckWithEnvironment() throws Exception {
        // Given
        List<CheckViolation> expectedCheckViolations = Arrays.asList(
                Mockito.mock(CheckViolation.class),
                Mockito.mock(CheckViolation.class)
        );
        when(sqlCheckService.getRules(rules, DIALECT_TYPE, jdbcTemplate)).thenReturn(getRules(rules));
        when(sqlCheckService.fullFillRiskLevel(rules, expectedCheckViolations)).thenReturn(Collections.emptyList());
        when(environmentService.detail(ENVIRONMENT_ID)).thenReturn(new Environment());
        when(obConsoleDataSourceFactory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(new BasicDataSource().getConnection());
        when(sqlCheckService.check(ArgumentMatchers.anyLong(), DATABASE_NAME, ArgumentMatchers.anyList(),
                ArgumentMatchers.any(ConnectionConfig.class))).thenAnswer(invocation -> {
            List<OffsetString> sqls = invocation.getArgument(2);
            List<CheckViolation> actualCheckViolations = SqlCheckUtil.buildCheckViolations(sqls, expectedCheckViolations);
            assertThat(actualCheckViolations, is(sameInstance(expectedCheckViolations)));
            return actualCheckViolations;
        });

        // When
        List<CheckViolation> checkViolations = sqlCheckService.check(ENVIRONMENT_ID, DATABASE_NAME, Arrays.asList(new OffsetString(SQL)), CONNECTION_CONFIG);

        // Then
        assertThat(checkViolations, notNullValue());
        assertThat(checkViolations, hasSize(2));
    }

    @Test(expected = NullPointerException.class)
    public void testCheckWithNullEnvironmentId() throws Exception {
        // Given
        String databaseName = "testDatabaseName";
        List<OffsetString> sqls = Arrays.asList(new OffsetString(SQL));
        ConnectionConfig connectionConfig = CONNECTION_CONFIG;

        // When & Then
        throwException(NullPointerException.class, () -> sqlCheckService.check(null, databaseName, sqls, connectionConfig));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithEmptyDatabaseName() throws Exception {
        // Given
        Long environmentId = ENVIRONMENT_ID;
        List<OffsetString> sqls = Arrays.asList(new OffsetString(SQL));
        ConnectionConfig connectionConfig = CONNECTION_CONFIG;

        // When & Then
        throwException(IllegalArgumentException.class, () -> sqlCheckService.check(environmentId, "", sqls, connectionConfig));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithEmptySqls() throws Exception {
        // Given
        Long environmentId = ENVIRONMENT_ID;
        String databaseName = DATABASE_NAME;
        ConnectionConfig connectionConfig = CONNECTION_CONFIG;

        // When & Then
        throwException(IllegalArgumentException.class, () -> sqlCheckService.check(environmentId, databaseName, Collections.emptyList(), connectionConfig));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullConnectionConfig() throws Exception {
        // Given
        Long environmentId = ENVIRONMENT_ID;
        String databaseName = DATABASE_NAME;
        List<OffsetString> sqls = Arrays.asList(new OffsetString(SQL));

        // When & Then
        throwException(IllegalArgumentException.class, () -> sqlCheckService.check(environmentId, databaseName, sqls, null));
    }

    private List<SqlCheckRule> getRules(List<Rule> rules) {
        return SqlCheckService.getRules(rules, DIALECT_TYPE, jdbcTemplate);
    }
}