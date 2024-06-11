package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OrganizationType;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);

        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(authenticationFacade.currentUser()).thenReturn(new User());

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(session.getRuleSetId()).thenReturn(1L);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(false);
        when(sqlConsoleRuleService.getProperties(any(), any(), any(), any())).thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(any(), any(), any(), any())).thenReturn(Optional.of(new ArrayList<>()));

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String executeStage = sqlConsoleInterceptor.getExecuteStageName();

        assertEquals("SQL_CONSOLE_RULE", executeStage);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        response.setStatus(SqlExecuteStatus.SUCCESS);
        when(session.getRuleSetId()).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        assertTrue(response.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam() {
        when(authenticationFacade.currentUser()).thenReturn(new User(OrganizationType.INDIVIDUAL));

        boolean result = sqlConsoleInterceptor.isIndividualTeam();

        assertTrue(result);
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM table", 1);
        DialectType dialectType = DialectType.MYSQL;

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertNotNull(result);
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();

        assertEquals(2, order);
    }

    class User implements UserDetails {

        @Override
        public OrganizationType getOrganizationType() {
            return OrganizationType.TEAM;
        }
    }
}