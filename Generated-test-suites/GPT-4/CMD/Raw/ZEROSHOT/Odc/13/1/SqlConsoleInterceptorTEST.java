
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {
  @InjectMocks
  private SqlConsoleInterceptor sqlConsoleInterceptor;

  @Autowired
  private AuthenticationFacade authenticationFacade;

  @Autowired
  private RuleService ruleService;

  @Autowired
  private SqlConsoleRuleService sqlConsoleRuleService;

  @Test
  public void testDoPreHandle() {
    SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
    SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
    ConnectionSession session = mock(ConnectionSession.class);
    SqlExecuteResult result = mock(SqlExecuteResult.class); 

    Map<String, Object> context = new HashMap<>();
    context.put(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED, false);

    boolean preHandleResult = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
    
    assertEquals(false, preHandleResult);
  }

  @Test
  public void testDoAfterCompletion() {
    SqlExecuteResult response = mock(SqlExecuteResult.class);
    ConnectionSession session = mock(ConnectionSession.class);
      
    Map<String, Object> context = new HashMap<>();
    sqlConsoleInterceptor.doAfterCompletion(response, session, context);

    assertEquals(SqlConsoleInterceptor.getExecuteStageName(), "SQL_CONSOLE_RULE");
  }

  @Test
  public void testGetOrder() {
    int order = sqlConsoleInterceptor.getOrder();
    
    assertEquals("Expected order to be 2.", 2, order);
  }
}
