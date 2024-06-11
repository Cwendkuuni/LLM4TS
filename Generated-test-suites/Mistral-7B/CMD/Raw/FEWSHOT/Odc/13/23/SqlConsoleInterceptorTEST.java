package com.oceanbase.odc.service.session.interceptor;

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
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@Slf4j
public class SqlConsoleInterceptorTest {

    private static final String TEST_RULE_SET_ID = "testRuleSetId";
    private static final String TEST_RULE_NAME = "testRuleName";
    private static final int TEST_MAX_RETURN_ROWS = 10;
    private static final int TEST_MAX_EXECUTE_SQLS = 5;
    private static final String TEST_NOT_ALLOWED_CREATE_PL = "NOT_ALLOWED_CREATE_PL";
    private static final String TEST_ALLOW_SQL_TYPES = "ALLOW_SQL_TYPES";

    private ConnectionSession session;
    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private AuthenticationFacade authenticationFacade;

    @Before
    public void setup() {
        session = Mockito.mock(ConnectionSession.class);
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        ruleService = Mockito.mock(RuleService.class);
        sqlConsoleRuleService = Mockito.mock(SqlConsoleRuleService.class);
        authenticationFacade = Mockito.mock(AuthenticationFacade.class);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
    }

    @Test
    public void testHandle_noRuleSetId_returnTrue() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = Mockito.mock(Map.class);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle_isIndividualTeam_returnTrue() {
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.INDIVIDUAL);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(TEST_RULE_SET_ID);

        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = Mockito.mock(Map.class);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle_noNeedSqlConsoleCheck_returnTrue() {
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(TEST_RULE_SET_ID);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK))
                .thenReturn(Boolean.FALSE);

        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context2 = Mockito.mock(Map.class);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context2);
        assertTrue(result);
    }

    @Test
    public void testHandle_queryLimit_setQueryLimit() {
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(TEST_RULE_SET_ID);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(Boolean.TRUE);

        Optional<Integer> queryLimitOptional = Optional.of(TEST_MAX_RETURN_ROWS);
        when(sqlConsoleRuleService.getProperties(TEST_RULE_SET_ID, SqlConsoleRules.MAX_RETURN_ROWS,
                eq(session.getDialectType()), anyInt()))
                .thenReturn(queryLimitOptional);

        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        request.setQueryLimit(15);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = Mockito.mock(Map.class);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertTrue(result);
        assertEquals(TEST_MAX_RETURN_ROWS, request.getQueryLimit());
    }

    @Test
    public void testHandle_maxSqlSize_setMaxSqlSize() {
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(TEST_RULE_SET_ID);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(Boolean.TRUE);

        Optional<Integer> queryLimitOptional = Optional.of(TEST_MAX_EXECUTE_SQLS);
        when(sqlConsoleRuleService.getProperties(TEST_RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS,
                eq(session.getDialectType()), anyInt()))
                .thenReturn(queryLimitOptional);

        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        SqlTuplesWithViolation sqlTuple = Mockito.mock(SqlTuplesWithViolation.class);
        sqlTuple.setViolatedRules(new ArrayList<>());
        sqlTuple.setSqlTuple(Mockito.mock(SqlTuple.class));
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);
        Map<String, Object> context = Mockito.mock(Map.class);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertTrue(result);
        assertEquals(TEST_MAX_EXECUTE_SQLS, response.getSqls().size());
    }

    @Test
    public void testHandle_forbiddenToCreatePl_returnFalse() {
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(TEST_RULE_SET_ID);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(Boolean.TRUE);
        when(sqlConsoleRuleService.isForbidden(eq(TEST_NOT_ALLOWED_CREATE_PL), session))
                .thenReturn(true);

        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        SqlTuplesWithViolation sqlTuple = Mockito.mock(SqlTuplesWithViolation.class);
        sqlTuple.setViolatedRules(new ArrayList<>());
        sqlTuple.setSqlTuple(Mockito.mock(SqlTuple.class));
        sqlTuple.getSqlTuple().setAst(Mockito.mock(AbstractSyntaxTree.class));
        sqlTuple.getSqlTuple().getAst().setSqlType(SqlType.DDL);
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);
        Map<String, Object> context = Mockito.mock(Map.class);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testHandle_allowSqlTypes_returnFalse() {
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(TEST_RULE_SET_ID);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(Boolean.TRUE);
        when(sqlConsoleRuleService.getListProperties(TEST_RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES,
                eq(session.getDialectType()), any(Class.class)))
                .thenReturn(Optional.of(List.of("INSERT")));

        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        SqlTuplesWithViolation sqlTuple = Mockito.mock(SqlTuplesWithViolation.class);
        sqlTuple.setViolatedRules(new ArrayList<>());
        sqlTuple.setSqlTuple(Mockito.mock(SqlTuple.class));
        sqlTuple.getSqlTuple().setAst(Mockito.mock(AbstractSyntaxTree.class));
        sqlTuple.getSqlTuple().getAst().setSqlType(SqlType.UPDATE);
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);
        Map<String, Object> context = Mockito.mock(Map.class);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testHandle_noSyntaxError_allowExecute() {
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(TEST_RULE_SET_ID);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(Boolean.TRUE);

        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        SqlTuplesWithViolation sqlTuple = Mockito.mock(SqlTuplesWithViolation.class);
        sqlTuple.setViolatedRules(new ArrayList<>());
        sqlTuple.setSqlTuple(Mockito.mock(SqlTuple.class));
        sqlTuple.getSqlTuple().setAst(Mockito.mock(AbstractSyntaxTree.class));
        sqlTuple.getSqlTuple().getAst().setParseResult(new BasicResult(SqlType.SELECT));
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);
        Map<String, Object> context = Mockito.mock(Map.class);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle_syntaxError_allowExecute() {
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(TEST_RULE_SET_ID);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(Boolean.TRUE);

        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        SqlTuplesWithViolation sqlTuple = Mockito.mock(SqlTuplesWithViolation.class);
        sqlTuple.setViolatedRules(new ArrayList<>());
        sqlTuple.setSqlTuple(Mockito.mock(SqlTuple.class));
        sqlTuple.getSqlTuple().setAst(Mockito.mock(AbstractSyntaxTree.class));
        sqlTuple.getSqlTuple().getAst().setParseResult(new BasicResult(SqlType.SELECT, new SyntaxErrorException()));
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);
        Map<String, Object> context = Mockito.mock(Map.class);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion_notAllowedEditResultSet_setEditableFalse() {
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(TEST_RULE_SET_ID);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session))
                .thenReturn(true);

        SqlExecuteResult response = Mockito.mock(SqlExecuteResult.class);
        response.setResultSetMetaData(Mockito.mock(java.sql.ResultSetMetaData.class));
        Map<String, Object> context = Mockito.mock(Map.class);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        verify(response.getResultSetMetaData(), times(1)).setEditable(false);
    }

    @Test
    public void testDoAfterCompletion_allowedEditResultSet_setEditableTrue() {
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(TEST_RULE_SET_ID);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session))
                .thenReturn(false);

        SqlExecuteResult response = Mockito.mock(SqlExecuteResult.class);
        Map<String, Object> context = Mockito.mock(Map.class);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        verify(response.getResultSetMetaData(), never()).setEditable(false);
    }
}