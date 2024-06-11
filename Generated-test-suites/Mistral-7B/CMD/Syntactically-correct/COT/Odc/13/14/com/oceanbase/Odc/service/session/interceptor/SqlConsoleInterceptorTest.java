import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        when(sqlConsoleInterceptor.authenticationFacade).thenReturn(authenticationFacade);
        when(sqlConsoleInterceptor.ruleService).thenReturn(ruleService);
        when(sqlConsoleInterceptor.sqlConsoleRuleService).thenReturn(sqlConsoleRuleService);
        new AnnotatedFieldUtils().injectFields(sqlConsoleInterceptor);
    }

    @Test
    public void testDoPreHandle_True() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(DialectType.class), Integer.class))
                .thenReturn(Optional.of(10));

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        Assert.assertTrue(result);
    }

    @Test
    public void testDoPreHandle_False() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(DialectType.class), Integer.class))
                .thenReturn(Optional.empty());

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        Assert.assertFalse(result);
    }

    @Test
    public void testHandle_True() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(DialectType.class), Integer.class))
                .thenReturn(Optional.of(10));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = mock(SqlTuple.class);
        sqlTuple.setQueryLimit(15);
        sqlTuples.add(sqlTuple);
        when(response.getSqls()).thenReturn(sqlTuples);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        Assert.assertTrue(result);
    }

    @Test
    public void testHandle_False() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(DialectType.class), Integer.class))
                .thenReturn(Optional.empty());

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = mock(SqlTuple.class);
        sqlTuple.setQueryLimit(15);
        sqlTuples.add(sqlTuple);
        when(response.getSqls()).thenReturn(sqlTuples);

        Optional<Integer> queryLimit = sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                any(DialectType.class), Integer.class);
        when(queryLimit.isPresent()).thenReturn(false);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        Assert.assertFalse(result);
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);

        when(sqlTuple.getAst()).thenReturn(ast);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL);

        Assert.assertNotNull(result);
        Assert.assertEquals(SqlType.OTHERS, result.getSqlType());
    }

    @Test(expected = SyntaxErrorException.class)
    public void testDetermineSqlType_WithSyntaxError() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);

        when(sqlTuple.getAst()).thenReturn(ast);
        when(ast.getParseResult()).thenThrow(new SyntaxErrorException("Syntax error occurred"));

        sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL);
    }
}