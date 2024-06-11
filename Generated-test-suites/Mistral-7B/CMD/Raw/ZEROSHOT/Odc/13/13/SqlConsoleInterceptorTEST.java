package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedField;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OrganizationType;
import com.oceanbase.odc.core.sql.execute.model.RuleViolation;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

public class SqlConsoleInterceptorTest {

    private static final long RULE_SET_ID = 1L;
    private static final OrganizationType ORGANIZATION_TYPE_TEAM = OrganizationType.TEAM;
    private static final OrganizationType ORGANIZATION_TYPE_INDIVIDUAL = OrganizationType.INDIVIDUAL;

    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        sqlConsoleInterceptor = new SqlConsoleInterceptor() {
            @AutowiredAnnotatedField
            public void setAuthenticationFacade(AuthenticationFacade authenticationFacade) {
                this.authenticationFacade = authenticationFacade;
            }

            @AutowiredAnnotatedField
            public void setRuleService(RuleService ruleService) {
                this.ruleService = ruleService;
            }

            @AutowiredAnnotatedField
            public void setSqlConsoleRuleService(SqlConsoleRuleService sqlConsoleRuleService) {
                this.sqlConsoleRuleService = sqlConsoleRuleService;
            }
        };
        when(authenticationFacade.currentUser()).thenReturn(new Object());
    }

    @Test
    public void testDoPreHandle_noIntercept() throws Exception {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(sqlConsoleRuleService.getProperties(anyLong(), any(SqlConsoleRules.class), any(DialectType.class),
                any(Class.class))).thenReturn(Optional.empty());

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_intercept() throws Exception {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(sqlConsoleRuleService.getProperties(anyLong(), any(SqlConsoleRules.class), any(DialectType.class),
                any(Class.class))).thenReturn(Optional.of(1));

        when(sqlConsoleRuleService.getListProperties(anyLong(), any(SqlConsoleRules.class), any(DialectType.class),
                any(Class.class))).thenReturn(Optional.of(Collections.singletonList("DDL")));

        when(ruleService.getByRulesetIdAndName(anyLong(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))).thenReturn(
                Optional.of(new Rule()));

        doReturn(new RuleViolation()).when(ruleService).getByRulesetIdAndName(anyLong(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS
                .getRuleName()));

        ReflectionTestUtils.setFieldValue(sqlConsoleInterceptor, "maxExecuteSqlsRule", new Rule());

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testHandle_noViolation() throws Exception {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);

        SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor() {
            @AutowiredAnnotatedField
            public void setSqlConsoleRuleService(SqlConsoleRuleService sqlConsoleRuleService) {
                this.sqlConsoleRuleService = sqlConsoleRuleService;
            }
        };

        when(sqlConsoleRuleService.getProperties(anyLong(), any(SqlConsoleRules.class), any(DialectType.class),
                any(Class.class))).thenReturn(Optional.empty());

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple(1, "SELECT * FROM table1", null, null, null, null, null, null, null, null, null,
                1, null, null);
        sqlTuples.add(sqlTuple);

        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(sqlTuple.getAst()).thenReturn(ast);
        when(ast.getParseResult()).thenReturn(new BasicResult(SqlType.SELECT));

        when(sqlConsoleRuleService.isForbidden(anyString(), eq(session))).thenReturn(false);

        assertNull(sqlConsoleInterceptor.handle(request, response, session, context, sqlTuples));
    }

    @Test
    public void testHandle_violation() throws Exception {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);

        SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor() {
            @AutowiredAnnotatedField
            public void setSqlConsoleRuleService(SqlConsoleRuleService sqlConsoleRuleService) {
                this.sqlConsoleRuleService = sqlConsoleRuleService;
            }
        };

        when(sqlConsoleRuleService.getProperties(anyLong(), any(SqlConsoleRules.class), any(DialectType.class),
                any(Class.class))).thenReturn(Optional.empty());

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple(1, "CREATE TABLE table1 (id INT PRIMARY KEY)", null, null, null, null, null, null,
                null, null, null, 1, null, null);
        sqlTuples.add(sqlTuple);

        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(sqlTuple.getAst()).thenReturn(ast);
        when(ast.getParseResult()).thenReturn(new BasicResult(SqlType.CREATE_TABLE));

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, eq(session))).thenReturn(true);

        RuleViolation violation = new RuleViolation();
        violation.setLevel(1);
        violation.setLocalizedMessage("Forbidden create pl sql");

        Rule rule = new Rule();
        rule.setViolation(violation);

        when(ruleService.getByRulesetIdAndName(anyLong(), eq(SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName()))).thenReturn(
                Optional.of(rule));

        Optional<Rule> ruleOptional = sqlConsoleInterceptor.handle(request, response, session, context, sqlTuples);
        assertNotNull(ruleOptional.get());
    }

    @Test
    public void testDoAfterCompletion_allowExport() throws Exception {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        sqlExecuteResult.setAllowExport(true);

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, eq(session)))
                .thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);

        verify(sqlExecuteResult).setAllowExport(true);
    }

    @Test
    public void testDoAfterCompletion_noAllowExport() throws Exception {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        sqlExecuteResult.setAllowExport(true);

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, eq(session)))
                .thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);

        verify(sqlExecuteResult).setAllowExport(false);
    }

    @Test
    public void testIsIndividualTeam() throws Exception {
        assertFalse(sqlConsoleInterceptor.isIndividualTeam());

        ReflectionTestUtils.setFieldValue(sqlConsoleInterceptor, "authenticationFacade", new Object() {
            @AutowiredAnnotatedField
            public void setOrganizationType(OrganizationType organizationType) {
                this.organizationType = organizationType;
            }

            OrganizationType organizationType;
        });

        assertEquals(ORGANIZATION_TYPE_INDIVIDUAL, sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test(expected = SQLException.class)
    public void testDetermineSqlType_parseError() throws Exception {
        SqlTuple sqlTuple = new SqlTuple(1, "SELECT * FROM table1 OR 1 = 1;", null, null, null, null, null, null, null,
                null, null, null, 1, null, null);

        SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor() {
            @AutowiredAnnotatedField
            public void setSqlConsoleRuleService(SqlConsoleRuleService sqlConsoleRuleService) {
                this.sqlConsoleRuleService = sqlConsoleRuleService;
            }
        };

        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(sqlTuple.getAst()).thenReturn(ast);
        when(ast.getParseResult()).thenThrow(new SyntaxErrorException("Syntax error"));

        sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL);
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }
}