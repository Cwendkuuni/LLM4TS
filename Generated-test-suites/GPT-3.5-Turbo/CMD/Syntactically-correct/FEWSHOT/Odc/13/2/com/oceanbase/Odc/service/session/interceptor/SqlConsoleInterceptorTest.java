import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

public class SqlConsoleInterceptorTest {
    private SqlConsoleInterceptor interceptor;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private ConnectionSession session;

    @Before
    public void setUp() {
        interceptor = new SqlConsoleInterceptor();
        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();
        session = mock(ConnectionSession.class);
    }

    @Test
    public void testDoPreHandle() {
        // Test case 1: All parameters are non-null
        assertTrue(interceptor.doPreHandle(request, response, session, new HashMap<>()));

        // Test case 2: Null ruleSetId and individual team
        when(ConnectionSessionUtil.getRuleSetId(any(ConnectionSession.class))).thenReturn(null);
        when(interceptor.isIndividualTeam()).thenReturn(true);
        assertTrue(interceptor.doPreHandle(request, response, session, new HashMap<>()));

        // Test case 3: Context contains SQL_CHECK_INTERCEPTED
        HashMap<String, Object> context = new HashMap<>();
        context.put("SQL_CHECK_INTERCEPTED", true);
        assertTrue(interceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testHandle() {
        // Test case 1: RuleSetId is null and individual team
        when(ConnectionSessionUtil.getRuleSetId(any(ConnectionSession.class))).thenReturn(null);
        when(interceptor.isIndividualTeam()).thenReturn(true);
        assertTrue(interceptor.handle(request, response, session, new HashMap<>()));

        // Test case 2: Context does not contain NEED_SQL_CONSOLE_CHECK
        HashMap<String, Object> context = new HashMap<>();
        context.put("SQL_CHECK_INTERCEPTED", false);
        assertTrue(interceptor.handle(request, response, session, context));
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals("SQL_CONSOLE_RULE", interceptor.getExecuteStageName());
    }

    @Test
    public void testDoAfterCompletion() {
        // Test case 1: Status is not SUCCESS
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.FAILURE);
        interceptor.doAfterCompletion(response, session, new HashMap<>());

        // Test case 2: Individual team
        when(interceptor.isIndividualTeam()).thenReturn(true);
        interceptor.doAfterCompletion(response, session, new HashMap<>());

        // Test case 3: Not allowed edit resultset
        ruleService = mock(RuleService.class);
        when(ruleService.isForbidden(any(), any(ConnectionSession.class))).thenReturn(true);
        response.setStatus(SqlExecuteStatus.SUCCESS);
        interceptor.doAfterCompletion(response, session, new HashMap<>());
        verify(response.getResultSetMetaData()).setEditable(false);

        // Test case 4: Not allowed export resultset
        when(ruleService.isForbidden(any(), any(ConnectionSession.class))).thenReturn(false);
        interceptor.doAfterCompletion(response, session, new HashMap<>());
        assertTrue(response.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam() {
        authenticationFacade = mock(AuthenticationFacade.class);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        assertTrue(interceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType() {
        // To be implemented
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, interceptor.getOrder());
    }
}