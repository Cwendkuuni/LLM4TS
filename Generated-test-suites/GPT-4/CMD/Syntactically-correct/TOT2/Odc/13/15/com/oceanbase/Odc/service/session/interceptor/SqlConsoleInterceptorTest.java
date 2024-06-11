package com.oceanbase.odc.service.session.interceptor;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private ConnectionSession session;
    private Map<String, Object> context;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();

        // might need to mock dependencies here
        // e.g. AuthenticationFacade authenticationFacadeMock = Mockito.mock(AuthenticationFacade.class);
        // ReflectionTestUtils.setField(sqlConsoleInterceptor, "authenticationFacade", authenticationFacadeMock);

        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();
        session = new ConnectionSession();
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        // Set up data in request, response, session, context as needed

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        // ... assert something about result or about state changes in response, session, context
    }

    @Test
    public void testDoAfterCompletion() {
        // Set up data in response, session, context as needed

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        // ... assert something state changes in response, session, context
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();

        // Check that order is as expected
        assertEquals(2, order);
    }
}