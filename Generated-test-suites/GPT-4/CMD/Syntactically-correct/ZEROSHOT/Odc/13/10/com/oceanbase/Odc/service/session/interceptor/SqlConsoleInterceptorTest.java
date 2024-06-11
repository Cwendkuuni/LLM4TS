package com.oceanbase.odc.service.session.interceptor;

import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import static org.junit.Assert.*;

public class SqlConsoleInterceptorTest {

    @InjectMocks
    SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    ConnectionSession connectionSession;

    @Mock
    SqlAsyncExecuteReq request;

    @Mock
    SqlAsyncExecuteResp response;

    @Mock
    SqlExecuteResult executeResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle() {
        MultiValueMap<String, Object> context = new LinkedMultiValueMap<>();

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        // Change this according to your requirement
        assertTrue(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String result = sqlConsoleInterceptor.getExecuteStageName();

        assertEquals("SQL_CONSOLE_RULE", result);
    }

    @Test
    public void testDoAfterCompletion() {
        MultiValueMap<String, Object> context = new LinkedMultiValueMap<>();

        sqlConsoleInterceptor.doAfterCompletion(executeResult, connectionSession, context);

        // here you can verify that the necessary methods
        // have been called on your mocks by the "doAfterCompletion" method.
    }

    @Test
    public void testGetOrder() {
        int result = sqlConsoleInterceptor.getOrder();

        assertEquals(2, result);
    }
}
