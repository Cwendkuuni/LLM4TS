package com.oceanbase.odc.service.session.interceptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;
import static com.oceanbase.odc.core.shared.constant.DialectType.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;

public class SqlConsoleInterceptorTest {

    private ConnectionSession session;
    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Before
    public void setup() {
        session = mock(ConnectionSession.class);
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        when(authenticationFacade.currentUser()).thenReturn(new Object());
        ConnectionSessionUtil.setRuleSetId(session, 1L);
    }

    @Test
    public void testDoPreHandle_noRuleSetId_shouldReturnTrue() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.empty());

        boolean result = new SqlConsoleInterceptor().doPreHandle(request, response, session, null);
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_maxReturnRowsRule_shouldIntercept() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setQueryLimit(100);
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        Rule rule = Mockito.mock(Rule.class);
        RuleViolation violation = new RuleViolation();
        violation.setLevel(1);
        violation.setLocalizedMessage("Maximum number of return rows exceeded.");
        rule.setViolation(violation);

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())).thenReturn(Optional.of(rule));

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, MYSQL, Integer.class))
                .thenReturn(Optional.of(10));

        boolean result = new SqlConsoleInterceptor().doPreHandle(request, response, session, null);
        assertFalse(result);
    }

    @Test
    public void testDoPreHandle_maxExecuteSqlsRule_shouldIntercept() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1);
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        Rule rule = Mockito.mock(Rule.class);
        RuleViolation violation = new RuleViolation();
        violation.setLevel(1);
        violation.setLocalizedMessage("Maximum number of execute sqls exceeded.");
        rule.setViolation(violation);

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())).thenReturn(Optional.of(rule));

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, MYSQL, String.class))
                .thenReturn(Optional.of(new ArrayList<>()));

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, MYSQL, String.class))
                .thenReturn(Optional.of(new ArrayList<>()));

        response.setSqls(List.of(sqlTuple));
        boolean result = new SqlConsoleInterceptor().doPreHandle(request, response, session, null);
        assertFalse(result);
    }

    @Test
    public void testDoPreHandle_allowedSqlTypesRule_shouldAllow() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1);
        sqlTuple.setQueryLimit(10);
        sqlTuple.setOriginalSql("SELECT * FROM users WHERE id = 1");
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        Rule rule = Mockito.mock(Rule.class);
        RuleViolation violation = new RuleViolation();
        violation.setLevel(1);
        violation.setLocalizedMessage("SQL type is not allowed.");
        rule.setViolation(violation);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.of(rule));

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, ORACLE, String.class))
                .thenReturn(Optional.of(List.of("DDL", "DML")));

        response.setSqls(List.of(sqlTuple));

        boolean result = new SqlConsoleInterceptor().doPreHandle(request, response, session, null);
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_syntaxError_shouldAllow() throws SQLException {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1);
        sqlTuple.setOriginalSql("CREATE TABLE user(id INT)");
        sqlTuple.setQueryLimit(10);
        sqlTuple.setParseResult(new BasicResult(SqlType.DDL, null, null, null, null, null, null, null, true, true,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null));
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, ORACLE, String.class))
                .thenReturn(Optional.of(List.of("DDL", "DML")));

        response.setSqls(List.of(sqlTuple));

        boolean result = new SqlConsoleInterceptor().doPreHandle(request, response, session, null);
        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion_noViolations_shouldAllowExport() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        result.setAllowExport(true);

        boolean result2 = new SqlConsoleInterceptor().doAfterCompletion(result, session, null);
        assertTrue(result2);
    }

    @Test
    public void testDoAfterCompletion_withViolations_shouldDenyExport() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        result.setAllowExport(false);

        Rule rule = Mockito.mock(Rule.class);
        RuleViolation violation = new RuleViolation();
        violation.setLevel(1);
        violation.setLocalizedMessage("Maximum number of return rows exceeded.");
        rule.setViolation(violation);

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(rule));

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session))
                .thenReturn(false);

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session))
                .thenReturn(true);

        boolean result2 = new SqlConsoleInterceptor().doAfterCompletion(result, session, null);
        assertFalse(result2);
    }

    @Test
    public void testGetExecuteStageName_shouldReturnCorrectName() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, interceptor.getExecuteStageName());
    }

    @Test
    public void testIsIndividualTeam_shouldReturnFalse() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        assertFalse(interceptor.isIndividualTeam());
    }

    @Test
    public void testIsIndividualTeam_shouldReturnTrue() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        AuthenticationFacade authenticationFacadeSpy = Mockito.spy(authenticationFacade);
        interceptor.setAuthenticationFacade(authenticationFacadeSpy);
        assertTrue(interceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType_shouldDetermineCorrectType() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM users WHERE id = 1");

        BasicResult result = interceptor.determineSqlType(sqlTuple, MYSQL);
        assertEquals(SqlType.DML, result.getSqlType());
    }
}