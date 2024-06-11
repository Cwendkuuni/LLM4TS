import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.session.ConnectionSession;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.HashMap;

public class SqlConsoleInterceptorTest {

  SqlConsoleInterceptor sqlConsoleInterceptor;
  SqlAsyncExecuteReq requestMock;
  SqlAsyncExecuteResp responseMock;
  ConnectionSession sessionMock;
  Map<String, Object> contextMock;

  @Before
  public void setUp() {
    sqlConsoleInterceptor = new SqlConsoleInterceptor();
    requestMock = Mockito.mock(SqlAsyncExecuteReq.class);
    responseMock = Mockito.mock(SqlAsyncExecuteResp.class);
    sessionMock = Mockito.mock(ConnectionSession.class);
    contextMock = new HashMap<>();
  }

  @Test
  public void testDoPreHandleWithNonNullContext() {
    contextMock.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
    boolean result = sqlConsoleInterceptor.doPreHandle(requestMock, responseMock, sessionMock, contextMock);

    assertTrue(result);
  }

  @Test
  public void testDoPreHandleWithNullContext() {
    boolean result = sqlConsoleInterceptor.doPreHandle(requestMock, responseMock, sessionMock, contextMock);

    assertTrue(result);
  }

  // More test methods for edge cases and error scenarios...
}