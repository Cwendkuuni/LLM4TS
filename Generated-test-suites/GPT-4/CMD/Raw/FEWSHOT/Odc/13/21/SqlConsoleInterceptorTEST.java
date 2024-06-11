
package com.oceanbase.odc.service.session.interceptor;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;

import java.util.Map;
import java.util.HashMap;

@RunWith(JUnit4.class)
@ComponentScan(basePackages = {"com.oceanbase.odc.service.session.interceptor"})
public class SqlConsoleInterceptorTests {
    // Create the class object you want to test
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    // Mock the dependencies
    @MockBean
    private ConnectionSession session;
    @MockBean
    private SqlAsyncExecuteReq req;
    @MockBean
    private SqlAsyncExecuteResp resp; 
    @MockBean
    private SqlExecuteResult sqlExecuteResult;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle() {
        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.doPreHandle(req, resp, session, context);
        assertFalse(result); // This should be modified based on your test logic
    }

    @Test
    public void testDoAfterCompletion() {
        Map<String, Object> context = new HashMap<>();
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);
        assertTrue(context.isEmpty()); // This should be modified based on your test logic
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }
}
