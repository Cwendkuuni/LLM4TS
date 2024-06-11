package com.oceanbase.odc.service.session.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    private ConnectionSession session;
    private SqlConsoleInterceptor interceptor;

    @BeforeAll
    public void setup() {
        session = Mockito.mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getRuleSetId()).thenReturn(1L);
        interceptor = new SqlConsoleInterceptor();
        interceptor.setAuthenticationFacade(authenticationFacade);
        interceptor.setRuleService(ruleService);
        interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_allowed_sql() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("SELECT 1", 1L), new SqlTuple("SELECT 2", 2L));
        response.setSqls(new ArrayList<>(sqlTuples));

        Map<String, Object> context = new HashMap<>();
        boolean sqlConsoleIntercepted = interceptor.doPreHandle(request, response, session, context);
        assertTrue(sqlConsoleIntercepted);
    }

    @Test
    public void testDoPreHandle_forbidden_sql() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("CREATE TABLE test (id int)", 1L),
                new SqlTuple("SELECT 1", 2L));
        response.setSqls(new ArrayList<>(sqlTuples));

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(true);

        Map<String, Object> context = new HashMap<>();
        boolean sqlConsoleIntercepted = interceptor.doPreHandle(request, response, session, context);
        assertFalse(sqlConsoleIntercepted);
    }

    @Test
    public void testDoPreHandle_maxExecuteSqls_exceeded() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("SELECT 1", 1L), new SqlTuple("SELECT 2", 1L),
                new SqlTuple("SELECT 3", 1L), new SqlTuple("SELECT 4", 1L), new SqlTuple("SELECT 5", 1L));
        response.setSqls(new ArrayList<>(sqlTuples));

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL,
                Integer.class)).thenReturn(3);

        Map<String, Object> context = new HashMap<>();
        boolean sqlConsoleIntercepted = interceptor.doPreHandle(request, response, session, context);
        assertFalse(sqlConsoleIntercepted);
    }

    @Test
    public void testDoPreHandle_maxReturnRows_exceeded() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQueryLimit(100);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("SELECT * FROM test limit 101", 1L));
        response.setSqls(new ArrayList<>(sqlTuples));

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class)).thenReturn(50);

        Map<String, Object> context = new HashMap<>();
        boolean sqlConsoleIntercepted = interceptor.doPreHandle(request, response, session, context);
        assertFalse(sqlConsoleIntercepted);
    }

    @Test
    public void testDoPreHandle_allowSqlTypes_not_allowed() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("SELECT 1", 1L), new SqlTuple("CREATE PROCEDURE p()", 2L));
        response.setSqls(new ArrayList<>(sqlTuples));

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Arrays.asList("SELECT"));

        Map<String, Object> context = new HashMap<>();
        boolean sqlConsoleIntercepted = interceptor.doPreHandle(request, response, session, context);
        assertFalse(sqlConsoleIntercepted);
    }

    @Test
    public void testDoPreHandle_sqlType_error() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("--", 1L));
        response.setSqls(new ArrayList<>(sqlTuples));

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Arrays.asList("SELECT"));

        Map<String, Object> context = new HashMap<>();
        Executable executable = () -> interceptor.doPreHandle(request, response, session, context);

        assertThrows(RuntimeException.class, executable);
    }

    @Test
    public void testDoAfterCompletion_allowed_export() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);

        interceptor.doAfterCompletion(result, session, new HashMap<>());

        assertTrue(result.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_not_allowed_export() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session))
                .thenReturn(true);

        interceptor.doAfterCompletion(result, session, new HashMap<>());

        assertFalse(result.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_editable_resultSet() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        result.setResultSetMetaData(new ResultSetMetaData() {
            @Override
            public boolean isAutoCommit() throws SQLException {
                return false;
            }
        });

        interceptor.doAfterCompletion(result, session, new HashMap<>());

        assertFalse(result.getResultSetMetaData().isEditable());
    }

    @Test
    public void testDoAfterCompletion_not_editable_resultSet() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        result.setResultSetMetaData(new ResultSetMetaData() {
            @Override
            public boolean isAutoCommit() throws SQLException {
                return true;
            }
        });

        interceptor.doAfterCompletion(result, session, new HashMap<>());

        assertTrue(result.getResultSetMetaData().isEditable());
    }
}