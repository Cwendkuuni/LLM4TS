package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

public class SqlConsoleInterceptorTest {
    
    @Test
    public void testDoPreHandle() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(context.get(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED)).thenReturn(true);
        
        assertTrue(interceptor.doPreHandle(request, response, session, context));
    }
    
    @Test
    public void testHandle() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        
        assertTrue(interceptor.handle(request, response, session, context));
    }
    
    @Test
    public void testGetExecuteStageName() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        
        assertEquals("SqlConsoleRule", interceptor.getExecuteStageName());
    }
    
    @Test
    public void testDoAfterCompletion() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        
        interceptor.doAfterCompletion(response, session, context);
        
        assertFalse(context.containsKey(SQL_CONSOLE_INTERCEPTED));
    }
    
    @Test
    public void testIsIndividualTeam() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        
        assertTrue(interceptor.isIndividualTeam());
    }
    
    @Test
    public void testDetermineSqlType() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        SqlTuple sqlTuple = mock(SqlTuple.class);
        DialectType dialectType = DialectType.MYSQL;
        
        BasicResult result = interceptor.determineSqlType(sqlTuple, dialectType);
        
        assertEquals(SqlType.OTHERS, result.getSqlType());
    }
    
    @Test
    public void testGetOrder() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        
        assertEquals(2, interceptor.getOrder());
    }
}