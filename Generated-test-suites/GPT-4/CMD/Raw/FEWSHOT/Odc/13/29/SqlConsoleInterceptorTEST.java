package com.oceanbase.odc.service.session.interceptor;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @Mock
    ConnectionSession session;

    @Mock
    SqlAsyncExecuteReq request;

    @Mock
    SqlAsyncExecuteResp response;

    @Mock
    SqlExecuteResult sqlExecuteResult;

    @InjectMocks
    SqlConsoleInterceptor sqlConsoleInterceptor;

    @Test
    public void testDoPreHandle() {
        Map<String, Object> context = new HashMap<>();
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        Boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion() {
        Map<String, Object> context = new HashMap<>();
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);
        verify(sqlExecuteResult, times(1)).getStatus();
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }

    @Test
    public void testGetExecuteStageName() {
        String stageName = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals("SQL_CONSOLE_RULE", stageName);
    }

}