
package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.session.ConnectionSession;
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
    @Mock
    private SqlExecuteResult executeResult;

    private Map<String, Object> context;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        when(response.getSqls()).thenReturn(new ArrayList<>());
        boolean doPreHandle = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        assertTrue(doPreHandle);
    }

    @Test
    public void testDoAfterCompletion() {
        when(executeResult.getStatus()).thenReturn(null);
        sqlConsoleInterceptor.doAfterCompletion(executeResult, connectionSession, context);
        assertFalse(executeResult.isAllowExport());
        
        when(executeResult.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);
        sqlConsoleInterceptor.doAfterCompletion(executeResult, connectionSession, context);
        assertTrue(executeResult.isAllowExport());
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }
}
