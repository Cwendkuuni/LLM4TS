package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private ConnectionSession mockSession;

    @Mock
    private SqlAsyncExecuteReq mockRequest;

    @Mock
    private SqlAsyncExecuteResp mockResponse;

    @Test
    public void testDoPreHandle() {
        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.doPreHandle(mockRequest, mockResponse, mockSession, context);

        assertTrue(result);
        assertTrue(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testGetExecuteStageName() {
        assertThat(sqlConsoleInterceptor.getExecuteStageName(), is(SqlExecuteStages.SQL_CONSOLE_RULE));
    }

    @Test
    public void testGetOrder() {
        assertThat(sqlConsoleInterceptor.getOrder(), is(2));
    }

    @Test
    public void testHandle() {
        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.doPreHandle(mockRequest, mockResponse, mockSession, context);

        assertTrue(result);
    }

    // Continue with the test cases for rest of the methods in SqlConsoleInterceptor...
}