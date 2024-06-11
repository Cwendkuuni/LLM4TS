package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@Slf4j
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    private SqlConsoleInterceptor interceptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        interceptor = new SqlConsoleInterceptor();
        interceptor.authenticationFacade = authenticationFacade;
        interceptor.ruleService = ruleService;
        interceptor.sqlConsoleRuleService = sqlConsoleRuleService;
    }

    @After
    public void teardown() {
        interceptor = null;
    }

    @Test
    public void testDoPreHandle_noRuleSetId_returnTrue() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        boolean result = interceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_ruleSetIdPresent_returnAllowExecute() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, ArgumentMatchers.eq(DialectType.MySQL), Integer.class))
                .thenReturn(Optional.of(10));

        boolean result = interceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_ruleSetIdPresent_returnFalse_maxSqlSizeRuleViolated() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, ArgumentMatchers.eq(DialectType.MySQL), String.class))
                .thenReturn(Optional.of(List.of("SELECT")));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId("1");
        sqlTuple.setOriginalSql("SELECT * FROM table1");
        sqlTuple.setAst(mock(AbstractSyntaxTree.class));
        sqlTuples.add(sqlTuple);

        response.setSqls(sqlTuples);

        Optional<Rule> maxSqlSizeRule = Optional.of(new Rule());
        when(ruleService.getByRulesetIdAndName(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())).thenReturn(Optional.of(maxSqlSizeRule.get()));
        RuleViolation violation = new RuleViolation();
        violation.setLevel(RuleViolation.Level.ERROR);
        violation.setLocalizedMessage("Maximum number of SQLs in a batch execution exceeded.");
        maxSqlSizeRule.get().setViolation(violation);

        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS, ArgumentMatchers.eq(DialectType.MySQL), Integer.class))
                .thenReturn(Optional.of(3));

        boolean expectedResult = false;
        boolean result = interceptor.doPreHandle(request, response, session, context);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDoPreHandle_ruleSetIdPresent_returnFalse_createPlForbidden() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId("1");
        sqlTuple.setOriginalSql("CREATE PROCEDURE p1()");
        sqlTuple.setAst(mock(AbstractSyntaxTree.class));
        sqlTuples.add(sqlTuple);

        response.setSqls(sqlTuples);

        Rule violationRule = new Rule();
        RuleViolation violation = new RuleViolation();
        violation.setLevel(RuleViolation.Level.ERROR);
        violation.setLocalizedMessage("Creation of PL is not allowed.");
        violationRule.setViolation(violation);

        when(ruleService.getByRulesetIdAndName(ruleSetId, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName()))
                .thenReturn(Optional.of(violationRule));

        boolean expectedResult = false;
        boolean result = interceptor.doPreHandle(request, response, session, context);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDoPreHandle_ruleSetIdPresent_returnFalse_allowSqlTypesRuleViolated() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, ArgumentMatchers.eq(DialectType.MySQL), String.class))
                .thenReturn(Optional.of(List.of("INSERT", "SELECT")));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId("1");
        sqlTuple.setOriginalSql("UPDATE table1 SET col1 = 'value1'");
        sqlTuple.setAst(mock(AbstractSyntaxTree.class));
        sqlTuples.add(sqlTuple);

        response.setSqls(sqlTuples);

        Rule violationRule = new Rule();
        RuleViolation violation = new RuleViolation();
        violation.setLevel(RuleViolation.Level.ERROR);
        violation.setLocalizedMessage("Only SELECT and INSERT SQLs are allowed.");
        violationRule.setViolation(violation);

        when(ruleService.getByRulesetIdAndName(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.of(violationRule));

        boolean expectedResult = false;
        boolean result = interceptor.doPreHandle(request, response, session, context);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDoPreHandle_ruleSetIdPresent_returnTrue_noRulesViolated() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, ArgumentMatchers.eq(DialectType.MySQL), String.class))
                .thenReturn(Optional.of(List.of("SELECT", "INSERT")));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple1 = new SqlTuple();
        sqlTuple1.setSqlId("1");
        sqlTuple1.setOriginalSql("SELECT * FROM table1");
        sqlTuple1.setAst(mock(AbstractSyntaxTree.class));
        sqlTuples.add(sqlTuple1);

        SqlTuple sqlTuple2 = new SqlTuple();
        sqlTuple2.setSqlId("2");
        sqlTuple2.setOriginalSql("INSERT INTO table1 (col1) VALUES ('value1')");
        sqlTuple2.setAst(mock(AbstractSyntaxTree.class));
        sqlTuples.add(sqlTuple2);

        response.setSqls(sqlTuples);

        boolean expectedResult = true;
        boolean result = interceptor.doPreHandle(request, response, session, context);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDoAfterCompletion_noRulesViolated_allowExport() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        result.setAllowExport(true);

        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        interceptor.doAfterCompletion(result, session, context);

        assertTrue(result.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_forbiddenToEditResultSet_disallowExport() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);

        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);

        interceptor.doAfterCompletion(result, session, context);

        assertFalse(result.isAllowExport());
    }

    @Test
    public void testGetExecuteStageName_returnsSqlConsoleRuleStageName() {
        String expected = SqlExecuteStages.SQL_CONSOLE_RULE;
        String actual = interceptor.getExecuteStageName();

        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testDetermineSqlType_astIsNull_throwNullPointerException() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM table1");

        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();

        interceptor.determineSqlType(sqlTuple, DialectType.MySQL);
    }

    @Test
    public void testDetermineSqlType_sqlTypeIsUnknown_setSqlTypeToOthers() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM table1");
        sqlTuple.setAst(mock(AbstractSyntaxTree.class));

        BasicResult basicResult = interceptor.determineSqlType(sqlTuple, DialectType.MySQL);

        assertEquals(SqlType.OTHERS, basicResult.getSqlType());
    }

    @Test
    public void testGetOrder_returnsInterceptorOrder() {
        int expected = 2;
        int actual = interceptor.getOrder();

        assertEquals(expected, actual);
    }
}