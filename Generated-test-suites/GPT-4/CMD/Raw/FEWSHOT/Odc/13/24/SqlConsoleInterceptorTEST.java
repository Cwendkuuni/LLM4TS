package com.oceanbase.odc.service.session.interceptor;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

import java.util.HashMap;
import java.util.Map;

public class SqlConsoleInterceptorTest {

    @Autowired
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        boolean result =  sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        Assert.assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = Mockito.mock(SqlExecuteResult.class);
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        // assertions need to be added based on what you are expecting
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        
        Assert.assertEquals(2, order);
    }
    
    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = Mockito.mock(SqlTuple.class);
        DialectType dialectType = Mockito.mock(DialectType.class);
        
        BasicResult result  = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);
        
        // assertions need to be added based on what you are expecting
    }
    
    // Similarly add test methods for other methods present in the SqlConsoleInterceptor class. Each method should be tested for different scenarios.
}