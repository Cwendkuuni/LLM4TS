package com.oceanbase.odc.service.session.interceptor;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SqlConsoleInterceptorTest {
   
   private SqlConsoleInterceptor instance;
   
   @Before
   public void setUp() {
      instance = new SqlConsoleInterceptor();
   }

   @Test
   public void testDoPreHandle() {
      // Prepare mock data
      SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class); 
      SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
      ConnectionSession session = mock(ConnectionSession.class); 
      Map<String, Object> context = new HashMap<String, Object>(); 
      // Call the method to test 
      boolean result = instance.doPreHandle(request, response, session, context);
      // Validate the result 
      assertTrue(result);
   }

   @Test
   public void testGetExecuteStageName() {
      // Call the method to test 
      String result = instance.getExecuteStageName();
      // Validate the result 
      assertEquals("SQL_CONSOLE_RULE", result);
   }

   @Test
   public void testDoAfterCompletion() {
      // Prepare mock data
      SqlExecuteResult response = mock(SqlExecuteResult.class); 
      ConnectionSession session = mock(ConnectionSession.class); 
      Map<String, Object> context = new HashMap<String, Object>(); 
      // Call the method to test 
      instance.doAfterCompletion(response, session, context);
      // Validate
      // This is not validated as it produces no return and has no clear side-effect
   }

   @Test
   public void testGetOrder() {
      // Call the method to test 
      int result = instance.getOrder();
      // Validate the result 
      assertEquals(2, result);
   }
}