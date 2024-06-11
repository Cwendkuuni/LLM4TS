package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
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
    private SqlConsoleInterceptor interceptor;

    private ConnectionSession session;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private Map<String, Object> context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();
        context = new HashMap<>();
    }

    @After
    public void teardown() {
        reset(authenticationFacade, ruleService, sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_returnTrue_whenRuleSetIdIsNull() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        assertFalse(interceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_returnTrue_whenIsIndividualTeam() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        assertTrue(interceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_returnFalse_whenNeedSqlConsoleCheckIsFalse() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
        assertFalse(interceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testHandle_returnTrue_whenMaxReturnRowsIsNotNullAndLessThanRequestLimit() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        Optional<Integer> queryLimit = Optional.of(5);
        when(sqlConsoleRuleService.getProperties(eq(1L), eq(SqlConsoleRules.MAX_RETURN_ROWS), any(), eq(Integer.class)))
                .thenReturn(queryLimit);
        request.setQueryLimit(10);
        when(request.getQueryLimit()).thenReturn(10);
        when(response.getSqls()).thenReturn(new ArrayList<>());
        assertTrue(interceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_returnFalse_whenMaxReturnRowsIsNotNullAndGreaterThanRequestLimit() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        Optional<Integer> queryLimit = Optional.of(5);
        when(sqlConsoleRuleService.getProperties(eq(1L), eq(SqlConsoleRules.MAX_RETURN_ROWS), any(), eq(Integer.class)))
                .thenReturn(queryLimit);
        request.setQueryLimit(11);
        when(request.getQueryLimit()).thenReturn(11);
        when(response.getSqls()).thenReturn(new ArrayList<>());
        assertFalse(interceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_allowExecuteIsFalse_whenMaxSqlSizeIsNotNullAndSqlSizeIsGreaterThanLimit() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        Optional<Integer> maxSqlSize = Optional.of(3);
        when(sqlConsoleRuleService.getProperties(eq(1L), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(), eq(Integer.class)))
                .thenReturn(maxSqlSize);
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(new SqlTuple());
        response.setSqls(sqlTuples);
        AtomicBoolean allowExecute = new AtomicBoolean(true);
        interceptor.handle(request, response, session, context);
        assertFalse(allowExecute.get());
    }

    @Test
    public void testHandle_forbiddenToCreatePlIsTrue_whenSqlTypeIsCreatePL() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        SqlType sqlType = SqlType.CREATE_PL;
        when(determineSqlType(any(SqlTuple.class), any(DialectType.class))).thenReturn(new BasicResult(sqlType));
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(new SqlTuple());
        response.setSqls(sqlTuples);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);
        AtomicBoolean allowExecute = new AtomicBoolean(true);
        interceptor.handle(request, response, session, context);
        assertFalse(allowExecute.get());
    }

    @Test
    public void testHandle_allowExecuteIsFalse_whenSqlTypeIsNotAllowed() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        List<String> allowedSqlTypes = new ArrayList<>();
        when(sqlConsoleRuleService.getListProperties(eq(1L), SqlConsoleRules.ALLOW_SQL_TYPES, any(), eq(String.class)))
                .thenReturn(allowedSqlTypes);
        SqlType sqlType = SqlType.OTHERS;
        when(determineSqlType(any(SqlTuple.class), any(DialectType.class))).thenReturn(new BasicResult(sqlType));
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(new SqlTuple());
        response.setSqls(sqlTuples);
        assertFalse(interceptor.handle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletion_allowExportIsFalse_whenNotAllowedExportResultset() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(true);
        SqlExecuteResult executeResult = new SqlExecuteResult();
        executeResult.setAllowExport(false);
        interceptor.doAfterCompletion(executeResult, session, context);
        assertEquals(false, executeResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_allowExportIsTrue_whenAllowedExportResultset() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(false);
        SqlExecuteResult executeResult = new SqlExecuteResult();
        executeResult.setAllowExport(true);
        interceptor.doAfterCompletion(executeResult, session, context);
        assertEquals(true, executeResult.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam_returnTrue_whenOrganizationTypeIsIndividual() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        assertTrue(interceptor.isIndividualTeam());
    }

    @Test
    public void testIsIndividualTeam_returnFalse_whenOrganizationTypeIsNotIndividual() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        assertFalse(interceptor.isIndividualTeam());
    }

    @Test(expected = AssertionError.class)
    public void testDetermineSqlType_returnNull_whenExceptionThrown() {
        SqlTuple sqlTuple = new SqlTuple();
        DialectType dialectType = DialectType.MYSQL;
        when(determineSqlType(sqlTuple, dialectType)).thenThrow(new AssertionError());
        interceptor.doPreHandle(request, response, session, context);
    }

}