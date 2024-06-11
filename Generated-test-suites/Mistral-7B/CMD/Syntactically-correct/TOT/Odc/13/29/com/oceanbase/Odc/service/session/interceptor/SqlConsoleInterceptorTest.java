package com.oceanbase.odc.service.session.interceptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.session.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;

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

    private ConnectionSession session;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;

    @Before
    public void init() {
        session = mock(ConnectionSession.class);
        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenAnswer(invocation -> {
            Rule rule = new Rule();
            RuleViolation violation = new RuleViolation();
            violation.setLevel(1);
            violation.setLocalizedMessage("test error message");
            rule.setViolation(violation);
            return rule;
        });
        when(sqlConsoleRuleService.getListProperties(anyLong(), anyString(), any(DialectType.class), any(Class[]
                .class))).thenReturn(Arrays.asList("test"));
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), any(Class
                .class))).thenReturn(Optional.of(1));
        when(sqlConsoleRuleService.isForbidden(anyString(), any())).thenReturn(false);
    }

    @Test
    public void testHandleWithEmptyList() {
        when(response.getSqls()).thenReturn(Collections.emptyList());
        sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
        assertThat(session.getUserDataMap().get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED)).isFalse();
    }

    @Test
    public void testHandleWithSqlTuples() {
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple(1, "test sql", "test sql id"),
                new SqlTuple(2, "test sql 2", "test sql id 2"), new SqlTuple(3, "test sql 3", "test sql id 3"));
        when(response.getSqls()).thenReturn(Collections.singletonList(new SqlTuplesWithViolation(sqlTuples, null)));

        sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
        assertThat(session.getUserDataMap().get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED)).isFalse();

        request.setQueryLimit(2);
        sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
        assertThat(session.getUserDataMap().get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED)).isFalse();

        sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class)
                .ifPresent(limit -> request.setQueryLimit(limit));
        sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
        assertThat(session.getUserDataMap().get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED)).isFalse();

        sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL, String[].class)
                .ifPresent(allowSqlTypes -> {
                    sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
                    assertThat(session.getUserDataMap().get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED)).isFalse();
                    if (allowSqlTypes.contains("SELECT")) {
                        sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
                        assertThat(session.getUserDataMap().get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED)).isFalse();
                    } else {
                        sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
                        assertThat(session.getUserDataMap().get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED)).isFalse();
                    }
                });

        sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL, DialectType.MYSQL, Boolean.class)
                .ifPresent(forbiddenToCreatePl -> {
                    when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                            .thenReturn(Objects.equals(forbiddenToCreatePl, Boolean.FALSE));
                    sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
                    assertThat(session.getUserDataMap().get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED)).isFalse();

                    when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                            .thenReturn(Objects.equals(forbiddenToCreatePl, Boolean.TRUE));
                    sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
                    assertThat(session.getUserDataMap().get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED)).isFalse();

                    AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
                    when(sqlTuples.get(0).getAst()).thenReturn(ast);
                    when(ast.getParseResult().getSqlType()).thenReturn(SqlType.DDL);
                    sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
                    assertThat(session.getUserDataMap().get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED)).isFalse();

                    when(sqlTuples.get(0).getAst()).thenReturn(null);
                    sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
                    assertThat(session.getUserDataMap().get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED)).isFalse();
                });

        sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL, String.class)
                .ifPresent(allowSqlTypes -> {
                    String sqlType = allowSqlTypes.get(0);
                    sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
                    assertThat(session.getUserDataMap().get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED)).isFalse();

                    AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
                    when(sqlTuples.get(0).getAst()).thenReturn(ast);
                    when(ast.getParseResult().getSqlType()).thenReturn(SqlType.valueOf(sqlType));
                    sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
                    if (!sqlType.equals("DDL")) {
                        assertThat(session.getUserDataMap().get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED)).isFalse();
                    } else {
                        sqlConsoleRuleService.getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName())
                                .ifPresent(rule -> {
                                    sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
                                    RuleViolation violation = session.getUserDataMap().get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED);
                                    assertThat(violation).isNotNull();
                                    assertThat(violation.getLevel()).isGreaterThanOrEqualTo(rule.getLevel());
                                });
                    }
                });
    }

    @Test
    public void testHandleWithError() {
        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(sqlTuples.get(0).getAst()).thenReturn(ast);
        when(ast.getParseResult().getSqlType()).thenThrow(new SyntaxErrorException("Syntax error"));
        sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
        assertThat(session.getUserDataMap().get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED)).isNull();
    }

    @Test
    public void testDoAfterCompletionWithEmptyList() {
        when(response.getSqls()).thenReturn(Collections.emptyList());
        sqlConsoleInterceptor.doAfterCompletion(null, session, Collections.emptyMap());
    }

    @Test
    public void testDoAfterCompletionWithSqlExecuteResult() {
        SqlExecuteResult executeResult = mock(SqlExecuteResult.class);
        when(response.getResultSetMetaData()).thenReturn(executeResult);
        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, Collections.emptyMap());

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(false);
        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, Collections.emptyMap());
        assertThat(executeResult.isAllowExport()).isTrue();

        sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session).thenReturn(true);
        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, Collections.emptyMap());
        assertThat(executeResult.isAllowExport()).isFalse();
    }

    @Test
    public void testDoAfterCompletionWithException() {
        sqlConsoleInterceptor.doAfterCompletion(null, session, Collections.emptyMap());
    }
}