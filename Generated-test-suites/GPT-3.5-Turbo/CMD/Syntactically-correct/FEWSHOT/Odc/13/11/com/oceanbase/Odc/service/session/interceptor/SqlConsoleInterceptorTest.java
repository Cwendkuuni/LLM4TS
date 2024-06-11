// create class for test cases
package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor interceptor;

    @Before
    public void setUp() {
        interceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle() {
        // Test case 1: Check if pre handle returns true when all conditions are met
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(context.get(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED)).thenReturn(true);

        assertTrue(interceptor.doPreHandle(request, response, session, context));

        // Test case 2: Check if pre handle returns false when rule is not set for team
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        assertTrue(interceptor.doPreHandle(request, response, session, context));

        // Test case 3: Check if pre handle returns true when SQL_CHECK_INTERCEPTED is null
        when(context.get(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED)).thenReturn(null);

        assertTrue(interceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletion() {
        // Test case 1: Check if allowExport is set to true when status is SUCCESS
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(response.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);
        when(authenticationFacade.currentUser()).thenReturn(new User());
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET), any(ConnectionSession.class))).thenReturn(false);

        interceptor.doAfterCompletion(response, session, context);

        assertTrue(response.isAllowExport());

        // Test case 2: Check if allowExport is set to false when status is not SUCCESS
        when(response.getStatus()).thenReturn(SqlExecuteStatus.ERROR);

        interceptor.doAfterCompletion(response, session, context);

        assertFalse(response.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam() {
        // Test case 1: Check if isIndividualTeam returns true for INDIVIDUAL organization type
        when(authenticationFacade.currentUser()).thenReturn(new User(OrganizationType.INDIVIDUAL));

        assertTrue(interceptor.isIndividualTeam());

        // Test case 2: Check if isIndividualTeam returns false for other organization types
        when(authenticationFacade.currentUser()).thenReturn(new User(OrganizationType.TEAM));

        assertFalse(interceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType() {
        // Test case 1: Check if determineSqlType returns correct BasicResult for valid input
        SqlTuple sqlTuple = mock(SqlTuple.class);
        when(sqlTuple.getAst()).thenReturn(mock(AbstractSyntaxTree.class));
        when(sqlTuple.getOffset()).thenReturn(0);
        when(sqlTuple.getOriginalSql()).thenReturn("SELECT * FROM table");

        BasicResult result = interceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        assertEquals(SqlType.SELECT, result.getSqlType());

        // Test case 2: Check if determineSqlType handles SyntaxErrorException and sets result as OTHERS
        when(sqlTuple.getAst()).thenThrow(SyntaxErrorException.class);

        result = interceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        assertEquals(SqlType.OTHERS, result.getSqlType());
    }
}