package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSetMetaData;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
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

@Slf4j
public class SqlConsoleInterceptorTest {

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
    }

    @Test
    public void testDoPreHandle() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        // Test when ruleSetId is null
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);

        // Test when ruleSetId is present and no need to check sql console
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, Boolean.FALSE);
        result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);

        // Test when ruleSetId is present and need to check sql console
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, Boolean.TRUE);

        // Test when sqlConsoleIntercepted is true and sqlCheckIntercepted is true
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(10));
        SqlAsyncExecuteReq request1 = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response1 = new SqlAsyncExecuteResp();
        ConnectionSession session1 = mock(ConnectionSession.class);
        Map<String, Object> context1 = new HashMap<>();
        context1.put(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED, Boolean.TRUE);
        context1.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, Boolean.TRUE);
        SqlTuple sqlTuple1 = new SqlTuple("select * from test limit 11", 1, 1, null, null, null, null, null);
        List<SqlTuple> sqlTuples1 = Collections.singletonList(sqlTuple1);
        SqlAsyncExecuteResp responseExpected1 = new SqlAsyncExecuteResp();
        responseExpected1.setSqls(sqlTuples1);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Collections.singletonList("SELECT"));
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())).thenReturn(Optional.of(
                new Rule()));
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName())).thenReturn(Optional.of(
                new Rule()));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(1));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL, String.class))
                .thenReturn(Optional.of("SELECT"));
        when(determineSqlType(sqlTuple1, DialectType.MYSQL)).thenReturn(new BasicResult(SqlType.SELECT));

        SqlAsyncExecuteReq request2 = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response2 = new SqlAsyncExecuteResp();
        ConnectionSession session2 = mock(ConnectionSession.class);
        Map<String, Object> context2 = new HashMap<>();
        result = sqlConsoleInterceptor.doPreHandle(request2, response2, session2, context2);
        assertTrue(result);

        verify(sqlConsoleRuleService, times(1)).getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class);
        verify(sqlConsoleRuleService, times(1)).getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                DialectType.MYSQL, String.class);
        verify(sqlConsoleRuleService, times(1)).isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session);
        verify(sqlConsoleRuleService, times(1)).getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                DialectType.MYSQL, Integer.class);
        verify(ruleService, times(2)).getByRulesetIdAndName(1L, anyString());
        verify(determineSqlType(sqlTuple1, DialectType.MYSQL), times(1)).getSqlType();
        verify(determineSqlType(sqlTuple1, DialectType.MYSQL), times(1)).getSyntaxError();

        // Test when sqlConsoleIntercepted is true and sqlCheckIntercepted is false
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(10));
        SqlAsyncExecuteReq request3 = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response3 = new SqlAsyncExecuteResp();
        ConnectionSession session3 = mock(ConnectionSession.class);
        Map<String, Object> context3 = new HashMap<>();
        context3.put(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED, Boolean.TRUE);
        context3.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, Boolean.FALSE);
        SqlTuple sqlTuple2 = new SqlTuple("select * from test limit 11", 1, 1, null, null, null, null, null);
        List<SqlTuple> sqlTuples2 = Collections.singletonList(sqlTuple2);
        SqlAsyncExecuteResp responseExpected2 = new SqlAsyncExecuteResp();
        responseExpected2.setSqls(sqlTuples2);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Collections.singletonList("SELECT"));
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())).thenReturn(Optional.of(
                new Rule()));
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName())).thenReturn(Optional.of(
                new Rule()));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(1));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL, String.class))
                .thenReturn(Optional.of("SELECT"));
        when(determineSqlType(sqlTuple2, DialectType.MYSQL)).thenReturn(new BasicResult(SqlType.SELECT));

        result = sqlConsoleInterceptor.doPreHandle(request3, response3, session3, context3);
        assertFalse(result);

        verify(sqlConsoleRuleService, times(1)).getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class);
        verify(sqlConsoleRuleService, times(1)).getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                DialectType.MYSQL, String.class);
        verify(sqlConsoleRuleService, times(1)).isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session);
        verify(sqlConsoleRuleService, times(1)).getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                DialectType.MYSQL, Integer.class);
        verify(ruleService, times(2)).getByRulesetIdAndName(1L, anyString());
        verify(determineSqlType(sqlTuple2, DialectType.MYSQL), times(1)).getSqlType();
        verify(determineSqlType(sqlTuple2, DialectType.MYSQL), times(1)).getSyntaxError();

        // Test when sqlConsoleRuleService.isForbidden returns true
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);
        SqlAsyncExecuteReq request4 = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response4 = new SqlAsyncExecuteResp();
        ConnectionSession session4 = mock(ConnectionSession.class);
        Map<String, Object> context4 = new HashMap<>();
        context4.put(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED, Boolean.TRUE);
        context4.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, Boolean.TRUE);
        SqlTuple sqlTuple3 = new SqlTuple("create procedure p()", 1, 1, null, null, null, null, null);
        List<SqlTuple> sqlTuples3 = Collections.singletonList(sqlTuple3);
        SqlAsyncExecuteResp responseExpected3 = new SqlAsyncExecuteResp();
        responseExpected3.setSqls(sqlTuples3);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Collections.singletonList("CREATE PROCEDURE"));
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName())).thenReturn(Optional.of(
                new Rule()));
        when(determineSqlType(sqlTuple3, DialectType.MYSQL)).thenReturn(new BasicResult(SqlType.CREATE_PROCEDURE));

        result = sqlConsoleInterceptor.doPreHandle(request4, response4, session4, context4);
        assertFalse(result);

        verify(sqlConsoleRuleService, times(1)).getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                DialectType.MYSQL, String.class);
        verify(ruleService, times(1)).getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName());
        verify(determineSqlType(sqlTuple3, DialectType.MYSQL), times(1)).getSqlType();

        // Test when sqlConsoleRuleService.getProperties returns null
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.empty());
        result = sqlConsoleInterceptor.doPreHandle(request1, response1, session1, context1);
        assertFalse(result);

        verify(sqlConsoleRuleService, times(1)).getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class);
    }

    @Test
    public void testDoAfterCompletion() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        Map<String, Object> context = new HashMap<>();

        // Test when status is not success
        when(response.getStatus()).thenReturn(SqlExecuteStatus.FAILED);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        verify(response, times(0)).setAllowExport(anyBoolean());

        // Test when ruleSetId is null or individual team
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        verify(response, times(0)).setAllowExport(anyBoolean());

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        verify(response, times(0)).setAllowExport(anyBoolean());

        // Test when not forbidden to edit result set
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(false);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        verify(response, times(1)).setAllowExport(true);

        // Test when forbidden to edit result set
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        verify(response, times(1)).setAllowExport(false);
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals(SqlConsoleInterceptor.SqlExecuteStages.SQL_CONSOLE_RULE,
                sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }

    private BasicResult determineSqlType(@NonNull SqlTuple sqlTuple, @NonNull DialectType dialectType) {
        return sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);
    }
}