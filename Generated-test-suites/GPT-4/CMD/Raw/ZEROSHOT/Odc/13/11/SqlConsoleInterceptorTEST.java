
package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

public class SqlConsoleInterceptorTest {

    @Mock
    private ConnectionSession session;
    @Mock
    private SqlAsyncExecuteReq request;
    @Mock
    private SqlAsyncExecuteResp response;
    @Mock
    private Map<String, Object> context;
    @Mock
    private AuthenticationFacade authenticationFacade;
    
    @InjectMocks
    SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        context = new HashMap<>();
    }

    @Test
    public void doPreHandle_injects_interception_and_returns_false_when_sqlCheckInterceptor_intercepted() {
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        boolean res = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue((boolean) context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
        assertFalse(res);
    }

    @Test
    public void doPreHandle_injects_interception_and_returns_true_when_sqlCheckInterceptor_not_intercepted() {
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, false);
        boolean res = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue((boolean) context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
        assertTrue(res);
    }

    @Test
    public void doAfterCompletion_allows_export_when_individualTeam() {
        SqlExecuteResult result = mock(SqlExecuteResult.class);
        when(authenticationFacade.currentUser().getOrganizationType() == OrganizationType.INDIVIDUAL).thenReturn(true);
    
        sqlConsoleInterceptor.doAfterCompletion(result, session, context);
    
        verify(result).setAllowExport(true);
    }
    
    // Further tests for each method omitted for brevity
}
