
package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

public class SqlConsoleInterceptorTest {

    @Test
    public void testDoPreHandle() {
        // Arrange
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<String, Object>();

        SqlConsoleInterceptor sqlConsoleInterceptor = spy(new SqlConsoleInterceptor());

        doReturn(true).when(sqlConsoleInterceptor).handle(any(SqlAsyncExecuteReq.class), any(SqlAsyncExecuteResp.class), any(ConnectionSession.class), anyMap());

        // Act
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        // Assert
        assertTrue(result);
        verify(sqlConsoleInterceptor, times(1)).handle(any(SqlAsyncExecuteReq.class), any(SqlAsyncExecuteResp.class), any(ConnectionSession.class), anyMap());
    }
}
