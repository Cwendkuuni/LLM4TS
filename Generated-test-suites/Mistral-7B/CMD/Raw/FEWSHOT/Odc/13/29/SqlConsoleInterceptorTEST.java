package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class SqlConsoleInterceptorTest {
    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private ConnectionSession connectionSession;
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        connectionSession = mock(ConnectionSession.class);
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_NoRuleSetId_ReturnTrue() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(null);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_IndividualTeam_ReturnTrue() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_NoNeedCheck_ReturnTrue() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(false);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_LimitSizeReached_ReturnFalse() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(), Integer.class))
                .thenReturn(Optional.of(5));
        when(request.getQueryLimit()).thenReturn(null);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        assertFalse(result);
    }

    @Test
    public void testDoPreHandle_LimitSizeNotReached_ReturnTrue() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(), Integer.class))
                .thenReturn(Optional.of(10));
        when(request.getQueryLimit()).thenReturn(5);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        assertTrue(result);
    }

    @Test
    public void testHandle_NoLimitSize_ReturnTrue() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testHandle_LimitSizeReached_ReturnFalse() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(), Integer.class))
                .thenReturn(Optional.of(5));
        when(request.getQueryLimit()).thenReturn(10);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        assertFalse(result);
    }

    @Test
    public void testDetermineSqlType_ValidSql_ReturnBasicResult() {
        SqlTuple sqlTuple = new SqlTuple();
        DialectType dialectType = DialectType.MYSQL;
        BasicResult expectedResult = new BasicResult(SqlType.SELECT);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
                .thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, dialectType, String.class))
                .thenReturn(Optional.of(List.of("SELECT")));

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDetermineSqlType_InvalidSql_ReturnBasicResult() {
        SqlTuple sqlTuple = new SqlTuple();
        DialectType dialectType = DialectType.MYSQL;
        BasicResult expectedResult = new BasicResult(SqlType.UNKNOWN);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
                .thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, dialectType, String.class))
                .thenReturn(Optional.empty());

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDoAfterCompletion_NoRulesViolated_AllowExport() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);

        assertTrue(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_RulesViolated_DisallowExport() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        Rule rule = mock(Rule.class);
        RuleViolation ruleViolation = mock(RuleViolation.class);
        when(rule.getLevel()).thenReturn(1);
        when(rule.getProperties()).thenReturn(Map.of("name", "testRule"));
        when(ruleViolation.getLevel()).thenReturn(1);
        when(ruleViolation.getLocalizedMessage())
                .thenReturn("Test rule message: {0}");
        when(ruleViolation.getOffset()).thenReturn(1);
        when(ruleViolation.getStart()).thenReturn(1);
        when(ruleViolation.getStop()).thenReturn(11);
        when(ruleService.getByRulesetIdAndName(1L, "testRule"))
                .thenReturn(Optional.of(rule));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, connectionSession))
                .thenReturn(false);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, connectionSession))
                .thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);

        assertFalse(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_RulesViolatedButCanEditResultSet_AllowExport() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        Rule rule = mock(Rule.class);
        RuleViolation ruleViolation = mock(RuleViolation.class);
        when(rule.getLevel()).thenReturn(1);
        when(rule.getProperties()).thenReturn(Map.of("name", "testRule"));
        when(ruleViolation.getLevel()).thenReturn(1);
        when(ruleViolation.getLocalizedMessage())
                .thenReturn("Test rule message: {0}");
        when(ruleViolation.getOffset()).thenReturn(1);
        when(ruleViolation.getStart()).thenReturn(1);
        when(ruleViolation.getStop()).thenReturn(11);
        when(ruleService.getByRulesetIdAndName(1L, "testRule"))
                .thenReturn(Optional.of(rule));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, connectionSession))
                .thenReturn(false);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, connectionSession))
                .thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);

        assertTrue(sqlExecuteResult.isAllowExport());
    }
}