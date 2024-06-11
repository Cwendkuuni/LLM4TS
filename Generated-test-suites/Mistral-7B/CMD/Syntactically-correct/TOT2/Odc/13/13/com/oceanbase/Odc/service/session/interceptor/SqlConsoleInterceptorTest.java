package com.oceanbase.odc.service.session.interceptor;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
class SqlConsoleInterceptorTest {

    @MockBean
    private AuthenticationFacade authenticationFacade;

    @MockBean
    private RuleService ruleService;

    @MockBean
    private SqlConsoleRuleService sqlConsoleRuleService;

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    private ConnectionSession connectionSession;

    private SqlAsyncExecuteReq sqlAsyncExecuteReq;

    private SqlAsyncExecuteResp sqlAsyncExecuteResp;

    @BeforeEach
    void setup() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        ReflectionTestUtils.setFieldValue(sqlConsoleInterceptor, "authenticationFacade", authenticationFacade);
        ReflectionTestUtils.setFieldValue(sqlConsoleInterceptor, "ruleService", ruleService);
        ReflectionTestUtils.setFieldValue(sqlConsoleInterceptor, "sqlConsoleRuleService", sqlConsoleRuleService);
        connectionSession = mock(ConnectionSession.class);
        sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
    }

    @Test
    void testDoPreHandle_sqlConsoleInterceptedIsFalse_returnTrue() {
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(null);
        doCallRealMethod().thenReturn(false);

        assertTrue(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, new HashMap<>()));
    }

    @Test
    void testDoPreHandle_sqlConsoleInterceptedIsTrueAndSqlCheckInterceptedIsTrue_returnTrue() {
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        doCallRealMethod().thenReturn(true);
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.SQL_CHECK_INTERCEPTED, true);

        assertTrue(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context));
    }

    @Test
    void testDoPreHandle_sqlConsoleInterceptedIsTrueButSqlCheckInterceptedIsFalse_returnFalse() {
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        doCallRealMethod().thenReturn(true);
        Map<String, Object> context = new HashMap<>();

        assertFalse(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context));
    }

    @Test
    void testHandle_ruleSetIdIsNotNullAndNeedSqlConsoleCheckIsTrue_returnsFalse() {
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(), any()))
                .thenReturn(Optional.of(5));

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, new HashMap<>());

        assertFalse(allowExecute.get());
    }

    @Test
    void testHandle_ruleSetIdIsNotNullAndNeedSqlConsoleCheckIsFalse_returnsTrue() {
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(), any()))
                .thenReturn(Optional.empty());

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, new HashMap<>());

        assertTrue(allowExecute.get());
    }

    @Test
    void testHandle_maxSqlSizeIsDefinedAndSqlTuplesSizeIsGreaterThanMaxSqlSize_returnsFalse() {
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, any(), any()))
                .thenReturn(Optional.of(3));
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple());

        AtomicReference<Rule> maxSqlSizeRule = new AtomicReference<>();
        AtomicBoolean allowExecute = new AtomicBoolean(true);
        sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, new HashMap<>());

        Rule rule = new Rule();
        RuleViolation violation = new RuleViolation();
        violation.setLevel(1);
        violation.setLocalizedMessage("Maximum number of SQLs exceeded");
        maxSqlSizeRule.set(rule);
        maxSqlSizeRule.get().setViolation(violation);

        verify(ruleService).getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
        verify(ruleService, times(1)).getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())
                .thenReturn(Optional.of(rule));

        assertEquals(1, sqlTuples.size());
        assertEquals(3, sqlAsyncExecuteReq.getQueryLimit());

        assertFalse(allowExecute.get());
        verifyZeroInteractions(sqlConsoleRuleService);
    }

    @Test
    void testHandle_maxSqlSizeIsNotDefined_returnsTrue() {
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, any(), any()))
                .thenReturn(Optional.empty());

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, new HashMap<>());

        assertEquals(0, sqlAsyncExecuteReq.getQueryLimit());

        assertTrue(allowExecute.get());
        verifyZeroInteractions(sqlConsoleRuleService);
    }

    @Test
    void testHandle_forbiddenToCreatePlAndQueryIsDDL_returnsFalse() {
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
                .thenReturn(true);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(), any()))
                .thenReturn(Optional.of(Arrays.asList("DDL")));

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, new HashMap<>());

        verify(ruleService).getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName());
        verify(ruleService, times(1)).getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName())
                .thenReturn(Optional.of(new Rule()));

        assertFalse(allowExecute.get);
        verifyZeroInteractions(sqlConsoleRuleService);
    }

    @Test
    void testHandle_forbiddenToCreatePlAndQueryIsNotDDL_returnsTrue() {
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
                .thenReturn(true);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(), any()))
                .thenReturn(Optional.of(Arrays.asList("DDL", "DML")));

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, new HashMap<>());

        assertTrue(allowExecute.get());
        verifyZeroInteractions(sqlConsoleRuleService);
    }

    @Test
    void testHandle_allowSqlTypesIsDefinedAndQueryTypeIsNotAllowed_returnsFalse() {
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(), any()))
                .thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
                .thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(), any()))
                .thenReturn(Optional.of(Arrays.asList("DML")));

        AtomicReference<Rule> rule = new AtomicReference<>();
        AtomicBoolean allowExecute = new AtomicBoolean(true);
        sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, new HashMap<>());

        Rule ruleObj = new Rule();
        RuleViolation violation = new RuleViolation();
        violation.setLevel(1);
        violation.setLocalizedMessage("SQL type is not allowed");
        rule.set(ruleObj);
        ruleObj.setViolation(violation);

        verify(ruleService).getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName());
        verify(ruleService, times(1)).getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName())
                .thenReturn(Optional.of(ruleObj));

        assertFalse(allowExecute.get());
        verifyZeroInteractions(sqlConsoleRuleService);
    }

    @Test
    void testHandle_allowSqlTypesIsNotDefined_returnsTrue() {
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(), any()))
                .thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
                .thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(), any()))
                .thenReturn(Optional.empty());

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, new HashMap<>());

        assertTrue(allowExecute.get());
        verifyZeroInteractions(sqlConsoleRuleService);
    }

    @Test
    void testDoAfterCompletion_statusIsSuccessAndRuleSetIdIsNotNull_returnsTrue() {
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(ruleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, connectionSession)).thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(new SqlExecuteResult(), connectionSession, new HashMap<>());

        assertTrue(sqlAsyncExecuteResp.isAllowExport());
        verifyZeroInteractions(ruleService);
    }

    @Test
    void testDoAfterCompletion_statusIsSuccessAndRuleSetIdIsNotNullAndRuleIsForbidden_returnsFalse() {
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(ruleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, connectionSession)).thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(new SqlExecuteResult(), connectionSession, new HashMap<>());

        assertFalse(sqlAsyncExecuteResp.isAllowExport());
        verifyZeroInteractions(ruleService);
    }

    @Test
    void testDoAfterCompletion_statusIsNotSuccess_returnsTrue() {
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        sqlConsoleInterceptor.doAfterCompletion(new SqlExecuteResult(), connectionSession, new HashMap<>());

        assertTrue(sqlAsyncExecuteResp.isAllowExport());
        verifyZeroInteractions(ruleService);
    }

    @Test
    void testGetExecuteStageName_returnsSqlConsoleRule() {
        String executeStageName = sqlConsoleInterceptor.getExecuteStageName();

        assertEquals("Sql Console Rule", executeStageName);
    }

    @Test
    void testGetOrder_returns2() {
        int order = sqlConsoleInterceptor.getOrder();

        assertEquals(2, order);
    }
}