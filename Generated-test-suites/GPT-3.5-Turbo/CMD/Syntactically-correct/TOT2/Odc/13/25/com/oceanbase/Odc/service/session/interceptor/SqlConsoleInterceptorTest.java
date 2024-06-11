import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;

public class SqlConsoleInterceptorTest {

    @Test
    public void testDoPreHandle() {
        // Test if the method returns true when all conditions are met
        // Setup
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);

        // Execution
        boolean result = interceptor.doPreHandle(request, response, session, context);

        // Verification
        assertTrue(result);
    }


    @Test
    public void testDoPreHandleWithNonExistentRuleSet() {
        // Test if the method returns true when ruleSetId is null
        // Setup
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        // Execution
        boolean result = interceptor.doPreHandle(request, response, session, context);

        // Verification
        assertTrue(result);
    }

    // Add more test cases here for other public methods in SqlConsoleInterceptor class

}