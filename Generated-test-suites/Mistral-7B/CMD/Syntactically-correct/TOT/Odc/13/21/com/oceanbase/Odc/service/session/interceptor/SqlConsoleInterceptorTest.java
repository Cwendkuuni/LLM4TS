package com.oceanbase.odc.service.session.interceptor;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
import org.mockito.StickyEnumSet;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.core.sql.parser.SyntaxErrorException;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxError;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleSuccess() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = Collections.emptyMap();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Optional.of(Arrays.asList("SELECT")));

        when(sqlConsoleInterceptor.handle(request, response, session, context)).thenReturn(true);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testHandleMaxSqlSize() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = Collections.emptyMap();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(10));

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Optional.empty());

        when(sqlConsoleInterceptor.handle(request, response, session, context)).thenThrow(new RuntimeException());

        StickyEnumSet<SqlConsoleRules> rules = StickyEnumSet.create(SqlConsoleRules.class);
        Rule violationRule = new Rule();
        violationRule.setName(SqlConsoleRules.MAX_EXECUTE_SQLS.getName());
        RuleViolation violation = new RuleViolation();
        violation.setLevel(Rule.Level.ERROR);
        violation.setMessage("Maximum number of SQLs reached");
        violationRule.setViolation(violation);
        rules.put(SqlConsoleRules.MAX_EXECUTE_SQLS, violationRule);

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getName())).thenReturn(Optional.of(violationRule));

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);

        when(sqlConsoleInterceptor.handle(request, response, session, context)).thenReturn(false);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testHandleForbiddenSqlType() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = Collections.emptyMap();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(10));

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Optional.of(Arrays.asList("DDL")));

        when(sqlConsoleInterceptor.handle(request, response, session, context)).thenThrow(new RuntimeException());

        StickyEnumSet<SqlConsoleRules> rules = StickyEnumSet.create(SqlConsoleRules.class);
        Rule violationRule = new Rule();
        violationRule.setName(SqlConsoleRules.ALLOW_SQL_TYPES.getName());
        RuleViolation violation = new RuleViolation();
        violation.setLevel(Rule.Level.ERROR);
        violation.setMessage("Forbidden SQL type: DDL");
        violationRule.setViolation(violation);
        rules.put(SqlConsoleRules.ALLOW_SQL_TYPES, violationRule);

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getName())).thenReturn(Optional.of(violationRule));

        when(sqlConsoleInterceptor.handle(request, response, session, context)).thenReturn(false);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testHandleSyntaxError() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = Collections.emptyMap();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(10));

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);

        when(sqlConsoleInterceptor.handle(request, response, session, context)).thenThrow(new SyntaxErrorException("Test syntax error"));

        when(sqlConsoleInterceptor.handle(request, response, session, context)).thenReturn(false);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletionSuccess() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = Collections.emptyMap();
        SqlExecuteResult response = new SqlExecuteResult();

        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(false);

        when(sqlConsoleInterceptor.doAfterCompletion(response, session, context)).thenReturn();

        assertTrue(response.isAllowExport());
    }

    @Test
    public void testDoAfterCompletionForbiddenEditResultSet() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = Collections.emptyMap();
        SqlExecuteResult response = new SqlExecuteResult();

        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);

        when(sqlConsoleInterceptor.doAfterCompletion(response, session, context)).thenReturn();

        assertFalse(response.isAllowExport());
    }

    @Test
    public void testDoAfterCompletionSuccessIndividualTeam() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = Collections.emptyMap();
        SqlExecuteResult response = new SqlExecuteResult();

        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        when(sqlConsoleInterceptor.doAfterCompletion(response, session, context)).thenReturn();

        assertTrue(response.isAllowExport());
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals("SQL_CONSOLE_RULE", sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }
}