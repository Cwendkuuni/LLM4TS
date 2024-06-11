import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import org.junit.Test;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        DialectType dialectType = DialectType.ORACLE;
        
        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);
        assertNotNull(result);
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }
}