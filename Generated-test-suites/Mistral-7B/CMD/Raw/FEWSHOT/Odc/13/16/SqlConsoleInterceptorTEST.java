package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlAsyncExecuteReq;
import com.oceanbase.odc.core.sql.execute.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationResponse;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq.SqlExecuteRequestType;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

@Slf4j
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    private ConnectionSession session;
    private SqlConsoleInterceptor interceptor;

    @Before
    public void setup() {
        session = mock(ConnectionSession.class);
        interceptor = new SqlConsoleInterceptor();
        interceptor.setAuthenticationFacade(authenticationFacade);
        interceptor.setRuleService(ruleService);
        interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_emptyRequest_returnsTrue() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        assertTrue(interceptor.doPreHandle(request, response, session, new HashMap<>()));
    }

    @Test
    public void testDoPreHandle_noRuleSetId_returnsTrue() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQuery(new StringBuilder("SELECT * FROM table;").toString());
        request.setRequestType(SqlExecuteRequestType.SQL_CONSOLE);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        assertTrue(interceptor.doPreHandle(request, response, session, new HashMap<>()));
    }

    @Test
    public void testDoPreHandle_individualTeam_returnsTrue() {
        AuthenticationResponse user = new AuthenticationResponse();
        user.setOrganizationType(OrganizationType.INDIVIDUAL);
        when(authenticationFacade.currentUser()).thenReturn(user);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQuery(new StringBuilder("SELECT * FROM table;").toString());
        request.setRequestType(SqlExecuteRequestType.SQL_CONSOLE);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        assertTrue(interceptor.doPreHandle(request, response, session, new HashMap<>()));
    }

    @Test
    public void testDoPreHandle_noNeedCheck_returnsTrue() {
        AuthenticationResponse user = new AuthenticationResponse();
        user.setOrganizationType(OrganizationType.TEAM);
        when(authenticationFacade.currentUser()).thenReturn(user);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQuery(new StringBuilder("SELECT * FROM table;").toString());
        request.setRequestType(SqlExecuteRequestType.SQL_CONSOLE);
        request.setNeedCheckSqlConsole(false);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        assertTrue(interceptor.doPreHandle(request, response, session, new HashMap<>()));
    }

    @Test
    public void testDoPreHandle_limitReached_limitsQuery() {
        AuthenticationResponse user = new AuthenticationResponse();
        user.setOrganizationType(OrganizationType.TEAM);
        when(authenticationFacade.currentUser()).thenReturn(user);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQuery(new StringBuilder("SELECT * FROM table;").toString());
        request.setRequestType(SqlExecuteRequestType.SQL_CONSOLE);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        List<SqlTuple> sqlTuples = Collections.singletonList(new SqlTuple());
        when(response.getSqls()).thenReturn(sqlTuples);

        int maxExecuteSqls = 3;
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL, Integer.class))
                .thenReturn(Optional.of(maxExecuteSqls));

        assertFalse(interceptor.doPreHandle(request, response, session, new HashMap<>()));
    }

    @Test
    public void testDoPreHandle_maxExecuteSqlsRuleViolation_blocksQuery() {
        AuthenticationResponse user = new AuthenticationResponse();
        user.setOrganizationType(OrganizationType.TEAM);
        when(authenticationFacade.currentUser()).thenReturn(user);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQuery(new StringBuilder("SELECT * FROM table;").toString());
        request.setRequestType(SqlExecuteRequestType.SQL_CONSOLE);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        List<SqlTuple> sqlTuples = Collections.singletonList(new SqlTuple());
        when(response.getSqls()).thenReturn(sqlTuples);

        int maxExecuteSqls = 3;
        int maxReturnRows = 10;
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL, Integer.class))
                .thenReturn(Optional.of(maxExecuteSqls));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL, Integer.class))
                .thenReturn(Optional.of(maxReturnRows));

        Rule maxExecuteSqlsRule = mock(Rule.class);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(maxExecuteSqlsRule));
        when(maxExecuteSqlsRule.getProperties()).thenReturn(Map.of("max_sqls", maxExecuteSqls));

        Rule maxReturnRowsRule = mock(Rule.class);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_RETURN_ROWS.getRuleName()))
                .thenReturn(Optional.of(maxReturnRowsRule));
        when(maxReturnRowsRule.getProperties()).thenReturn(Map.of("max_rows", maxReturnRows));

        SqlTuple sqlTuple = sqlTuples.get(0);
        sqlTuple.setLimit(maxReturnRows + 1);

        assertFalse(interceptor.doPreHandle(request, response, session, new HashMap<>()));
    }

    @Test
    public void testDoPreHandle_notAllowedCreatePl_blocksQuery() {
        AuthenticationResponse user = new AuthenticationResponse();
        user.setOrganizationType(OrganizationType.TEAM);
        when(authenticationFacade.currentUser()).thenReturn(user);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQuery(new StringBuilder("CREATE PROCEDURE p()").toString());
        request.setRequestType(SqlExecuteRequestType.SQL_CONSOLE);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        List<SqlTuple> sqlTuples = Collections.singletonList(new SqlTuple());
        when(response.getSqls()).thenReturn(sqlTuples);

        Rule notAllowedCreatePlRule = mock(Rule.class);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName()))
                .thenReturn(Optional.of(notAllowedCreatePlRule));
        when(notAllowedCreatePlRule.getProperties()).thenReturn(Map.of("is_allowed", false));

        assertFalse(interceptor.doPreHandle(request, response, session, new HashMap<>()));
    }

    @Test
    public void testDoPreHandle_notAllowedExecuteSqlType_blocksQuery() {
        AuthenticationResponse user = new AuthenticationResponse();
        user.setOrganizationType(OrganizationType.TEAM);
        when(authenticationFacade.currentUser()).thenReturn(user);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQuery(new StringBuilder("SELECT * FROM table;").toString());
        request.setRequestType(SqlExecuteRequestType.SQL_CONSOLE);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        List<SqlTuple> sqlTuples = Collections.singletonList(new SqlTuple());
        when(response.getSqls()).thenReturn(sqlTuples);

        int maxExecuteSqls = 3;
        int maxReturnRows = 10;
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL, Integer.class))
                .thenReturn(Optional.of(maxExecuteSqls));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL, Integer.class))
                .thenReturn(Optional.of(maxReturnRows));

        Rule allowSqlTypesRule = mock(Rule.class);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.of(allowSqlTypesRule));
        when(allowSqlTypesRule.getProperties()).thenReturn(Map.of("sql_types", "INSERT"));

        SqlTuple sqlTuple = sqlTuples.get(0);
        sqlTuple.setSqlType(SqlType.SELECT);

        assertFalse(interceptor.doPreHandle(request, response, session, new HashMap<>()));
    }

    @Test
    public void testDoAfterCompletion_noRuleSetId_noAction() {
        SqlExecuteResult response = new SqlExecuteResult();

        assertFalse(interceptor.doAfterCompletion(response, session, new HashMap<>()));
    }

    @Test
    public void testDoAfterCompletion_individualTeam_noAction() {
        AuthenticationResponse user = new AuthenticationResponse();
        user.setOrganizationType(OrganizationType.INDIVIDUAL);
        when(authenticationFacade.currentUser()).thenReturn(user);

        SqlExecuteResult response = new SqlExecuteResult();

        assertFalse(interceptor.doAfterCompletion(response, session, new HashMap<>()));
    }

    @Test
    public void testDoAfterCompletion_team_allowsExport() {
        AuthenticationResponse user = new AuthenticationResponse();
        user.setOrganizationType(OrganizationType.TEAM);
        when(authenticationFacade.currentUser()).thenReturn(user);

        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(false);

        assertTrue(interceptor.doAfterCompletion(response, session, new HashMap<>()));
    }

    @Test
    public void testDoAfterCompletion_team_disallowsExport() {
        AuthenticationResponse user = new AuthenticationResponse();
        user.setOrganizationType(OrganizationType.TEAM);
        when(authenticationFacade.currentUser()).thenReturn(user);

        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(true);

        assertFalse(interporter.doAfterCompletion(response, session, new HashMap<>()));
    }
}