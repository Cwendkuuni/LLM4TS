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
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;

@Slf4j
public class SqlConsoleInterceptorTest {

    private static final String NEED_SQL_CONSOLE_CHECK = "NEED_SQL_CONSOLE_CHECK";
    private static final String SQL_CONSOLE_INTERCEPTED = "SQL_CONSOLE_INTERCEPTED";
    private static final String NOT_ALLOWED_CREATE_PL = "NOT_ALLOWED_CREATE_PL";
    private static final String ALLOW_SQL_TYPES = "ALLOW_SQL_TYPES";
    private static final String MAX_EXECUTE_SQLS = "MAX_EXECUTE_SQLS";
    private static final String MAX_RETURN_ROWS = "MAX_RETURN_ROWS";

    private SqlConsoleInterceptor interceptor;
    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        interceptor = new SqlConsoleInterceptor();
        interceptor.setAuthenticationFacade(authenticationFacade);
        interceptor.setRuleService(ruleService);
        interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testHandleTrue() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Long ruleSetId = 1L;

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(sqlConsoleRuleService.getProperties(ruleSetId, MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.getProperties(ruleSetId, MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);

        boolean result = interceptor.handle(request, response, session, context);
        assertTrue(result);
        verify(sqlConsoleRuleService, never()).getListProperties(ruleSetId, ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class);
    }

    @Test
    public void testHandleFalseMaxSqls() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Long ruleSetId = 1L;

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(sqlConsoleRuleService.getProperties(ruleSetId, MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.getListProperties(ruleSetId, ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Optional.of(List.of("SELECT")));
        when(response.getSqls()).thenReturn(new ArrayList<>());

        boolean result = interceptor.handle(request, response, session, context);
        assertFalse(result);
        verify(sqlConsoleRuleService, times(1)).getListProperties(ruleSetId, ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class);
    }

    @Test
    public void testHandleFalseMaxSqlsWithViolations() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Long ruleSetId = 1L;

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(sqlConsoleRuleService.getProperties(ruleSetId, MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.getListProperties(ruleSetId, ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Optional.of(List.of("SELECT")));
        when(response.getSqls()).thenReturn(List.of(new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM t", null),
                        List.of(new Rule()))));

        boolean result = interceptor.handle(request, response, session, context);
        assertFalse(result);
        verify(sqlConsoleRuleService, times(1)).getListProperties(ruleSetId, ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class);
    }

    @Test
    public void testHandleFalseCreatePl() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Long ruleSetId = 1L;

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(sqlConsoleRuleService.getProperties(ruleSetId, MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);
        when(sqlConsoleRuleService.getProperties(ruleSetId, ALLOW_SQL_TYPES, DialectType.MYSQL, String.class))
                .thenReturn(Optional.empty());

        boolean result = interceptor.handle(request, response, session, context);
        assertFalse(result);
        verify(sqlConsoleRuleService, times(1)).isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session);
    }

    @Test
    public void testHandleFalseAllowSqlTypes() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Long ruleSetId = 1L;

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(sqlConsoleRuleService.getProperties(ruleSetId, MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(ruleSetId, ALLOW_SQL_TYPES, DialectType.MYSQL, String.class))
                .thenReturn(Optional.of(List.of("INSERT")));
        when(response.getSqls()).thenReturn(List.of(new SqlTuplesWithViolation(new SqlTuple("CREATE TABLE t (id INT)", null),
                        List.of(new Rule()))));

        boolean result = interceptor.handle(request, response, session, context);
        assertFalse(result);
        verify(sqlConsoleRuleService, times(1)).getListProperties(ruleSetId, ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class);
    }

    @Test
    public void testDoAfterCompletionAllowExport() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult result = new SqlExecuteResult();
        result.setAllowExport(true);

        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(false);

        interceptor.doAfterCompletion(result, session, context);

        assertTrue(result.isAllowExport());
        verify(sqlConsoleRuleService, never()).isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session);
    }

    @Test
    public void testDoAfterCompletionForbiddenExport() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult result = new SqlExecuteResult();

        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);

        interceptor.doAfterCompletion(result, session, context);

        assertFalse(result.isAllowExport());
        verify(sqlConsoleRuleService, times(1)).isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session);
    }
}