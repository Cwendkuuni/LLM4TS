package com.oceanbase.odc.service.session.interceptor;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle() {
        // Test case when ruleSetId is null and isIndividualTeam is false
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        when(session.getRuleSetId()).thenReturn(null);
        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
        
        // Test case when context does not contain NEED_SQL_CONSOLE_CHECK
        when(session.getRuleSetId()).thenReturn(123L);
        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));

        // Test case when context contains SQL_CHECK_INTERCEPTED
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletion() {
        // Test case when response status is not SUCCESS
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        response.setStatus(SqlExecuteStatus.FAILURE);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        // Verify that allowExport should not be set
        assertNull(response.isAllowExport());
        
        // Test case when user is INDIVIDUAL
        when(session.getRuleSetId()).thenReturn(null);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        response.setStatus(SqlExecuteStatus.SUCCESS);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        // Verify that allowExport should be set to true
        assertTrue(response.isAllowExport());
        
        // Test case when rules are applied
        when(session.getRuleSetId()).thenReturn(123L);
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET), any(ConnectionSession.class))).thenReturn(true);
        response.setResultSetMetaData(mock(ResultSetMetaData.class));
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        // Verify that allowExport should be set to false
        assertFalse(response.isAllowExport());
    }

    @Test
    public void testHandle() {
        // Test case when ruleSetId is null and isIndividualTeam is false
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        when(session.getRuleSetId()).thenReturn(null);
        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
        
        // Test case when NEED_SQL_CONSOLE_CHECK is false
        when(session.getRuleSetId()).thenReturn(123L);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));

        // Test case when queryLimit is present
        RuleService ruleService = mock(RuleService.class);
        when(sqlConsoleRuleService.getProperties(eq(123L), eq(SqlConsoleRules.MAX_RETURN_ROWS), any(DialectType.class), eq(Integer.class))).thenReturn(Optional.of(10));
        when(ruleService.getByRulesetIdAndName(eq(123L), eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))).thenReturn(Optional.empty());
        when(response.getSqls()).thenReturn(new ArrayList<>());
        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testDetermineSqlType() {
        // Test case when AbstractSyntaxTree is null
        SqlTuple sqlTuple = mock(SqlTuple.class);
        ConnectionSession session = mock(ConnectionSession.class);
        when(sqlTuple.getAst()).thenReturn(null);
        when(sqlTuple.getSqlId()).thenReturn("123");
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        assertNotNull(sqlConsoleInterceptor.determineSqlType(sqlTuple, session.getDialectType()));
        
        // Test case when exception is thrown
        when(sqlTuple.getAst()).thenThrow(new SyntaxErrorException("Syntax Error"));
        assertNotNull(sqlConsoleInterceptor.determineSqlType(sqlTuple, session.getDialectType()));
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