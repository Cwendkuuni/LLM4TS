import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private ConnectionSession session;
    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        session = Mockito.mock(ConnectionSession.class);
        authenticationFacade = Mockito.mock(AuthenticationFacade.class);
        ruleService = Mockito.mock(RuleService.class);
        sqlConsoleRuleService = Mockito.mock(SqlConsoleRuleService.class);
        Mockito.when(authenticationFacade.currentUser()).thenReturn(Mockito.mock(Object.class));
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_true() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Mockito.when(sqlConsoleRuleService.getProperties(Mockito.anyLong(), Mockito.eq(SqlConsoleRules.MAX_RETURN_ROWS),
                Mockito.any(DialectType.class), Mockito.eq(Integer.class))).thenReturn(Optional.of(10));

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());
        Assert.assertTrue(result);
    }

    @Test
    public void testDoPreHandle_false() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Mockito.when(sqlConsoleRuleService.getProperties(Mockito.anyLong(), Mockito.eq(SqlConsoleRules.MAX_RETURN_ROWS),
                Mockito.any(DialectType.class), Mockito.eq(Integer.class))).thenReturn(Optional.empty());

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());
        Assert.assertFalse(result);
    }

    @Test
    public void testHandle_true() {
        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(sqlTuples);
        AtomicBoolean allowExecute = new AtomicBoolean(true);

        Mockito.when(sqlConsoleRuleService.getProperties(Mockito.anyLong(), Mockito.eq(SqlConsoleRules.MAX_EXECUTE_SQLS),
                Mockito.any(DialectType.class), Mockito.eq(Integer.class))).thenReturn(Optional.of(3));

        Optional<Rule> maxSqlSizeRule = Optional.of(Mockito.mock(Rule.class));

        RuleViolation violation = Mockito.mock(RuleViolation.class);
        Mockito.when(ruleService.getByRulesetIdAndName(Mockito.anyLong(), Mockito.eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))).thenReturn(Optional.of(Mockito.mock(Rule.class)));
        Mockito.when(ruleService.getByRulesetIdAndName(Mockito.anyLong(), Mockito.eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))).thenAnswer(new Answer<Rule>() {
            @Override
            public Rule answer(InvocationOnMock invocation) throws Throwable {
                Rule rule = Mockito.mock(Rule.class);
                rule.setViolation(violation);
                return rule;
            }
        });

        RuleViolation maxSqlSizeViolation = Mockito.mock(RuleViolation.class);
        Mockito.when(violation.setOffset(Mockito.anyInt())).then(invocation -> {
            maxSqlSizeViolation.setOffset(invocation.getArgument(0));
            return maxSqlSizeViolation;
        });
        Mockito.when(violation.setStart(Mockito.anyInt())).then(invocation -> {
            maxSqlSizeViolation.setStart(invocation.getArgument(0));
            return maxSqlSizeViolation;
        });
        Mockito.when(violation.setStop(Mockito.anyInt())).then(invocation -> {
            maxSqlSizeViolation.setStop(invocation.getArgument(0));
            return maxSqlSizeViolation;
        });

        sqlConsoleInterceptor.handle(request, response, session, new HashMap<>());
        Assert.assertFalse(allowExecute.get());
    }

    @Test(expected = AssertionError.class)
    public void testHandle_exception() {
        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(sqlTuples);

        sqlConsoleInterceptor.handle(null, response, session, new HashMap<>());
    }

    @Test
    public void testDetermineSqlType_correct() {
        SqlTuple sqlTuple = new SqlTuple();
        AbstractSyntaxTree ast = Mockito.mock(AbstractSyntaxTree.class);
        sqlTuple.setAst(ast);
        DialectType dialectType = DialectType.MYSQL;

        BasicResult basicResult = new BasicResult();
        basicResult.setSqlType(SqlType.SELECT);

        Mockito.when(ast.getParseResult()).thenReturn(basicResult);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);
        Assert.assertEquals(SqlType.SELECT, result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_unknown() {
        SqlTuple sqlTuple = new SqlTuple();
        AbstractSyntaxTree ast = Mockito.mock(AbstractSyntaxTree.class);
        sqlTuple.setAst(ast);
        DialectType dialectType = DialectType.MYSQL;

        BasicResult basicResult = new BasicResult();
        basicResult.setSqlType(null);

        Mockito.when(ast.getParseResult()).thenReturn(basicResult);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);
        Assert.assertEquals(SqlType.OTHERS, result.getSqlType());
    }

    @Test
    public void testGetExecuteStageName() {
        String result = sqlConsoleInterceptor.getExecuteStageName();
        Assert.assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, result);
    }

    @Test
    public void testDoAfterCompletion_exportAllowed() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setAllowExport(true);

        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());
    }

    @Test
    public void testDoAfterCompletion_exportForbidden() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setAllowExport(false);

        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());
    }

    @Test
    public void testIsIndividualTeam_true() {
        Mockito.when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        boolean result = sqlConsoleInterceptor.isIndividualTeam();
        Assert.assertTrue(result);
    }

    @Test
    public void testIsIndividualTeam_false() {
        Mockito.when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.ENTERPRISE);

        boolean result = sqlConsoleInterceptor.isIndividualTeam();
        Assert.assertFalse(result);
    }
}