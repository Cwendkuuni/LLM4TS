package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.cli.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuplesWithViolation;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@Slf4j
public class SqlConsoleInterceptorTest {

    private ConnectionSession session;
    private SqlConsoleInterceptor interceptor;
    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Before
    public void setUp() {
        session = Mockito.mock(ConnectionSession.class);
        authenticationFacade = Mockito.mock(AuthenticationFacade.class);
        ruleService = Mockito.mock(RuleService.class);
        sqlConsoleRuleService = Mockito.mock(SqlConsoleRuleService.class);
        interceptor = new SqlConsoleInterceptor();
        interceptor.setAuthenticationFacade(authenticationFacade);
        interceptor.setRuleService(ruleService);
        interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_NullRuleSetId() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        boolean result = interceptor.doPreHandle(request, response, session, context);
        verify(session).getRuleSetId();
        verifyNoInteractions(authenticationFacade, ruleService, sqlConsoleRuleService);
        assertFalse(result);
    }

    @Test
    public void testDoPreHandle_IndividualTeam() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        boolean result = interceptor.doPreHandle(request, response, session, context);
        verify(session).getRuleSetId();
        verifyNoInteractions(authenticationFacade, ruleService, sqlConsoleRuleService);
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_NoNeedCheck() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(session.get(NEED_SQL_CONSOLE_CHECK)).thenReturn(false);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        boolean result = interceptor.doPreHandle(request, response, session, context);
        verify(session).getRuleSetId();
        verify(session).get(NEED_SQL_CONSOLE_CHECK);
        assertTrue(result);
    }

    @Test
    public void testHandle_NoForbiddenRule() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(context.get(NEED_SQL_CONSOLE_CHECK)).thenReturn(true);

        SqlTuplesWithViolation sqlTuples = new SqlTuplesWithViolation();
        sqlTuples.setSqlTuple(TestUtils.mockSqlTuple("SELECT * FROM test", 1, null, null, null));
        response.setSqls(new ArrayList<SqlTuplesWithViolation>() {{
            add(sqlTuples);
        }});

        Optional<Integer> queryLimitOpt = Optional.of(10);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                Mockito.any(DialectType.class), Integer.class)).thenReturn(queryLimitOpt);
        request.setQueryLimit(20);
        boolean result = interceptor.handle(request, response, session, context);
        verify(session).getRuleSetId();
        verify(sqlConsoleRuleService).getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                Mockito.any(DialectType.class), Integer.class);

        // query limit not forbidden, so result should be true
        assertTrue(result);
    }

    @Test
    public void testHandle_MaxSqlSizeForbidden() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(context.get(NEED_SQL_CONSOLE_CHECK)).thenReturn(true);

        SqlTuplesWithViolation sqlTuples = new SqlTuplesWithViolation();
        sqlTuples.setSqlTuple(TestUtils.mockSqlTuple("SELECT * FROM test", 1, null, null, null));
        response.setSqls(new ArrayList<SqlTuplesWithViolation>() {{
            add(sqlTuples);
        }});

        Optional<Integer> queryLimitOpt = Optional.of(10);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                Mockito.any(DialectType.class), Integer.class)).thenReturn(queryLimitOpt);
        Optional<Integer> maxSqlSizeOpt = Optional.of(5);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                Mockito.any(DialectType.class), Integer.class)).thenReturn(maxSqlSizeOpt);

        request.setQueryLimit(15);
        boolean result = interceptor.handle(request, response, session, context);
        verify(session).getRuleSetId();
        verify(sqlConsoleRuleService).getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                Mockito.any(DialectType.class), Integer.class);
        verify(sqlConsoleRuleService).getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                Mockito.any(DialectType.class), Integer.class);

        // max sql size forbidden, so result should be false
        assertFalse(result);
    }

    @Test
    public void testHandle_ForbiddenCreatePl() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(context.get(NEED_SQL_CONSOLE_CHECK)).thenReturn(true);

        SqlTuplesWithViolation sqlTuples = new SqlTuplesWithViolation();
        sqlTuples.setSqlTuple(TestUtils.mockSqlTuple("CREATE PROCEDURE test()", 1, null, null, null));
        response.setSqls(new ArrayList<SqlTuplesWithViolation>() {{
            add(sqlTuples);
        }});

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);
        boolean result = interceptor.handle(request, response, session, context);
        verify(session).getRuleSetId();
        verify(sqlConsoleRuleService).isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session);

        // create pl forbidden, so result should be false
        assertFalse(result);
    }

    @Test
    public void testHandle_AllowSqlTypes() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(context.get(NEED_SQL_CONSOLE_CHECK)).thenReturn(true);

        SqlTuplesWithViolation sqlTuples = new SqlTuplesWithViolation();
        sqlTuples.setSqlTuple(TestUtils.mockSqlTuple("SELECT * FROM test", 1, null, null, null));
        response.setSqls(new ArrayList<SqlTuplesWithViolation>() {{
            add(sqlTuples);
        }});

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                Mockito.any(DialectType.class), String.class)).thenReturn(Arrays.asList("SELECT"));
        boolean result = interceptor.handle(request, response, session, context);
        verify(session).getRuleSetId();
        verify(sqlConsoleRuleService).getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                Mockito.any(DialectType.class), String.class);

        // allow sql types, so result should be true
        assertTrue(result);
    }

    @Test
    public void testHandle_SyntaxError() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(context.get(NEED_SQL_CONSOLE_CHECK)).thenReturn(true);

        SqlTuplesWithViolation sqlTuples = new SqlTuplesWithViolation();
        sqlTuples.setSqlTuple(TestUtils.mockSqlTuple("CREATE FROM test", 1, null, null, null));
        response.setSqls(new ArrayList<SqlTuplesWithViolation>() {{
            add(sqlTuples);
        }});

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                Mockito.any(DialectType.class), String.class)).thenReturn(Arrays.asList("SELECT", "CREATE"));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                Mockito.any(DialectType.class), Integer.class)).thenReturn(Optional.empty());

        AbstractSyntaxTree ast = Mockito.mock(AbstractSyntaxTree.class);
        when(sqlTuples.getSqlTuple().getAst()).thenReturn(ast);
        when(ast.getParseResult().getSyntaxError()).thenReturn(true);
        boolean result = interceptor.handle(request, response, session, context);
        verify(session).getRuleSetId();
        verify(sqlConsoleRuleService).getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                Mockito.any(DialectType.class), String.class);
        verify(sqlConsoleRuleService).getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                Mockito.any(DialectType.class), Integer.class);
        verify(sqlTuples.getSqlTuple()).getAst();

        // syntax error, so result should be false
        assertFalse(result);
    }
}