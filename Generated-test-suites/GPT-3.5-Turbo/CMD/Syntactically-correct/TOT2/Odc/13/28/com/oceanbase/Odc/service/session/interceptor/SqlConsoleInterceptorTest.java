import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class SqlConsoleInterceptorTest {

    @Mock
    ConnectionSession connectionSession;
    @Mock
    SqlAsyncExecuteReq request;
    @Mock
    SqlAsyncExecuteResp response;

    private SqlConsoleInterceptor interceptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        interceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle_AllowsExecution() {
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.SQL_CHECK_INTERCEPTED, true);
        boolean result = interceptor.doPreHandle(request, response, connectionSession, context);
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_DisallowsExecution() {
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.SQL_CHECK_INTERCEPTED, false);
        boolean result = interceptor.doPreHandle(request, response, connectionSession, context);
        assertFalse(result);
    }

    // Additional test cases can be added for other methods in SqlConsoleInterceptor class

}