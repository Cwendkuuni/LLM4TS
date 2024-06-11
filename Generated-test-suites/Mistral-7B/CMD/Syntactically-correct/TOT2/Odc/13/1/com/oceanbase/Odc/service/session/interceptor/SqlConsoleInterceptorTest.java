package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.User;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRules;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlCheckInterceptor;

/**
 * Test class for SqlConsoleInterceptor
 */
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private User currentUser;

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_NoRuleSetId_ReturnsTrue() {
        // Given
        when(authenticationFacade.currentUser()).thenReturn(currentUser);
        when(currentUser.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(null);

        // When
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, Collections.emptyMap());

        // Then
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_IndividualTeam_ReturnsTrue() {
        // Given
        when(authenticationFacade.currentUser()).thenReturn(currentUser);
        when(currentUser.getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(Optional.ofNullable(1L));

        // When
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, Collections.emptyMap());

        // Then
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_RuleSetId_ChecksRules() {
        // Given
        when(authenticationFacade.currentUser()).thenReturn(currentUser);
        when(currentUser.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(Optional.ofNullable(1L));

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, ArgumentMatchers.eq(DialectType.MySQL),
                Integer.class)).thenReturn(Optional.ofNullable(10));

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQueryLimit(20);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = Collections.emptyMap();

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        // Then
        assertFalse(result);
        verify(ruleService, times(1)).getByRulesetIdAndName(1L, SqlConsoleRules.MAX_RETURN_ROWS.getRuleName());
    }

    @Test
    public void testDoPreHandle_AllowExecute() {
        // Given
        when(authenticationFacade.currentUser()).thenReturn(currentUser);
        when(currentUser.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(Optional.ofNullable(1L));

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = Collections.emptyMap();

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        // Then
        assertTrue(result);
    }

    // Add more test cases for other methods in SqlConsoleInterceptor

    @Test
    public void testHandle_ForbiddenToCreatePl_ViolationRule() {
        // Given
        when(authenticationFacade.currentUser()).thenReturn(currentUser);
        when(currentUser.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(Optional.ofNullable(1L));

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQueryLimit(1);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = Collections.emptyMap();

        List<Rule> violationRules = new ArrayList<>();
        Rule rule = mock(Rule.class);
        RuleViolation violation = new RuleViolation();
        violation.setLevel(1);
        violation.setLocalizedMessage("Maximum number of SQLs allowed to execute exceeded.");
        violation.setOffset(0);
        violation.setStart(0);
        violation.setStop(request.getOriginalSql().length());
        violationRule.setViolation(violation);
        violationRules.add(rule);

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, ArgumentMatchers.eq(DialectType.MySQL),
                String.class)).thenReturn(Arrays.asList("DDL"));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName()))
                .thenReturn(Optional.ofNullable(rule));

        // When
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        // Then
        assertFalse(result);
        verify(sqlConsoleRuleService, times(1)).getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, ArgumentMatchers.eq(DialectType.MySQL),
                Integer.class);
        verify(ruleService, times(1)).getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName());
    }

    @Test
    public void testHandle_AllowExecute() {
        // Given
        when(authenticationFacade.currentUser()).thenReturn(currentUser);
        when(currentUser.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(Optional.ofNullable(1L));

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQueryLimit(10);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = Collections.emptyMap();

        // When
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        // Then
        assertTrue(result);
        verify(sqlConsoleRuleService, never()).getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, ArgumentMatchers.eq(DialectType.MySQL),
                Integer.class);
        verify(ruleService, never()).getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName());
    }

    @Test
    public void testDoAfterCompletion_NotAllowedToEditResultSet_ReturnsFalse() {
        // Given
        when(authenticationFacade.currentUser()).thenReturn(currentUser);
        when(currentUser.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(Optional.ofNullable(1L));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);

        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session1 = mock(ConnectionSession.class);
        Map<String, Object> context1 = Collections.emptyMap();

        // When
        boolean result = sqlConsoleInterceptor.doAfterCompletion(response, session1, context1);

        // Then
        assertFalse(result);
    }

    @Test
    public void testDoAfterCompletion_AllowExport() {
        // Given
        when(authenticationFacade.currentUser()).thenReturn(currentUser);
        when(currentUser.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(Optional.ofNullable(1L));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(false);

        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session1 = mock(ConnectionSession.class);
        Map<String, Object> context1 = Collections.emptyMap();

        // When
        boolean result = sqlConsoleInterceptor.doAfterCompletion(response, session1, context1);

        // Then
        assertTrue(result);
    }

    // Add more test cases for other methods in SqlConsoleInterceptor

    @Test
    public void testDetermineSqlType_BasicResult() {
        // Given
        String sql = "SELECT * FROM employees WHERE id = ?";
        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        BasicResult basicResult = new BasicResult(SqlType.SQL);

        when(authenticationFacade.currentUser()).thenReturn(currentUser);
        when(currentUser.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(Optional.ofNullable(1L));

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, ArgumentMatchers.eq(DialectType.MySQL),
                String.class)).thenReturn(Arrays.asList("DDL", "DML", "SQL"));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.ofNullable(rule));

        when(ast.getParseResult()).thenReturn(basicResult);

        // When
        BasicResult result = sqlConsoleInterceptor.determineSqlType((SqlTuple) null, DialectType.MySQL);

        // Then
        assertEquals(basicResult, result);
    }

    @Test
    public void testDetermineSqlType_SyntaxError() {
        // Given
        String sql = "SELECT * FROM employees WHERE id = ?";
        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        BasicResult basicResult = new BasicResult(SqlType.UNKNOWN);

        when(authenticationFacade.currentUser()).thenReturn(currentUser);
        when(currentUser.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(Optional.ofNullable(1L));

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, ArgumentMatchers.eq(DialectType.MySQL),
                String.class)).thenReturn(Arrays.asList("DDL", "DML", "SQL"));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.ofNullable(rule));

        when(ast.getParseResult()).thenThrow(new SyntaxErrorException("Error message"));

        // When
        BasicResult result = sqlConsoleInterceptor.determineSqlType((SqlTuple) null, DialectType.MySQL);

        // Then
        assertEquals(basicResult, result);
    }

    // Add more test cases for other methods in SqlConsoleInterceptor

    @Test
    public void testGetExecuteStageName() {
        // Given
        when(authenticationFacade.currentUser()).thenReturn(currentUser);
        when(currentUser.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(Optional.ofNullable(1L));

        // When
        String result = sqlConsoleInterceptor.getExecuteStageName();

        // Then
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, result);
    }

    @Test
    public void testGetOrder() {
        // Given
        when(authenticationFacade.currentUser()).thenReturn(currentUser);
        when(currentUser.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(Optional.ofNullable(1L));

        // When
        int result = sqlConsoleInterceptor.getOrder();

        // Then
        assertEquals(2, result);
    }
}