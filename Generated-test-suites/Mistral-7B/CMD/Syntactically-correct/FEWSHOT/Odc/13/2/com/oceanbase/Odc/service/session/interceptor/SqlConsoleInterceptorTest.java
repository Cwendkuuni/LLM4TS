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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResp;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
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

    private ConnectionSession session;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();
    }

    @Test
    public void testHandle_noNeedCheck_returnTrue() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.CORPORATE);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, eq(DialectType.MYSQL),
                eq(Integer.class))).thenReturn(Optional.empty());

        // When
        boolean result = sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());

        // Then
        assertTrue(result);
    }

    @Test
    public void testHandle_maxReturnRowsReached_limitQueryLimit() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.CORPORATE);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, eq(DialectType.MYSQL),
                eq(Integer.class))).thenReturn(Optional.of(10));
        request.setQueryLimit(12);

        // When
        boolean result = sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());

        // Then
        assertEquals(10, request.getQueryLimit());
        assertTrue(result);
    }

    @Test
    public void testHandle_maxReturnRowsReached_doNotLimitQueryLimit() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.CORPORATE);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, eq(DialectType.MYSQL),
                eq(Integer.class))).thenReturn(Optional.of(10));
        request.setQueryLimit(12);

        // When
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, Boolean.TRUE);
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        // Then
        assertFalse(result);
    }

    @Test
    public void testHandle_maxSqlSizeReached_limitSqlSize() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.CORPORATE);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS, eq(DialectType.MYSQL),
                eq(Integer.class))).thenReturn(Optional.of(2));
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(new SqlTuple("id:1", "SELECT * FROM table1"));
        sqlTuples.add(new SqlTuple("id:2", "SELECT * FROM table1"));
        response.setSqls(sqlTuples);

        // When
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, Boolean.TRUE);
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        // Then
        assertEquals(2, sqlTuples.size());
        assertFalse(result);
    }

    @Test
    public void testHandle_plDdlForbidden_limitCreatePl() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.CORPORATE);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(new SqlTuple("id:1", "CREATE TABLE table1 (id INT)"));
        response.setSqls(sqlTuples);

        // When
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, Boolean.TRUE);
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        // Then
        assertEquals(1, sqlTuples.size());
        assertFalse(result);
    }

    @Test
    public void testHandle_sqlTypeForbidden_limitSqlType() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.CORPORATE);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, eq(DialectType.MYSQL),
                eq(String.class))).thenReturn(Optional.of(List.of("SELECT")));
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(new SqlTuple("id:1", "INSERT INTO table1 (id) VALUES (1)"));
        response.setSqls(sqlTuples);

        // When
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, Boolean.TRUE);
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        // Then
        assertEquals(1, sqlTuples.size());
        assertFalse(result);
    }

    @Test
    public void testDoAfterCompletion_allowExport() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.CORPORATE);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(false);
        SqlExecuteResult sqlExecuteResult = mock(SqlExecuteResult.class);
        when(sqlExecuteResult.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);

        // When
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, Collections.emptyMap());

        // Then
        assertTrue(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_doNotAllowExport() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.CORPORATE);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(true);
        SqlExecuteResult sqlExecuteResult = mock(SqlExecuteResult.class);
        when(sqlExecuteResult.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);

        // When
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, Collections.emptyMap());

        // Then
        assertFalse(sqlExecuteResult.isAllowExport());
    }
}