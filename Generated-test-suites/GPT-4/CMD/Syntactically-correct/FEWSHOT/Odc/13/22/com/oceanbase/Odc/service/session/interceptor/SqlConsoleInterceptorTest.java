package com.oceanbase.odc.service.session.interceptor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacadeMock;
    @Mock
    private RuleService ruleServiceMock;
    @Mock
    private SqlConsoleRuleService sqlConsoleRuleServiceMock;
    @Mock
    private ConnectionSession connectionSessionMock;
    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        Mockito.when(authenticationFacadeMock.currentUser()).thenReturn(null);
    }

    @Test
    public void testInterceptor() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        
        boolean intercepted = sqlConsoleInterceptor.doPreHandle(request, response, connectionSessionMock, context);
        
        assertEquals(false, intercepted);
    }

    @Test
    public void testOrder() {
        int order = sqlConsoleInterceptor.getOrder();

        assertThat(order, is(2));
    }
}