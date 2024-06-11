package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private SqlConsoleRuleService sqlConsoleRuleService;

    private ConnectionSession session = Mockito.mock(ConnectionSession.class);
    private SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();

    @Test
    public void testDoPreHandle_ReturnTrue_WhenRuleSetIdIsNull() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();

        boolean result = interceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_ReturnTrue_WhenIsIndividualTeam() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();

        Mockito.when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        boolean result = interceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle_ReturnTrue_WhenRuleSetIdIsNull() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        Optional<Integer> queryLimit = Optional.empty();
        AtomicBoolean allowExecute = new AtomicBoolean(true);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setSqls(sqlTuples);
        response.setSqlExecuteResult(sqlExecuteResult);

        boolean result = interceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle_ReturnFalse_WhenMaxSqlSizeExceeded() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        Mockito.when(sqlConsoleRuleService.getProperties(ArgumentMatchers.anyLong(),
                SqlConsoleRules.MAX_RETURN_ROWS, ArgumentMatchers.any(DialectType.class), Integer.class))
                .thenReturn(Optional.of(10));
        Mockito.when(request.getQueryLimit()).thenReturn(20);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setSqls(sqlTuples);
        response.setSqlExecuteResult(sqlExecuteResult);

        AtomicReference<Rule> maxSqlSizeRule = new AtomicReference<>();
        Optional<List<Rule>> violatedRulesOpt = Optional.of(new ArrayList<>());

        boolean result = interceptor.handle(request, response, session, context);
        assertFalse(result);
        assertEquals(1, maxSqlSizeRule.get().getViolation().getLevel().intValue());
        assertEquals("Maximum number of SQLs allowed to be executed is 10. Only 20 were provided.",
                maxSqlSizeRule.get().getViolation().getLocalizedMessage());
    }

    @Test
    public void testHandle_ReturnTrue_WhenSqlTypeIsAllowed() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        Mockito.when(sqlConsoleRuleService.getListProperties(ArgumentMatchers.anyLong(),
                SqlConsoleRules.ALLOW_SQL_TYPES, ArgumentMatchers.any(DialectType.class), String.class))
                .thenReturn(Optional.of(new ArrayList<>()));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setSqls(sqlTuples);
        response.setSqlExecuteResult(sqlExecuteResult);

        BasicResult parseResult = Mockito.mock(BasicResult.class);
        Mockito.when(parseResult.getSqlType()).thenReturn(SqlType.DDL);
        sqlTuples.add(new SqlTuplesWithViolation(new SqlTuple("test", parseResult), new RuleViolation()));

        List<Rule> violatedRules = new ArrayList<>();
        Rule rule = Mockito.mock(Rule.class);
        Mockito.when(rule.getLevel()).thenReturn(Rule.Level.INFO);
        Mockito.when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.of(rule));

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        boolean result = interceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle_ReturnFalse_WhenSqlTypeIsNotAllowed() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        Mockito.when(sqlConsoleRuleService.getListProperties(ArgumentMatchers.anyLong(),
                SqlConsoleRules.ALLOW_SQL_TYPES, ArgumentMatchers.any(DialectType.class), String.class))
                .thenReturn(Optional.of(new ArrayList<>()));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setSqls(sqlTuples);
        response.setSqlExecuteResult(sqlExecuteResult);

        BasicResult parseResult = Mockito.mock(BasicResult.class);
        Mockito.when(parseResult.getSqlType()).thenReturn(SqlType.SELECT);
        sqlTuples.add(new SqlTuplesWithViolation(new SqlTuple("test", parseResult), new RuleViolation()));

        List<Rule> violatedRules = new ArrayList<>();
        Rule rule = Mockito.mock(Rule.class);
        Mockito.when(rule.getLevel()).thenReturn(Rule.Level.ERROR);
        Mockito.when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.empty());

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        boolean result = interceptor.handle(request, response, session, context);
        assertFalse(result);
        assertEquals(Rule.Level.ERROR, violatedRules.get(0).getLevel());
        assertEquals("SQL type 'SELECT' is not allowed.", violatedRules.get(0).getViolation().getLocalizedMessage());
    }

    @Test
    public void testHandle_ReturnFalse_WhenForbiddenToCreatePl() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(true);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setSqls(sqlTuples);
        response.setSqlExecuteResult(sqlExecuteResult);

        List<Rule> violatedRules = new ArrayList<>();
        Rule rule = Mockito.mock(Rule.class);
        Mockito.when(rule.getLevel()).thenReturn(Rule.Level.ERROR);
        Mockito.when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName()))
                .thenReturn(Optional.of(rule));

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        boolean result = interceptor.handle(request, response, session, context);
        assertFalse(result);
        assertEquals(Rule.Level.ERROR, violatedRules.get(0).getLevel());
        assertEquals("Creating a PL is not allowed.", violatedRules.get(0).getViolation().getLocalizedMessage());
    }

    @Test
    public void testDoAfterCompletion_AllowExportIsTrue() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setAllowExport(true);

        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();

        assertTrue(interceptor.doAfterCompletion(sqlExecuteResult, session, context));
    }

    @Test
    public void testDoAfterCompletion_AllowExportIsFalse() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setAllowExport(false);

        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();

        assertFalse(interceptor.doAfterCompletion(sqlExecuteResult, session, context));
    }

    @Test
    public void testIsIndividualTeam_ReturnTrue() {
        Mockito.when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.INDIVIDUAL);

        assertTrue(interceptor.isIndividualTeam());
    }

    @Test
    public void testIsIndividualTeam_ReturnFalse() {
        Mockito.when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);

        assertFalse(interceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType_ReturnBasicResult() {
        SqlTuple sqlTuple = new SqlTuple("test", null);
        AbstractSyntaxTree ast = Mockito.mock(AbstractSyntaxTree.class);
        sqlTuple.setAst(ast);

        DialectType dialectType = DialectType.MYSQL;

        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();

        BasicResult result = interceptor.determineSqlType(sqlTuple, dialectType);

        assertNotNull(result);
    }
}