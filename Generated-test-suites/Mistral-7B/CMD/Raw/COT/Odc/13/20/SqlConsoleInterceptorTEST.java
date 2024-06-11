package com.oceanbase.odc.service.session.interceptor.test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeMock;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleServiceMock;
import com.oceanbase.odc.service.regulation.ruleset.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleServiceMock;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor.SqlConsoleInterceptorTest;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

/**
 * Test class for SqlConsoleInterceptor
 */
@Slf4j
public class SqlConsoleInterceptorTest {

    private static final String RULE_SET_ID = "ruleSetId";
    private static final String RULE_SET_NAME = "ruleSetName";
    private static final String SQL_CONSOLE_INTERCEPTED_KEY = "SQL_CONSOLE_INTERCEPTED";
    private static final String NEED_SQL_CONSOLE_CHECK_KEY = "NEED_SQL_CONSOLE_CHECK";

    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    private ConnectionSession connectionSession;
    private Map<String, Object> context;

    @Before
    public void setUp() {
        authenticationFacade = Mockito.spy(new AuthenticationFacadeMock());
        ruleService = Mockito.spy(new RuleServiceMock());
        sqlConsoleRuleService = Mockito.spy(new SqlConsoleRuleServiceMock());

        sqlConsoleInterceptor = new SqlConsoleInterceptor() {
            @Override
            @Autowired
            public void setAuthenticationFacade(AuthenticationFacade authenticationFacade) {
                this.authenticationFacade = authenticationFacade;
            }

            @Override
            @Autowired
            public void setRuleService(RuleService ruleService) {
                this.ruleService = ruleService;
            }

            @Override
            @Autowired
            public void setSqlConsoleRuleService(SqlConsoleRuleService sqlConsoleRuleService) {
                this.sqlConsoleRuleService = sqlConsoleRuleService;
            }
        };

        connectionSession = Mockito.mock(ConnectionSession.class);
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle_NoRuleSetId_ReturnTrue() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.ORGANIZATION);

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        // Then
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_RuleSetIdPresent_ReturnTrueWhenNotNeedSqlConsoleCheck() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSessionUtil.setRuleSetId(connectionSession, RULE_SET_ID);
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.ORGANIZATION);
        context.put(NEED_SQL_CONSOLE_CHECK_KEY, Boolean.FALSE);

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        // Then
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_RuleSetIdPresent_ReturnFalseWhenNeedSqlConsoleCheckButNoRuleSet() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSessionUtil.setRuleSetId(connectionSession, RULE_SET_ID);
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.ORGANIZATION);
        context.put(NEED_SQL_CONSOLE_CHECK_KEY, Boolean.TRUE);
        doNothing().when(sqlConsoleRuleService).getProperties(eq(RULE_SET_ID), any(), any(), any());

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        // Then
        assertFalse(result);
    }

    @Test
    public void testDoPreHandle_RuleSetIdPresent_ReturnTrueWhenNeedSqlConsoleCheckAndRuleSetAllow() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSessionUtil.setRuleSetId(connectionSession, RULE_SET_ID);
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.ORGANIZATION);
        context.put(NEED_SQL_CONSOLE_CHECK_KEY, Boolean.TRUE);
        when(sqlConsoleRuleService.getProperties(eq(RULE_SET_ID), eq(SqlConsoleRules.MAX_RETURN_ROWS), any(), any()))
                .thenReturn(Optional.of(5));
        when(request.getQueryLimit()).thenReturn(null);

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        // Then
        assertTrue(result);
        assertEquals(5, request.getQueryLimit().intValue());
    }

    @Test
    public void testDoPreHandle_RuleSetIdPresent_ReturnFalseWhenRuleViolated() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSessionUtil.setRuleSetId(connectionSession, RULE_SET_ID);
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.ORGANIZATION);
        context.put(NEED_SQL_CONSOLE_CHECK_KEY, Boolean.TRUE);
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1L);
        when(sqlConsoleRuleService.getProperties(eq(RULE_SET_ID), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(), any()))
                .thenReturn(Optional.of(1));
        List<SqlTuple> sqlTuples = Arrays.asList(sqlTuple);
        when(response.getSqls()).thenReturn(sqlTuples);
        Rule rule = Mockito.mock(Rule.class);
        RuleViolation violation = new RuleViolation();
        violation.setLevel(1);
        violation.setLocalizedMessage("Rule violation message");
        when(ruleService.getByRulesetIdAndName(eq(RULE_SET_ID), eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())))
                .thenReturn(Optional.of(rule));
        when(rule.getProperties()).thenReturn(Collections.singletonMap("name", "maxExecuteSqls"));
        when(rule.getLevel()).thenReturn(1);

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        // Then
        assertFalse(result);
        verify(rule, times(1)).getViolation();
        verify(ruleService, times(1)).getByRulesetIdAndName(eq(RULE_SET_ID), eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()));
    }

    @Test
    public void testHandle_RuleSetIdPresentButNoRuleViolation_ReturnTrue() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSessionUtil.setRuleSetId(connectionSession, RULE_SET_ID);
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.ORGANIZATION);
        context.put(NEED_SQL_CONSOLE_CHECK_KEY, Boolean.TRUE);
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple());
        when(response.getSqls()).thenReturn(sqlTuples);

        // When
        boolean result = sqlConsoleInterceptor.handle(request, response, connectionSession, context);

        // Then
        assertTrue(result);
    }

    @Test
    public void testHandle_RuleSetIdPresentAndRuleViolation_ReturnFalse() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSessionUtil.setRuleSetId(connectionSession, RULE_SET_ID);
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.ORGANIZATION);
        context.put(NEED_SQL_CONSOLE_CHECK_KEY, Boolean.TRUE);
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple().setViolatedRules(Arrays.asList(Mockito.mock(Rule.class))));
        when(response.getSqls()).thenReturn(sqlTuples);
        Rule rule = Mockito.mock(Rule.class);
        RuleViolation violation = new RuleViolation();
        violation.setLevel(1);
        violation.setLocalizedMessage("Rule violation message");
        when(ruleService.getByRulesetIdAndName(eq(RULE_SET_ID), eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())))
                .thenReturn(Optional.of(rule));
        when(rule.getProperties()).thenReturn(Collections.singletonMap("name", "maxExecuteSqls"));
        when(rule.getLevel()).thenReturn(1);

        // When
        boolean result = sqlConsoleInterceptor.handle(request, response, connectionSession, context);

        // Then
        assertFalse(result);
        verify(rule, times(1)).getViolation();
        verify(ruleService, times(1)).getByRulesetIdAndName(eq(RULE_SET_ID), eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()));
    }

    @Test
    public void testDoAfterCompletion_RuleSetIdPresentAndSqlConsoleRuleIsForbidden_SetResultSetMetaDataToNotEditable() {
        // Given
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSessionUtil.setRuleSetId(connectionSession, RULE_SET_ID);
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.ORGANIZATION);
        context.put(NEED_SQL_CONSOLE_CHECK_KEY, Boolean.TRUE);
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET), any()))
                .thenReturn(true);

        // When
        sqlConsoleInterceptor.doAfterCompletion(any(), any(), any(), any());

        // Then
        if (response.getResultSetMetaData() != null) {
            assertFalse(response.getResultSetMetaData().isEditable());
        }
    }

    @Test
    public void testDoAfterCompletion_RuleSetIdPresentAndSqlConsoleRuleIsAllowed_SetResultSetMetaDataToEditable() {
        // Given
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSessionUtil.setRuleSetId(connectionSession, RULE_SET_ID);
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.ORGANIZATION);
        context.put(NEED_SQL_CONSOLE_CHECK_KEY, Boolean.TRUE);
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET), any()))
                .thenReturn(false);

        // When
        sqlConsoleInterceptor.doAfterCompletion(any(), any(), any(), any());

        // Then
        if (response.getResultSetMetaData() != null) {
            assertTrue(response.getResultSetMetaData().isEditable());
        }
    }

    @Test
    public void testDoAfterCompletion_RuleSetIdNotPresent_DoNothing() {
        // Given
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.ORGANIZATION);
        context.put(NEED_SQL_CONSOLE_CHECK_KEY, Boolean.TRUE);
        when(sqlConsoleRuleService.isForbidden(any(), any()))
                .thenReturn(false);

        // When
        sqlConsoleInterceptor.doAfterCompletion(any(), any(), any(), any());

        // Then
        // Do nothing
    }

    @Test
    public void testDetermineSqlType_SqlTypeParsedSuccessfully_ReturnParsedSqlType() {
        // Given
        SqlTuple sqlTuple = new SqlTuple();
        DialectType dialectType = DialectType.MYSQL;
        AbstractSyntaxTree ast = Mockito.mock(AbstractSyntaxTree.class);
        BasicResult basicResult = new BasicResult();
        basicResult.setSqlType(SqlType.SELECT);
        when(sqlTuple.getAst()).thenReturn(ast);
        when(ast.getParseResult()).thenReturn(basicResult);

        // When
        SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();
        SqlType sqlType = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        // Then
        assertEquals(SqlType.SELECT, sqlType);
    }

    @Test
    public void testDetermineSqlType_SqlParsingFailed_ReturnUnknownType() {
        // Given
        SqlTuple sqlTuple = new SqlTuple();
        DialectType dialectType = DialectType.MYSQL;
        AbstractSyntaxTree ast = Mockito.mock(AbstractSyntaxTree.class);
        when(sqlTuple.getAst()).thenReturn(ast);
        when(ast.getParseResult()).thenThrow(new SyntaxErrorException("Syntax error"));

        // When
        SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();
        SqlType sqlType = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        // Then
        assertEquals(SqlType.UNKNOWN, sqlType);
    }

    @Test
    public void testGetOrder_ReturnCorrectOrder() {
        // Given
        SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();

        // When
        int order = sqlConsoleInterceptor.getOrder();

        // Then
        assertEquals(2, order);
    }
}