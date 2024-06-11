package com.oceanbase.odc.service.session.interceptor;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SqlConsoleInterceptorTest {

    private Mockery context = new JUnit4Mockery();
    
    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private ConnectionSession session;
    private Map<String, Object> contextMap;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        request = context.mock(SqlAsyncExecuteReq.class);
        response = context.mock(SqlAsyncExecuteResp.class);
        session = context.mock(ConnectionSession.class);
        contextMap = new HashMap<String, Object>();
    }

    //test case for doPreHandle method
    @Test
    public void testDoPreHandle() {
        context.checking(new Expectations() {{
            allowing(request).getQueryLimit(); will(returnValue(1));
            allowing(response).getSqls(); will(returnValue(new ArrayList<>()));
            allowing(session).getDialectType(); will(returnValue(DialectType.ORACLE));
        }});

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, contextMap);
        assertTrue(result);
    }
}