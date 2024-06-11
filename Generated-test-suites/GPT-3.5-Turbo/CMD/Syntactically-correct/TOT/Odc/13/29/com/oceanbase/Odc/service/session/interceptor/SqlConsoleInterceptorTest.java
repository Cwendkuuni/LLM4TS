
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

public class SqlConsoleInterceptorTest {

    @Mock
    private ConnectionSession session;

    @InjectMocks
    private SqlConsoleInterceptor interceptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        boolean result = interceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setStatus("SUCCESS");

        interceptor.doAfterCompletion(response, session, new HashMap<>());

        // Validation code for the behavior after completion
    }
}

