package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuplesWithViolation;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Test class for {@link SqlConsoleInterceptor}.
 */
@Slf4j
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    private ConnectionSession session;
    private SqlConsoleInterceptor interceptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        interceptor = new SqlConsoleInterceptor();
        interceptor.setAuthenticationFacade(authenticationFacade);
        interceptor.setRuleService(ruleService);
        interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @After
    public void tearDown() {
        interceptor = null;
        session = null;
    }

    @Test
    public void testHandle_whenRuleSetIdIsNull_thenReturnTrue() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.INDIVIDUAL);

        // When
        boolean result = interceptor.handle(request, response, session, new HashMap<>());

        // Then
        assertTrue(result);
    }

    @Test
    public void testHandle_whenIsIndividualTeam_thenReturnTrue() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.INDIVIDUAL);

        // When
        boolean result = interceptor.handle(request, response, session, new HashMap<>());

        // Then
        assertTrue(result);
    }

    @Test
    public void testHandle_whenNeedSqlConsoleCheckIsFalse_thenReturnTrue() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(100));
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);

        // When
        boolean result = interceptor.handle(request, response, session, context);

        // Then
        assertTrue(result);
    }

    @Test
    public void testHandle_whenMaxReturnRowsIsSet_thenSetQueryLimit() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(50));

        // When
        interceptor.handle(request, response, session, new HashMap<>());

        // Then
        assertEquals(50, request.getQueryLimit());
    }

    @Test
    public void testHandle_whenMaxSqlSizeIsSetAndSqlsSizeExceedsLimit_thenSetMaxSqlSizeRuleViolation() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL, String.class))
                .thenReturn(Optional.of("maxSqlSize"));
        when(ruleService.getByRulesetIdAndName(1L, "maxSqlSize"))
                .thenReturn(Optional.of(new Rule()));

        SqlTuplesWithViolation sqlTuples = new SqlTuplesWithViolation();
        sqlTuples.setSqlTuple(new SqlTuple());
        sqlTuples.setViolatedRules(new ArrayList<>());

        response.setSqls(new ArrayList<>());
        response.getSqls().add(sqlTuples);

        // When
        interceptor.handle(request, response, session, new HashMap<>());

        // Then
        List<Rule> violatedRules = sqlTuples.getViolatedRules();
        Rule maxSqlSizeRule = violatedRules.get(violatedRules.size() - 1);
        assertEquals(RuleViolation.Level.ERROR, maxSqlSizeRule.getViolation().getLevel());
        assertEquals("maxSqlSize", maxSqlSizeRule.getViolation().getLocalizedMessage().getArgs()[0]);
    }

    @Test
    public void testHandle_whenForbiddenToCreatePl_thenSetCreatePlRuleViolation() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);
        when(ruleService.getByRulesetIdAndName(1L, "NOT_ALLOWED_CREATE_PL"))
                .thenReturn(Optional.of(new Rule()));

        SqlTuplesWithViolation sqlTuples = new SqlTuplesWithViolation();
        sqlTuples.setSqlTuple(new SqlTuple());
        sqlTuples.setViolatedRules(new ArrayList<>());

        response.setSqls(new ArrayList<>());
        response.getSqls().add(sqlTuples);

        // When
        interceptor.handle(request, response, session, new HashMap<>());

        // Then
        List<Rule> violatedRules = sqlTuples.getViolatedRules();
        Rule createPlRule = violatedRules.get(violatedRules.size() - 1);
        assertEquals(RuleViolation.Level.ERROR, createPlRule.getViolation().getLevel());
        assertEquals("NOT_ALLOWED_CREATE_PL", createPlRule.getViolation().getLocalizedMessage().getRuleName());
    }

    @Test
    public void testHandle_whenAllowSqlTypesIsSetAndSqlTypeIsNotAllowed_thenSetAllowSqlTypesRuleViolation() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser().getOrganizationType())
                .thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL, String.class))
                .thenReturn(Optional.of(new String[]{"SELECT"}));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(determineSqlType(any(SqlTuple.class), eq(DialectType.MYSQL)))
                .thenReturn(new BasicResult(SqlType.CREATE_PROCEDURE));

        SqlTuplesWithViolation sqlTuples = new SqlTuplesWithViolation();
        sqlTuples.setSqlTuple(new SqlTuple());
        sqlTuples.setViolatedRules(new ArrayList<>());

        response.setSqls(new ArrayList<>());
        response.getSqls().add(sqlTuples);

        // When
        interceptor.handle(request, response, session, new HashMap<>());

        // Then
        List<Rule> violatedRules = sqlTuples.getViolatedRules();
        Rule allowSqlTypesRule = violatedRules.get(violatedRules.size() - 1);
        assertEquals(RuleViolation.Level.ERROR, allowSqlTypesRule.getViolation().getLevel());
        assertEquals("ALLOW_SQL_TYPES", allowSqlTypesRule.getViolation().getLocalizedMessage().getRuleName());
    }
}