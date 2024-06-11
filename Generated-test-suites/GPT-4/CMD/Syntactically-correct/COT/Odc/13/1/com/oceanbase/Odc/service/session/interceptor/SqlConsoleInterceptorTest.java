package com.oceanbase.odc.service.session.interceptor;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;

public class SqlConsoleInterceptorTest {
  
  @Autowired
  private SqlConsoleInterceptor sqlConsoleInterceptor;
  
  @Test
  public void testDoPreHandle()  {
    SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
    SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
    ConnectionSession session = mock(ConnectionSession.class);
    Map<String, Object> context = new HashMap<>();
    boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
    assertTrue(result);
  }

  @Test
  public void testDoAfterCompletion()  {
    SqlExecuteResult response = mock(SqlExecuteResult.class);
    ConnectionSession session = mock(ConnectionSession.class);
    Map<String, Object> context = new HashMap<>();
    sqlConsoleInterceptor.doAfterCompletion(response, session, context);
  }

  @Test
  public void testIsIndividualTeam()  {
    boolean result = sqlConsoleInterceptor.isIndividualTeam();
    assertFalse(result);
  }

  @Test
  public void testDetermineSqlType()  {
    SqlTuple sqlTuple = mock(SqlTuple.class);
    DialectType dialectType = mock(DialectType.class);
    BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);
    assertNotNull(result);
  }

  @Test
  public void testGetOrder()  {
    int result = sqlConsoleInterceptor.getOrder();
    assertEquals(2, result);
  }

  @Test
  public void testGetExecuteStageName()  {
    String result = sqlConsoleInterceptor.getExecuteStageName();
    assertEquals("SQL_CONSOLE_RULE", result);
  }
}
