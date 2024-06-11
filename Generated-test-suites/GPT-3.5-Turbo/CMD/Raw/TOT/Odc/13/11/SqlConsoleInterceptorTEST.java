import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.session.ConnectionSession;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor interceptor;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private ConnectionSession session;
    private Map<String, Object> context;

    @Before
    public void setUp() {
        interceptor = new SqlConsoleInterceptor();
        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();
        session = new ConnectionSession();
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        context.put(SqlConsoleInterceptor.SQL_CHECK_INTERCEPTED, false);
        boolean result = interceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle() {
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        boolean result = interceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String executeStageName = interceptor.getExecuteStageName();
        assertNotNull(executeStageName);
        assertEquals("SQL_CONSOLE_RULE", executeStageName);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlAsyncExecuteResp successResponse = new SqlAsyncExecuteResp();
        successResponse.setStatus(SqlExecuteStatus.SUCCESS);
        interceptor.doAfterCompletion(successResponse, session, context);
        assertTrue(successResponse.isAllowExport());

        SqlAsyncExecuteResp errorResponse = new SqlAsyncExecuteResp();
        errorResponse.setStatus(SqlExecuteStatus.ERROR);
        interceptor.doAfterCompletion(errorResponse, session, context);
        assertFalse(errorResponse.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam() {
        boolean result = interceptor.isIndividualTeam();
        assertFalse(result);
    }

    @Test
    public void testDetermineSqlType() {
        // Add test cases for determining SQL type
    }

}