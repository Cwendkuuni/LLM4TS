import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.List;

import org.junit.Test;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals("SQL_CONSOLE_RULE", sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        
        assertTrue(response.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam() {
        assertTrue(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = new SqlTuple();
        DialectType dialectType = DialectType.ORACLE;

        assertNotNull(sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType));
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }
}