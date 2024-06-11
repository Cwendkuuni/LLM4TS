import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();

    @Test
    public void testDoPreHandle() {
        // typical use case
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);

        // edge case
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);

        // error scenario
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, false);
        result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testDoAfterCompletion() {
        // typical use case
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        assertTrue(response.isAllowExport());

        // edge case
        session.setUserType(UserType.ADMIN);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        assertFalse(response.getResultSetMetaData().isEditable());

        // error scenario
        response.setStatus(SqlExecuteStatus.FAIL);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        assertFalse(response.isAllowExport());
    }

}