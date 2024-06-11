package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSetMetaData;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.session.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationResponse;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;

/**
 * Test class for SqlConsoleInterceptor
 */
public class SqlConsoleInterceptorTest {

    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
    }

    @Test
    public void testDoPreHandle_NoRuleSetId_ReturnTrue() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(createMockAuthenticationResponse("INDIVIDUAL"));

        boolean actual = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        assertTrue(actual);

        verifyNoInteractions(ruleService);
    }

    @Test
    public void testDoPreHandle_RuleSetIdPresent_ReturnTrue_NoNeedCheck() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(createMockAuthenticationResponse("TEAM"));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);

        boolean actual = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        assertTrue(actual);

        verifyNoInteractions(ruleService);
    }

    @Test
    public void testDoPreHandle_RuleSetIdPresent_ReturnFalse_MaxSqlSizeViolation() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(createMockAuthenticationResponse("TEAM"));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("id", "SELECT * FROM table1;", 1),
                new SqlTuple("id", "SELECT * FROM table1, table2;", 2));
        response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(sqlTuples)));

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(new Rule()));

        RuleViolation maxSqlSizeRuleViolation = new RuleViolation();
        maxSqlSizeRuleViolation.setLevel(Rule.Level.ERROR);
        maxSqlSizeRuleViolation.setLocalizedMessage("Maximum number of SQLs exceeded.");

        Rule maxSqlSizeRule = new Rule();
        maxSqlSizeRule.setViolation(maxSqlSizeRuleViolation);

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(maxSqlSizeRule));

        boolean actual = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        assertFalse(actual);

        ArgumentCaptor<Rule> ruleCaptor = ArgumentCaptor.forClass(Rule.class);
        verify(ruleService).getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
        verify(ruleService).getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())
                .thenReturn(Optional.of(maxSqlSizeRule));
        verifyNoMoreInteractions(ruleService);
    }

    @Test
    public void testDoPreHandle_RuleSetIdPresent_ReturnFalse_ForbiddenCreatePl() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(createMockAuthenticationResponse("TEAM"));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("id", "CREATE PROCEDURE p1()", 1),
                new SqlTuple("id", "SELECT * FROM table1;", 2));
        response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(sqlTuples)));

        when(ruleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
                .thenReturn(true);

        RuleViolation notAllowedCreatePlRuleViolation = new RuleViolation();
        notAllowedCreatePlRuleViolation.setLevel(Rule.Level.ERROR);
        notAllowedCreatePlRuleViolation.setLocalizedMessage("Not allowed to create PL.");

        Rule notAllowedCreatePlRule = new Rule();
        notAllowedCreatePlRule.setViolation(notAllowedCreatePlRuleViolation);

        when(ruleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
                .thenReturn(true);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName()))
                .thenReturn(Optional.of(notAllowedCreatePlRule));

        boolean actual = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        assertFalse(actual);

        ArgumentCaptor<Rule> ruleCaptor = ArgumentCaptor.forClass(Rule.class);
        verify(ruleService).isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession);
        verify(ruleService).getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName());
        verifyNoMoreInteractions(ruleService);
    }

    @Test
    public void testDoPreHandle_RuleSetIdPresent_ReturnTrue_AllowedSqlType() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(createMockAuthenticationResponse("TEAM"));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("id", "SELECT * FROM table1;", 1),
                new SqlTuple("id", "CREATE TABLE table2 (id INT PRIMARY KEY)", 2));
        response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(sqlTuples)));

        when(ruleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL, String.class))
                .thenReturn(Arrays.asList("DDL"));

        RuleViolation allowSqlTypesRuleViolation = new RuleViolation();
        allowSqlTypesRuleViolation.setLevel(Rule.Level.INFO);
        allowSqlTypesRuleViolation.setLocalizedMessage("Allowed SQL type: DDL.");

        Rule allowSqlTypesRule = new Rule();
        allowSqlTypesRule.setViolation(allowSqlTypesRuleViolation);

        when(ruleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL, String.class))
                .thenReturn(Arrays.asList("DDL"));
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.of(allowSqlTypesRule));

        boolean actual = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        assertTrue(actual);

        ArgumentCaptor<Rule> ruleCaptor = ArgumentCaptor.forClass(Rule.class);
        verify(ruleService).getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL, String.class);
        verify(ruleService).getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName());
        verifyNoMoreInteractions(ruleService);
    }

    @Test
    public void testDoPreHandle_RuleSetIdPresent_ReturnTrue_IgnoredSyntaxError() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(createMockAuthenticationResponse("TEAM"));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("id", "CREATE TABLE table2 (id INT PRIMARY KEY)", 1),
                new SqlTuple("id", "SELECT * FROM table1;", 2));
        SqlTuple sqlTuple = new SqlTuple("id", "CREATE TABLE table3 (id INT PRIMARY KEY)", 3);
        sqlTuple.setSyntaxError(true);
        List<SqlTuple> sqlTuplesWithSyntaxError = Arrays.asList(sqlTuples.get(0), sqlTuple, sqlTuples.get(1));
        response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(sqlTuplesWithSyntaxError)));

        when(ruleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL, String.class))
                .thenReturn(Collections.emptyList());

        boolean actual = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        assertTrue(actual);

        verifyNoInteractions(ruleService);
    }

    @Test
    public void testDoAfterCompletion_NoRuleSetId_ReturnNull() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);

        assertNull(sqlExecuteResult.getAllowExport());
    }

    @Test
    public void testDoAfterCompletion_RuleSetIdPresent_ReturnFalse_NotAllowedExportResultset() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        sqlExecuteResult.setResultSetMetaData(mock(ResultSetMetaData.class));

        when(authenticationFacade.currentUser()).thenReturn(createMockAuthenticationResponse("TEAM"));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        when(ruleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, connectionSession))
                .thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);

        assertFalse(sqlExecuteResult.isAllowExport());

        verify(ruleService).isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, connectionSession);
    }

    @Test
    public void testDoAfterCompletion_RuleSetIdPresent_ReturnTrue_AllowedExportResultset() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        sqlExecuteResult.setResultSetMetaData(mock(ResultSetMetaData.class));

        when(authenticationFacade.currentUser()).thenReturn(createMockAuthenticationResponse("TEAM"));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        when(ruleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, connectionSession))
                .thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);

        assertTrue(sqlExecuteResult.isAllowExport());

        verifyNoInteractions(ruleService);
    }

    private AuthenticationResponse createMockAuthenticationResponse(String organizationType) {
        AuthenticationResponse authenticationResponse = mock(AuthenticationResponse.class);
        when(authenticationResponse.getOrganizationType()).thenReturn(organizationType);
        return authenticationResponse;
    }

}