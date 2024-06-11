import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;

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

        when(request.getQueryLimit()).thenReturn(null);
        when(session.getDialectType()).thenReturn(null);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String stageName = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals("SQL_CONSOLE_RULE", stageName);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        // It is difficult to test functionality without more information
    }

    @Test
    public void testIsIndividualTeam() {
        boolean result = sqlConsoleInterceptor.isIndividualTeam();
        assertFalse(result);
        // Additional test cases may be needed depending on the implementation
    }

    @Test
    public void testDetermineSqlType() {
        // Test cases for determineSqlType method
        // Mock necessary objects and test the method with different scenarios
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }
}