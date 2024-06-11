package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.exception.OdcServiceException;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResp;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.core.sql.parser.SyntaxErrorException;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules.MAX_EXECUTE_SQLS;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules.MAX_RETURN_ROWS;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules.ALLOW_SQL_TYPES;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules.NOT_ALLOWED_CREATE_PL;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxError;

/**
 * ClassName: SqlConsoleInterceptorTest
 * Description: Junit test file for SqlConsoleInterceptor class
 * @author Lebie
 * @Date 2023/7/17 15:37
 */

@Slf4j
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    private ConnectionSession connectionSession;
    private User user;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        user = new User();
        connectionSession = new ConnectionSession();
        ConnectionSessionUtil.setCurrentSession(connectionSession);
        when(authenticationFacade.currentUser()).thenReturn(user);
    }

    @Test
    public void testHandle_allowExecute() {
        Long ruleSetId = 1L;
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, any(DialectType.class),
                Integer.class)).thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, any(DialectType.class),
                String.class)).thenReturn(Optional.of(List.of("DDL")));

        boolean result = sqlConsoleInterceptor.handle(request, response, connectionSession, new HashMap<>());
        assertTrue(result);
    }

    @Test
    public void testHandle_forbiddenToCreatePl() {
        Long ruleSetId = 1L;
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOffset(0);
        sqlTuple.setOriginalSql("CREATE TABLE TEST_TABLE(ID INT PRIMARY KEY)");
        sqlTuple.setSqlId("test_sql_id");
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.NOT_ALLOWED_CREATE_PL, any(DialectType.class),
                Boolean.class)).thenReturn(Optional.of(true));

        boolean result = sqlConsoleInterceptor.handle(request, response, connectionSession, new HashMap<>());
        assertFalse(result);
    }

    @Test
    public void testHandle_allowSqlTypes() {
        Long ruleSetId = 1L;
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOffset(0);
        sqlTuple.setOriginalSql("SELECT * FROM TEST_TABLE");
        sqlTuple.setSqlId("test_sql_id");
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);
        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, any(DialectType.class),
                String.class)).thenReturn(Optional.of(List.of("DDL")));

        assertThrows(OdcServiceException.class, () -> sqlConsoleInterceptor.handle(request, response, connectionSession,
                new HashMap<>()));

        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, any(DialectType.class),
                String.class)).thenReturn(Optional.of(List.of("DML")));

        boolean result = sqlConsoleInterceptor.handle(request, response, connectionSession, new HashMap<>());
        assertTrue(result);
    }

    @Test
    public void testHandle_sqlSyntaxError() {
        Long ruleSetId = 1L;
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOffset(0);
        sqlTuple.setOriginalSql("SELECT * FROM TEST_TABLE WHERE ID < 0");
        sqlTuple.setSqlId("test_sql_id");
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS, any(DialectType.class),
                Integer.class)).thenReturn(Optional.of(1));

        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, any(DialectType.class),
                String.class)).thenReturn(Optional.empty());

        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(sqlTuple.getAst()).thenReturn(ast);
        when(ast.getParseResult()).thenThrow(new SyntaxErrorException());

        assertThrows(OdcServiceException.class, () -> sqlConsoleInterceptor.handle(request, response, connectionSession,
                new HashMap<>()));
    }

    @Test
    public void testHandle_maxExecuteSqls() {
        Long ruleSetId = 1L;
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuple sqlTuple1 = new SqlTuple();
        sqlTuple1.setOffset(0);
        sqlTuple1.setOriginalSql("SELECT * FROM TEST_TABLE");
        sqlTuple1.setSqlId("test_sql_id_1");
        SqlTuple sqlTuple2 = new SqlTuple();
        sqlTuple2.setOffset(0);
        sqlTuple2.setOriginalSql("SELECT * FROM TEST_TABLE2");
        sqlTuple2.setSqlId("test_sql_id_2");
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(sqlTuple1);
        sqlTuples.add(sqlTuple2);
        response.setSqls(sqlTuples);
        when(sqlConsoleRuleService.getProperties(ruleSetId, MAX_EXECUTE_SQLS, any(DialectType.class), Integer.class))
                .thenReturn(Optional.of(1));

        boolean result = sqlConsoleInterceptor.handle(request, response, connectionSession, new HashMap<>());
        assertFalse(result);
    }

    @Test
    public void testDoAfterCompletion_notAllowExport() {
        Long ruleSetId = 1L;
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, connectionSession))
                .thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>());

        assertFalse(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_allowExport() {
        Long ruleSetId = 1L;
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, connectionSession))
                .thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>());

        assertTrue(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testDetermineSqlType_unknown() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM TEST_TABLE WHERE ID < 0");
        when(sqlTuple.getAst()).thenReturn(null);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL);

        assertEquals(SqlType.OTHERS, result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_ddl() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("CREATE TABLE TEST_TABLE(ID INT PRIMARY KEY)");
        when(sqlTuple.getAst()).thenReturn(mock(AbstractSyntaxTree.class));

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL);

        assertEquals(SqlType.DDL, result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_dml() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM TEST_TABLE WHERE ID = 1");
        when(sqlTuple.getAst()).thenReturn(mock(AbstractSyntaxTree.class));

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL);

        assertEquals(SqlType.DML, result.getSqlType());
    }

    @Test
    public void testGetOrder() {
        int result = sqlConsoleInterceptor.getOrder();

        assertEquals(2, result);
    }
}