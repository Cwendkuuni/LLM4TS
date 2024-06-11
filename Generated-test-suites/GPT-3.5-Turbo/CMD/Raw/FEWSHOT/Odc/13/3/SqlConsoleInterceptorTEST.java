package com.oceanbase.odc.service.session.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private ConnectionSession session;
    private Map<String, Object> context;

    @Before
    public void setup() {
        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();
        session = new ConnectionSession();
        context = new HashMap<>();
        ReflectionTestUtils.setField(sqlConsoleInterceptor, "authenticationFacade", authenticationFacade);
        ReflectionTestUtils.setField(sqlConsoleInterceptor, "ruleService", ruleService);
        ReflectionTestUtils.setField(sqlConsoleInterceptor, "sqlConsoleRuleService", sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle() {
        when(authenticationFacade.currentUser()).thenReturn(orgName);
        assertNotNull(authenticationFacade.currentUser());
        when(ruleService.getByRulesetIdAndName(any(), any())).thenReturn(rule);
        
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        
        // Asserts
        assertEquals(true, result);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult executeResult = new SqlExecuteResult();
        executeResult.setStatus(SqlExecuteStatus.SUCCESS);
        session.setRuleSetId(1L);
        
        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, context);
        
        // Asserts
        assertEquals(true, executeResult.isAllowExport());
    }
}