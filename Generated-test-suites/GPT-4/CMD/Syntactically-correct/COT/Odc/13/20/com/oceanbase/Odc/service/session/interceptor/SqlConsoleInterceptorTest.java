package com.oceanbase.odc.service.session.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor interceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlAsyncExecuteReq sqlAsyncExecuteReq;

    @Mock
    private SqlAsyncExecuteResp sqlAsyncExecuteResp;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private SqlExecuteResult sqlExecuteResult;

    @Before
    public void setUp() {
        // Setup initial conditions under which each test will run
        // Set user, connection and result parameters using Mockito
    }

    @Test
    public void testDoPreHandle() {
        HashMap<String, Object> context = new HashMap<>();
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        boolean result = interceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context);
        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion() {
        HashMap<String, Object> context = new HashMap<>();
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        interceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);
        assertTrue(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testGetOrder() {
        int order = interceptor.getOrder();
        assertEquals(2, order);
    }

    // Add more test methods here as needed, each method represents a test case
}
