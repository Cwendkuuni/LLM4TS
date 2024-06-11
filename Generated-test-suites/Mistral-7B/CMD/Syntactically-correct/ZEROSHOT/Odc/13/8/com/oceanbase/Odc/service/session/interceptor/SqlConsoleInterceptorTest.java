package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
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
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

/**
 * Unit tests for the {@link SqlConsoleInterceptor} class.
 */
public class SqlConsoleInterceptorTest {

    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private ConnectionSession connectionSession;

    @Before
    public void setup() {
        authenticationFacade = Mockito.mock(AuthenticationFacade.class);
        ruleService = Mockito.mock(RuleService.class);
        sqlConsoleRuleService = Mockito.mock(SqlConsoleRuleService.class);
        connectionSession = Mockito.mock(ConnectionSession.class);
    }

    @Test
    public void testDoPreHandle_allowed() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.isIndividualTeam(connectionSession)).thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Optional.of(Arrays.asList("DDL", "DML")));

        // When
        boolean result = new SqlConsoleInterceptor().doPreHandle(request, response, connectionSession, new HashMap<>());

        // Then
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_maxSqlSizeReached() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        sqlTuple.setSqlTuple(new SqlTuple());
        response.setSqls(Collections.singletonList(sqlTuple));
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.isIndividualTeam(connectionSession)).thenReturn(false);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(1));

        // When
        boolean result = new SqlConsoleInterceptor().doPreHandle(request, response, connectionSession, new HashMap<>());

        // Then
        assertFalse(result);
    }

    @Test
    public void testDoPreHandle_notAllowedCreatePl() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        sqlTuple.setSqlTuple(new SqlTuple());
        response.setSqls(Collections.singletonList(sqlTuple));
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.isIndividualTeam(connectionSession)).thenReturn(false);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
                .thenReturn(true);

        // When
        boolean result = new SqlConsoleInterceptor().doPreHandle(request, response, connectionSession, new HashMap<>());

        // Then
        assertFalse(result);
    }

    @Test
    public void testDoPreHandle_allowedWithCheckIntercepted() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.isIndividualTeam(connectionSession)).thenReturn(false);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Optional.of(Arrays.asList("DDL", "DML")));

        // When
        Map<String, Object> context = new HashMap<>();
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        boolean result = new SqlConsoleInterceptor().doPreHandle(request, response, connectionSession, context);

        // Then
        assertFalse(result);
    }

    @Test
    public void testDoPreHandle_allowedWithSqlConsoleIntercepted() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.isIndividualTeam(connectionSession)).thenReturn(false);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Optional.of(Arrays.asList("DDL", "DML")));

        // When
        Map<String, Object> context = new HashMap<>();
        context.put(NEED_SQL_CONSOLE_CHECK, true);
        context.put(SQL_CONSOLE_INTERCEPTED, true);

        // Then
        boolean result = new SqlConsoleInterceptor().doPreHandle(request, response, connectionSession, context);

        // Then
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_notAllowedMaxSqlSize() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        sqlTuple.setSqlTuple(new SqlTuple());
        response.setSqls(Collections.singletonList(sqlTuple));
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.isIndividualTeam(connectionSession)).thenReturn(false);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(1));

        // When
        RuleMaxSqlSize rule = mock(Rule.class);
        RuleViolation violation = new RuleViolation();
        violation.setLevel(RuleViolation.Level.ERROR);
        violation.setLocalizedMessage("Maximum number of SQLs reached.");
        when(ruleService.getByRulesetIdAndName(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(rule));
        when(rule.getProperties()).thenReturn(Collections.singletonMap("limit", "1"));
        when(rule.getMetadata().getPropertyMetadatas().get(0).getName()).thenReturn("limit");

        // When
        Map<String, Object> context = new HashMap<>();
        boolean result = new SqlConsoleInterceptor().doPreHandle(request, response, connectionSession, context);

        // Then
        assertFalse(result);
        verify(ruleService).getByRulesetIdAndName(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
    }

    @Test
    public void testDoPreHandle_notAllowedSqlType() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        sqlTuple.setSqlTuple(new SqlTuple());
        response.setSqls(Collections.singletonList(sqlTuple));
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.isIndividualTeam(connectionSession)).thenReturn(false);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Optional.of(Collections.singletonList("DDL")));

        // When
        RuleSqlType rule = mock(Rule.class);
        RuleViolation violation = new RuleViolation();
        violation.setLevel(RuleViolation.Level.WARNING);
        violation.setLocalizedMessage("Only DDL SQLs are allowed.");
        when(ruleService.getByRulesetIdAndName(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.of(rule));
        when(rule.getProperties()).thenReturn(Collections.singletonMap("type", "DML"));
        when(rule.getMetadata().getPropertyMetadatas().get(0).getName()).thenReturn("type");

        // When
        Map<String, Object> context = new HashMap<>();
        BasicResult basicResult = new BasicResult();
        basicResult.setSqlType(SqlType.DML);
        when(determineSqlType(eq(sqlTuple.getSqlTuple()), eq(DialectType.MYSQL))).thenReturn(basicResult);

        // Then
        boolean result = new SqlConsoleInterceptor().doPreHandle(request, response, connectionSession, context);

        // Then
        assertFalse(result);
        verify(ruleService).getByRulesetIdAndName(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName());
    }

    @Test
    public void testDetermineSqlType_unknown() {
        // Given
        SqlTuple sqlTuple = new SqlTuple();

        // When
        BasicResult result = new SqlConsoleInterceptor().determineSqlType(sqlTuple, DialectType.MYSQL);

        // Then
        assertEquals(SqlType.UNKNOWN, result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_ddl() {
        // Given
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("CREATE TABLE my_table (id INT PRIMARY KEY)");

        // When
        BasicResult result = new SqlConsoleInterceptor().determineSqlType(sqlTuple, DialectType.MYSQL);

        // Then
        assertEquals(SqlType.DDL, result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_dml() {
        // Given
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM my_table");

        // When
        BasicResult result = new SqlConsoleInterceptor().determineSqlType(sqlTuple, DialectType.MYSQL);

        // Then
        assertEquals(SqlType.DML, result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_ddlWithSyntaxError() {
        // Given
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("CREATE TABLE my_table (id INT PRIMARY KEY");

        // When
        BasicResult result = new SqlConsoleInterceptor().determineSqlType(sqlTuple, DialectType.MYSQL);

        // Then
        assertEquals(SqlType.UNKNOWN, result.getSqlType());
        assertTrue(result.isSyntaxError());
    }

    @Test
    public void testDoAfterCompletion_notAllowedEditResultSet() {
        // Given
        SqlExecuteResult result = new SqlExecuteResult();
        result.setAllowExport(true);
        result.setStatus(SqlExecuteStatus.SUCCESS);

        // When
        new SqlConsoleInterceptor().doAfterCompletion(result, connectionSession, new HashMap<>());

        // Then
        assertFalse(result.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_allowedExport() {
        // Given
        SqlExecuteResult result = new SqlExecuteResult();
        result.setAllowExport(false);
        result.setStatus(SqlExecuteStatus.SUCCESS);

        // When
        new SqlConsoleInterceptor().doAfterCompletion(result, connectionSession, new HashMap<>());

        // Then
        assertTrue(result.isAllowExport());
    }

    @Test
    public void testGetExecuteStageName() {
        // Given

        // When
        String result = new SqlConsoleInterceptor().getExecuteStageName();

        // Then
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, result);
    }
}