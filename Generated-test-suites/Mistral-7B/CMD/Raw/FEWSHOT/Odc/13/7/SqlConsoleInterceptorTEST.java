package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;

@Component
public class SqlConsoleInterceptorTest {

    @Mock
    private ConnectionSession session;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @InjectMocks
    private SqlConsoleInterceptor interceptor;

    private User currentUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        currentUser = new User();
        when(authenticationFacade.currentUser()).thenReturn(currentUser);
    }

    @Test
    public void testDoPreHandle_noRuleSetId_returnsTrue() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        boolean result = interceptor.doPreHandle(request, response, session, new HashMap<>());

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_individualTeam_returnsTrue() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        boolean result = interceptor.doPreHandle(request, response, session, new HashMap<>());

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_ruleSetIdPresent_returnsAllowExecute() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        AtomicBoolean allowExecute = new AtomicBoolean(true);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(new SqlTuple(1, "select * from table1"));

        response.setSqls(sqlTuples);

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL, Integer.class))
                .thenReturn(Optional.of(5));
        when(request.getQueryLimit()).thenReturn(10);

        List<SqlTuple> filteredSqlTuples = interceptor.handle(request, response, session, new HashMap<>()).stream()
                .map(SqlTuplesWithViolation::getSqlTuple).collect(Collectors.toList());

        assertEquals(5, filteredSqlTuples.size());

        verify(sqlConsoleRuleService, times(1)).getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL,
                Integer.class);

        assertTrue(interceptor.doPreHandle(request, response, session, new HashMap<>()));
        assertTrue(allowExecute.get());
    }

    @Test
    public void testDoPreHandle_ruleSetIdPresent_returnsFalse() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        AtomicBoolean allowExecute = new AtomicBoolean(true);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(new SqlTuple(1, "select * from table1"));

        response.setSqls(sqlTuples);

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL, Integer.class))
                .thenReturn(Optional.of(5));
        when(request.getQueryLimit()).thenReturn(10);

        List<SqlTuple> filteredSqlTuples = Arrays.asList(sqlTuples.get(0));

        doAnswer(invocation -> {
            SqlConsoleInterceptor interceptor = invocation.getArgument(0);
            SqlAsyncExecuteReq req = invocation.getArgument(1);
            SqlAsyncExecuteResp resp = invocation.getArgument(2);
            ConnectionSession session = invocation.getArgument(3);
            Map<String, Object> context = invocation.getArgument(4);

            when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                    .thenReturn(false);
            when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL,
                    String.class)).thenReturn(Optional.of(Arrays.asList("SELECT")));

            SqlTuplesWithViolation result = interceptor.handle(req, resp, session, context).get(0);
            interceptor.doAfterCompletion(new SqlExecuteResult(new SqlExecuteStatus(SqlExecuteStatus.SUCCESS), null, null),
                    session, context);

            return null;
        }).when(interceptor).handle(eq(request), eq(response), eq(session), eq(new HashMap<>()));

        List<SqlTuple> filteredSqlTuples = interceptor.handle(request, response, session, new HashMap<>()).stream()
                .map(SqlTuplesWithViolation::getSqlTuple).collect(Collectors.toList());

        assertEquals(1, filteredSqlTuples.size());

        verify(sqlConsoleRuleService, times(1)).getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL,
                Integer.class);
        verify(sqlConsoleRuleService, times(1)).isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session);
        verify(sqlConsoleRuleService, times(1)).getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                DialectType.MySQL, String.class);

        assertFalse(interceptor.doPreHandle(request, response, session, new HashMap<>()));
        assertFalse(allowExecute.get());
    }

    @Test
    public void testDoAfterCompletion_forbiddenToCreatePl() {
        SqlExecuteResult result = new SqlExecuteResult(new SqlExecuteStatus(SqlExecuteStatus.SUCCESS), null, null);

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        interceptor.doAfterCompletion(result, session, new HashMap<>());

        assertTrue(result.isAllowExport());

        verify(session, times(1)).setAllowExport(false);
    }

    @Test
    public void testDoAfterCompletion_notForbiddenToCreatePl() {
        SqlExecuteResult result = new SqlExecuteResult(new SqlExecuteStatus(SqlExecuteStatus.SUCCESS), null, null);

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        interceptor.doAfterCompletion(result, session, new HashMap<>());

        assertTrue(result.isAllowExport());

        verify(session, never()).setAllowExport(false);
    }
}