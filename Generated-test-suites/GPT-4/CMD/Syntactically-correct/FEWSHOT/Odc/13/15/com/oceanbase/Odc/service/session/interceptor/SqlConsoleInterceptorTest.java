package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import org.junit.Before;
import org.junit.Test;

public class SqlConsoleInterceptorTest {
    private SqlConsoleInterceptor interceptor;

    @Before
    public void setUp() {
        interceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        boolean result = interceptor.doPreHandle(request, response, session, context);

        assertEquals(true, result);
    }

    private SqlAsyncExecuteResp generateMockSqlAsyncExecuteResp() {
        List<SqlTuplesWithViolation> sqls = new ArrayList<>();
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        when(response.getSqls()).thenReturn(sqls);
        return response;
    }

    @Test
    public void testDoAfterCompletion() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED, true);

        // Mock the SqlExecuteResult
        SqlExecuteResult result = mock(SqlExecuteResult.class); 
        when(result.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);
        
        interceptor.doAfterCompletion(result, session, context);

        assertEquals(true, result.isAllowExport());
    }
    
    @Test
    public void testGetOrder() {
        assertEquals(2, interceptor.getOrder());
    }

    // Add more test methods for other functionalities as per requirement.
    // For now, doAfterCompletion, getOrder method is demonstrated as sample.
}