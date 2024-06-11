import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        // Add assertions here
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();

        assertEquals(2, order);
    }

}