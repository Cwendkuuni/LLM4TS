import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.parse.AbstractSyntaxTree;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor interceptor;

    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        interceptor.setAuthenticationFacade(authenticationFacade);
        interceptor.setRuleService(ruleService);
        interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_whenSessionIsNull_thenReturnTrue() {
        ConnectionSession session = null;
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();

        boolean result = interceptor.doPreHandle(request, response, session, context);
        Assert.assertTrue(result);
    }

    @Test
    public void testDoPreHandle_whenRuleSetIdIsNull_thenReturnTrue() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        when(session.getRuleSetId()).thenReturn(null);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();

        boolean result = interceptor.doPreHandle(request, response, session, context);
        Assert.assertTrue(result);
    }

    @Test
    public void testDoPreHandle_whenNeedSqlConsoleCheckIsFalse_thenReturnTrue() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        when(session.getRuleSetId()).thenReturn(1L);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);

        boolean result = interceptor.doPreHandle(request, response, session, context);
        Assert.assertTrue(result);
    }

    @Test
    public void testHandle_whenRuleSetIdIsNull_thenReturnTrue() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        boolean result = interceptor.handle(request, response, session, context);
        Assert.assertTrue(result);
    }

    @Test
    public void testHandle_whenRuleSetIdIsPresent_thenInvokeRuleService() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        when(session.getRuleSetId()).thenReturn(1L);

        interceptor.handle(request, response, session, context);

        Mockito.verify(ruleService, Mockito.times(1)).getByRulesetIdAndName(1L,
                SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
    }

    @Test
    public void testDoAfterCompletion_whenStatusIsNotSuccess_thenReturn() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        interceptor.doAfterCompletion(response, session, context);
    }

    @Test
    public void testDoAfterCompletion_whenRuleSetIdIsNull_thenAllowExportIsTrue() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        when(session.getRuleSetId()).thenReturn(null);

        interceptor.doAfterCompletion(response, session, context);
        Assert.assertTrue(response.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam_whenOrganizationTypeIsIndividual_thenReturnTrue() {
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.INDIVIDUAL);

        boolean result = interceptor.isIndividualTeam();
        Assert.assertTrue(result);
    }

    @Test
    public void testIsIndividualTeam_whenOrganizationTypeIsNotIndividual_thenReturnFalse() {
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.ENTERPRISE);

        boolean result = interceptor.isIndividualTeam();
        Assert.assertFalse(result);
    }

    @Test(expected = AssertionError.class)
    public void testDetermineSqlType_whenAstIsNull_thenThrowAssertionError() {
        SqlTuple sqlTuple = new SqlTuple();
        DialectType dialectType = DialectType.MYSQL;
        interceptor.determineSqlType(sqlTuple, dialectType);
    }

    @Test
    public void testDetermineSqlType_whenSqlTypeIsUnknown_thenSetSqlTypeToOthers() {
        SqlTuple sqlTuple = new SqlTuple();
        DialectType dialectType = DialectType.MYSQL;
        interceptor.determineSqlType(sqlTuple, dialectType);

        Assert.assertEquals(SqlType.OTHERS, sqlTuple.getAst().getParseResult().getSqlType());
    }

    @Test
    public void testGetExecuteStageName_whenCalled_thenReturnSqlConsoleRule() {
        String result = interceptor.getExecuteStageName();
        Assert.assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, result);
    }

    @Test
    public void testGetOrder_whenCalled_thenReturn2() {
        int result = interceptor.getOrder();
        Assert.assertEquals(2, result);
    }
}