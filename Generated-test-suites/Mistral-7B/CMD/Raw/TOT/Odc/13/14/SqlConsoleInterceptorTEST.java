package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.core.exception.OceanbaseException;
import com.oceanbase.odc.core.exception.RuleViolationException;
import com.oceanbase.odc.core.model.AbstractSyntaxTree;
import com.oceanbase.odc.core.model.ConnectionSession;
import com.oceanbase.odc.core.model.SqlExecuteResult;
import com.oceanbase.odc.core.model.SqlTuplesWithViolation;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OrganizationType;
import com.oceanbase.odc.core.shared.constant.SqlConsoleRules;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlExecuteStatus;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation.SqlTuple;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

public class SqlConsoleInterceptorTest {

    private static final String VALID_SQL = "SELECT * FROM employees WHERE salary > 5000";;
    private static final String INVALID_SQL = "XXX";
    private static final String INVALID_SQL_WITH_SYNTAX_ERROR = "SELECT * FROM employees WHERE salary > 5000;--comment";
    private static final String MAX_EXECUTE_SQLS_RULE_NAME = "maxExecuteSqls";
    private static final String NOT_ALLOWED_CREATE_PL_RULE_NAME = "notAllowedCreatePl";
    private static final String ALLOW_SQL_TYPES_RULE_NAME = "allowSqlTypes";
    private static final int MAX_EXECUTE_SQLS = 1;
    private static final int MAX_RETURN_ROWS = 10;
    private static final List<String> ALLOW_SQL_TYPES = Collections.singletonList("SELECT");

    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private ConnectionSession session;
    private SqlConsoleInterceptor interceptor;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private SqlExecuteResult executeResult;

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

        when(authenticationFacade.currentUser()).thenReturn(createUser(OrganizationType.TEAM));
        when(ruleService.getByRulesetIdAndName(anyLong(), eq(MAX_EXECUTE_SQLS_RULE_NAME))).thenReturn(
                Optional.of(createRule(MAX_EXECUTE_SQLS_RULE_NAME, MAX_EXECUTE_SQLS, 3)));
        when(ruleService.getByRulesetIdAndName(anyLong(), eq(NOT_ALLOWED_CREATE_PL_RULE_NAME)))
                .thenReturn(Optional.of(createRule(NOT_ALLOWED_CREATE_PL_RULE_NAME, null, 1)));
        when(ruleService.getByRulesetIdAndName(anyLong(), eq(ALLOW_SQL_TYPES_RULE_NAME)))
                .thenReturn(Optional.of(createRule(ALLOW_SQL_TYPES_RULE_NAME, ALLOW_SQL_TYPES, 2)));
        when(sqlConsoleRuleService.getProperties(anyLong(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(DialectType.class),
                anyInt.class)).thenAnswer(new Answer<Integer>() {
                    @Override
                    public Integer answer(InvocationOnMock invocation) throws Throwable {
                        return MAX_EXECUTE_SQLS;
                    }
                });
        when(sqlConsoleRuleService.getListProperties(anyLong(), eq(SqlConsoleRules.ALLOW_SQL_TYPES), any(DialectType.class),
                anyInt.class)).thenAnswer(new Answer<List<String>>() {
                    @Override
                    public List<String> answer(InvocationOnMock invocation) throws Throwable {
                        return ALLOW_SQL_TYPES;
                    }
                });
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        request = new SqlAsyncExecuteReq();
        request.setQuery(VALID_SQL);
        request.setQueryLimit(10);
        response = new SqlAsyncExecuteResp();
        response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(new SqlTuple(VALID_SQL), new ArrayList<>())));
        executeResult = new SqlExecuteResult();
        executeResult.setStatus(SqlExecuteStatus.SUCCESS);
        executeResult.setResultSetMetaData(null);
    }

    @Test
    public void testDoPreHandleWithValidSqlAndLimitLessThanMax() {
        givenDoPreHandleWithValidSqlAndLimitLessThanMax();
        thenDoPreHandleReturnsTrue();
    }

    @Test
    public void testDoPreHandleWithValidSqlAndLimitGreaterThanMax() {
        givenDoPreHandleWithValidSqlAndLimitGreaterThanMax();
        thenDoPreHandleReturnsFalse();
    }

    @Test
    public void testDoPreHandleWithSqlConsoleCheckDisabled() {
        givenDoPreHandleWithSqlConsoleCheckDisabled();
        thenDoPreHandleReturnsTrue();
    }

    @Test(expected = RuleViolationException.class)
    public void testDoPreHandleWithMaxExecuteSqlsViolation() {
        givenDoPreHandleWithMaxExecuteSqlsViolation();
        thenDoPreHandleReturnsFalse();
    }

    @Test(expected = RuleViolationException.class)
    public void testDoPreHandleWithNotAllowedCreatePlViolation() {
        givenDoPreHandleWithNotAllowedCreatePlViolation();
        thenDoPreHandleReturnsFalse();
    }

    @Test(expected = RuleViolationException.class)
    public void testDoPreHandleWithAllowSqlTypesViolation() {
        givenDoPreHandleWithAllowSqlTypesViolation();
        thenDoPreHandleReturnsFalse();
    }

    @Test
    public void testDoAfterCompletionWithValidSql() {
        givenDoAfterCompletionWithValidSql();
        thenAllowExportIsTrue();
    }

    @Test
    public void testDoAfterCompletionWithInvalidSql() {
        givenDoAfterCompletionWithInvalidSql();
        thenAllowExportIsFalse();
    }

    @Test
    public void testDoAfterCompletionWithForbiddenEditResultSet() {
        givenDoAfterCompletionWithForbiddenEditResultSet();
        thenAllowExportIsFalse();
    }

    @Test
    public void testDoAfterCompletionWithValidSqlAndIndividualTeam() {
        givenDoAfterCompletionWithValidSqlAndIndividualTeam();
        thenAllowExportIsTrue();
    }

    @Test(expected = OceanbaseException.class)
    public void testDoAfterCompletionWithRuleViolation() {
        givenDoAfterCompletionWithRuleViolation();
        thenThrowRuleViolationException();
    }

    private void givenDoPreHandleWithValidSqlAndLimitLessThanMax() {
        when(ruleService.getByRulesetIdAndName(anyLong(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))).thenReturn(
                Optional.of(createRule(MAX_EXECUTE_SQLS_RULE_NAME, MAX_EXECUTE_SQLS, 3)));
        when(sqlConsoleRuleService.getProperties(anyLong(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(DialectType.class),
                anyInt.class)).thenAnswer(new Answer<Integer>() {
                    @Override
                    public Integer answer(InvocationOnMock invocation) throws Throwable {
                        return MAX_EXECUTE_SQLS - 1;
                    }
                });

        interceptor.doPreHandle(request, response, session, Collections.emptyMap());
    }

    private void givenDoPreHandleWithValidSqlAndLimitGreaterThanMax() {
        when(ruleService.getByRulesetIdAndName(anyLong(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))).thenReturn(
                Optional.of(createRule(MAX_EXECUTE_SQLS_RULE_NAME, MAX_EXECUTE_SQLS, 3)));
        when(sqlConsoleRuleService.getProperties(anyLong(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(DialectType.class),
                anyInt.class)).thenAnswer(new Answer<Integer>() {
                    @Override
                    public Integer answer(InvocationOnMock invocation) throws Throwable {
                        return MAX_EXECUTE_SQLS;
                    }
                });

        interceptor.doPreHandle(request, response, session, Collections.emptyMap());
    }

    private void givenDoPreHandleWithSqlConsoleCheckDisabled() {
        when(ruleService.getByRulesetIdAndName(anyLong(), eq(SqlConsoleRules.NEED_SQL_CONSOLE_CHECK.getRuleName()))).thenReturn(
                Optional.of(createRule(SqlConsoleRules.NEED_SQL_CONSOLE_CHECK.getRuleName(), Boolean.FALSE, 0)));

        interceptor.doPreHandle(request, response, session, Collections.emptyMap());
    }

    private void givenDoPreHandleWithMaxExecuteSqlsViolation() {
        when(ruleService.getByRulesetIdAndName(anyLong(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))).thenReturn(
                Optional.of(createRule(MAX_EXECUTE_SQLS_RULE_NAME, MAX_EXECUTE_SQLS, 3)));
        when(sqlConsoleRuleService.getProperties(anyLong(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(DialectType.class),
                anyInt.class)).thenAnswer(new Answer<Integer>() {
                    @Override
                    public Integer answer(InvocationOnMock invocation) throws Throwable {
                        return MAX_EXECUTE_SQLS;
                    }
                });

        interceptor.doPreHandle(request, response, session, Collections.emptyMap());
    }

    private void givenDoPreHandleWithNotAllowedCreatePlViolation() {
        when(ruleService.getByRulesetIdAndName(anyLong(), eq(SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName()))).thenReturn(
                Optional.of(createRule(NOT_ALLOWED_CREATE_PL_RULE_NAME, null, 1)));
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQuery(VALID_SQL);
        request.setQueryLimit(10);

        interceptor.doPreHandle(request, response, session, Collections.emptyMap());
    }

    private void givenDoPreHandleWithAllowSqlTypesViolation() {
        when(ruleService.getByRulesetIdAndName(anyLong(), eq(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))).thenReturn(
                Optional.of(createRule(ALLOW_SQL_TYPES_RULE_NAME, ALLOW_SQL_TYPES, 2)));
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        interceptor.doPreHandle(request, response, session, Collections.emptyMap());

        String sql = "CREATE PROCEDURE PROCEDURE_NAME()";
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQuery(sql);
        request.setQueryLimit(10);

        interceptor.doPreHandle(request, response, session, Collections.emptyMap());
    }

    private void givenDoAfterCompletionWithValidSql() {
        interceptor.doAfterCompletion(executeResult, session, Collections.emptyMap());
    }

    private void givenDoAfterCompletionWithInvalidSql() {
        executeResult.setStatus(SqlExecuteStatus.FAILED);
        interceptor.doAfterCompletion(executeResult, session, Collections.emptyMap());
    }

    private void givenDoAfterCompletionWithForbiddenEditResultSet() {
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);

        interceptor.doAfterCompletion(executeResult, session, Collections.emptyMap());
    }

    private void givenDoAfterCompletionWithValidSqlAndIndividualTeam() {
        when(authenticationFacade.currentUser()).thenReturn(createUser(OrganizationType.INDIVIDUAL));

        interceptor.doAfterCompletion(executeResult, session, Collections.emptyMap());
    }

    private void givenDoAfterCompletionWithRuleViolation() {
        RuleViolation violation = new RuleViolation();
        violation.setLevel(Rule.Level.ERROR);
        violation.setLocalizedMessage("Rule violation message");

        Rule rule = new Rule();
        rule.setViolation(violation);

        Map<String, Object> context = new HashMap<>();
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(new SqlTuple(VALID_SQL), new ArrayList<>())));

        interceptor.doAfterCompletion(executeResult, session, context);

        verify(ruleService, times(1)).getByRulesetIdAndName(anyLong(), eq(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET.getRuleName()));
    }

    private Rule createRule(final String ruleName, final Object ruleProperty, final int ruleOrder) {
        Rule rule = Mockito.spy(new Rule());
        rule.setName(ruleName);
        rule.setRuleOrder(ruleOrder);
        rule.setProperties(Collections.singletonMap(ruleName, ruleProperty));
        return rule;
    }

    private User createUser(final OrganizationType organizationType) {
        User user = new User();
        user.setOrganizationType(organizationType);
        return user;
    }

    private void thenAllowExportIsTrue() {
        assertTrue(response.isAllowExport());
    }

    private void thenAllowExportIsFalse() {
        assertFalse(response.isAllowExport());
    }

    private void thenDoPreHandleReturnsTrue() {
        assertTrue(interceptor.doPreHandle(request, response, session, Collections.emptyMap()));
    }

    private void thenDoPreHandleReturnsFalse() {
        assertFalse(interceptor.doPreHandle(request, response, session, Collections.emptyMap()));
    }

    private void thenThrowRuleViolationException() {
        try {
            interceptor.doAfterCompletion(executeResult, session, Collections.emptyMap());
            fail("Expected RuleViolationException to be thrown");
        } catch (RuleViolationException e) {
            // expected
        }
    }

}