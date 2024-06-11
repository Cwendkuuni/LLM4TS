
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    SqlAsyncExecuteReq mockReq;
    @Mock
    SqlAsyncExecuteResp mockResp;
    @Mock
    ConnectionSession mockSession;
    @Mock
    SqlExecuteResult mockSqlExecuteResult;
    @Mock
    Map<String, Object> mockContext;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
    }

    @Test(expected = NullPointerException.class)
    public void testDoPreHandleWithNullParameters() {
        sqlConsoleInterceptor.doPreHandle(null, null, null, null);
    }

    @Test
    public void testDoPreHandleSuccess() {
        boolean result = sqlConsoleInterceptor.doPreHandle(mockReq, mockResp, mockSession, mockContext);
        assertTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testDoAfterCompletionWithNullParameters() {
        sqlConsoleInterceptor.doAfterCompletion(null, null, null);
    }

    @Test
    public void testDoAfterCompletionSuccess() {
        when(mockSqlExecuteResult.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);
        sqlConsoleInterceptor.doAfterCompletion(mockSqlExecuteResult, mockSession, mockContext);
    }

    @Test
    public void testDoAfterCompletionNotSuccess() {
        when(mockSqlExecuteResult.getStatus()).thenReturn(SqlExecuteStatus.FAILURE);
        sqlConsoleInterceptor.doAfterCompletion(mockSqlExecuteResult, mockSession, mockContext);
    }
}
