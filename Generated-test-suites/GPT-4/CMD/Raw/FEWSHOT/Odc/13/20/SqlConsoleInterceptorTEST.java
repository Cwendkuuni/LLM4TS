
package com.oceanbase.odc.service.session.interceptor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.HashMap;
import java.util.Map;
  
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;

/**
* SqlConsoleInterceptor Test Class
*/
public class SqlConsoleInterceptorTest {

  @InjectMocks
  SqlConsoleInterceptor sqlConsoleInterceptor;

  @Mock
  AuthenticationFacade authenticationFacade;

  @Mock
  RuleService ruleService;
  
  @Mock
  SqlConsoleRuleService sqlConsoleRuleService;

  @Mock
  SqlAsyncExecuteReq request;

  @Mock
  SqlAsyncExecuteResp response;

  @Mock
  ConnectionSession session;
   
  Map<String, Object> context;
  
  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    context = new HashMap<>();
  }

  @Test
  public void testDoPreHandle() {
    boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
    Assert.assertTrue(result); // or assertFalse based on the expected behaviour
  }

  @Test
  public void testGetExecuteStageName() {
    String result = sqlConsoleInterceptor.getExecuteStageName();
    Assert.assertEquals("Expected stage name", result);
  }

  @Test
  public void testDoAfterCompletion() {
    SqlExecuteResult response = new SqlExecuteResult();
    sqlConsoleInterceptor.doAfterCompletion(response, session, context);
    // add assertions to validate the response
  }

  @Test
  public void testGetOrder() {
    int result = sqlConsoleInterceptor.getOrder();
    Assert.assertEquals(2, result);
  }

}
