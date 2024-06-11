
package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

public class SqlConsoleInterceptorTest {

    @Test
    public void testDoPreHandle() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        assertNotNull(interceptor.doPreHandle(request, response, session, null));
    }

    @Test
    public void testDetermineSqlType() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        assertNotNull(interceptor.determineSqlType(null, null));
    }

    @Test
    public void testDoAfterCompletion() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        interceptor.doAfterCompletion(response, session, null);
        assertNotNull(response);
    }

}
