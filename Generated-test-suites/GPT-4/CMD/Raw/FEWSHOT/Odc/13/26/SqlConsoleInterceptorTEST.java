
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
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {
    
    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private ConnectionSession connectionSession;
    @Mock
    private SqlAsyncExecuteReq sqlAsyncExecuteReq;
    @Mock
    private SqlAsyncExecuteResp sqlAsyncExecuteResp;
    private Map<String, Object> context = new HashMap<>();
    @Mock
    private SqlExecuteResult sqlExecuteResult;
        
    @Before
    public void setup() {
        //Initialize setup
    }

    @Test
    public void testDoPreHandle() {
        // Assuming the required context
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, Boolean.FALSE);
        boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context);
        assertEquals(true, result);
    }

    @Test
    public void testDoAfterCompletion() {
        // Assuming the required context
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);
        // Validate the result
    }

    @Test
    public void testGetOrder() {
        int result = sqlConsoleInterceptor.getOrder();
        assertEquals(2, result);
    }

    @Test
    public void testGetExecuteStageName() {
        String result = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals("SQL_CONSOLE_RULE", result);
    }

    @Test
    public void testShouldHandle() {
        // Assuming the required context
        boolean result = sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context);
        assertEquals(true, result);
    }
    
}
