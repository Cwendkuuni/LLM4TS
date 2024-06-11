package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

/**
 * Test class for SqlConsoleInterceptor
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
    private Map<String, Object> context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle_ReturnsFalseWhenRuleSetIdIsNull() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(session.getRuleSetId()).thenReturn(null);

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_ReturnsFalseWhenIsIndividualTeam() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testHandle_ReturnsTrueWhenRuleSetIdIsNull() {
        Long ruleSetId = null;
        Optional<Integer> queryLimit = Optional.empty();
        AtomicBoolean allowExecute = new AtomicBoolean(true);
        List<SqlTuple> sqlTuples = new ArrayList<>();
        Map<String, BasicResult> sqlId2BasicResult = new HashMap<>();

        when(session.getRuleSetId()).thenReturn(ruleSetId);

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context, queryLimit, sqlTuples, sqlId2BasicResult,
                allowExecute));
    }

    @Test
    public void testHandle_ReturnsFalseWhenQueryLimitIsReached() {
        Long ruleSetId = 1L;
        Integer queryLimit = 5;
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        AtomicBoolean allowExecute = new AtomicBoolean(true);
        List<SqlTuple> sqlTuples = new ArrayList<>();
        Map<String, BasicResult> sqlId2BasicResult = new HashMap<>();

        when(session.getRuleSetId()).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, any(), Integer.class))
                .thenReturn(Optional.of(queryLimit));
        when(request.getQueryLimit()).thenReturn(queryLimit + 1);

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context, queryLimit, sqlTuples, sqlId2BasicResult,
                allowExecute));
    }

    @Test
    public void testHandle_ReturnsFalseWhenMaxSqlSizeIsReached() {
        Long ruleSetId = 1L;
        Integer queryLimit = 5;
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        AtomicBoolean allowExecute = new AtomicBoolean(true);
        List<SqlTuple> sqlTuples = new ArrayList<>();
        Map<String, BasicResult> sqlId2BasicResult = new HashMap<>();

        when(session.getRuleSetId()).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS, any(), Integer.class))
                .thenReturn(Optional.of(queryLimit));
        when(response.getSqls()).thenReturn(sqlTuples);
        when(sqlTuples.size()).thenReturn(queryLimit + 1);

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context, queryLimit, sqlTuples, sqlId2BasicResult,
                allowExecute));
    }

    @Test
    public void testHandle_ReturnsFalseWhenForbiddenToCreatePl() {
        Long ruleSetId = 1L;
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        AtomicBoolean allowExecute = new AtomicBoolean(true);
        List<SqlTuple> sqlTuples = new ArrayList<>();
        Map<String, BasicResult> sqlId2BasicResult = new HashMap<>();

        when(session.getRuleSetId()).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);
        when(response.getSqls()).thenReturn(sqlTuples);
        sqlTuples.add(new SqlTuple("create procedure p (a int)", ""));

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context, null, sqlTuples, sqlId2BasicResult,
                allowExecute));
    }

    @Test
    public void testHandle_ReturnsFalseWhenSqlTypeIsNotAllowed() {
        Long ruleSetId = 1L;
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        AtomicBoolean allowExecute = new AtomicBoolean(true);
        List<SqlTuple> sqlTuples = new ArrayList<>();
        Map<String, BasicResult> sqlId2BasicResult = new HashMap<>();

        when(session.getRuleSetId()).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, any(), String.class))
                .thenReturn(Optional.of(List.of("DDL")));
        when(response.getSqls()).thenReturn(sqlTuples);
        sqlTuples.add(new SqlTuple("SELECT * FROM table1", ""));
        BasicResult basicResult = new BasicResult(SqlType.DCL);
        sqlId2BasicResult.put("1", basicResult);

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context, null, sqlTuples, sqlId2BasicResult,
                allowExecute));
    }

    @Test
    public void testDoAfterCompletion_SetsAllowExportToFalseWhenNotAllowed() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();

        when(session.getRuleSetId()).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        assertFalse(response.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_SetsAllowExportToTrueWhenAllowed() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();

        when(session.getRuleSetId()).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        assertTrue(response.isAllowExport());
    }

    @Test
    public void testDetermineSqlType_ReturnsBasicResultWithSqlTypeDDLWhenCreateProcedureStatement()
            throws Exception {
        String sql = "CREATE PROCEDURE p (a INT)";
        AbstractSyntaxTree ast = AbstractSyntaxTreeFactories.getAstFactory(DialectType.MySQL, 0)
                .parse(sql);
        BasicResult expectedBasicResult = new BasicResult(SqlType.DDL);

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(new SqlTuple(sql, ""), DialectType.MySQL);

        assertEquals(expectedBasicResult, basicResult);
    }

    @Test
    public void testDetermineSqlType_ReturnsBasicResultWithSqlTypeDDLWhenCreateFunctionStatement()
            throws Exception {
        String sql = "CREATE FUNCTION f() RETURNS INT";
        AbstractSyntaxTree ast = AbstractSyntaxTreeFactories.getAstFactory(DialectType.MySQL, 0)
                .parse(sql);
        BasicResult expectedBasicResult = new BasicResult(SqlType.DDL);

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(new SqlTuple(sql, ""), DialectType.MySQL);

        assertEquals(expectedBasicResult, basicResult);
    }

    @Test
    public void testDetermineSqlType_ReturnsBasicResultWithSqlTypeDDLWhenCreateViewStatement()
            throws Exception {
        String sql = "CREATE VIEW v AS SELECT * FROM table1";
        AbstractSyntaxTree ast = AbstractSyntaxTreeFactories.getAstFactory(DialectType.MySQL, 0)
                .parse(sql);
        BasicResult expectedBasicResult = new BasicResult(SqlType.DDL);

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(new SqlTuple(sql, ""), DialectType.MySQL);

        assertEquals(expectedBasicResult, basicResult);
    }

    @Test
    public void testDetermineSqlType_ReturnsBasicResultWithSqlTypeDMLWhenSelectStatement()
            throws Exception {
        String sql = "SELECT * FROM table1";
        AbstractSyntaxTree ast = AbstractSyntaxTreeFactories.getAstFactory(DialectType.MySQL, 0)
                .parse(sql);
        BasicResult expectedBasicResult = new BasicResult(SqlType.DML);

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(new SqlTuple(sql, ""), DialectType.MySQL);

        assertEquals(expectedBasicResult, basicResult);
    }

    @Test
    public void testDetermineSqlType_ReturnsBasicResultWithSqlTypeDMLWhenUpdateStatement()
            throws Exception {
        String sql = "UPDATE table1 SET column1 = 1 WHERE column2 = 1";
        AbstractSyntaxTree ast = AbstractSyntaxTreeFactories.getAstFactory(DialectType.MySQL, 0)
                .parse(sql);
        BasicResult expectedBasicResult = new BasicResult(SqlType.DML);

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(new SqlTuple(sql, ""), DialectType.MySQL);

        assertEquals(expectedBasicResult, basicResult);
    }

    @Test
    public void testDetermineSqlType_ReturnsBasicResultWithSqlTypeDMLWhenDeleteStatement()
            throws Exception {
        String sql = "DELETE FROM table1 WHERE column2 = 1";
        AbstractSyntaxTree ast = AbstractSyntaxTreeFactories.getAstFactory(DialectType.MySQL, 0)
                .parse(sql);
        BasicResult expectedBasicResult = new BasicResult(SqlType.DML);

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(new SqlTuple(sql, ""), DialectType.MySQL);

        assertEquals(expectedBasicResult, basicResult);
    }

    @Test
    public void testDetermineSqlType_ReturnsBasicResultWithSqlTypeUnknownWhenInvalidStatement()
            throws Exception {
        String sql = "INVALID SQL";
        AbstractSyntaxTree ast = AbstractSyntaxTreeFactories.getAstFactory(DialectType.MySQL, 0)
                .parse(sql);
        BasicResult expectedBasicResult = new BasicResult(SqlType.UNKNOWN);

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(new SqlTuple(sql, ""), DialectType.MySQL);

        assertEquals(expectedBasicResult, basicResult);
    }

    @Test(expected = SyntaxErrorException.class)
    public void testDetermineSqlType_ThrowsSyntaxErrorExceptionWhenParseError() throws Exception {
        String sql = "INVALID SQL";
        AbstractSyntaxTree ast = null;

        sqlConsoleInterceptor.determineSqlType(new SqlTuple(sql, ""), DialectType.MySQL);
    }
}