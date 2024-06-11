package com.oceanbase.odc.service.session.interceptor;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor interceptor;
    private SqlAsyncExecuteReq mockRequest;
    private SqlAsyncExecuteResp mockResponse;
    private ConnectionSession mockSession;
    private Map<String, Object> mockContext;
    private SqlExecuteResult mockResult;

    @Before
    public void setup() {
        interceptor = new SqlConsoleInterceptor();
        mockRequest = mock(SqlAsyncExecuteReq.class);
        mockResponse = mock(SqlAsyncExecuteResp.class);
        mockSession = mock(ConnectionSession.class);
        mockContext = new HashMap<>();
        mockResult = mock(SqlExecuteResult.class);
    }

    @Test
    public void testDoPreHandle() {
        // Assuming the handle method returns true
        boolean result = interceptor.doPreHandle(mockRequest, mockResponse, mockSession, mockContext);
        // Expected result is true
        assertEquals(true, result);
    }

    @Test
    public void testDoAfterCompletion() {
        interceptor.doAfterCompletion(mockResult, mockSession, mockContext);
        // No immediate way to assert the result as the method is void.
        // However, we can verify that certain methods were called within this method.
        verify(mockResult, times(1)).getStatus();
    }

    @Test
    public void testGetOrder() {
        int order = interceptor.getOrder();
        // The getOrder method should return 2 as defined in the source code.
        assertEquals(2, order);
    }

    @Test
    public void testGetExecuteStageName() {
        String stageName = interceptor.getExecuteStageName();
        // getExecuteStageName() should return "SQL_CONSOLE_RULE"
        assertEquals("SQL_CONSOLE_RULE", stageName);
    }
}