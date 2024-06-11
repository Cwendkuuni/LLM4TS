package com.oceanbase.odc.service.session.interceptor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor interceptor;

    @Mock
    private SqlAsyncExecuteReq request;

    @Mock
    private SqlAsyncExecuteResp response;

    @Mock
    private ConnectionSession session;

    @Mock
    private Map<String, Object> context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testGetOrder() {
        assertEquals(2, interceptor.getOrder());
    }

    @Test
    public void testDoPreHandle() {
        // Mock necessary method
        when(context.get(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED)).thenReturn(null);
        // Test
        assertTrue(interceptor.doPreHandle(request, response, session, context));
	}
}