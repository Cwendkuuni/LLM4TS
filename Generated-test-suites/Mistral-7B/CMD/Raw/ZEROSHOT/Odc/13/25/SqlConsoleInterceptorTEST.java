import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.session.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.CurrentUser;
import com.oceanbase.odc.service.iam.auth.User;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlCheckInterceptor;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

/**
 * ClassName: SqlConsoleInterceptorTest <br/>
 * Description: <br/>
 */
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Mock
    private ConnectionSession session;

    @Mock
    private SqlAsyncExecuteResp sqlAsyncExecuteResp;

    @Mock
    private SqlExecuteResult sqlExecuteResult;

    @Mock
    private CurrentUser currentUser;

    @Mock
    private User user;

    @Mock
    private SqlTuple sqlTuple;

    @Mock
    private AbstractSyntaxTree abstractSyntaxTree;

    @Mock
    private BasicResult basicResult;

    private final static int RULE_SET_ID = 1L;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(authenticationFacade.currentUser()).thenReturn(currentUser);
        when(user.getOrganizationType()).thenReturn(OrganizationType.CORPORATE);
        when(session.getRuleSetId()).thenReturn(RULE_SET_ID);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sqlConsoleRuleService.getListProperties(RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL, String.class))
                .thenReturn(Arrays.asList("DDL"));
        when(sqlConsoleRuleService.getProperties(RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(1));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getProperties(RULE_SET_ID, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(100));
        when(sqlConsoleRuleService.getByRulesetIdAndName(RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(ruleService);
        when(ruleService.getByRulesetIdAndName(RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(ruleService);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(false);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(false);
    }

    @Test
    public void testHandle_returnFalse_whenRuleSetIsNull() {
        when(session.getRuleSetId()).thenReturn(null);
        assertFalse(sqlConsoleInterceptor.handle(mock(SqlAsyncExecuteReq.class), mock(SqlAsyncExecuteResp.class), session,
                mock(Map.class)));
    }

    @Test
    public void testHandle_returnFalse_whenNeedSqlConsoleCheckIsFalse() {
        when(session.getRuleSetId()).thenReturn(RULE_SET_ID);
        when(context.get(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED)).thenReturn(Boolean.FALSE);
        assertFalse(sqlConsoleInterceptor.handle(mock(SqlAsyncExecuteReq.class), mock(SqlAsyncExecuteResp.class), session,
                mock(Map.class)));
    }

    @Test
    public void testHandle_returnTrue_whenQueryLimitIsNotSetOrLessThanLimit() {
        when(session.getRuleSetId()).thenReturn(RULE_SET_ID);
        when(context.get(NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.getProperties(RULE_SET_ID, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(10));
        when(request.getQueryLimit()).thenReturn(10);

        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = mock(Map.class);

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_returnFalse_whenMaxSqlSizeIsExceeded() {
        when(session.getRuleSetId()).thenReturn(RULE_SET_ID);
        when(context.get(NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.getProperties(RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(1));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(sqlTuple);

        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        response.setSqls(sqlTuples);

        Map<String, Object> context = mock(Map.class);

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_returnFalse_whenForbiddenToCreatePl() throws SQLException {
        when(session.getRuleSetId()).thenReturn(RULE_SET_ID);
        when(context.get(NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);
        when(sqlTuple.getSqlId()).thenReturn(1);
        when(sqlTuple.getAst()).thenReturn(abstractSyntaxTree);
        when(abstractSyntaxTree.getParseResult().isPlDdl()).thenReturn(true);

        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        response.setSqls(Arrays.asList(sqlTuple));

        Map<String, Object> context = mock(Map.class);

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_returnFalse_whenForbiddenToExecuteSqlType() throws SQLException {
        when(session.getRuleSetId()).thenReturn(RULE_SET_ID);
        when(context.get(NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getProperties(RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL, String.class))
                .thenReturn(Arrays.asList("DDL"));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(sqlTuple);

        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        response.setSqls(sqlTuples);

        Map<String, Object> context = mock(Map.class);

        when(sqlTuple.getSqlId()).thenReturn(1);
        when(sqlTuple.getAst()).thenReturn(abstractSyntaxTree);
        when(abstractSyntaxTree.getParseResult().getSqlType()).thenReturn(SqlType.UNKNOWN);

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_returnTrue_whenExecuteSqlTypeIsAllowed() throws SQLException {
        when(session.getRuleSetId()).thenReturn(RULE_SET_ID);
        when(context.get(NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getProperties(RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL, String.class))
                .thenReturn(Arrays.asList("DDL"));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(sqlTuple);

        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        response.setSqls(sqlTuples);

        Map<String, Object> context = mock(Map.class);

        when(sqlTuple.getSqlId()).thenReturn(1);
        when(sqlTuple.getAst()).thenReturn(abstractSyntaxTree);
        when(abstractSyntaxTree.getParseResult().getSqlType()).thenReturn(SqlType.DDL);

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testGetExecuteStageName_returnSqlConsoleRule() {
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testDoAfterCompletion_allowExport_whenNotForbidden() {
        when(session.getRuleSetId()).thenReturn(RULE_SET_ID);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(false);

        SqlExecuteResult sqlExecuteResult = mock(SqlExecuteResult.class);
        sqlExecuteResult.setAllowExport(true);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, mock(Map.class));

        assertTrue(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_forbiddenToEditResultSet_whenForbidden() {
        when(session.getRuleSetId()).thenReturn(RULE_SET_ID);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);

        SqlExecuteResult sqlExecuteResult = mock(SqlExecuteResult.class);
        sqlExecuteResult.setResultSetMetaData(mock(javax.sql.ResultSetMetaData.class));

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, mock(Map.class));

        assertFalse(sqlExecuteResult.isResultSetEditable());
    }

    @Test
    public void testDetermineSqlType_returnBasicResult() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        when(sqlTuple.getOriginalSql()).thenReturn("select * from test where id = 1");
        when(sqlTuple.getSqlId()).thenReturn(1L);
        when(sqlTuple.getAst()).thenReturn(abstractSyntaxTree);

        when(abstractSyntaxTree.getParseResult().getSyntaxError()).thenReturn(false);
        when(abstractSyntaxTree.getParseResult().getSqlType()).thenReturn(SqlType.DDL);

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        assertEquals(SqlType.DDL, basicResult.getSqlType());
    }

    @Test
    public void testDetermineSqlType_returnBasicResultWithSyntaxError() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        when(sqlTuple.getOriginalSql()).thenReturn("select * from test where id = 1 select * from test");
        when(sqlTuple.getSqlId()).thenReturn(1L);
        when(sqlTuple.getAst()).thenReturn(abstractSyntaxTree);

        when(abstractSyntaxTree.getParseResult().getSyntaxError()).thenReturn(true);

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        assertEquals(SqlType.UNKNOWN, basicResult.getSqlType());
        assertTrue(basicResult.isSyntaxError());
    }

}