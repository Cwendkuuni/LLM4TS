
package com.oceanbase.odc.service.session.interceptor;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @Mock
    ConnectionSession session;

    @Mock
    SqlAsyncExecuteReq request;

    @Mock
    SqlAsyncExecuteResp response;

    @InjectMocks
    SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        when(request.getQueryLimit()).thenReturn(100);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
    }

    @Test
    public void testDoPreHandle() {
        HashMap<String, Object> context = new HashMap<>();
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
        assertTrue(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
        assertEquals(false, context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals(sqlConsoleInterceptor.getExecuteStageName(), SqlExecuteStages.SQL_CONSOLE_RULE);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        HashMap<String, Object> context = new HashMap<>();
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);

        assertEquals(true, sqlExecuteResult.getAllowExport());
        assertNotNull(sqlExecuteResult.getResultSetMetaData());
        assertEquals(false, sqlExecuteResult.getResultSetMetaData().getEditable());
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }
}
