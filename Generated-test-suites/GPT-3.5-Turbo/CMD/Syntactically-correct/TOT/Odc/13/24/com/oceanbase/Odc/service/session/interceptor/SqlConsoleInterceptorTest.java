import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private ConnectionSession session;
    private Map<String, Object> context;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();
        session = mock(ConnectionSession.class);
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
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String executeStageName = sqlConsoleInterceptor.getExecuteStageName();

        assertEquals("SQL_CONSOLE_RULE", executeStageName);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);

        assertTrue(sqlExecuteResult.isAllowExport());
    }
}