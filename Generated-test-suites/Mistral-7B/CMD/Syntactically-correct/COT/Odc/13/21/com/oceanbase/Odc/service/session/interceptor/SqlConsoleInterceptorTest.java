package com.oceanbase.odc.service.session.interceptor.test;

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
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
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
    public void testDoPreHandle_noNeedCheck_returnTrue() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_noRuleSetId_returnTrue() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_handleFalse_returnFalse() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, eq(DialectType.MYSQL),
                eq(Integer.class))).thenReturn(Optional.of(10));
        when(request.getQueryLimit()).thenReturn(20);

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testHandle_noRuleViolation_returnTrue() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, eq(DialectType.MYSQL),
                eq(Integer.class))).thenReturn(Optional.of(10));
        when(request.getQueryLimit()).thenReturn(10);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, eq(DialectType.MYSQL),
                eq(String.class))).thenReturn(Optional.of(new ArrayList<>()));
        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("select * from test limit 10");
        sqlTuple.setSqlId("1");
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_maxSqlSizeViolation_returnFalse() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, eq(DialectType.MYSQL),
                eq(Integer.class))).thenReturn(Optional.of(10));
        when(request.getQueryLimit()).thenReturn(20);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, eq(DialectType.MYSQL),
                eq(String.class))).thenReturn(Optional.of(new ArrayList<>()));
        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple1 = new SqlTuple();
        sqlTuple1.setOriginalSql("select * from test limit 10");
        sqlTuple1.setSqlId("1");
        sqlTuples.add(sqlTuple1);
        SqlTuple sqlTuple2 = new SqlTuple();
        sqlTuple2.setOriginalSql("select * from test limit 15");
        sqlTuple2.setSqlId("2");
        sqlTuples.add(sqlTuple2);
        response.setSqls(sqlTuples);

        Rule rule = mock(Rule.class);
        RuleViolation violation = new RuleViolation();
        violation.setLevel("warn");
        violation.setLocalizedMessage("MaxExecuteSqlsViolation.message");
        when(ruleService.getByRulesetIdAndName(1L, Eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))).thenReturn(Optional.of(rule));

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_sqlTypeViolation_returnFalse() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, eq(DialectType.MYSQL),
                eq(Integer.class))).thenReturn(Optional.of(10));
        when(request.getQueryLimit()).thenReturn(10);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, eq(DialectType.MYSQL),
                eq(String.class))).thenReturn(Optional.of(new ArrayList<>()));
        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("select * from test limit 10");
        sqlTuple.setSqlId("1");
        sqlTuple.setAst(mock(AbstractSyntaxTree.class));
        sqlTuple.setSqlType(SqlType.DCL);
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        Rule rule = mock(Rule.class);
        RuleViolation violation = new RuleViolation();
        violation.setLevel("warn");
        violation.setLocalizedMessage("AllowSqlTypesViolation.message");
        when(ruleService.getByRulesetIdAndName(1L, Eq(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))).thenReturn(Optional.of(rule));

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletion_canExport_returnTrue() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult response = new SqlExecuteResult();
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(false);

        assertTrue(sqlConsoleInterceptor.doAfterCompletion(response, session, context));
    }

    @Test
    public void testDoAfterCompletion_cantEditResultSet_returnFalse() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult response = new SqlExecuteResult();
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);

        assertFalse(sqlConsoleInterceptor.doAfterCompletion(response, session, context));
    }

    @Test
    public void testDoAfterCompletion_cantExport_returnFalse() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult response = new SqlExecuteResult();
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(true);

        assertFalse(sqlConsoleInterceptor.doAfterCompletion(response, session, context));
    }

    @Test
    public void testGetOrder_return2() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }

}