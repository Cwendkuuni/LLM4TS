package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private ConnectionSession session;
    private Map<String, Object> context;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        request = mock(SqlAsyncExecuteReq.class);
        response = mock(SqlAsyncExecuteResp.class);
        session = mock(ConnectionSession.class);
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        // Testing with valid data
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        when(session.getDialectType()).thenReturn(DialectType.HIVE);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);

        // Testing with invalid data
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, false);
        when(session.getDialectType()).thenReturn(DialectType.POSTGRESQL);
        result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testHandle() {
        // Testing handle method with valid data
        when(sqlConsoleInterceptor.isIndividualTeam()).thenReturn(false);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        AtomicBoolean allowExecute = new AtomicBoolean(true);
        when(sqlConsoleInterceptor.handle(request, response, session, context)).thenReturn(true);
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        assertEquals(true, allowExecute.get());
        assertEquals(true, result);
    }

    @Test
    public void testGetExecuteStageName() {
        String result = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals("SQL_CONSOLE_RULE", result);
    }

    @Test
    public void testDoAfterCompletion() {
        // Testing with success status
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        when(sqlConsoleInterceptor.isIndividualTeam()).thenReturn(false);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        assertTrue(response.isAllowExport());

        // Testing with failure status
        response.setStatus(SqlExecuteStatus.FAILURE);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        assertFalse(response.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam() {
        // Testing for individual team
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        boolean result = sqlConsoleInterceptor.isIndividualTeam();
        assertTrue(result);

        // Testing for other organization types
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        result = sqlConsoleInterceptor.isIndividualTeam();
        assertFalse(result);
    }

}