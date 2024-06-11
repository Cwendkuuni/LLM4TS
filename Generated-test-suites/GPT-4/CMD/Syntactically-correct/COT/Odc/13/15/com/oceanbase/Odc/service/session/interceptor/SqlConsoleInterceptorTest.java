
package com.oceanbase.odc.service.session.interceptor;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {
    
    @InjectMocks 
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private SqlAsyncExecuteReq request;

    @Mock
    private SqlAsyncExecuteResp response;
    
    private Map<String, Object> context;

    @Before
    public void setUp() {
       context = new HashMap<>();
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals("SQL_CONSOLE_RULE", sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }

    @Test
    public void testDoPreHandle() {
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context));
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult executeResult = mock(SqlExecuteResult.class);
        when(executeResult.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);
        sqlConsoleInterceptor.doAfterCompletion(executeResult, connectionSession, context);
        verify(executeResult, never()).setAllowExport(true);
    }

    @Test
    public void testDoAfterCompletionFailure() {
        SqlExecuteResult executeResult = mock(SqlExecuteResult.class);
        when(executeResult.getStatus()).thenReturn(SqlExecuteStatus.FAILURE);
        sqlConsoleInterceptor.doAfterCompletion(executeResult, connectionSession, context);
        verify(executeResult, never()).setAllowExport(true);
    }

    //Implement additional edge case and exception tests below such as testing 'handle' method
}

