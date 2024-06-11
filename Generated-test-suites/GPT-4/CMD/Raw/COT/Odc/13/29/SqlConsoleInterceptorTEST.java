
package com.oceanbase.odc.service.session.interceptor;

import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.*;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import org.junit.Before;
import org.junit.After;

public class SqlConsoleInterceptorTest {
    
    SqlConsoleInterceptor sqlConsoleInterceptor;
    ConnectionSession session;
    SqlAsyncExecuteReq request;
    SqlAsyncExecuteResp response;
    Map<String, Object> context;

    @Before
    public void setup() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        session = mock(ConnectionSession.class);
        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandleNoCheckIntercepted() {
        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletion() {
        session.setResponseStatus(SqlExecuteStatus.SUCCESS);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        assertTrue(response.isAllowExport());
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }

    @After
    public void tearDown() {
        sqlConsoleInterceptor = null;
        session = null;
        request = null;
        response = null;
        context = null;
    }

}
