import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedFieldUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private RuleService ruleService;
    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AuthenticationFacade authenticationFacadeMock = mock(AuthenticationFacade.class);
        when(authenticationFacade.currentUser()).thenReturn(authenticationFacadeMock);
        AutowiredAnnotatedFieldUtils.forReadOnlyField(sqlConsoleInterceptor, "authenticationFacade")
                .setBean(authenticationFacadeMock);
        AutowiredAnnotatedFieldUtils.forReadOnlyField(sqlConsoleInterceptor, "ruleService")
                .setBean(ruleService);
        AutowiredAnnotatedFieldUtils.forReadOnlyField(sqlConsoleInterceptor, "sqlConsoleRuleService")
                .setBean(sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_AllowExecution() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        boolean result = sqlConsoleInterceptor.doPreHandle(mock(SqlAsyncExecuteReq.class), response, session, context);

        Assert.assertTrue(result);
    }

    @Test
    public void testDoPreHandle_ForbidExecutionWithIndividualTeam() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        boolean result = sqlConsoleInterceptor.doPreHandle(mock(SqlAsyncExecuteReq.class), response, session, context);

        Assert.assertTrue(result);
    }

    @Test
    public void testDoPreHandle_ForbidExecutionWithRuleSetIdNull() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        boolean result = sqlConsoleInterceptor.doPreHandle(mock(SqlAsyncExecuteReq.class), response, session, context);

        Assert.assertTrue(result);
    }

    @Test
    public void testDoPreHandle_ForbidExecutionWithNeedSqlConsoleCheckFalse() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(false);

        boolean result = sqlConsoleInterceptor.doPreHandle(mock(SqlAsyncExecuteReq.class), response, session, context);

        Assert.assertTrue(result);
    }

    @Test(expected = AssertionError.class)
    public void testDoPreHandle_ForbidExecutionWithInvalidRuleSetId() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(-1L);

        sqlConsoleInterceptor.doPreHandle(mock(SqlAsyncExecuteReq.class), response, session, context);
    }

    @Test
    public void testHandle_AllowExecution() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL, Integer.class))
                .thenReturn(Optional.of(10));

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        Assert.assertTrue(result);
    }

    @Test
    public void testHandle_ForbidExecutionWithMaxReturnRowsExceeded() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL, Integer.class))
                .thenReturn(Optional.of(5));
        when(request.getQueryLimit()).thenReturn(10);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        Assert.assertFalse(result);
    }

    @Test
    public void testHandle_AllowExecutionWithAllowExecuteTrue() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        boolean allowExecute = true;
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL, String.class))
                .thenReturn(Optional.of(List.of("SELECT")));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1);
        sqlTuple.setOriginalSql("SELECT * FROM table1");
        sqlTuple.setSqlType(SqlType.SQL);
        sqlTuple.setOffset(0);
        sqlTuples.add(sqlTuple);
        when(response.getSqls()).thenReturn(sqlTuples);

        response.setAllowExport(true);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        Assert.assertTrue(result);
    }

    @Test
    public void testHandle_ForbidExecutionWithForbiddenCreatePl() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        boolean allowExecute = false;
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL, String.class))
                .thenReturn(Optional.of(List.of("SELECT")));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1);
        sqlTuple.setOriginalSql("CREATE PROCEDURE p1()");
        sqlTuple.setSqlType(SqlType.PLDDL);
        sqlTuple.setOffset(0);
        sqlTuples.add(sqlTuple);
        when(response.getSqls()).thenReturn(sqlTuples);

        response.setAllowExport(true);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        Assert.assertFalse(result);
    }

    @Test
    public void testHandle_ForbidExecutionWithForbiddenSqlType() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        boolean allowExecute = false;
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL, String.class))
                .thenReturn(Optional.of(List.of("SELECT")));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1);
        sqlTuple.setOriginalSql("SELECT * FROM table1 CREATE PROCEDURE p1()");
        sqlTuple.setSqlType(SqlType.PLDDL_MIXED);
        sqlTuple.setOffset(0);
        sqlTuples.add(sqlTuple);
        when(response.getSqls()).thenReturn(sqlTuples);

        response.setAllowExport(true);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        Assert.assertFalse(result);
    }

    @Test
    public void testDoAfterCompletion_AllowExport() {
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        Assert.assertTrue(response.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_ForbidExport() {
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        Assert.assertFalse(response.isAllowExport());
    }

    @Test
    public void testDetermineSqlType_SQLTypeKnown() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        DialectType dialectType = DialectType.MySQL;

        BasicResult basicResult = new BasicResult();
        basicResult.setSqlType(SqlType.SQL);

        when(sqlTuple.getAst()).thenReturn(null);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        Assert.assertEquals(basicResult, result);
    }

    @Test
    public void testDetermineSqlType_SqlTypeUnknown() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        DialectType dialectType = DialectType.MySQL;

        BasicResult basicResult = new BasicResult();
        basicResult.setSqlType(SqlType.UNKNOWN);

        when(sqlTuple.getAst()).thenReturn(null);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        Assert.assertEquals(basicResult, result);
    }

    @Test
    public void testDetermineSqlType_SyntaxError() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        DialectType dialectType = DialectType.MySQL;

        BasicResult basicResult = new BasicResult();
        basicResult.setSyntaxError(true);

        when(sqlTuple.getAst()).thenThrow(new SyntaxErrorException());

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        Assert.assertEquals(basicResult, result);
    }

    @Test
    public void testDetermineSqlType_SqlTypeOther() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        DialectType dialectType = DialectType.MySQL;

        BasicResult basicResult = new BasicResult();
        basicResult.setSqlType(SqlType.OTHERS);

        when(sqlTuple.getAst()).thenReturn(null);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        Assert.assertEquals(basicResult, result);
    }
}