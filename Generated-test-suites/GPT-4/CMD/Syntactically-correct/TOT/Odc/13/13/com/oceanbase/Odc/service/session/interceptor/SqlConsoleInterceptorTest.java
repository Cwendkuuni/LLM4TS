package com.oceanbase.odc.service.session.interceptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor interceptor;

    @Mock
    private SqlAsyncExecuteResp response;
    
    @Mock
    private SqlAsyncExecuteReq request;
    
    @Mock
    private ConnectionSession session;
    
    @Mock
    private Map<String, Object> context;

    @Before
    public void setUp(){
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        when(request.getQueryLimit()).thenReturn(100);
        when(context.get(NEED_SQL_CONSOLE_CHECK)).thenReturn(null);
        when(context.get(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED)).thenReturn(null);
        assertNull(context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
        boolean result = interceptor.doPreHandle(request, response, session, context);
        assertEquals(true, result);
        assertNotNull(context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
        assertEquals(false, context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }
  
    // More tests can be added here to cover all methods and scenarios.

}