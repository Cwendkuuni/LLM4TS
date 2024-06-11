package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;
import java.util.Objects;

public class SqlConsoleInterceptorTest {

    @Mock
    private SqlAsyncExecuteReq mockRequest;

    @Mock
    private SqlAsyncExecuteResp mockResponse;

    @Mock
    private ConnectionSession mockSession;

    @Mock
    private Map<String, Object> mockContext;

    @Autowired
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Test
    public void testDoPreHandle() {
        boolean result = sqlConsoleInterceptor.doPreHandle(mockRequest, mockResponse, mockSession, mockContext);
        assertTrue(result);
    }

    @Test
    public void testHandle() {
        boolean result = sqlConsoleInterceptor.handle(mockRequest, mockResponse, mockSession, mockContext);
        assertFalse(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String stageName = sqlConsoleInterceptor.getExecuteStageName();
        assertNotNull(stageName);
        assertEquals("SQL_CONSOLE_RULE", stageName);
    }

    @Test
    public void testDoAfterCompletion() {
        sqlConsoleInterceptor.doAfterCompletion(mockResponse, mockSession, mockContext);
        // Add assertions as needed
    }

    @Test
    public void testIsIndividualTeam() {
        boolean result = sqlConsoleInterceptor.isIndividualTeam();
        assertTrue(result);
    }

    @Test
    public void testDetermineSqlType() {
        // Add test cases for determineSqlType method
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }
}