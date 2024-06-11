package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        
        context.put(SqlConsoleInterceptor.SQL_CHECK_INTERCEPTED, true);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
        
        context.put(SqlConsoleInterceptor.SQL_CHECK_INTERCEPTED, false);
        result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testHandle() {
        // Implement test cases for handle method of SqlConsoleInterceptor
    }

    @Test
    public void testGetExecuteStageName() {
        String executeStageName = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals("SqlExecuteStages.SQL_CONSOLE_RULE", executeStageName);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        
        when(response.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        // Add assertions for the logic in doAfterCompletion method
    }

    @Test
    public void testIsIndividualTeam() {
        boolean isIndividual = sqlConsoleInterceptor.isIndividualTeam();
        // Add assertions for the logic in isIndividualTeam method
    }

    @Test
    public void testDetermineSqlType() {
        // Implement test cases for determineSqlType method of SqlConsoleInterceptor
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }
}