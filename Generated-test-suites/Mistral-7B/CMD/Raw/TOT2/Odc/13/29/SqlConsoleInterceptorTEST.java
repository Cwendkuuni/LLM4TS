package com.oceanbase.odc.service.session.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.session.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor.SqlAsyncExecuteReqBuilder;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testDoPreHandle_noRuleSetId_returnTrue() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = getRequest();
        SqlAsyncExecuteResp response = getResponse();
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    void testDoPreHandle_ruleSetIdExists_returnTrue() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = getRequest();
        SqlAsyncExecuteResp response = getResponse();
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    void testDoPreHandle_ruleSetIdExists_needSqlConsoleCheckFalse_returnTrue() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = getRequest();
        SqlAsyncExecuteResp response = getResponse();
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    void testDoPreHandle_queryLimitExceeded_returnFalse() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = getRequest();
        SqlAsyncExecuteResp response = getResponse();
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                SqlConsoleInterceptor.DialectType.MySQL, Integer.class)).thenReturn(Optional.of(10));
        request.setQueryLimit(11);

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    void testHandle_maxExecuteSqlsRuleViolation_returnFalse() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = getRequest();
        SqlAsyncExecuteResp response = getResponse();
        Map<String, Object> context = new HashMap<>();
        List<SqlTuple> sqlTuples = getSqlTuples();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                SqlConsoleInterceptor.DialectType.MySQL, Integer.class)).thenReturn(Optional.of(2));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                SqlConsoleInterceptor.DialectType.MySQL, String.class)).thenReturn(Optional.empty());

        SqlConsoleInterceptor interceptor = spy(sqlConsoleInterceptor);
        doReturn(sqlTuples).when(interceptor).parseResponse(response);

        assertFalse(interceptor.handle(request, response, session, context));
    }

    @Test
    void testHandle_allowSqlTypesViolation_returnFalse() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = getRequest();
        SqlAsyncExecuteResp response = getResponse();
        Map<String, Object> context = new HashMap<>();
        List<SqlTuple> sqlTuples = getSqlTuples();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                SqlConsoleInterceptor.DialectType.MySQL, Integer.class)).thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                SqlConsoleInterceptor.DialectType.MySQL, String.class)).thenReturn(Optional.of(Arrays.asList("DDL")));

        SqlConsoleInterceptor interceptor = spy(sqlConsoleInterceptor);
        doReturn(sqlTuples).when(interceptor).parseResponse(response);

        SqlTuple sqlTuple1 = sqlTuples.get(0);
        sqlTuple1.setSqlType(SqlType.DML);

        SqlTuple sqlTuple2 = sqlTuples.get(1);
        sqlTuple2.setSqlType(SqlType.DDL);

        doCallRealMethod().when(interceptor).handle(request, response, session, context);

        verify(interceptor, times(1)).addRuleViolation(any(RuleViolation.class));
        verify(interceptor, times(1)).setPreHandleResult(false);
    }

    @Test
    void testDoAfterCompletion_allowExportTrue() {
        SqlExecuteResult result = getExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        sqlConsoleInterceptor.doAfterCompletion(result, session, context);

        assertTrue(result.isAllowExport());
    }

    @Test
    void testDoAfterCompletion_notAllowedEditResultSet_setEditableFalse() {
        SqlExecuteResult result = getExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(result, session, context);

        assertFalse(result.isEditable());
    }

    @Test
    void testDoAfterCompletion_allowedEditResultSet_setEditableTrue() {
        SqlExecuteResult result = getExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(result, session, context);

        assertTrue(result.isEditable());
    }

    private SqlAsyncExecuteReq getRequest() {
        return new SqlAsyncExecuteReqBuilder().build();
    }

    private SqlAsyncExecuteResp getResponse() {
        List<SqlTuple> sqlTuples = getSqlTuples();
        return new SqlAsyncExecuteResp(Collections.singletonList(new SqlTuplesWithViolation(new SqlTuple("", ""), sqlTuples)));
    }

    private List<SqlTuple> getSqlTuples() {
        return Collections.singletonList(new SqlTuple(1L, ""));
    }

    private SqlExecuteResult getExecuteResult() {
        return new SqlExecuteResult();
    }
}