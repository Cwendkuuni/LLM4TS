package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedFieldUtils;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.parse.AbstractSyntaxTree;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;

@SuppressWarnings("unchecked")
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
    public void initializeMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle_noRuleSetId_true() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_ruleSetId_true() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Long ruleSetId = 1L;
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS,
                SqlConsoleInterceptor.class.getDeclaredField("dialectType").getType(), Integer.class))
                .thenReturn(Optional.of(10));

        when(request.getQueryLimit()).thenReturn(null);

        when(sqlConsoleInterceptor.handle(request, response, session, context)).thenReturn(true);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_ruleSetId_false() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Long ruleSetId = 1L;
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS,
                SqlConsoleInterceptor.class.getDeclaredField("dialectType").getType(), Integer.class))
                .thenReturn(Optional.of(10));

        when(request.getQueryLimit()).thenReturn(11);

        when(sqlConsoleInterceptor.handle(request, response, session, context)).thenReturn(false);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testDoPreHandle_needSqlConsoleCheck_true() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Long ruleSetId = 1L;
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS,
                SqlConsoleInterceptor.class.getDeclaredField("dialectType").getType(), Integer.class))
                .thenReturn(Optional.empty());

        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS,
                SqlConsoleInterceptor.class.getDeclaredField("dialectType").getType(), Integer.class))
                .thenReturn(Optional.of(5));

        when(request.getQueryLimit()).thenReturn(null);

        when(sqlConsoleInterceptor.handle(request, response, session, context)).thenReturn(true);

        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_needSqlConsoleCheck_false() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Long ruleSetId = 1L;
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS,
                SqlConsoleInterceptor.class.getDeclaredField("dialectType").getType(), Integer.class))
                .thenReturn(Optional.empty());

        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS,
                SqlConsoleInterceptor.class.getDeclaredField("dialectType").getType(), Integer.class))
                .thenReturn(Optional.of(5));

        when(request.getQueryLimit()).thenReturn(null);

        when(sqlConsoleInterceptor.handle(request, response, session, context)).thenReturn(true);

        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle_noViolations_true() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        Long ruleSetId = 1L;
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS,
                SqlConsoleInterceptor.class.getDeclaredField("dialectType").getType(), Integer.class))
                .thenReturn(Optional.of(10));

        List<SqlTuple> sqlTuples = Arrays.asList(mock(SqlTuple.class), mock(SqlTuple.class));
        when(response.getSqls()).thenReturn(sqlTuples);

        when(sqlConsoleInterceptor.handle(request, response, session, context)).thenReturn(true);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle_violations_false() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        Long ruleSetId = 1L;
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS,
                SqlConsoleInterceptor.class.getDeclaredField("dialectType").getType(), Integer.class))
                .thenReturn(Optional.of(10));

        List<SqlTuple> sqlTuples = Arrays.asList(mock(SqlTuple.class), mock(SqlTuple.class));
        when(response.getSqls()).thenReturn(sqlTuples);

        List<Rule> violatedRules = Arrays.asList(mock(Rule.class), mock(Rule.class));
        when(sqlConsoleRuleService.getViolatedRules(ruleSetId, sqlTuples)).thenReturn(violatedRules);

        when(sqlConsoleInterceptor.handle(request, response, session, context)).thenReturn(false);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testGetExecuteStageName_correctName() {
        String executeStageName = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, executeStageName);
    }

    @Test
    public void testDoAfterCompletion_success_true() throws SQLException {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        response.setStatus(SqlExecuteStatus.SUCCESS);
        response.setAllowExport(true);

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        assertTrue(response.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_success_false() throws SQLException {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        response.setStatus(SqlExecuteStatus.SUCCESS);
        response.setAllowExport(false);

        doNothing().when(sqlConsoleRuleService)
                .isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        assertFalse(response.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_failure_true() throws SQLException {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        response.setStatus(SqlExecuteStatus.FAILURE);
        response.setAllowExport(true);

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        assertTrue(response.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam_true() {
        assertTrue(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testIsIndividualTeam_false() {
        assertFalse(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType_correctType() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        DialectType dialectType = DialectType.ORACLE;

        when(sqlTuple.getDialectType()).thenReturn(dialectType);
        when(sqlTuple.getSqlType()).thenReturn(SqlType.DDL);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(SqlType.DDL, result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_unknownType() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        DialectType dialectType = DialectType.ORACLE;

        when(sqlTuple.getDialectType()).thenReturn(dialectType);
        when(sqlTuple.getSqlType()).thenReturn(null);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(SqlType.OTHERS, result.getSqlType());
    }

    @Test
    public void testGetOrder_correctOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }
}