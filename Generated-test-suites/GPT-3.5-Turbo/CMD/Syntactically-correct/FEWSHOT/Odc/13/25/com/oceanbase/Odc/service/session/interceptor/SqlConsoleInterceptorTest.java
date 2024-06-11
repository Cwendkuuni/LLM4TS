package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SqlConsoleInterceptorTest {

    @Test
    public void testDoPreHandle() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(123L);

        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        assertFalse(interceptor.doPreHandle(request, response, session, context));

        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, false);
        assertTrue(interceptor.doPreHandle(request, response, session, context));

    }

    @Test
    public void testDoAfterCompletion() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(response.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        interceptor.doAfterCompletion(response, session, context);

        when(response.getStatus()).thenReturn(SqlExecuteStatus.ERROR);
        interceptor.doAfterCompletion(response, session, context);
    }

    @Test
    public void testGetExecuteStageName() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        assertEquals("SQL_CONSOLE_RULE", interceptor.getExecuteStageName());
    }

    @Test
    public void testIsIndividualTeam() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        assertTrue(interceptor.isIndividualTeam());
    }

}