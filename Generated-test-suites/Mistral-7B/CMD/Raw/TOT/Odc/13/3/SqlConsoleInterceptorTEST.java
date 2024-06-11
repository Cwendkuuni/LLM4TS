import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("JUnitTestMethodWithLeakedThreads")
public class SqlConsoleInterceptorTest {

    @MockBean
    private AuthenticationFacade authenticationFacade;

    @MockBean
    private RuleService ruleService;

    @MockBean
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Autowired
    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle_AllowExecute() {
        ConnectionSession session = mock(ConnectionSession.class);
        MockHttpSession mockHttpSession = new MockHttpSession();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq("SELECT * FROM test;", 0, mockHttpSession, null);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM test;", 1, null);
        SqlTuple sqlTupleWithViolation = new SqlTuple("SELECT * FROM test;", 1, null);
        List<SqlTuple> sqlTuples = List.of(sqlTuple, sqlTupleWithViolation);

        when(authenticationFacade.currentUser()).thenReturn(new User());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(DialectType.class), Integer.class))
                .thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(DialectType.class), String.class))
                .thenReturn(Optional.of(List.of("SELECT")));

        boolean sqlConsoleIntercepted = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());
        Assertions.assertTrue(sqlConsoleIntercepted);
    }

    @Test
    public void testDoPreHandle_NotAllowExecute_MaxSqlSize() {
        ConnectionSession session = mock(ConnectionSession.class);
        MockHttpSession mockHttpSession = new MockHttpSession();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq("SELECT * FROM test;", 0, mockHttpSession, null);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM test;", 1, null);
        SqlTuple sqlTupleWithViolation = new SqlTuple("SELECT * FROM test;", 1, null);
        List<SqlTuple> sqlTuples = List.of(sqlTuple, sqlTupleWithViolation);

        when(authenticationFacade.currentUser()).thenReturn(new User());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(DialectType.class), Integer.class))
                .thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, any(DialectType.class), Integer.class))
                .thenReturn(Optional.of(1));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(DialectType.class), String.class))
                .thenReturn(Optional.of(List.of("SELECT")));

        boolean sqlConsoleIntercepted = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());
        Assertions.assertFalse(sqlConsoleIntercepted);
    }

    @Test
    public void testDoPreHandle_NotAllowExecute_ForbiddenSqlType() {
        ConnectionSession session = mock(ConnectionSession.class);
        MockHttpSession mockHttpSession = new MockHttpSession();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq("UPDATE test SET age = 18 WHERE id = 1;", 0, mockHttpSession, null);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuple sqlTuple = new SqlTuple("UPDATE test SET age = 18 WHERE id = 1;", 1, null);
        List<SqlTuple> sqlTuples = List.of(sqlTuple);

        when(authenticationFacade.currentUser()).thenReturn(new User());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(DialectType.class), Integer.class))
                .thenReturn(Optional.empty());
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(DialectType.class), String.class))
                .thenReturn(Optional.of(List.of("SELECT, UPDATE")));

        boolean sqlConsoleIntercepted = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());
        Assertions.assertFalse(sqlConsoleIntercepted);
    }

    @Test
    public void testDoPreHandle_NotAllowExecute_SyntaxError() {
        ConnectionSession session = mock(ConnectionSession.class);
        MockHttpSession mockHttpSession = new MockHttpSession();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq("SELECT * FROM test FROM test;", 0, mockHttpSession, null);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM test FROM test;", 1, null);

        when(authenticationFacade.currentUser()).thenReturn(new User());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(DialectType.class), Integer.class))
                .thenReturn(Optional.empty());
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(DialectType.class), String.class))
                .thenReturn(Optional.of(List.of("SELECT")));
        when(sqlTuple.getAst()).thenThrow(new SyntaxErrorException());

        boolean sqlConsoleIntercepted = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());
        Assertions.assertFalse(sqlConsoleIntercepted);
    }

    @Test
    public void testDoAfterCompletion_AllowExport() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult(new SqlTuple("SELECT * FROM test;", 1, null),
                SqlExecuteStatus.SUCCESS, null, null);
        ConnectionSession session = mock(ConnectionSession.class);

        when(authenticationFacade.currentUser()).thenReturn(new User());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, new HashMap<>());
        Assertions.assertTrue(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_NotAllowExport() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult(new SqlTuple("SELECT * FROM test;", 1, null),
                SqlExecuteStatus.SUCCESS, null, null);
        ConnectionSession session = mock(ConnectionSession.class);

        when(authenticationFacade.currentUser()).thenReturn(new User());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, new HashMap<>());
        Assertions.assertFalse(sqlExecuteResult.isAllowExport());
    }

    private static class User {
        private OrganizationType organizationType = OrganizationType.TEAM;
    }
}