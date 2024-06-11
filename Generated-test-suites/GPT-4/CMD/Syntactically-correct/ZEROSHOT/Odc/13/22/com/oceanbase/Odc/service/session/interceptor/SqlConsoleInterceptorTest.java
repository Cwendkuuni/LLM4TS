package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;

import java.util.HashMap;
import java.util.Map;

public class SqlConsoleInterceptorTest {

    @InjectMocks
    SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    AuthenticationFacade authenticationFacade;

    @Mock
    RuleService ruleService;

    @Mock
    SqlConsoleRuleService sqlConsoleRuleService;

    @Mock
    ConnectionSession connectionSession;

    @Mock
    SqlAsyncExecuteReq sqlAsyncExecuteReq;

    @Mock
    SqlAsyncExecuteResp sqlAsyncExecuteResp;

    @Mock
    SqlExecuteResult sqlExecuteResult;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle() {
        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context);
        assertNotNull(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String result = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals("SQL_CONSOLE_RULE", result);
    }

    @Test
    public void testDoAfterCompletion() {
        Map<String, Object> context = new HashMap<>();
        Mockito.when(authenticationFacade.currentUser()).thenReturn(any());
        Mockito.when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        Mockito.when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(true);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);
    }

    @Test
    public void testGetOrder() {
        int result = sqlConsoleInterceptor.getOrder();
        assertEquals(2, result);
    }

}
