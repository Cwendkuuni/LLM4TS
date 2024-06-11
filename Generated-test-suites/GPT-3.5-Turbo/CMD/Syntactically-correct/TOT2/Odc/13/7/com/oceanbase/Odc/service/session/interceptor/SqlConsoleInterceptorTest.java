import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class SqlConsoleInterceptorTest {

    @Test
    public void testDoPreHandle_ValidRequest_ReturnTrue() {
        // Given
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();

        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        // When
        boolean result = interceptor.doPreHandle(request, response, session, context);

        // Then
        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion_SuccessWithExportAllowed_ResultIsExportAllowed() {
        // Given
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        response.setAllowExport(true);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        // When
        interceptor.doAfterCompletion(response, session, context);

        // Then
        assertTrue(response.isAllowExport());
    }

    // Add more test cases for other methods in SqlConsoleInterceptor class

}