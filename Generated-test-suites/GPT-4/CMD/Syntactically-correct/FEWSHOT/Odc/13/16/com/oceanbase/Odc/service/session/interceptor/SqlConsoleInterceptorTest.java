package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;

public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;
    
    private ConnectionSession session;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private Map<String, Object> context;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        context = new HashMap<>();
        request = mock(SqlAsyncExecuteReq.class);
        response = mock(SqlAsyncExecuteResp.class);
        session = mock(ConnectionSession.class);
    }

    @Test
    public void testDoPreHandleWithoutSqlCheckInterceptor() {
        when(authenticationFacade.currentUser()).thenReturn(mock(User.class)); // Assume a User exists
        ConnectionSessionUtil.setRuleSetId(session, 1L); // Just for test
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, null);
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }
    
    @Test
    public void testDoPreHandleWithSqlCheckInterceptor() {
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    // Implement further tests for other methods similar to the first one...
}