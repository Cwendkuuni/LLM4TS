package com.oceanbase.odc.service.session.interceptor.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRules;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules.MaxExecuteSqls;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor.HandleResult;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

/**
 * Test class for SqlConsoleInterceptor
 */
public class SqlConsoleInterceptorTest {

    private static final String TEST_RULE_SET_ID = "1";
    private static final DialectType TEST_DIALECT_TYPE = DialectType.MYSQL;
    private static final String TEST_SQL = "SELECT * FROM test_table WHERE id = 1";;

    private ConnectionSession session;
    private RuleService ruleService;
    private AuthenticationFacade authenticationFacade;
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void init() {
        User currentUser = Mockito.mock(User.class);
        when(currentUser.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        this.authenticationFacade = Mockito.mock(AuthenticationFacade.class);
        when(this.authenticationFacade.currentUser()).thenReturn(currentUser);

        this.ruleService = Mockito.mock(RuleService.class);

        this.session = Mockito.mock(ConnectionSession.class);
        ConnectionSessionUtil.initSession(session);
        ConnectionSessionUtil.setRuleSetId(session, TEST_RULE_SET_ID);

        this.sqlConsoleInterceptor = new SqlConsoleInterceptor();
        this.sqlConsoleInterceptor.setAuthenticationFacade(this.authenticationFacade);
        this.sqlConsoleInterceptor.setRuleService(this.ruleService);
    }

    @Test
    public void testDoPreHandle_true() {
        HandleResult result = this.sqlConsoleInterceptor.doPreHandle(prepareTestRequest(), prepareTestResponse(),
                this.session, new HashMap<>());
        assertTrue(result.isAllowExecute());
    }

    @Test
    public void testDoPreHandle_false() {
        when(this.ruleService.getProperties(TEST_RULE_SET_ID, MaxExecuteSqls.MAX_EXECUTE_SQLS, TEST_DIALECT_TYPE,
                Integer.class)).thenReturn(Optional.of(1));

        HandleResult result = this.sqlConsoleInterceptor.doPreHandle(prepareTestRequest(), prepareTestResponse(),
                this.session, new HashMap<>());
        assertFalse(result.isAllowExecute());
    }

    @Test
    public void testDoPreHandle_noRuleSetId() {
        ConnectionSessionUtil.setRuleSetId(this.session, null);

        HandleResult result = this.sqlConsoleInterceptor.doPreHandle(prepareTestRequest(), prepareTestResponse(),
                this.session, new HashMap<>());
        assertTrue(result.isAllowExecute());
    }

    @Test(expected = NullPointerException.class)
    public void testDoPreHandle_nullContext() {
        HandleResult result = this.sqlConsoleInterceptor.doPreHandle(prepareTestRequest(), prepareTestResponse(),
                this.session, null);
    }

    @Test
    public void testHandle_allowed() {
        SqlAsyncExecuteReq request = prepareTestRequest();
        SqlAsyncExecuteResp response = prepareTestResponse();
        HandleResult result = this.sqlConsoleInterceptor.handle(request, response, this.session, new HashMap<>());
        assertTrue(result.isAllowExecute());
    }

    @Test
    public void testHandle_forbiddenToCreatePl() {
        SqlAsyncExecuteReq request = prepareTestRequest();
        SqlAsyncExecuteResp response = prepareTestResponse();
        when(this.ruleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, this.session))
                .thenReturn(true);
        HandleResult result = this.sqlConsoleInterceptor.handle(request, response, this.session, new HashMap<>());
        assertFalse(result.isAllowExecute());
    }

    @Test
    public void testHandle_sqlTypeNotAllowed() {
        SqlAsyncExecuteReq request = prepareTestRequest();
        SqlAsyncExecuteResp response = prepareTestResponse();
        when(this.ruleService.getListProperties(TEST_RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES, TEST_DIALECT_TYPE,
                String.class)).thenReturn(Arrays.asList("DDL"));
        HandleResult result = this.sqlConsoleInterceptor.handle(request, response, this.session, new HashMap<>());
        assertFalse(result.isAllowExecute());
    }

    @Test(expected = SyntaxErrorException.class)
    public void testHandle_sqlSyntaxError() {
        SqlAsyncExecuteReq request = prepareTestRequest();
        SqlAsyncExecuteResp response = prepareTestResponse();
        HandleResult result = this.sqlConsoleInterceptor.handle(request, response, this.session, new HashMap<>());
        assertTrue(result.isAllowExecute());
        when(this.ruleService.getListProperties(TEST_RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES, TEST_DIALECT_TYPE,
                String.class)).thenReturn(Arrays.asList("DDL"));
        doThrow(new SyntaxErrorException("Test Syntax Error"))
                .when(this.sqlConsoleInterceptor)
                .determineSqlType(Mockito.any(SqlTuple.class), Mockito.any(DialectType.class));

        HandleResult result1 = this.sqlConsoleInterceptor.handle(request, response, this.session, new HashMap<>());
        assertFalse(result1.isAllowExecute());
    }

    @Test
    public void testDoAfterCompletion_notForbiddenToEditResultSet() {
        SqlExecuteResult response = prepareTestExecuteResult();
        this.sqlConsoleInterceptor.doAfterCompletion(response, this.session, new HashMap<>());
        assertTrue(response.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_forbiddenToEditResultSet() {
        SqlExecuteResult response = prepareTestExecuteResult();
        when(this.sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, this.session))
                .thenReturn(true);
        this.sqlConsoleInterceptor.doAfterCompletion(response, this.session, new HashMap<>());
        assertFalse(response.isAllowExport());
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, this.sqlConsoleInterceptor.getOrder());
    }

    private SqlAsyncExecuteReq prepareTestRequest() {
        return new SqlAsyncExecuteReq();
    }

    private SqlAsyncExecuteResp prepareTestResponse() {
        List<SqlTuplesWithViolation> sqlTuples = Arrays.asList(new SqlTuplesWithViolation(new SqlTuple(TEST_SQL), null));
        return new SqlAsyncExecuteResp(new ArrayList<>(), sqlTuples);
    }

    private SqlExecuteResult prepareTestExecuteResult() {
        return new SqlExecuteResult(new BasicResult(TEST_DIALECT_TYPE, TEST_SQL, null, null), null);
    }
}