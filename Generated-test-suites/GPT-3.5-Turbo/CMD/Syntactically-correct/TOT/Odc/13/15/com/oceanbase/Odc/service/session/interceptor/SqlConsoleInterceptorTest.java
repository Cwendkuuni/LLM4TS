import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SqlConsoleInterceptorTest {

    @InjectMocks
    SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    SqlAsyncExecuteReq request;

    @Mock
    SqlAsyncExecuteResp response;

    @Mock
    ConnectionSession session;

    @Mock
    Map<String, Object> context;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle_Success() {
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_Failure() {
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, false);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        
        assertFalse(result);
    }

    @Test
    public void testDoAfterCompletion_Success() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);
        
        assertTrue(sqlExecuteResult.isAllowExport());
    }
    
    @Test
    public void testDoAfterCompletion_Failure() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.FAILURE);
        
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);
        
        assertFalse(sqlExecuteResult.isAllowExport());
    }

    // Add more test cases for other methods in the SqlConsoleInterceptor class

}