import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.HashMap;
import java.util.Map;

public class SqlConsoleInterceptorTest {

    @InjectMocks
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
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle() {
        // Typical use case
        when(context.get(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED)).thenReturn(true);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);

        // Edge case
        when(context.get(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED)).thenReturn(false);
        result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion() {
        // Typical use case
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        assertTrue(response.isAllowExport());

        // Error scenario
        response.setStatus(SqlExecuteStatus.ERROR);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        assertFalse(response.isAllowExport());
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }

}