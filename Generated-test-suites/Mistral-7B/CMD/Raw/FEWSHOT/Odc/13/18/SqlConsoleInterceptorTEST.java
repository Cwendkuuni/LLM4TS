package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@Slf4j
public class SqlConsoleInterceptorTest {

    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private ConnectionSession session;
    private SqlConsoleInterceptor interceptor;

    @Before
    public void setup() {
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        session = mock(ConnectionSession.class);
        interceptor = new SqlConsoleInterceptor();
        interceptor.setAuthenticationFacade(authenticationFacade);
        interceptor.setRuleService(ruleService);
        interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_noRuleSetId_true() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        boolean result = interceptor.doPreHandle(request, response, session, new HashMap<>());

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_ruleSetId_true() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        boolean result = interceptor.doPreHandle(request, response, session, new HashMap<>());

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_needSqlConsoleCheck_false() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(context -> context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(false);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        boolean result = interceptor.doPreHandle(request, response, session, new HashMap<>());

        assertTrue(result);
    }

    @Test
    public void testHandle_forbiddenToCreatePl_false() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(false);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        boolean result = interceptor.handle(request, response, session, new HashMap<>());

        assertTrue(result);
    }

    @Test
    public void testHandle_forbiddenToCreatePl_true() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        boolean result = interceptor.handle(request, response, session, new HashMap<>());

        assertFalse(result);
    }

    @Test
    public void testHandle_allowSqlTypes_true() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL,
                String.class)).thenReturn(List.of("SELECT"));

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSql("SELECT * FROM table_name");
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        SqlTuplesWithViolation sqlTuples = new SqlTuplesWithViolation();
        sqlTuples.setSqlTuple(new SqlTuple("SELECT * FROM table_name", 1, 1, null, null, null));
        List<SqlTuple> sqlTuplesList = List.of(sqlTuples.getSqlTuple());
        response.setSqls(sqlTuplesList);

        boolean result = interceptor.handle(request, response, session, new HashMap<>());

        assertTrue(result);
    }

    @Test
    public void testHandle_allowSqlTypes_false() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL,
                String.class)).thenReturn(List.of("INSERT"));

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSql("SELECT * FROM table_name");
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        SqlTuplesWithViolation sqlTuples = new SqlTuplesWithViolation();
        sqlTuples.setSqlTuple(new SqlTuple("SELECT * FROM table_name", 1, 1, null, null, null));
        List<SqlTuple> sqlTuplesList = List.of(sqlTuples.getSqlTuple());
        response.setSqls(sqlTuplesList);

        Rule rule = mock(Rule.class);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName())).thenReturn(rule);

        boolean result = interceptor.handle(request, response, session, new HashMap<>());

        assertFalse(result);
    }

    @Test
    public void testHandle_maxSqlSize_true() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL,
                Integer.class)).thenReturn(Optional.of(5));

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        List<SqlTuple> sqlTuples = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            SqlTuplesWithViolation sqlTuplesWithViolation = new SqlTuplesWithViolation();
            sqlTuplesWithViolation.setSqlTuple(new SqlTuple("SELECT * FROM table_name", 1, 1, null, null, null));
            sqlTuples.add(sqlTuplesWithViolation.getSqlTuple());
        }
        response.setSqls(sqlTuples);

        boolean result = interceptor.handle(request, response, session, new HashMap<>());

        assertTrue(result);
    }

    @Test
    public void testHandle_maxSqlSize_false() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL,
                Integer.class)).thenReturn(Optional.of(2));

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        List<SqlTuple> sqlTuples = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SqlTuplesWithViolation sqlTuplesWithViolation = new SqlTuplesWithViolation();
            sqlTuplesWithViolation.setSqlTuple(new SqlTuple("SELECT * FROM table_name", 1, 1, null, null, null));
            sqlTuples.add(sqlTuplesWithViolation.getSqlTuple());
        }
        response.setSqls(sqlTuples);

        Rule rule = mock(Rule.class);
        RuleViolation violation = new RuleViolation();
        violation.setLevel(Rule.Level.ERROR);
        violation.setLocalizedMessage("Maximum number of execute sqls exceeded. Current number: 3, maximum number: 2");
        rule.setViolation(violation);

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())).thenReturn(rule);

        boolean result = interceptor.handle(request, response, session, new HashMap<>());

        assertFalse(result);
    }

    @Test
    public void testDetermineSqlType_sqlTypeUnknown_sqlTypeOthers() {
        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM table_name", 1, 1, null, null, null);

        BasicResult result = interceptor.determineSqlType(sqlTuple, DialectType.MySQL);

        assertEquals(SqlType.OTHERS, result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_sqlTypeKnown_sqlTypeKnown() {
        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(ast.getParseResult().getSqlType()).thenReturn(SqlType.SELECT);

        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM table_name", 1, 1, null, ast, null);

        BasicResult result = interceptor.determineSqlType(sqlTuple, DialectType.MySQL);

        assertEquals(SqlType.SELECT, result.getSqlType());
    }

    @Test(expected = SyntaxErrorException.class)
    public void testDetermineSqlType_syntaxError() {
        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(ast.getParseResult().getSyntaxError()).thenReturn(true);

        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM table_name", 1, 1, null, ast, null);

        interceptor.determineSqlType(sqlTuple, DialectType.MySQL);
    }
}