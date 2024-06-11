package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertFalse;
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
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResp;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade.User;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.model.SqlTuple;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

/**
 * @Author: Lebie
 * @Date: 2023/7/17 15:37
 * @Description: []
 */
public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private User user;
    private ConnectionSession session;
    private ConnectionSessionUtil connectionSessionUtil;
    private SqlExecuteResp sqlExecuteResp;
    private SqlAsyncExecuteReq sqlAsyncExecuteReq;

    @Before
    public void setup() {
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        user = mock(User.class);
        session = mock(ConnectionSession.class);
        connectionSessionUtil = mock(ConnectionSessionUtil.class);
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
        when(authenticationFacade.currentUser()).thenReturn(user);
        when(session.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(123L);
        sqlExecuteResp = new SqlAsyncExecuteResp();
        sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
    }

    @Test
    public void testDoPreHandle_noRuleSetId() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlExecuteResp, session, new HashMap<>());
        assertTrue(sqlExecuteResp.isAllowExport());
    }

    @Test
    public void testDoPreHandle_individualTeam() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlExecuteResp, session, new HashMap<>());
        assertTrue(sqlExecuteResp.isAllowExport());
    }

    @Test
    public void testDoPreHandle_falseNeedCheck() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(123L);
        when(session.getDialectType()).thenReturn(DialectType.MySQL);
        when(context.get(NEED_SQL_CONSOLE_CHECK)).thenReturn(false);
        sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlExecuteResp, session, new HashMap<>());
        assertTrue(sqlExecuteResp.isAllowExport());
    }

    @Test
    public void testHandle_simpleSqlWithNoViolations() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(123L);
        when(session.getDialectType()).thenReturn(DialectType.MySQL);
        SqlAsyncExecuteResp sqlExecuteResp = mock(SqlAsyncExecuteResp.class);
        when(sqlExecuteResp.getSqls()).thenReturn(new ArrayList<>());
        sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlExecuteResp, session, new HashMap<>());
        ArgumentCaptor<SqlAsyncExecuteReq> sqlAsyncExecuteReqArgumentCaptor = ArgumentCaptor.forClass(SqlAsyncExecuteReq.class);
        verify(authenticationFacade).checkPermission(eq(User.class), eq(AuthenticationFacade.PermissionType.SQLCONSOLE),
                sqlAsyncExecuteReqArgumentCaptor.capture());
        SqlAsyncExecuteReq capturedSqlAsyncExecuteReq = sqlAsyncExecuteReqArgumentCaptor.getValue();
        assertEquals(sqlAsyncExecuteReq, capturedSqlAsyncExecuteReq);
    }

    @Test(expected = RuntimeException.class)
    public void testHandle_maxRowsViolation() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(123L);
        when(session.getDialectType()).thenReturn(DialectType.MySQL);
        SqlAsyncExecuteResp sqlExecuteResp = mock(SqlAsyncExecuteResp.class);
        when(sqlExecuteResp.getSqls()).thenReturn(new ArrayList<>());
        when(sqlConsoleRuleService.getProperties(123L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL, Integer.class))
                .thenReturn(Optional.of(5));
        when(sqlExecuteResp.getSqls().size()).thenReturn(6);
        sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlExecuteResp, session, new HashMap<>());
    }

    @Test
    public void testHandle_sqlTypeViolation() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(123L);
        when(session.getDialectType()).thenReturn(DialectType.MySQL);
        SqlAsyncExecuteResp sqlExecuteResp = mock(SqlAsyncExecuteResp.class);
        when(sqlExecuteResp.getSqls()).thenReturn(new ArrayList<>());
        when(sqlConsoleRuleService.getListProperties(123L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL,
                String.class)).thenReturn(Optional.of(new ArrayList<>()));
        when(sqlExecuteResp.getSqls().get(0).getOriginalSql()).thenReturn("select * from test where id = 1");
        when(determineSqlType(Mockito.any(SqlTuple.class), Mockito.any(DialectType.class))).thenReturn(
                new BasicResult(SqlType.DDL));
        sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlExecuteResp, session, new HashMap<>());
        ArgumentCaptor<Rule> ruleArgumentCaptor = ArgumentCaptor.forClass(Rule.class);
        verify(ruleService, times(1)).getByRulesetIdAndName(eq(123L), eq(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()));
        Rule capturedRule = ruleArgumentCaptor.getValue();
        assertEquals(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName(), capturedRule.getName());
    }

    @Test
    public void testDoAfterCompletion_noViolations() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(123L);
        when(session.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        sqlConsoleInterceptor.doAfterCompletion(new SqlExecuteResult(), session, new HashMap<>());
        assertTrue(sqlConsoleInterceptor.isIndividualTeam());
        assertTrue(sqlExecuteResp.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_forbiddenEditResultSet() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(123L);
        when(session.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        sqlConsoleInterceptor.doAfterCompletion(new SqlExecuteResult(), session, new HashMap<>());
        assertFalse(sqlExecuteResp.getResultSetMetaData().isEditable());
    }

    @Test
    public void testDoAfterCompletion_notAllowedExport() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(123L);
        when(session.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session))
                .thenReturn(true);
        sqlConsoleInterceptor.doAfterCompletion(new SqlExecuteResult(), session, new HashMap<>());
        assertFalse(sqlExecuteResp.isAllowExport());
    }

    private BasicResult determineSqlType(SqlTuple sqlTuple, DialectType dialectType) {
        return new BasicResult(SqlType.DDL);
    }

    private static void assertEquals(SqlAsyncExecuteReq expected, SqlAsyncExecuteReq actual) {
        assertEquals(expected.getQuery(), actual.getQuery());
        assertEquals(expected.getQueryLimit(), actual.getQueryLimit());
        assertEquals(expected.getSessionId(), actual.getSessionId());
    }

    private static boolean isIndividualTeam() {
        return false;
    }
}