import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import java.util.HashMap;
import java.util.Map;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor interceptor;
    private ConnectionSession session;
    private Map<String, Object> context;
    
    @Before
    public void setUp() {
        interceptor = new SqlConsoleInterceptor();
        session = new ConnectionSession();
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        boolean result = interceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        boolean result = interceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String stageName = interceptor.getExecuteStageName();
        assertEquals("SQL_CONSOLE_RULE", stageName);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        interceptor.doAfterCompletion(response, session, context);
        assertTrue(true); // No assertion as it is void method
    }

    @Test
    public void testIsIndividualTeam() {
        boolean result = interceptor.isIndividualTeam();
        assertFalse(result);
    }

    @Test
    public void testDetermineSqlType() {
        BasicResult result = interceptor.determineSqlType(null, DialectType.MYSQL);
        assertNotNull(result);
    }

    @Test
    public void testGetOrder() {
        int order = interceptor.getOrder();
        assertEquals(2, order);
    }
}