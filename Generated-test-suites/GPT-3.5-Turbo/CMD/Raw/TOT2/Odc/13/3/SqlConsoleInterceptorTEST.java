import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private ConnectionSession session;
    private Map<String, Object> context;

    @Before
    public void setup() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        request = mock(SqlAsyncExecuteReq.class);
        response = mock(SqlAsyncExecuteResp.class);
        session = mock(ConnectionSession.class);
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandleAllowedCase() {
        when(session.getRuleSetId()).thenReturn(123L);
        when(session.isIndividualTeam()).thenReturn(false);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        
        assertEquals(true, result);
    }

    @Test
    public void testDoPreHandleNotAllowedCase() {
        when(session.getRuleSetId()).thenReturn(null);
        when(session.isIndividualTeam()).thenReturn(false);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        
        assertEquals(false, result);
    }

    @Test
    public void testDoAfterCompletionNotSuccessCase() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult(SqlExecuteStatus.FAILURE);
        
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);
        
        // Add assertions here
    }

    @Test
    public void testDoAfterCompletionSuccessCase() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult(SqlExecuteStatus.SUCCESS);
        
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);
        
        // Add assertions here
    }

    // Add more test cases for other methods in the SqlConsoleInterceptor class

}