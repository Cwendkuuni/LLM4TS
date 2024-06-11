package com.oceanbase.odc.service.session.interceptor;
import static org.junit.Assert.*;
import org.junit.Test;

public class SqlConsoleInterceptorTest {

    @Test
    public void testDoPreHandle() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        
        boolean result = interceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        
        interceptor.doAfterCompletion(response, session, context);

        assertTrue(response.isAllowExport());
    }

    @Test
    public void testGetOrder() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        int order = interceptor.getOrder();
        
        assertEquals(2, order);
    }

    @Test(expected = NullPointerException.class)
    public void testDoPreHandle_NullPointerException() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        interceptor.doPreHandle(null, null, null, null);
    }

    @Test(expected = SyntaxErrorException.class)
    public void testDetermineSqlType_SyntaxErrorException() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        SqlTuple sqlTuple = new SqlTuple();
        DialectType dialectType = new DialectType();
        
        interceptor.determineSqlType(sqlTuple, dialectType);
    }

    @Test
    public void testDoAfterCompletion_StatusNotSuccess() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.FAIL);
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        
        interceptor.doAfterCompletion(response, session, context);

        assertFalse(response.isAllowExport());
    }
}