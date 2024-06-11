import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.interceptor.SqlCheckInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private Map<String, Object> context;
    private ConnectionSession session;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        context = new HashMap<>();
        session = mock(ConnectionSession.class);
    }

    @Test
    public void testHandle_whenRuleSetIdIsNull() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testHandle_whenRuleSetIdNotNull() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        when(session.getRuleSetId()).thenReturn(123L);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion_whenStatusNotSuccess() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.FAILURE);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        // No exception thrown means test passed
    }

    // Add more test cases here to cover all methods in SqlConsoleInterceptor class

}