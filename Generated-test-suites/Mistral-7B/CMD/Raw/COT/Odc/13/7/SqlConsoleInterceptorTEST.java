import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.annotation.Order;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResp;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @Mock
    private ConnectionSession session;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private RuleService ruleService;
    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @InjectMocks
    private SqlConsoleInterceptor interceptor;

    private ConnectionSessionUtil connectionSessionUtil;

    @Before
    public void setup() {
        connectionSessionUtil = new ConnectionSessionUtil();
    }

    @Test
    @Order(1)
    public void testDoPreHandle_true() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        when(interceptor.handle(any(SqlAsyncExecuteReq.class), any(SqlAsyncExecuteResp.class), any(ConnectionSession.class),
                anyMap())).thenReturn(true);

        Assert.assertTrue(interceptor.doPreHandle(request, response, session, new HashMap<>()));
    }

    @Test
    @Order(2)
    public void testDoPreHandle_false() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        when(interceptor.handle(any(SqlAsyncExecuteReq.class), any(SqlAsyncExecuteResp.class), any(ConnectionSession.class),
                anyMap())).thenReturn(false);

        Assert.assertFalse(interceptor.doPreHandle(request, response, session, new HashMap<>()));
    }

    @Test
    @Order(3)
    public void testHandle_true() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession context = mock(ConnectionSession.class);

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(context)).thenReturn(1L);
        when(interceptor.handle(request, response, context, new HashMap<>())).thenReturn(true);

        Assert.assertTrue(interceptor.doPreHandle(request, response, session, new HashMap<>()));
    }

    @Test(expected = NullPointerException.class)
    @Order(4)
    public void testHandle_nullSession() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        interceptor.doPreHandle(request, response, null, new HashMap<>());
    }

    @Test(expected = NullPointerException.class)
    @Order(5)
    public void testHandle_nullContext() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        interceptor.doPreHandle(request, response, session, null);
    }

    @Test
    @Order(6)
    public void testDetermineSqlType() throws Exception {
        String sql = "SELECT * FROM table1 WHERE id > 10";
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql(sql);

        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(sqlTuple.getAst()).thenReturn(ast);
        when(ast.getParseResult()).thenReturn(new BasicResult(SqlType.SQL));

        BasicResult result = interceptor.determineSqlType(sqlTuple, DialectType.MySQL);
        Assert.assertEquals(SqlType.SQL, result.getSqlType());
    }

    @Test
    @Order(7)
    public void testDetermineSqlType_unknown() throws Exception {
        String sql = "INVALID";
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql(sql);

        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(sqlTuple.getAst()).thenReturn(ast);

        BasicResult result = interceptor.determineSqlType(sqlTuple, DialectType.MySQL);
        Assert.assertEquals(SqlType.OTHERS, result.getSqlType());
    }

    @Test(expected = SyntaxErrorException.class)
    @Order(8)
    public void testDetermineSqlType_syntaxError() throws Exception {
        String sql = "SELECT * FROM table1 WHERE id > 10";
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql(sql);

        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(sqlTuple.getAst()).thenThrow(new SyntaxErrorException());

        interceptor.determineSqlType(sqlTuple, DialectType.MySQL);
    }

    @Test
    @Order(9)
    public void testDoAfterCompletion_true() {
        SqlExecuteResult result = mock(SqlExecuteResult.class);
        result.setAllowExport(true);

        interceptor.doAfterCompletion(result, session, new HashMap<>());
    }

    @Test
    @Order(10)
    public void testDoAfterCompletion_false() {
        SqlExecuteResult result = mock(SqlExecuteResult.class);
        result.setAllowExport(false);

        interceptor.doAfterCompletion(result, session, new HashMap<>());
    }

    @Test
    @Order(11)
    public void testIsIndividualTeam_true() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        Assert.assertTrue(interceptor.isIndividualTeam());
    }

    @Test
    @Order(12)
    public void testIsIndividualTeam_false() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.CORPORATE);

        Assert.assertFalse(interceptor.isIndividualTeam());
    }

    private Map<String, Object> anyMap() {
        return new HashMap<>();
    }
}