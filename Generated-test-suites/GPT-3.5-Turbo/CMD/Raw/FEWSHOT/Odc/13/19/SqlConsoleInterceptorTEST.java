package com.oceanbase.odc.service.session.interceptor;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqlConsoleInterceptorTest {

    @Mock
    private ConnectionSession session;

    private Map<String, Object> context;

    private SqlConsoleInterceptor interceptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        interceptor = new SqlConsoleInterceptor();
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(request.getQueryLimit()).thenReturn(10);

        boolean result = interceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
        assertEquals(Boolean.TRUE, context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);

        when(request.getQueryLimit()).thenReturn(10);

        boolean result = interceptor.handle(request, response, session, context);

        assertTrue(result);
        assertEquals(Boolean.TRUE, context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testGetExecuteStageName() {
        String expected = "SqlExecuteStages.SQL_CONSOLE_RULE";
        String result = interceptor.getExecuteStageName();

        assertEquals(expected, result);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        interceptor.doAfterCompletion(response, session, context);

        // Assert whatever needs to be done here
    }

    @Test
    public void testIsIndividualTeam() {
        // Test logic here
    }

    @Test
    public void testDetermineSqlType() {
        // Test logic here
    }

    @Test
    public void testGetOrder() {
        int expected = 2;
        int result = interceptor.getOrder();

        assertEquals(expected, result);
    }
}