package com.oceanbase.odc.service.session.interceptor.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Test class for SqlConsoleInterceptor.
 */
@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle_allowExecute() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Mockito.when(authenticationFacade.currentUser()).thenReturn(new Object());
        Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        Mockito.when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                DialectType.MYSQL, Integer.class)).thenReturn(Optional.of(10));

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_forbiddenToExecute() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Mockito.when(authenticationFacade.currentUser()).thenReturn(new Object());
        Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId("1");
        sqlTuple.setOriginalSql("CREATE TABLE test(id INT PRIMARY KEY)");
        sqlTuples.add(sqlTuple);

        Mockito.when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                DialectType.MYSQL, String.class)).thenReturn(Optional.of(new ArrayList<>()));
        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(false);

        response.setSqls(sqlTuples);

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testHandle_allowExecute() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Mockito.when(authenticationFacade.currentUser()).thenReturn(new Object());
        Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        Mockito.when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                DialectType.MYSQL, Integer.class)).thenReturn(Optional.of(10));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId("1");
        sqlTuple.setOriginalSql("SELECT * FROM test WHERE id = 1");
        sqlTuple.setQueryLimit(null);
        sqlTuples.add(sqlTuple);

        Mockito.when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                DialectType.MYSQL, String.class)).thenReturn(Optional.of(new ArrayList<>()));
        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(false);

        response.setSqls(sqlTuples);

        Mockito.when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(new Rule()));

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_forbiddenToExecute() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Mockito.when(authenticationFacade.currentUser()).thenReturn(new Object());
        Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        Mockito.when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                DialectType.MYSQL, Integer.class)).thenReturn(Optional.of(10));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId("1");
        sqlTuple.setOriginalSql("CREATE TABLE test(id INT PRIMARY KEY)");
        sqlTuple.setQueryLimit(null);
        sqlTuples.add(sqlTuple);

        Mockito.when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                DialectType.MYSQL, String.class)).thenReturn(Optional.of(new ArrayList<>()));
        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(true);

        response.setSqls(sqlTuples);

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_maxExecuteSqlsViolation() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Mockito.when(authenticationFacade.currentUser()).thenReturn(new Object());
        Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        Mockito.when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                DialectType.MYSQL, Integer.class)).thenReturn(Optional.of(10));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple1 = new SqlTuple();
        sqlTuple1.setSqlId("1");
        sqlTuple1.setOriginalSql("SELECT * FROM test WHERE id = 1");
        sqlTuple1.setQueryLimit(null);
        sqlTuples.add(sqlTuple1);

        SqlTuple sqlTuple2 = new SqlTuple();
        sqlTuple2.setSqlId("2");
        sqlTuple2.setOriginalSql("SELECT * FROM test WHERE id = 2");
        sqlTuple2.setQueryLimit(null);
        sqlTuples.add(sqlTuple2);

        Mockito.when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                DialectType.MYSQL, String.class)).thenReturn(Optional.of(new ArrayList<>()));
        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(false);

        response.setSqls(sqlTuples);

        Mockito.when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(new Rule()));

        Rule rule = Mockito.mock(Rule.class);
        RuleViolation violation = new RuleViolation();
        violation.setLevel(1);
        violation.setLocalizedMessage("Max execute sqls violation");
        rule.setViolation(violation);

        Mockito.when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(rule));

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletion_allowExport() {
        SqlExecuteResult response = new SqlExecuteResult();

        assertTrue(sqlConsoleInterceptor.doAfterCompletion(response, null, null, null));
    }

    @Test
    public void testDoAfterCompletion_forbiddenToExport() {
        SqlExecuteResult response = new SqlExecuteResult();

        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, null))
                .thenReturn(true);

        assertFalse(sqlConsoleInterceptor.doAfterCompletion(response, null, null, null));
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals("SqlConsoleRule", sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testIsIndividualTeam() {
        assertTrue(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test(expected = NullPointerException.class)
    public void testDetermineSqlType_nullAst() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM test WHERE id = 1");
        sqlTuple.setAst(null);

        assertNull(sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL));
    }

    @Test
    public void testDetermineSqlType_sqlTypeOther() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM test WHERE id = 1");
        sqlTuple.setAst(Mockito.mock(AbstractSyntaxTree.class));

        BasicResult basicResult = new BasicResult();
        basicResult.setSqlType(SqlType.OTHERS);

        Mockito.when(sqlTuple.getAst().getParseResult()).thenReturn(basicResult);

        assertEquals(SqlType.OTHERS, sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL));
    }

    @Test
    public void testDetermineSqlType_sqlTypeOtherWithSyntaxError() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM test WHERE id = 1");
        sqlTuple.setAst(Mockito.mock(AbstractSyntaxTree.class));

        BasicResult basicResult = new BasicResult();
        basicResult.setSqlType(SqlType.OTHERS);
        basicResult.setSyntaxError(true);

        Mockito.when(sqlTuple.getAst().getParseResult()).thenReturn(basicResult);

        assertEquals(SqlType.OTHERS, sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL));
    }

    @Test
    public void testDetermineSqlType_sqlTypeUnknown() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM test WHERE id = 1");
        sqlTuple.setAst(Mockito.mock(AbstractSyntaxTree.class));

        BasicResult basicResult = new BasicResult();
        basicResult.setSqlType(SqlType.UNKNOWN);

        Mockito.when(sqlTuple.getAst().getParseResult()).thenReturn(basicResult);

        assertEquals(SqlType.UNKNOWN, sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL));
    }

    @Test
    public void testDetermineSqlType_sqlTypeValid() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM test WHERE id = 1");
        sqlTuple.setAst(Mockito.mock(AbstractSyntaxTree.class));

        BasicResult basicResult = new BasicResult();
        basicResult.setSqlType(SqlType.DDL);

        Mockito.when(sqlTuple.getAst().getParseResult()).thenReturn(basicResult);

        assertEquals(SqlType.DDL, sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL));
    }
}