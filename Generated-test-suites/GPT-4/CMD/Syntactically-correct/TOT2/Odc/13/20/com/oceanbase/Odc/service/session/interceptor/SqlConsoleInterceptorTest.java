package com.oceanbase.odc.service.session.interceptor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

   @Mock
   RuleService ruleService;
   
   @InjectMocks
   SqlConsoleInterceptor sqlConsoleInterceptor;
   
  /*...Instructions for generating testcases for handle, getExecuteStageName, doAfterCompletion, isIndividualTeam, determineSqlType, and getOrder here...*/

  @Test
  public void testDoPreHandle() {
    ConnectionSession session = new ConnectionSession();
    SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
    SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
    Map<String, Object> context = new HashMap<>();
    when(ruleService.getByRulesetIdAndName(any(Long.class), anyString())).thenReturn(Optional.of(new Rule()));
    assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
  }
}
