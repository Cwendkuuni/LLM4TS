package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;

@RunWith(JUnit4.class)
@SpringBootTest
public class SqlConsoleInterceptorTest {
  
  @InjectMocks
  private SqlConsoleInterceptor interceptor;

  @Mock
  private ConnectionSession connectionSession;

  @Mock
  private SqlAsyncExecuteReq sqlAsyncExecuteRequest;

  @Mock
  private SqlAsyncExecuteResp sqlAsyncExecuteResponse;

  @Test
  public void doPreHandleTest() {
    Map<String, Object> context = new HashMap<>();
    context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, Boolean.TRUE);
    context.put(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED, Boolean.FALSE);

    boolean result = interceptor.doPreHandle(sqlAsyncExecuteRequest, sqlAsyncExecuteResponse, connectionSession, context);

    assertEquals(true, result);
  }

  // Add additional tests for other methods here following the same pattern

  @Test
  public void testDoAfterCompletion() {
    // Create Map for context
    Map<String, Object> context = new HashMap<>();

    // Create SqlExecuteResult mock
    SqlExecuteResult sqlExecuteResult = Mockito.mock(SqlExecuteResult.class);
    Mockito.when(sqlExecuteResult.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);

    // Perform test
    interceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);

    // Verify interactions
    Mockito.verify(sqlExecuteResult).getStatus();
    Mockito.verify(connectionSession, Mockito.atLeastOnce()).getDialectType();
  }
}