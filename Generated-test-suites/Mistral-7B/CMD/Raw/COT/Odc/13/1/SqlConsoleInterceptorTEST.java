package com.oceanbase.odc.service.session.interceptor.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor.SqlConsoleInterceptorTest;

@Slf4j
public class SqlConsoleInterceptorTest {

    private static final String TEST_RULE_SET_ID = "12345";
    private static final String TEST_RULE_SET_NAME = "test_ruleset";
    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        authenticationFacade = Mockito.mock(AuthenticationFacade.class);
        ruleService = Mockito.mock(RuleService.class);
        sqlConsoleRuleService = Mockito.mock(SqlConsoleRuleService.class);
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_EmptyRulesetId_ReturnsTrue() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(authenticationFacade.currentUser()).thenReturn(null);
        when(sqlConsoleRuleService.getProperties(TEST_RULE_SET_ID, SqlConsoleRules.MAX_RETURN_ROWS, any(), any()))
                .thenReturn(Optional.empty());

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_MaxReturnRowsReached_LimitsQuery() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(authenticationFacade.currentUser()).thenReturn(null);
        when(sqlConsoleRuleService.getProperties(TEST_RULE_SET_ID, SqlConsoleRules.MAX_RETURN_ROWS, any(), any()))
                .thenReturn(Optional.of(10));
        request.setQueryLimit(11);

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
        assertEquals(10, request.getQueryLimit());
    }

    @Test
    public void testDoPreHandle_NoRulesetId_ReturnsTrue() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(authenticationFacade.currentUser()).thenReturn(null);

        when(sqlConsoleRuleService.getProperties(TEST_RULE_SET_ID, any(), any(), any()))
                .thenReturn(Optional.empty());

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_AllowedToExecute_ReturnsTrue() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(authenticationFacade.currentUser()).thenReturn(null);
        when(sqlConsoleRuleService.getProperties(TEST_RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS, any(), any()))
                .thenReturn(Optional.of(1));
        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId("testId");
        sqlTuple.setOriginalSql("testSql");
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_MaxSqlSizeExceeded_ForbidsExecution() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(authenticationFacade.currentUser()).thenReturn(null);
        when(sqlConsoleRuleService.getProperties(TEST_RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS, any(), any()))
                .thenReturn(Optional.of(1));
        when(sqlConsoleRuleService.getListProperties(TEST_RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES, any(), any()))
                .thenReturn(Optional.of(new ArrayList<>()));
        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple1 = new SqlTuple();
        sqlTuple1.setSqlId("testId1");
        sqlTuple1.setOriginalSql("testSql1");
        sqlTuples.add(sqlTuple1);
        SqlTuple sqlTuple2 = new SqlTuple();
        sqlTuple2.setSqlId("testId2");
        sqlTuple2.setOriginalSql("testSql2");
        sqlTuples.add(sqlTuple2);
        response.setSqls(sqlTuples);

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_AllowedSqlType_AllowsExecution() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(authenticationFacade.currentUser()).thenReturn(null);
        when(sqlConsoleRuleService.getProperties(TEST_RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS, any(), any()))
                .thenReturn(Optional.of(1));
        when(sqlConsoleRuleService.getListProperties(TEST_RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES, any(), any()))
                .thenReturn(Optional.of(new ArrayList<>()));
        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple1 = new SqlTuple();
        sqlTuple1.setSqlId("testId1");
        sqlTuple1.setOriginalSql("testSql1");
        sqlTuple1.setParseResult(new BasicResult(SqlType.SELECT));
        sqlTuples.add(sqlTuple1);
        SqlTuple sqlTuple2 = new SqlTuple();
        sqlTuple2.setSqlId("testId2");
        sqlTuple2.setOriginalSql("testSql2");
        sqlTuple2.setParseResult(new BasicResult(SqlType.INSERT));
        sqlTuples.add(sqlTuple2);
        response.setSqls(sqlTuples);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test(expected = NullPointerException.class)
    public void testDoPreHandle_NullSession_ThrowsException() {
        ConnectionSession session = null;
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();

        sqlConsoleInterceptor.doPreHandle(request, response, session, context);
    }

    @Test
    public void testHandle_ForbiddenToCreatePl_ForbidsExecution() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(authenticationFacade.currentUser()).thenReturn(null);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);
        when(sqlConsoleRuleService.getProperties(TEST_RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS, any(), any()))
                .thenReturn(Optional.of(1));
        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId("testId");
        sqlTuple.setOriginalSql("create procedure testProcedure()");
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_AllowedToCreatePl_AllowsExecution() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(authenticationFacade.currentUser()).thenReturn(null);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getProperties(TEST_RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS, any(), any()))
                .thenReturn(Optional.of(1));
        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId("testId");
        sqlTuple.setOriginalSql("create procedure testProcedure()");
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testGetExecuteStageName_ReturnsExpectedStageName() {
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testDoAfterCompletion_AllowsExport_IfNotForbidden() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlExecuteResult response = new SqlExecuteResult();
        Map<String, Object> context = new HashMap<>();

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        assertTrue(response.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_ForbidsExport_IfForbidden() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlExecuteResult response = new SqlExecuteResult();
        Map<String, Object> context = new HashMap<>();

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        assertFalse(response.isAllowExport());
    }

    @Test
    public void testDetermineSqlType_ReturnsExpectedSqlType() {
        SqlTuple sqlTuple = new SqlTuple();
        String sql = "SELECT * FROM testTable";
        sqlTuple.setOriginalSql(sql);
        when(sqlTuple.getAst()).thenReturn(null);

        assertEquals(SqlType.SELECT, sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL));
    }

    @Test
    public void testDetermineSqlType_ReturnsUnknownType_WhenParseError() {
        SqlTuple sqlTuple = new SqlTuple();
        String sql = "SELECT * FROM testTable/*";
        sqlTuple.setOriginalSql(sql);
        when(sqlTuple.getAst()).thenThrow(new SyntaxErrorException("Test Error"));

        assertEquals(SqlType.UNKNOWN, sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL));
    }

    @Test
    public void testDetermineSqlType_ReturnsOtherType_WhenOtherType() {
        SqlTuple sqlTuple = new SqlTuple();
        String sql = "DO TEST_PROCEDURE()";
        sqlTuple.setOriginalSql(sql);
        when(sqlTuple.getAst()).thenReturn(null);

        assertEquals(SqlType.OTHERS, sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL));
    }
}