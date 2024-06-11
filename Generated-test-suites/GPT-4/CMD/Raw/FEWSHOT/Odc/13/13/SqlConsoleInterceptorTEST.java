package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

public class SqlConsoleInterceptorTest {

  @InjectMocks private SqlConsoleInterceptor sqlConsoleInterceptor;

  @Mock private ConnectionSession connectionSession;
  @Mock private SqlAsyncExecuteReq request;
  @Mock private SqlAsyncExecuteResp response;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }
  
  @Test
  public void testGetOrder() {
    assertEquals(2, sqlConsoleInterceptor.getOrder());
  }

  @Test
  public void testDoPreHandle() {
    // Given
    SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
    SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
    ConnectionSession session = new ConnectionSession();
    Map<String, Object> context = new HashMap<>();
    context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, Boolean.FALSE);

    // Spy sqlConsoleInterceptor
    SqlConsoleInterceptor sqlConsoleInterceptorSpy = spy(sqlConsoleInterceptor);
    doReturn(true).when(sqlConsoleInterceptorSpy).handle(any(), any(), any(), any());

    // When
    boolean actualReturnValue = sqlConsoleInterceptorSpy.doPreHandle(request, response, session, context);

    // Then
    assertEquals(true, actualReturnValue);
  }

  @Test
  public void testDoAfterCompletion() {
    // Implement test
  }

  // More unit tests for other methods...
}