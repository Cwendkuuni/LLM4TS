package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

public class SqlConsoleInterceptorTest {

    @InjectMocks
    SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    ConnectionSession connectionSession;
    @Mock
    SqlAsyncExecuteReq request;
    @Mock
    SqlAsyncExecuteResp response;
    @Mock
    Map<String, Object> context;
    @Mock
    RuleService ruleService;
    @Mock
    SqlConsoleRuleService sqlConsoleRuleService;
    @Mock
    AuthenticationFacade authenticationFacade;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle(){
        Map<String, Object> context = new HashMap<>();
        when(sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context)).thenReturn(false);
        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context));

        when(sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context)).thenReturn(true);
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context));
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals("SQL_CONSOLE_RULE", sqlConsoleInterceptor.getExecuteStageName());
    }
    
    // Similarly, we can write tests for other methods like doAfterCompletion(), isIndividualTeam(), and determineSqlType() depending on their function and business requirement.
}