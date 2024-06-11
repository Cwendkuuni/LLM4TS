import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
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
        request = Mockito.mock(SqlAsyncExecuteReq.class);
        response = Mockito.mock(SqlAsyncExecuteResp.class);
        session = Mockito.mock(ConnectionSession.class);
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle() {
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String result = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals("SQL_CONSOLE_RULE", result);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult sqlExecuteResult = Mockito.mock(SqlExecuteResult.class);
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);
        // Add assertions here
    }

}