package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;

public class SqlConsoleInterceptorTest {

    @Mock
    private ConnectionSession session;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(authenticationFacade.currentUser()).thenReturn(mockUser());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
    }

    @Test
    public void testDoPreHandle_returnsTrueWhenRuleSetIdIsNullOrIndividualTeam() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle_returnsTrueWhenMaxReturnRowsIsGreaterThanOrEqualQueryLimitAndNoViolatedRules() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQueryLimit(10);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(new ArrayList<>());
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, session.getDialectType(),
                Integer.class)).thenReturn(Optional.of(15));
        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_returnsFalseWhenMaxReturnRowsIsLessThanQueryLimitAndViolatedMaxExecuteSqlsRule() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQueryLimit(5);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(new ArrayList<>(List.of(mockSqlTuple("sql1"), mockSqlTuple("sql2"))));
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, session.getDialectType(),
                Integer.class)).thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, session.getDialectType(),
                String.class)).thenReturn(Optional.of(new ArrayList<>()));
        Rule maxExecSqlsRule = mock(Rule.class);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())).thenReturn(Optional.of(maxExecSqlsRule));
        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
        verify(maxExecSqlsRule).getViolation();
    }

    @Test
    public void testHandle_returnsFalseWhenForbiddenToCreatePl() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(new ArrayList<>(List.of(mockSqlTuple("CREATE PROCEDURE p1()"))));
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);
        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_returnsFalseWhenForbiddenToExecuteSqlType() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(new ArrayList<>(List.of(mockSqlTuple("SELECT * FROM tab1"))));
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, session.getDialectType(),
                String.class)).thenReturn(Optional.of(new ArrayList<>(List.of("DDL"))));
        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletion_setsAllowExportToFalseWhenNotAllowedExportResultset() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(true);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);
        assertFalse(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_setsAllowExportToTrueWhenAllowedExportResultset() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(false);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);
        assertTrue(sqlExecuteResult.isAllowExport());
    }

    private SqlAsyncExecuteReq mockSqlTuple(String sql) {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSql(sql);
        return request;
    }

    private User mockUser() {
        User user = mock(User.class);
        when(user.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        return user;
    }

}