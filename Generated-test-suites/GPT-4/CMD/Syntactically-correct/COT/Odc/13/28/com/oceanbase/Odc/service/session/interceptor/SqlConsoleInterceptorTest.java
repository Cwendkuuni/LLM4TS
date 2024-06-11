package com.oceanbase.odc.service.session.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.session.ConnectionSession;
import java.util.*;

public class SqlConsoleInterceptorTest {
    
    private SqlConsoleInterceptor interceptor;

    @Before
    public void setUp() {
        interceptor = new SqlConsoleInterceptor();
        interceptor.authenticationFacade = mock(AuthenticationFacade.class);
        interceptor.ruleService = mock(RuleService.class);
        interceptor.sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
    }
    
    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        assertTrue(interceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult result = new SqlExecuteResult();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        interceptor.doAfterCompletion(result, session, context);
        assertTrue(result.isAllowExport());
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, interceptor.getOrder());
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals("SQL_CONSOLE_RULE", interceptor.getExecuteStageName());
    }
}