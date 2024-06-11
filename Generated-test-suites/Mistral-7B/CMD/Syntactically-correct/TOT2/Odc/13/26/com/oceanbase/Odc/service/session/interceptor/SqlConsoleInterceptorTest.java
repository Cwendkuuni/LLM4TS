package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;

public class SqlConsoleInterceptorTest {

    private AuthenticationFacade authenticationFacade;
    private ConnectionSession session;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private SqlConsoleInterceptor interceptor;

    @Before
    public void setUp() {
        authenticationFacade = mock(AuthenticationFacade.class);
        session = mock(ConnectionSession.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        interceptor = new SqlConsoleInterceptor();

        interceptor.setAuthenticationFacade(authenticationFacade);
        interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(Optional.of(1L));
    }

    @Test
    public void testDoPreHandle_allowedToExecute() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, Mockito.any(),
                Mockito.eq(Integer.class))).thenReturn(Optional.of(10));

        boolean result = interceptor.doPreHandle(request, response, session, new HashMap<>());
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_queryLimitExceeded() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, Mockito.any(),
                Mockito.eq(Integer.class))).thenReturn(Optional.of(5));

        request.setQueryLimit(11);

        boolean result = interceptor.doPreHandle(request, response, session, new HashMap<>());
        assertFalse(result);
    }

    @Test
    public void testDoPreHandle_notNeedCheck() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, Mockito.any(),
                Mockito.eq(Integer.class))).thenReturn(Optional.ofNullable(null));

        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, Boolean.FALSE);

        boolean result = interceptor.doPreHandle(request, response, session, new HashMap<>());
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_sqlConsoleIntercepted() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession context = new HashMap<>();

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, Mockito.any(),
                Mockito.eq(Integer.class))).thenReturn(Optional.of(5));

        when(sqlConsoleRuleService.isForbidden(Mockito.anyString(), session)).thenReturn(false);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple("testSql", 1, 1, "testSqlId");
        sqlTuple.initAst(null);
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, Mockito.any(),
                Mockito.eq(String.class))).thenReturn(Optional.of(List.of("TestType")));

        when(sqlTuple.getSqlType()).thenReturn(SqlType.TESTTYPE);

        context.put(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED, Boolean.TRUE);

        boolean result = interceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle_allowedToExecute() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession context = new HashMap<>();

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, Mockito.any(),
                Mockito.eq(Integer.class))).thenReturn(Optional.of(5));

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(false);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple("testSql", 1, 1, "testSqlId");
        sqlTuple.initAst(null);
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        List<Rule> violatedRules = new ArrayList<>();
        Rule rule = new Rule();
        RuleViolation violation = new RuleViolation();
        violation.setLevel(1);
        violation.setLocalizedMessage("TestMessage");
        violation.setOffset(0);
        violation.setStart(0);
        violation.setStop(sqlTuple.getOriginalSql().length());
        rule.setViolation(violation);
        violatedRules.add(rule);

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, Mockito.any(),
                Mockito.eq(String.class))).thenReturn(Optional.of(List.of("TestType")));

        when(sqlTuple.getSqlType()).thenReturn(SqlType.TESTTYPE);

        ArgumentCaptor<Map<String, Object>> contextCaptor = ArgumentCaptor.forClass(Map.class);

        interceptor.handle(request, response, session, context);

        Mockito.verify(context).put(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED, Boolean.TRUE);
        Mockito.verifyNoMoreInteractions(context);
    }

    @Test
    public void testHandle_forbiddenToCreatePl() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession context = new HashMap<>();

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, Mockito.any(),
                Mockito.eq(Integer.class))).thenReturn(Optional.of(5));

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple("create table test (id int)", 1, 1, "testSqlId");
        sqlTuple.initAst(null);
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        Rule rule = new Rule();
        RuleViolation violation = new RuleViolation();
        violation.setLevel(1);
        violation.setLocalizedMessage("TestMessage");
        violation.setOffset(0);
        violation.setStart(0);
        violation.setStop(sqlTuple.getOriginalSql().length());
        rule.setViolation(violation);

        ArgumentCaptor<Map<String, Object>> contextCaptor = ArgumentCaptor.forClass(Map.class);

        interceptor.handle(request, response, session, context);

        Mockito.verify(context).put(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED, Boolean.FALSE);
        Mockito.verify(context).put(SqlConsoleInterceptor.SQL_CONSOLE_RULE_VIOLATIONS, violatedRules);
        Mockito.verifyNoMoreInteractions(context);
    }

    @Test
    public void testHandle_sqlTypesNotAllowed() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession context = new HashMap<>();

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, Mockito.any(),
                Mockito.eq(Integer.class))).thenReturn(Optional.of(5));

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, Mockito.any(),
                Mockito.eq(String.class))).thenReturn(Optional.of(List.of("SELECT", "INSERT")));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple("insert into test (id) values (1)", 1, 1, "testSqlId");
        sqlTuple.initAst(null);
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        Rule rule = new Rule();
        RuleViolation violation = new RuleViolation();
        violation.setLevel(1);
        violation.setLocalizedMessage("TestMessage");
        violation.setOffset(0);
        violation.setStart(0);
        violation.setStop(sqlTuple.getOriginalSql().length());
        rule.setViolation(violation);

        ArgumentCaptor<Map<String, Object>> contextCaptor = ArgumentCaptor.forClass(Map.class);

        interceptor.handle(request, response, session, context);

        Mockito.verify(context).put(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED, Boolean.FALSE);
        Mockito.verify(context).put(SqlConsoleInterceptor.SQL_CONSOLE_RULE_VIOLATIONS, violatedRules);
        Mockito.verifyNoMoreInteractions(context);
    }

    @Test
    public void testDoAfterCompletion_allowedToExport() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setAllowExport(true);

        interceptor.doAfterCompletion(result, session, new HashMap<>());
        assertTrue(result.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_notAllowedToExport() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setAllowExport(false);

        interceptor.doAfterCompletion(result, session, new HashMap<>());
        assertFalse(result.isAllowExport());
    }

    @Test
    public void testGetExecuteStageName() {
        String result = interceptor.getExecuteStageName();
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, result);
    }
}