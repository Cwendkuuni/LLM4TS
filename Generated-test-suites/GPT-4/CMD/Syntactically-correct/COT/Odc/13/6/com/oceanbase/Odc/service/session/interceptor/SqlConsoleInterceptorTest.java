package com.oceanbase.odc.service.session.interceptor;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class SqlConsoleInterceptorTest {

    @Mock
    SqlAsyncExecuteReq request;

    @Mock
    SqlAsyncExecuteResp response;

    @Mock
    ConnectionSession session;

    @Mock
    Map<String, Object> context;

    @Mock
    SqlExecuteResult resp;

    @InjectMocks
    SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle() {
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion() {
        sqlConsoleInterceptor.doAfterCompletion(resp, session, context);
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }

}