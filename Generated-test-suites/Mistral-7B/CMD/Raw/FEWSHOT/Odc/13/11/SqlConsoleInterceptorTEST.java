package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.model.User;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.BasicResult;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@Slf4j
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    private User user;
    private ConnectionSession session;
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        user = new User();
        session = mock(ConnectionSession.class);
        when(authenticationFacade.currentUser()).thenReturn(user);
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testIsIndividualTeam() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        assertTrue(sqlConsoleInterceptor.isIndividualTeam());

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        assertFalse(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType() throws Exception {
        // Given
        String sql = "SELECT * FROM users WHERE id = 1";
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql(sql);

        DialectType dialectType = DialectType.MYSQL;
        BasicResult basicResult = new BasicResult(SqlType.SELECT);

        when(sqlConsoleRuleService.getProperties(anyLong(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS),
                eq(dialectType), eq(Integer.class))).thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(anyLong(), eq(SqlConsoleRules.ALLOW_SQL_TYPES),
                eq(dialectType), eq(String.class))).thenReturn(Optional.of(Collections.singletonList("SELECT")));

        // When
        SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
        SqlTuple result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        // Then
        assertEquals(sqlTuple.getOriginalSql(), result.getOriginalSql());
        assertEquals(SqlType.SELECT, result.getSqlType());
    }

    @Test
    public void testHandle() throws Exception {
        // Given
        String sql = "SELECT * FROM users WHERE id = 1";
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQuery(sql);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql(sql);
        sqlTuple.setOffset(0);
        sqlTuple.setSqlId(1);
        sqlTuples.add(new SqlTuplesWithViolation(sqlTuple, new ArrayList<>()));
        response.setSqls(sqlTuples);

        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                eq(DialectType.MYSQL), eq(Integer.class))).thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                eq(DialectType.MYSQL), eq(String.class))).thenReturn(Optional.of(Collections.singletonList("SELECT")));
        Rule rule = mock(Rule.class);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.of(rule));
        RuleViolation violation = mock(RuleViolation.class);
        when(violation.getLevel()).thenReturn(1);
        when(violation.getLocalizedMessage(any())).thenReturn("Maximum number of SQLs exceeded.");

        // When
        SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        // Then
        assertTrue(result);
        verify(ruleService, times(1)).getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName());
        verifyNoMoreInteractions(ruleService);
    }

    @Test
    public void testHandleWithViolation() throws Exception {
        // Given
        String sql = "CREATE TABLE users (id INT PRIMARY KEY)";
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQuery(sql);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql(sql);
        sqlTuple.setOffset(0);
        sqlTuple.setSqlId(1);
        sqlTuple.setAst(null);
        sqlTuples.add(new SqlTuplesWithViolation(sqlTuple, new ArrayList<>()));
        response.setSqls(sqlTuples);

        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                eq(DialectType.MYSQL), eq(Integer.class))).thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                eq(DialectType.MYSQL), eq(String.class))).thenReturn(Optional.empty());
        Rule rule = mock(Rule.class);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName()))
                .thenReturn(Optional.of(rule));
        RuleViolation violation = mock(RuleViolation.class);
        when(violation.getLevel()).thenReturn(2);
        when(violation.getLocalizedMessage(any())).thenReturn("PL/DDL statements are not allowed.");

        // When
        SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        // Then
        assertFalse(result);
        verify(ruleService, times(1)).getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName());
        verify(ruleService, times(1)).getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName());
    }

    @Test
    public void testHandleWithSyntaxError() throws Exception {
        // Given
        String sql = "SELECT * FROM users WHERE id = 12345";
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQuery(sql);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql(sql);
        sqlTuple.setOffset(0);
        sqlTuple.setSqlId(1);
        sqlTuple.setAst(null);
        sqlTuples.add(new SqlTuplesWithViolation(sqlTuple, new ArrayList<>()));
        response.setSqls(sqlTuples);

        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                eq(DialectType.MYSQL), eq(Integer.class))).thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
                eq(DialectType.MYSQL), eq(String.class))).thenReturn(Optional.empty());

        // When
        SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        // Then
        assertFalse(result);
        verify(ruleService, never()).getByRulesetIdAndName(anyString(), any());
    }
}