import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @Mock
    private ConnectionSession mockSession;

    @Mock
    private AuthenticationFacade mockAuthenticationFacade;

    @Mock
    private RuleService mockRuleService;

    @Mock
    private SqlConsoleRuleService mockSqlConsoleRuleService;

    private SqlConsoleInterceptor interceptor;

    @Before
    public void setUp() {
        interceptor = new SqlConsoleInterceptor();
        interceptor.setAuthenticationFacade(mockAuthenticationFacade);
        interceptor.setRuleService(mockRuleService);
        interceptor.setSqlConsoleRuleService(mockSqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle() {
        // Test case 1
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(mockSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(mockSession.getRuleSetId()).thenReturn(1L);
        when(mockAuthenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        // Simulate need SQL console check
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        boolean result = interceptor.doPreHandle(request, response, mockSession, context);
        assertTrue(result);

        // Test case 2
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
        result = interceptor.doPreHandle(request, response, mockSession, context);
        assertTrue(result);

        // Test case 3
        context.remove(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK);
        result = interceptor.doPreHandle(request, response, mockSession, context);
        assertTrue(result);
    }

    @Test
    public void testHandle() {
        // To be implemented
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, interceptor.getOrder());
    }

    @Test
    public void testDoAfterCompletion() {
        // To be implemented
    }

    @Test
    public void testIsIndividualTeam() {
        // To be implemented
    }

    @Test
    public void testDetermineSqlType() {
        // To be implemented
    }
}