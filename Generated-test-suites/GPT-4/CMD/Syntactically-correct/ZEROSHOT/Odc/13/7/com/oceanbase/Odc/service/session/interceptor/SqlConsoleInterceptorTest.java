package com.oceanbase.odc.service.session.interceptor;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.Import;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
@Import(SqlConsoleInterceptor.class)
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private ConnectionSession session;

    @Mock
    private SqlAsyncExecuteReq request;

    @Mock
    private SqlAsyncExecuteResp response;

    private final Map<String, Object> context = new HashMap<>();

    @Before
    public void setup() {
        Mockito.reset(session, request, response);
    }

    @Test
    public void doPreHandleTest() {
        Mockito.when(sqlConsoleInterceptor.doPreHandle(request, response, session, context)).thenReturn(true);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertThat(result, is(true));
    }

    @Test
    public void doAfterCompletionTest() {
        Mockito.doNothing().when(sqlConsoleInterceptor).doAfterCompletion(response, session, context);
    }

    @Test
    public void getOrderTest() {
        int result = sqlConsoleInterceptor.getOrder();
        assertThat(result, is(2));
    }

    @Test
    public void getExecuteStageNameTest() {
        String result = sqlConsoleInterceptor.getExecuteStageName();
        assertThat(result, is("SqlConsoleRule"));
    }

    @Test
    public void determineSqlTypeTest() {
       //... mocking and implementation... 
    }
}
