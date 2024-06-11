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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

/**
 * Test class for SqlConsoleInterceptor.
 */
@Slf4j
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private RuleService ruleService;
    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    private ConnectionSession session;
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_true() {
        User user = new User();
        when(authenticationFacade.currentUser()).thenReturn(user);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL,
                Integer.class)).thenReturn(Optional.of(10));

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_false() {
        User user = new User();
        when(authenticationFacade.currentUser()).thenReturn(user);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL,
                Integer.class)).thenReturn(Optional.of(10));

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testHandle_true() {
        User user = new User();
        when(authenticationFacade.currentUser()).thenReturn(user);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL,
                Integer.class)).thenReturn(Optional.of(10));

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId("id1");
        sqlTuple.setOffset(0);
        sqlTuple.setOriginalSql("select * from emp");
        sqlTuple.setSqlType(new BasicResult(SqlType.SQL));
        sqlTuples.add(sqlTuple);

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setSqls(sqlTuples);

        when(response.getSqls()).thenReturn(sqlExecuteResult.getSqls());

        Optional<Integer> queryLimit = sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                DialectType.MySQL, Integer.class);
        Optional<Integer> maxSqlSize = sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                DialectType.MySQL, Integer.class);

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL,
                String.class)).thenReturn(Optional.of(new ArrayList<>()));

        SqlConsoleInterceptor sqlConsoleInterceptor = spy(new SqlConsoleInterceptor());
        doAnswer(invocation -> {
            SqlConsoleInterceptor interceptor = (SqlConsoleInterceptor) invocation.getTarget();
            interceptor.handle(request, response, session, context);
            return true;
        }).when(sqlConsoleInterceptor).handle(eq(request), eq(response), eq(session), eq(context));

        SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        sqlAsyncExecuteReq.setSqlId("id1");
        sqlAsyncExecuteReq.setQueryLimit(5);

        sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, response, session, context);

        verify(sqlConsoleInterceptor).handle(sqlAsyncExecuteReq, response, session, context);
    }

    @Test
    public void testHandle_false() {
        User user = new User();
        when(authenticationFacade.currentUser()).thenReturn(user);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL,
                Integer.class)).thenReturn(Optional.of(10));

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId("id1");
        sqlTuple.setOffset(0);
        sqlTuple.setOriginalSql("select * from emp");
        sqlTuple.setSqlType(new BasicResult(SqlType.SQL));
        sqlTuples.add(sqlTuple);

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setSqls(sqlTuples);

        when(response.getSqls()).thenReturn(sqlExecuteResult.getSqls());

        Optional<Integer> queryLimit = sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                DialectType.MySQL, Integer.class);
        Optional<Integer> maxSqlSize = sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                DialectType.MySQL, Integer.class);

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL,
                String.class)).thenReturn(Optional.of(new ArrayList<>()));

        SqlConsoleInterceptor sqlConsoleInterceptor = spy(new SqlConsoleInterceptor());
        doAnswer(invocation -> {
            SqlConsoleInterceptor interceptor = (SqlConsoleInterceptor) invocation.getTarget();
            interceptor.handle(request, response, session, context);
            return false;
        }).when(sqlConsoleInterceptor).handle(eq(request), eq(response), eq(session), eq(context));

        SqlAsyncExecuteReq sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        sqlAsyncExecuteReq.setSqlId("id1");
        sqlAsyncExecuteReq.setQueryLimit(11);

        sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, response, session, context);

        verify(sqlConsoleInterceptor).handle(sqlAsyncExecuteReq, response, session, context);

        assertTrue(context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED) == Boolean.FALSE);
    }

    @Test
    public void testDoAfterCompletion_true() {
        User user = new User();
        when(authenticationFacade.currentUser()).thenReturn(user);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        sqlExecuteResult.setAllowExport(true);

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(false);

        SqlConsoleInterceptor sqlConsoleInterceptor = spy(new SqlConsoleInterceptor());
        doAnswer(invocation -> {
            SqlConsoleInterceptor interceptor = (SqlConsoleInterceptor) invocation.getTarget();
            interceptor.doAfterCompletion(sqlExecuteResult, session, context);
            return;
        }).when(sqlConsoleInterceptor).doAfterCompletion(eq(sqlExecuteResult), eq(session), eq(context));

        sqlConsoleInterceptor.doPostHandle(sqlExecuteResult, session, context);

        verify(sqlConsoleInterceptor).doAfterCompletion(sqlExecuteResult, session, context);
    }

    @Test
    public void testDoAfterCompletion_false() {
        User user = new User();
        when(authenticationFacade.currentUser()).thenReturn(user);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        sqlExecuteResult.setAllowExport(false);

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);

        SqlConsoleInterceptor sqlConsoleInterceptor = spy(new SqlConsoleInterceptor());
        doAnswer(invocation -> {
            SqlConsoleInterceptor interceptor = (SqlConsoleInterceptor) invocation.getTarget();
            interceptor.doAfterCompletion(sqlExecuteResult, session, context);
            return;
        }).when(sqlConsoleInterceptor).doAfterCompletion(eq(sqlExecuteResult), eq(session), eq(context));

        sqlConsoleInterceptor.doPostHandle(sqlExecuteResult, session, context);

        verify(sqlConsoleInterceptor).doAfterCompletion(sqlExecuteResult, session, context);
    }

    @Test
    public void testDetermineSqlType_sql() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("select * from emp");

        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);

        when(sqlTuple.getAst()).thenReturn(ast);

        BasicResult expectedResult = new BasicResult(SqlType.SQL);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDetermineSqlType_ddl() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("create table emp (id int, name varchar(50))");

        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);

        when(sqlTuple.getAst()).thenReturn(ast);

        when(ast.getParseResult().isPlDdl()).thenReturn(true);

        BasicResult expectedResult = new BasicResult(SqlType.DDL);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDetermineSqlType_other() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("select * from emp where id = 1");

        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);

        when(sqlTuple.getAst()).thenReturn(ast);

        when(ast.getParseResult().isPlDdl()).thenReturn(false);

        BasicResult expectedResult = new BasicResult(SqlType.OTHERS);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL);

        assertEquals(expectedResult, result);
    }
}