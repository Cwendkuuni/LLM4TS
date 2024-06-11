package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

import java.util.HashMap;
import java.util.Map;

public class SqlConsoleInterceptorTest {
    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private SqlAsyncExecuteReq request;

    @Mock
    private SqlAsyncExecuteResp response;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ConnectionSessionUtil sessionUtil = new ConnectionSessionUtil();
        when(connectionSession.getRuleSetId()).thenReturn(123L);
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        context.put(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED, false);
    }

    @Test
    public void testDoPreHandleCase1() {
      //Test when SQL_CHECK_INTERCEPTED context is not null.
      //Expect result: false
        Map<String, Object> context = new HashMap<>();
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        assertEquals(false, result);
    }
  
    @Test
    public void testDoPreHandleCase2() {
      //Test when SQL_CHECK_INTERCEPTED context is null.
      //Expect result: true
        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        assertEquals(true, result);
    }

    @Test
    public void testDoAfterCompletion() {
      //Simply test doAfterCompletion method to cover the logic.
      SqlExecuteResult response = new SqlExecuteResult();
      Map<String, Object> context = new HashMap<>();
      sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, context);

      //Check if response's 'allowExport' variable is updated or not
      assertNull(response.isAllowExport());
    }

    @Test
    public void testGetOrder() {
        //Test getOrder method
        //Expect result: 2
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }

    @Test
    public void testGetExecuteStageName() {
        //Test getExecuteStageName method
        //Expect result: "SQL_CONSOLE_RULE"
        assertEquals("SQL_CONSOLE_RULE", sqlConsoleInterceptor.getExecuteStageName());
    }
}