package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationServiceMock;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

@Slf4j
public class SqlConsoleInterceptorTest {

    private static final String RULE_SET_ID = "1";
    private static final String DIALECT_TYPE = "mysql";
    private static final int MAX_RETURN_ROWS = 10;
    private static final int MAX_EXECUTE_SQLS = 5;
    private static final String[] ALLOW_SQL_TYPES = {"DDL"};

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

        when(authenticationFacade.currentUser()).thenReturn(createUser());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(RULE_SET_ID);
        when(session.getDialectType()).thenReturn(DIALECT_TYPE);
    }

    @Test
    public void testHandle_NoRuleSetId_ReturnTrue() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        SqlAsyncExecuteReq request = createRequest();
        SqlAsyncExecuteResp response = createResponse();

        boolean actual = interceptor.handle(request, response, session, new HashMap<>());

        assertEquals(true, actual);
    }

    @Test
    public void testHandle_NotNeedCheck_ReturnTrue() {
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(false);

        SqlAsyncExecuteReq request = createRequest();
        SqlAsyncExecuteResp response = createResponse();

        boolean actual = interceptor.handle(request, response, session, new HashMap<>());

        assertEquals(true, actual);
    }

    @Test
    public void testHandle_IsIndividualTeam_ReturnTrue() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        SqlAsyncExecuteReq request = createRequest();
        SqlAsyncExecuteResp response = createResponse();

        boolean actual = interceptor.handle(request, response, session, new HashMap<>());

        assertEquals(true, actual);
    }

    @Test
    public void testHandle_MaxReturnRows_LimitQuery() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(RULE_SET_ID);
        when(sqlConsoleRuleService.getProperties(RULE_SET_ID, SqlConsoleRules.MAX_RETURN_ROWS, DIALECT_TYPE, Integer.class))
                .thenReturn(Optional.of(MAX_RETURN_ROWS));

        SqlAsyncExecuteReq request = createRequest();
        SqlAsyncExecuteResp response = createResponse();

        interceptor.handle(request, response, session, new HashMap<>());

        assertEquals(MAX_RETURN_ROWS, request.getQueryLimit());
    }

    @Test
    public void testHandle_MaxExecuteSqls_LimitNumberOfSqls() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(RULE_SET_ID);
        when(sqlConsoleRuleService.getListProperties(RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES, DIALECT_TYPE,
                String.class)).thenReturn(Optional.of((List<String>) Arrays.asList("DDL")));

        SqlAsyncExecuteReq request = createRequest();
        SqlAsyncExecuteResp response = createResponseWithSqls(6);

        interceptor.handle(request, response, session, new HashMap<>());

        assertEquals(MAX_EXECUTE_SQLS, response.getSqls().size());
    }

    @Test
    public void testHandle_MaxExecuteSqls_ForbidLastSql() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(RULE_SET_ID);
        when(sqlConsoleRuleService.getListProperties(RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES, DIALECT_TYPE,
                String.class)).thenReturn(Optional.of((List<String>) Arrays.asList("DDL")));

        SqlAsyncExecuteReq request = createRequest();
        SqlAsyncExecuteResp response = createResponseWithSqls(MAX_EXECUTE_SQLS + 1);

        interceptor.handle(request, response, session, new HashMap<>());

        assertFalse(response.isIntercepted());
    }

    @Test
    public void testHandle_ForbiddenCreatePl_Violation() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(RULE_SET_ID);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);

        SqlAsyncExecuteReq request = createRequest();
        SqlAsyncExecuteResp response = createResponseWithCreatePlSql();

        interceptor.handle(request, response, session, new HashMap<>());

        List<Rule> violatedRules = response.getViolatedRules();
        Rule expectedRule = new Rule();
        RuleViolation violation = new RuleViolation();
        violation.setLevel(RuleViolation.Level.ERROR);
        violation.setLocalizedMessage("SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getLocalizedMessage(null)");
        violation.setOffset(request.getSqls().get(0).getOffset());
        violation.setStart(0);
        violation.setStop(request.getSqls().get(0).getOriginalSql().length());
        expectedRule.setViolation(violation);

        assertEquals(1, violatedRules.size());
        assertEquals(expectedRule, violatedRules.get(0));
    }

    @Test
    public void testHandle_ForbiddenSqlType_Violation() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(RULE_SET_ID);
        when(sqlConsoleRuleService.getListProperties(RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES, DIALECT_TYPE,
                String.class)).thenReturn(Optional.of((List<String>) Arrays.asList("SELECT")));

        SqlAsyncExecuteReq request = createRequestSelectSql();
        SqlAsyncExecuteResp response = createResponse();

        interceptor.handle(request, response, session, new HashMap<>());

        List<Rule> violatedRules = response.getViolatedRules();
        Rule expectedRule = new Rule();
        RuleViolation violation = new RuleViolation();
        violation.setLevel(RuleViolation.Level.WARNING);
        violation.setLocalizedMessage("SqlConsoleRules.ALLOW_SQL_TYPES.getLocalizedMessage(new Object[] {rule.getProperties()."
                + "get(rule.getMetadata().getPropertyMetadatas().get(0).getName()).toString()})");
        violation.setOffset(request.getSqls().get(0).getOffset());
        violation.setStart(0);
        violation.setStop(request.getSqls().get(0).getOriginalSql().length());
        expectedRule.setViolation(violation);

        assertEquals(1, violatedRules.size());
        assertEquals(expectedRule, violatedRules.get(0));
    }

    @Test
    public void testDoAfterCompletion_NotAllowedEditResultset_EditableFalse() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(RULE_SET_ID);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);

        SqlExecuteResult response = createExecuteResult();

        interceptor.doAfterCompletion(response, session, new HashMap<>());

        if (Objects.nonNull(response.getResultSetMetaData())) {
            assertFalse(response.getResultSetMetaData().isEditable());
        }
    }

    @Test
    public void testDoAfterCompletion_AllowedExport_True() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(RULE_SET_ID);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(false);

        SqlExecuteResult response = createExecuteResult();

        interceptor.doAfterCompletion(response, session, new HashMap<>());

        assertTrue(response.isAllowExport());
    }

    private User createUser() {
        User user = new User();
        user.setOrganizationType(OrganizationType.TEAM);
        return user;
    }

    private SqlAsyncExecuteReq createRequest() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSqls(Collections.singletonList(createSqlTuple("SELECT * FROM users WHERE id = 1")));
        return request;
    }

    private SqlAsyncExecuteReq createRequestSelectSql() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSqls(Collections.singletonList(createSqlTuple("SELECT * FROM users")));
        return request;
    }

    private SqlAsyncExecuteResp createResponse() {
        return new SqlAsyncExecuteResp();
    }

    private SqlAsyncExecuteResp createResponseWithSqls(int numberOfSqls) {
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(new ArrayList<>());
        for (int i = 0; i < numberOfSqls; i++) {
            response.getSqls().add(createSqlTuple("SELECT * FROM users WHERE id = " + i));
        }
        return response;
    }

    private SqlAsyncExecuteResp createResponseWithCreatePlSql() {
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(Collections.singletonList(createSqlTuple("CREATE PROCEDURE testProc()")));
        return response;
    }

    private SqlTuple createSqlTuple(String sql) {
        return new SqlTuple("", sql, 0, 0, null, null, null, null, null);
    }

    private SqlExecuteResult createExecuteResult() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        return response;
    }

    private static class User {
        private OrganizationType organizationType;

        public OrganizationType getOrganizationType() {
            return organizationType;
        }

        public void setOrganizationType(OrganizationType organizationType) {
            this.organizationType = organizationType;
        }
    }
}