package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.authenticationFacade = mock(AuthenticationFacade.class);
        sqlConsoleInterceptor.ruleService = mock(RuleService.class);
        sqlConsoleInterceptor.sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertNotNull(result);
        assertTrue(result);
        assertNotNull(context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
        assertFalse((Boolean) context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        assertNotNull(response.isAllowExport());
        assertFalse(response.isAllowExport());
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();

        assertEquals(2, order);
    }
}