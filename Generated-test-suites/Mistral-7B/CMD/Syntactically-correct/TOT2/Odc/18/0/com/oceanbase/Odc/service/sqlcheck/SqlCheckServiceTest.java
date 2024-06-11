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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@Slf4j
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcOperations jdbcOperations;

    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private OBConsoleDataSourceFactory factory;

    @Mock
    private Environment environment;

    @Mock
    private ConnectionConfig connectionConfig;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        // Given
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.singletonList(new Rule()));
        when(factory.getDataSource()).thenReturn(singleConnectionDataSource);
        when(singleConnectionDataSource.getJdbcTemplate()).thenReturn(jdbcOperations);

        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MySQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);
        when(session.getRuleSetId()).thenReturn(1L);

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        sqlCheckRules.add(mock(SqlCheckRule.class));
        when(sqlCheckService.getRules(anyList(), any(DialectType.class), any(JdbcOperations.class))).thenReturn(sqlCheckRules);

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(RuleType.SQL_CHECK);
        checkViolations.add(violation);
        when(jdbcOperations.query(anyString(), anyArray())).thenReturn(checkViolations.toArray(new CheckViolation[0]));

        // When
        List<CheckResult> result = sqlCheckService.check(connectionSession, new SqlCheckReq());

        // Then
        verify(ruleService, times(1)).list(1L, QueryRuleMetadataParams.builder().build());
        verify(factory, times(1)).getDataSource();
        verify(singleConnectionDataSource, times(1)).getJdbcTemplate();
        verify(connectionSession, times(1)).getDialectType();
        verify(connectionSession, times(1)).getRuleSetId();
        verify(sqlCheckService, times(1)).getRules(Collections.singletonList(new Rule()), DialectType.MySQL, jdbcOperations);
        verify(jdbcOperations, times(1)).query(anyString(), (Object[]) anyArray());
        assertEquals(Collections.singletonList(result), SqlCheckUtil.buildCheckResults(checkViolations));
    }

    @Test
    public void testCheckWithEmptyRules() {
        // Given
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        // When
        List<CheckResult> result = sqlCheckService.check(connectionSession, new SqlCheckReq());

        // Then
        verify(ruleService, times(1)).list(1L, QueryRuleMetadataParams.builder().build());
        verifyZeroInteractions(sqlCheckService);
        verifyZeroInteractions(factory);
        verifyZeroInteractions(singleConnectionDataSource);
        verifyZeroInteractions(jdbcOperations);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCheckWithEmptySqlCheckRules() {
        // Given
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.singletonList(new Rule()));
        when(factory.getDataSource()).thenReturn(singleConnectionDataSource);
        when(singleConnectionDataSource.getJdbcTemplate()).thenReturn(jdbcOperations);

        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MySQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);
        when(session.getRuleSetId()).thenReturn(1L);

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        when(sqlCheckService.getRules(anyList(), any(DialectType.class), any(JdbcOperations.class))).thenReturn(sqlCheckRules);

        // When
        List<CheckResult> result = sqlCheckService.check(connectionSession, new SqlCheckReq());

        // Then
        verify(ruleService, times(1)).list(1L, QueryRuleMetadataParams.builder().build());
        verify(factory, times(1)).getDataSource();
        verify(singleConnectionDataSource, times(1)).getJdbcTemplate();
        verify(connectionSession, times(1)).getDialectType();
        verify(connectionSession, times(1)).getRuleSetId();
        verify(sqlCheckService, times(1)).getRules(Collections.singletonList(new Rule()), DialectType.MySQL, jdbcOperations);
        verifyZeroInteractions(jdbcOperations);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCheckWithException() {
        // Given
        when(environmentService.detail(anyLong())).thenThrow(new RuntimeException());

        // When
        List<CheckResult> result = sqlCheckService.check(connectionSession, new SqlCheckReq());

        // Then
        verify(environmentService, times(1)).detail(anyLong());
        verifyZeroInteractions(ruleService);
        verifyZeroInteractions(factory);
        verifyZeroInteractions(singleConnectionDataSource);
        verifyZeroInteractions(jdbcOperations);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCheckWithEmptySqls() {
        // Given
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.singletonList(new Rule()));
        when(factory.getDataSource()).thenReturn(singleConnectionDataSource);
        when(singleConnectionDataSource.getJdbcTemplate()).thenReturn(jdbcOperations);

        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MySQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);
        when(session.getRuleSetId()).thenReturn(1L);

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        sqlCheckRules.add(mock(SqlCheckRule.class));
        when(sqlCheckService.getRules(anyList(), any(DialectType.class), any(JdbcOperations.class))).thenReturn(sqlCheckRules);

        // When
        List<CheckResult> result = sqlCheckService.check(connectionSession, new SqlCheckReq());

        // Then
        verify(environmentService, times(1)).detail(anyLong());
        verify(ruleService, times(1)).list(1L, QueryRuleMetadataParams.builder().build());
        verify(factory, times(1)).getDataSource();
        verify(singleConnectionDataSource, times(1)).getJdbcTemplate();
        verifyZeroInteractions(jdbcOperations);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCheckEnvironment() {
        // Given
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.singletonList(new Rule()));
        when(factory.getDataSource()).thenReturn(singleConnectionDataSource);
        when(singleConnectionDataSource.getJdbcTemplate()).thenReturn(jdbcOperations);

        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users WHERE id = 1"));

        when(environment.getRulesetId()).thenReturn(1L);

        // When
        List<CheckViolation> result = sqlCheckService.check(1L, "testDatabase", sqls, connectionConfig);

        // Then
        verify(ruleService, times(1)).list(1L, QueryRuleMetadataParams.builder().build());
        verify(factory, times(1)).getDataSource();
        verify(jdbcOperations, times(sqls.size())).query(anyString(), (Object[]) anyArray());
        assertEquals(sqls.size(), result.size());
        verifyNoMoreInteractions(factory);
        verifyNoMoreInteractions(singleConnectionDataSource);
        verifyNoMoreInteractions(jdbcOperations);
    }

    @Test
    public void testCheckEnvironmentWithEmptySqls() {
        // Given
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.singletonList(new Rule()));
        when(factory.getDataSource()).thenReturn(singleConnectionDataSource);
        when(singleConnectionDataSource.getJdbcTemplate()).thenReturn(jdbcOperations);

        List<OffsetString> sqls = new ArrayList<>();

        // When
        List<CheckViolation> result = sqlCheckService.check(1L, "testDatabase", sqls, connectionConfig);

        // Then
        verify(ruleService, times(1)).list(1L, QueryRuleMetadataParams.builder().build());
        verify(factory, times(1)).getDataSource();
        verifyZeroInteractions(jdbcOperations);
        assertEquals(0, result.size());
        verifyNoMoreInteractions(factory);
        verifyNoMoreInteractions(singleConnectionDataSource);
        verifyNoMoreInteractions(jdbcOperations);
    }

    @Test
    public void testCheckEnvironmentWithException() {
        // Given
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenThrow(new RuntimeException());

        // When
        List<CheckViolation> result = sqlCheckService.check(1L, "testDatabase", sqls, connectionConfig);

        // Then
        verify(ruleService, times(1)).list(1L, QueryRuleMetadataParams.builder().build());
        verifyZeroInteractions(factory);
        verifyZeroInteractions(singleConnectionDataSource);
        verifyZeroInteractions(jdbcOperations);
        assertEquals(0, result.size());
    }
}