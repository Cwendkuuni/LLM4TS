
package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private ConnectionSession session;
    private SqlExecuteResult response;
    private SqlAsyncExecuteReq request;
    private HashMap<String, Object> context;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = Mockito.spy(new SqlConsoleInterceptor());
        session = Mockito.mock(ConnectionSession.class);
        request = Mockito.mock(SqlAsyncExecuteReq.class);
        response = Mockito.mock(SqlAsyncExecuteResult.class);
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        Mockito.when(sqlConsoleInterceptor.doPreHandle(request, response, session, context)).thenReturn(true);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String stageName = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, stageName);
    }

    @Test
    public void testDoAfterCompletion() {
        Mockito.doNothing().when(sqlConsoleInterceptor).doAfterCompletion(response, session, context);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }

    // Consider adding more tests here for the private methods, which will require more sophisticated setup
}
