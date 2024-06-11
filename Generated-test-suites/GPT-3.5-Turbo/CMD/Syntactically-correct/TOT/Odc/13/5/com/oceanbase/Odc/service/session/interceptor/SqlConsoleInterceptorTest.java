import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Test
    public void testDoPreHandle(){
        // Typical Use Case
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(sqlConsoleInterceptor.doPreHandle(eq(request), eq(response), eq(session), eq(context))).thenReturn(true);

        // Edge Case
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));

        // Error Scenario
        context.put(NEED_SQL_CONSOLE_CHECK, false);
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletion(){
        // Typical Use Case
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET), eq(session))).thenReturn(false);

        // Edge Case
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        // Error Scenario
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET), eq(session))).thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        assertFalse(response.getResultSetMetaData().isEditable());
    }
}