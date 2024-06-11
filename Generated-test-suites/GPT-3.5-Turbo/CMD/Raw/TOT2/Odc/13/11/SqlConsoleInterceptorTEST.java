import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
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
import static org.mockito.Mockito.when;

public class SqlConsoleInterceptorTest {

    @Mock
    private SqlAsyncExecuteReq mockRequest;

    @Mock
    private SqlAsyncExecuteResp mockResponse;

    @Mock
    private ConnectionSession mockSession;

    private SqlConsoleInterceptor interceptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        interceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle_withValidRequest_shouldReturnTrue() {
        Map<String, Object> context = new HashMap<>();
        when(mockSession.getDialectType()).thenReturn(DialectType.MYSQL);
        boolean result = interceptor.doPreHandle(mockRequest, mockResponse, mockSession, context);
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_withNullRequest_shouldReturnTrue() {
        Map<String, Object> context = new HashMap<>();
        when(mockSession.getDialectType()).thenReturn(DialectType.ORACLE);
        boolean result = interceptor.doPreHandle(null, mockResponse, mockSession, context);
        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion_withSuccessfulExecution_shouldAllowExport() {
        Map<String, Object> context = new HashMap<>();
        mockResponse.setStatus(SqlExecuteStatus.SUCCESS);
        mockResponse.setAllowExport(false);
        interceptor.doAfterCompletion(mockResponse, mockSession, context);
        assertTrue(mockResponse.isAllowExport());
    }
}