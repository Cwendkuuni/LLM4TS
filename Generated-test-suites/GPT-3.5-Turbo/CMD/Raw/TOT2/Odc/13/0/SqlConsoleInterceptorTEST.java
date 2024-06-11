import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SqlConsoleInterceptorTest {
    private SqlConsoleInterceptor sqlConsoleInterceptor;
    
    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
    }
    
    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        when(request.getQueryLimit()).thenReturn(10);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        when(sqlConsoleInterceptor.doPreHandle(request, response, session, context)).thenReturn(true);
        
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
        
        verify(request, times(1)).getQueryLimit();
        verify(session, times(2)).getDialectType();
        verify(ConnectionSessionUtil, times(1)).getRuleSetId(session);
    }
    
    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        
        when(response.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        when(sqlConsoleInterceptor.isIndividualTeam()).thenReturn(true);
        
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        
        verify(response, times(1)).getStatus();
        verify(ConnectionSessionUtil, times(1)).getRuleSetId(session);
        verify(sqlConsoleInterceptor, times(1)).isIndividualTeam();
    }
    
    @Test
    public void testIsIndividualTeam() {
        AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
        when(authenticationFacade.currentUser()).thenReturn(User.builder().organizationType(OrganizationType.INDIVIDUAL).build());
        
        assertTrue(sqlConsoleInterceptor.isIndividualTeam());
    }
}