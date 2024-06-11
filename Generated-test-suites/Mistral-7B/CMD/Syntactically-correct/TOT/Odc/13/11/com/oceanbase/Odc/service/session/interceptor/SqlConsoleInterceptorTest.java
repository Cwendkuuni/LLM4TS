package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeMock;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleServiceMock;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleServiceMock;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor.SqlConsoleInterceptorException;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

/**
 * Test class for {@link SqlConsoleInterceptor}.
 */
public class SqlConsoleInterceptorTest {

    private static final String SQL_CONSOLE_INTERCEPTED = "SQL_CONSOLE_INTERCEPTED";

    private ConnectionSession session;
    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setup() {
        session = Mockito.mock(ConnectionSession.class);
        authenticationFacade = new AuthenticationFacadeMock();
        ruleService = new RuleServiceMock();
        sqlConsoleRuleService = new SqlConsoleRuleServiceMock();
        sqlConsoleInterceptor = new SqlConsoleInterceptor() {
            {
                setAuthenticationFacade(authenticationFacade);
                setRuleService(ruleService);
                setSqlConsoleRuleService(sqlConsoleRuleService);
            }
        };
    }

    @Test
    public void testHandle_whenSqlIsAllowed_thenAllowExecute() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSql("SELECT * FROM users WHERE id = 1");

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(new SqlTuple(request.getSql().getBytes(),
                "1", "SELECT * FROM users WHERE id = 1", null, null, sqlExecuteResult))));

        // When
        boolean result = sqlConsoleInterceptor.handle(request, response, session, new HashMap<>());

        // Then
        assertTrue(result);
        verify(session, never()).setSqlConsoleIntercepted(SQL_CONSOLE_INTERCEPTED);
    }

    @Test
    public void testHandle_whenSqlIsForbidden_thenInterceptAndReturnFalse() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSql("CREATE TABLE my_table (id INT PRIMARY KEY, name VARCHAR(255))");

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(new SqlTuple(request.getSql().getBytes(),
                "1", "CREATE TABLE my_table (id INT PRIMARY KEY, name VARCHAR(255))", null, null, sqlExecuteResult))));

        doReturn(true).when(sqlConsoleRuleService)
                .isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_CREATE_PL), any());

        // When
        boolean result = sqlConsoleInterceptor.handle(request, response, session, new HashMap<>());

        // Then
        assertFalse(result);
        verify(session).setSqlConsoleIntercepted(SQL_CONSOLE_INTERCEPTED);
    }

    @Test
    public void testHandle_whenSqlTypeIsForbidden_thenInterceptAndReturnFalse() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSql("SELECT * FROM users WHERE id = 1");

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuple sqlTuple = new SqlTuple(request.getSql().getBytes(), "1", "SELECT * FROM users WHERE id = 1", null, null,
                null);
        BasicResult parseResult = new BasicResult(SqlType.XML);
        when(sqlTuple.getAst()).thenReturn(parseResult);

        doReturn(Arrays.asList(new Rule(SqlConsoleRules.ALLOW_SQL_TYPES, null, null, null, null, null))).when(ruleService)
                .getByRulesetIdAndName(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES);

        doReturn(false).when(sqlConsoleRuleService).isForbidden(eq(SqlConsoleRules.ALLOW_SQL_TYPES), any());

        // When
        boolean result = sqlConsoleInterceptor.handle(request, response, session, new HashMap<>());

        // Then
        assertFalse(result);
        verify(session).setSqlConsoleIntercepted(SQL_CONSOLE_INTERCEPTED);
    }

    @Test
    public void testHandle_whenSyntaxError_thenIgnore() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSql("CREATE DATABASASES my_database");

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(new SqlTuple(request.getSql().getBytes(),
                "1", "CREATE DATABASASES my_database", new SyntaxErrorException("Syntax error: unexpected identifier"), null,
                sqlExecuteResult))));

        // When
        boolean result = sqlConsoleInterceptor.handle(request, response, session, new HashMap<>());

        // Then
        assertTrue(result);
        verify(session, never()).setSqlConsoleIntercepted(SQL_CONSOLE_INTERCEPTED);
    }

    @Test(expected = SqlConsoleInterceptorException.class)
    public void testHandle_whenSqlExecutionFails_thenThrowException() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSql("SELECT * FROM non_existent_table");

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.FAILED);

        // When / Then
        sqlConsoleInterceptor.handle(request, response, session, new HashMap<>());
        fail("Expected SqlConsoleInterceptorException to be thrown");
    }

    @Test
    public void testDoAfterCompletion_whenSqlIsAllowed_thenAllowExport() {
        // Given
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSql("SELECT * FROM users WHERE id = 1");

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(new SqlTuple(request.getSql().getBytes(),
                "1", "SELECT * FROM users WHERE id = 1", null, null, sqlExecuteResult))));

        // When
        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

        // Then
        assertTrue(response.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_whenSqlIsForbidden_thenDontAllowExport() {
        // Given
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSql("CREATE TABLE my_table (id INT PRIMARY KEY, name VARCHAR(255))");

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);

        doReturn(true).when(sqlConsoleRuleService)
                .isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_CREATE_PL), any());

        // When
        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

        // Then
        assertFalse(response.isAllowExport());
    }

}