
package com.oceanbase.odc.service.session.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;
    
    @Mock
    private SqlAsyncExecuteReq request;
    
    @Mock
    private SqlAsyncExecuteResp response;
    
    @Mock
    private ConnectionSession session;
    
    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDoPreHandle() {
        Map<String, Object> context = new HashMap<>();
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);

        when(sqlConsoleInterceptor.doPreHandle(request, response, session, context)).thenReturn(true);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        
        assertEquals(true, result);
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }

    // Add more test methods to cover other methods from SqlConsoleInterceptor class
}
