package com.oceanbase.odc.service.session.interceptor;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@Slf4j
public class SqlConsoleInterceptorTest {

    @Rule
    public MockitoJUnit mockitoRule = new MockitoJUnit();

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle_true() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), anyClass()))
                .thenReturn(Optional.empty());

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
        verify(sqlConsoleInterceptor, times(0)).handle(request, response, session, context);
    }

    @Test
    public void testDoPreHandle_false() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), anyClass()))
                .thenReturn(Optional.of(1L));

        when(sqlConsoleInterceptor.handle(request, response, session, context)).thenReturn(false);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertFalse(result);
        verify(sqlConsoleInterceptor, times(1)).handle(request, response, session, context);
    }

    @Test
    public void testDoPreHandle_sqlConsoleIntercepted() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), anyClass()))
                .thenReturn(Optional.of(1L));
        when(sqlConsoleInterceptor.handle(request, response, session, context)).thenReturn(true);

        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        context.put(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED, false);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
        verify(sqlConsoleInterceptor, times(1)).handle(request, response, session, context);

        context.put(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED, true);
        result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
        verify(sqlConsoleInterceptor, times(1)).handle(request, response, session, context);
    }

    @Test
    public void testHandle_allowExecute_true() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = new ArrayList<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), anyClass()))
                .thenReturn(Optional.empty());
        doReturn(true).when(sqlConsoleInterceptor).checkAllowExecute(sqlTuples, request.getQueryLimit(), session);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle_allowExecute_false() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("testSql", "testSql;", 1, ""));

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), anyClass()))
                .thenReturn(Optional.of(1L));
        doReturn(false).when(sqlConsoleInterceptor).checkAllowExecute(sqlTuples, request.getQueryLimit(), session);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertFalse(result);
    }

    @Test(expected = RuntimeException.class)
    public void testHandle_sqlExecuteException() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("testSql", "testSql;", 1, ""));

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), anyClass()))
                .thenReturn(Optional.empty());
        doThrow(new SQLException("testException")).when(sqlConsoleInterceptor)
                .handleSqlExecute(sqlTuples, request.getQueryLimit(), session);

        sqlConsoleInterceptor.handle(request, response, session, context);
    }

    @Test
    public void testCheckAllowExecute_true() {
        ConnectionSession session = mock(ConnectionSession.class);
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("testSql1", "testSql1;", 1, ""),
                new SqlTuple("testSql2", "testSql2;", 1, ""));
        int queryLimit = 1;

        assertTrue(sqlConsoleInterceptor.checkAllowExecute(sqlTuples, queryLimit, session));
    }

    @Test
    public void testCheckAllowExecute_false() {
        ConnectionSession session = mock(ConnectionSession.class);
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("testSql1", "testSql1;", 1, ""),
                new SqlTuple("testSql2", "testSql2;", 1, ""), new SqlTuple("testSql3", "testSql3;", 1, ""));
        int queryLimit = 1;

        assertFalse(sqlConsoleInterceptor.checkAllowExecute(sqlTuples, queryLimit, session));
    }

    @Test
    public void testDoAfterCompletion_true() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult response = new SqlExecuteResult();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(sqlConsoleRuleService.isForbidden(anyString(), session)).thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
    }

    @Test
    public void testDoAfterCompletion_false() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult response = new SqlExecuteResult();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(sqlConsoleRuleService.isForbidden(anyString(), session)).thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
    }

    @Test
    public void testDetermineSqlType_validSql() throws SyntaxErrorException {
        String sql = "SELECT * FROM test_table WHERE col = 'test'";
        SqlTuple sqlTuple = new SqlTuple(sql, sql, 1, "testDb");
        DialectType dialectType = DialectType.MYSQL;

        SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();
        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(SqlType.SELECT, result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_invalidSql() throws SyntaxErrorException {
        String sql = "SELECT * FROM test_table WHERE col = 'test' test";
        SqlTuple sqlTuple = new SqlTuple(sql, sql, 1, "testDb");
        DialectType dialectType = DialectType.MYSQL;

        SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();
        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(SqlType.OTHERS, result.getSqlType());
    }
}