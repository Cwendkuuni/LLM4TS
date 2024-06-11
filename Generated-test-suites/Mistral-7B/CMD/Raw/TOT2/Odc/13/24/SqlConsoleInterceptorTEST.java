package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSetMetaData;
import java.util.Arrays;
import java.util.Collections;
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
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@Slf4j
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor interceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    private ConnectionSession session;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();
    }

    @Test
    public void testDoPreHandle_NoRuleSetId_ShouldReturnTrue() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenReturn(Optional.empty());
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), anyClass()))
                .thenReturn(Optional.empty());

        boolean result = interceptor.doPreHandle(request, response, session, Collections.emptyMap());
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_RuleSetIdAndSqlConsoleInterceptedFalse_ShouldReturnTrue() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.NEED_SQL_CONSOLE_CHECK.getRuleName()))
                .thenReturn(Optional.of(mock(Rule.class)));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(DialectType.class),
                Integer.class)).thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(DialectType.class),
                String.class)).thenReturn(Optional.of(Arrays.asList("SELECT")));

        Map<String, Object> context = Collections.emptyMap();
        context.put(NEED_SQL_CONSOLE_CHECK, false);

        boolean result = interceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_RuleSetIdAndSqlConsoleInterceptedTrue_ShouldReturnTrue() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.NEED_SQL_CONSOLE_CHECK.getRuleName()))
                .thenReturn(Optional.of(mock(Rule.class)));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(DialectType.class),
                Integer.class)).thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(DialectType.class),
                String.class)).thenReturn(Optional.of(Arrays.asList("SELECT")));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(false);

        Map<String, Object> context = Collections.emptyMap();
        context.put(NEED_SQL_CONSOLE_CHECK, true);

        boolean result = interceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_RuleSetIdAndSqlConsoleInterceptedTrue_SqlTuplesSizeGreaterThanMaxExecuteSqls_ShouldReturnFalse() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.NEED_SQL_CONSOLE_CHECK.getRuleName()))
                .thenReturn(Optional.of(mock(Rule.class)));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, any(DialectType.class),
                Integer.class)).thenReturn(Optional.of(3));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(DialectType.class),
                String.class)).thenReturn(Optional.of(Arrays.asList("SELECT")));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(false);

        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple(0, "SELECT 1 FROM table1", null),
                new SqlTuple(1, "CREATE PROCEDURE p1()", null), new SqlTuple(2, "SELECT 1 FROM table1", null));

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(sqlTuples);

        Map<String, Object> context = Collections.emptyMap();
        context.put(NEED_SQL_CONSOLE_CHECK, true);

        boolean result = interceptor.doPreHandle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testDoPreHandle_RuleSetIdAndSqlConsoleInterceptedTrue_SqlExecuteSqlTypeForbidden_ShouldReturnFalse() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.NEED_SQL_CONSOLE_CHECK.getRuleName()))
                .thenReturn(Optional.of(mock(Rule.class)));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, any(DialectType.class),
                Integer.class)).thenReturn(Optional.of(3));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(DialectType.class),
                String.class)).thenReturn(Optional.of(Arrays.asList("UPDATE")));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(false);

        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple(0, "SELECT 1 FROM table1", null),
                new SqlTuple(1, "CREATE PROCEDURE p1()", null));

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(sqlTuples);

        Map<String, Object> context = Collections.emptyMap();
        context.put(NEED_SQL_CONSOLE_CHECK, true);

        boolean result = interceptor.doPreHandle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testDoPreHandle_RuleSetIdAndSqlConsoleInterceptedTrue_SyntaxErrorException_ShouldReturnFalse() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.NEED_SQL_CONSOLE_CHECK.getRuleName()))
                .thenReturn(Optional.of(mock(Rule.class)));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, any(DialectType.class),
                Integer.class)).thenReturn(Optional.of(3));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(DialectType.class),
                String.class)).thenReturn(Optional.of(Arrays.asList("SELECT")));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(false);

        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple(0, "SELECT 1 FROM table1 WHERE 1=2", new SyntaxErrorException()));

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(sqlTuples);

        Map<String, Object> context = Collections.emptyMap();
        context.put(NEED_SQL_CONSOLE_CHECK, true);

        boolean result = interceptor.doPreHandle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testDoAfterCompletion_NoRuleSetId_ShouldDoNothing() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);

        interceptor.doAfterCompletion(result, session, Collections.emptyMap());
    }

    @Test
    public void testDoAfterCompletion_NoRuleSetId_AllowExportTrue_ShouldDoNothing() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        result.setAllowExport(true);

        interceptor.doAfterCompletion(result, session, Collections.emptyMap());
    }

    @Test
    public void testDoAfterCompletion_RuleSetId_NotForbiddenEditResultSet_ShouldSetAllowExportToTrue() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session))
                .thenReturn(false);

        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        result.setResultSetMetaData(mock(ResultSetMetaData.class));

        interceptor.doAfterCompletion(result, session, Collections.emptyMap());
        assertTrue(result.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_RuleSetId_ForbiddenEditResultSet_ShouldSetAllowExportToFalse() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session))
                .thenReturn(true);

        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        result.setResultSetMetaData(mock(ResultSetMetaData.class));

        interceptor.doAfterCompletion(result, session, Collections.emptyMap());
        assertFalse(result.isAllowExport());
    }

}