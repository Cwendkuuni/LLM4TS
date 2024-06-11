import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private SqlAsyncExecuteReq request;

    @Mock
    private SqlAsyncExecuteResp response;

    @Mock
    private ConnectionSession session;

    @Mock
    private Map<String, Object> context;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle() {
        // Typical use case
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(context.get(eq(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED))).thenReturn(true);
        when(context.get(eq(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK))).thenReturn(true);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);

        // Edge case
        when(context.get(eq(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED))).thenReturn(null);

        result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion() {
        // Typical use case
        when(session.getDialectType()).thenReturn(DialectType.ORACLE);
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET), eq(session))).thenReturn(false);

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);

        assertTrue(sqlExecuteResult.isAllowExport());

        // Error scenario
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET), eq(session))).thenReturn(true);

        sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);

        assertFalse(sqlExecuteResult.isAllowExport());
    }

}