import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private ConnectionSession session;
    private Map<String, Object> context;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        request = mock(SqlAsyncExecuteReq.class);
        response = mock(SqlAsyncExecuteResp.class);
        session = mock(ConnectionSession.class);
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        // Typical Use Case
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);

        // Edge Case
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
        result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);

        // Error Scenario
        result = sqlConsoleInterceptor.doPreHandle(null, response, session, context);
        assertFalse(result);
    }


    @Test
    public void testDoAfterCompletion() {
        // Typical Use Case
        SqlAsyncExecuteResp successResponse = new SqlAsyncExecuteResp();
        successResponse.setStatus(SqlExecuteStatus.SUCCESS);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        sqlConsoleInterceptor.doAfterCompletion(successResponse, session, context);
        assertTrue(successResponse.isAllowExport());

        // Edge Case
        SqlAsyncExecuteResp errorResponse = new SqlAsyncExecuteResp();
        errorResponse.setStatus(SqlExecuteStatus.ERROR);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
        sqlConsoleInterceptor.doAfterCompletion(errorResponse, session, context);
        assertFalse(errorResponse.isAllowExport());

        // Error Scenario
        sqlConsoleInterceptor.doAfterCompletion(successResponse, null, context);
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }
}