import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import java.util.HashMap;
import java.util.Map;

public class SqlConsoleInterceptorTest {

    @Mock
    private ConnectionSession sessionMock;

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, sessionMock, context);
        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        sqlConsoleInterceptor.doAfterCompletion(response, sessionMock, context);
        // No assertion, just checking for any exceptions
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.handle(request, response, sessionMock, context);
        assertTrue(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String executeStageName = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals("SQL_CONSOLE_RULE", executeStageName);
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }

}