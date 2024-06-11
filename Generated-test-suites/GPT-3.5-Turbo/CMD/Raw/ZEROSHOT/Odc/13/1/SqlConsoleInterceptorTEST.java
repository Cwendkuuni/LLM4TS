import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor interceptor;
    
    @Before
    public void setup() {
        interceptor = new SqlConsoleInterceptor();
        interceptor.setAuthenticationFacade(mock(AuthenticationFacade.class));
        interceptor.setRuleService(mock(RuleService.class));
        interceptor.setSqlConsoleRuleService(mock(SqlConsoleRuleService.class));
    }
    
    @Test
    public void testDoPreHandleWithValidContext() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        
        boolean result = interceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }
    
    @Test
    public void testDoPreHandleWithNullContext() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        
        boolean result = interceptor.doPreHandle(request, response, session, null);
        assertTrue(result);
    }
    
    @Test
    public void testDoAfterCompletionWithSuccessStatus() {
        SqlExecuteResult response = new SqlExecuteResult(SqlExecuteStatus.SUCCESS);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        
        interceptor.doAfterCompletion(response, session, context);
        assertTrue(response.isAllowExport());
    }
    
    @Test
    public void testDoAfterCompletionWithFailureStatus() {
        SqlExecuteResult response = new SqlExecuteResult(SqlExecuteStatus.FAILURE);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        
        interceptor.doAfterCompletion(response, session, context);
        assertFalse(response.isAllowExport());
    }
    
    @Test
    public void testGetOrder() {
        int order = interceptor.getOrder();
        assertEquals(2, order);
    }
}