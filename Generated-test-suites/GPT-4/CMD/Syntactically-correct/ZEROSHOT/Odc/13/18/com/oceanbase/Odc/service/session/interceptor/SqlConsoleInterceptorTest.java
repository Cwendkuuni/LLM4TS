package com.oceanbase.odc.service.session.interceptor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

public class SqlConsoleInterceptorTest {
    
    @Mock
    private RuleService ruleService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Mock
    private ConnectionSession connectionSession;

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        Assert.assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        Map<String, Object> context = new HashMap<>();
        sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, context);
        Assert.assertTrue(response.isAllowExport());
    }

    @Test
    public void testGetOrder() {
        Assert.assertEquals(2, sqlConsoleInterceptor.getOrder());
    }

    @Test
    public void testGetExecuteStageName() {
        Assert.assertEquals("SQL_CONSOLE_RULE", sqlConsoleInterceptor.getExecuteStageName());
    }
}
