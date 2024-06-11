import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Before
    public void setUp() {
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);

        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.authenticationFacade = authenticationFacade;
        sqlConsoleInterceptor.ruleService = ruleService;
        sqlConsoleInterceptor.sqlConsoleRuleService = sqlConsoleRuleService;
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(false);
        
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(session.getDialectType()).thenReturn(DialectType.POSTGRESQL);
        when(sqlConsoleRuleService.getProperties(anyLong(), any(), any(), any())).thenReturn(java.util.Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(anyLong(), any(), any(), any())).thenReturn(java.util.Optional.of(List.of("SELECT")));
        when(session.getRuleSetId()).thenReturn(123L);
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(false);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals("SQL_CONSOLE_RULE", sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(response.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);
        when(session.getRuleSetId()).thenReturn(123L);
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        verify(response, times(1)).setAllowExport(anyBoolean());
    }

    @Test
    public void testIsIndividualTeam() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        assertTrue(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType() throws Exception {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        when(sqlTuple.getAst()).thenReturn(null);

        assertEquals(SqlType.OTHERS, sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL).getSqlType());
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }
}