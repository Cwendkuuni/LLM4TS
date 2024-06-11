import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        // Typical use case
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));

        // Edge case
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, false);
        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));

        // Error scenario
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult executeResult = new SqlExecuteResult();
        executeResult.setStatus(SqlExecuteStatus.SUCCESS);

        // Typical use case
        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, context);
        assertTrue(executeResult.isAllowExport());

        // Edge case
        executeResult.setStatus(SqlExecuteStatus.FAILURE);
        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, context);
        assertFalse(executeResult.isAllowExport());

        // Error scenario
        executeResult.setStatus(SqlExecuteStatus.SUCCESS);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        assertFalse(sqlConsoleInterceptor.isIndividualTeam());
    }
}