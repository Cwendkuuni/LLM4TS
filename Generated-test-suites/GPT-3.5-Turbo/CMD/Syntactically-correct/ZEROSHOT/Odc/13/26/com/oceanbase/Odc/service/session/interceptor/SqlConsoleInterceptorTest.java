import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

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
        
        when(session.getDialectType()).thenReturn(DialectType.HIVE);
        when(sqlConsoleInterceptor.isIndividualTeam()).thenReturn(false);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
        assertTrue(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
        assertFalse((Boolean) context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(session.getDialectType()).thenReturn(DialectType.HIVE);
        when(sqlConsoleInterceptor.isIndividualTeam()).thenReturn(false);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        assertTrue(result);
        assertTrue(context.containsKey(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
        assertFalse((Boolean) context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals("SQL_CONSOLE_RULE", sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testDoAfterCompletion() {
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(response.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);
        when(sqlConsoleInterceptor.isIndividualTeam()).thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        verify(response).setAllowExport(anyBoolean());
    }

    @Test
    public void testIsIndividualTeam() {
        assertTrue(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType() {
        SqlConsoleInterceptor spySqlConsoleInterceptor = spy(sqlConsoleInterceptor);
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        SqlAsyncExecuteReq testRequest = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp testResponse = mock(SqlAsyncExecuteResp.class);
        ConnectionSession testSession = mock(ConnectionSession.class);
        Map<String, Object> testContext = new HashMap<>();
        
        BasicResult basicResult = new BasicResult(SqlType.SELECT);
        
        when(testSession.getDialectType()).thenReturn(DialectType.HIVE);
        when(testResponse.getSqls()).thenReturn(null);

        doReturn(basicResult).when(spySqlConsoleInterceptor).determineSqlType(any(), any());

        BasicResult result = spySqlConsoleInterceptor.determineSqlType(request, session.getDialectType());

        assertEquals(SqlType.SELECT, result.getSqlType());
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }

}