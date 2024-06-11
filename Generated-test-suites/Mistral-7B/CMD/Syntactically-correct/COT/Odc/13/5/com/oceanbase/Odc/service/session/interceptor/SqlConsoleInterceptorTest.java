package com.oceanbase.odc.service.session.interceptor.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
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
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for SqlConsoleInterceptor class
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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle_noRuleSetId_true() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(new Object());

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_ruleSetIdPresent_true() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Long ruleSetId = 1L;

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(10));

        request.setQueryLimit(100);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
        assertEquals(10, request.getQueryLimit());
    }

    @Test
    public void testDoPreHandle_handleException_false() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Long ruleSetId = 1L;

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class)).thenThrow(new RuntimeException());

        request.setQueryLimit(100);

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testHandle_noViolations_true() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        List<SqlTuple> sqlTuples = new ArrayList<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_violationsPresent_false() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        List<SqlTuple> sqlTuples = new ArrayList<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        Rule rule = mock(Rule.class);
        RuleViolation violation = mock(RuleViolation.class);

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(rule));
        when(rule.getProperties()).thenReturn(Map.of("max_sql_limit", "5"));
        when(rule.getLevel()).thenReturn(1);

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.empty());
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Optional.of(List.of("DML")));

        sqlTuples.add(new SqlTuple("SELECT * FROM table1", 1, 1, "SELECT * FROM table1"));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(5));

        when(response.getSqls()).thenReturn(sqlTuples);

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
        verify(rule, times(1)).getProperties();
        verify(rule, times(1)).getLevel();
        verify(violation, times(1)).setOffset(sqlTuples.get(0).getOffset());
        verify(violation, times(1)).setStart(0);
        verify(violation, times(1)).setStop(sqlTuples.get(0).getOriginalSql().length());
        verify(ruleViolation, times(1)).setViolation(violation);
        verify(rule, times(1)).setViolation(violation);
        verify(sqlConsoleInterceptor, times(1)).getExecuteStageName();
        verify(ruleService, times(2)).getByRulesetIdAndName(1L, anyString());
    }

    @Test
    public void testDetermineSqlType_validSql_correctType() {
        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM table1", 1, 1, "SELECT * FROM table1");
        DialectType dialectType = DialectType.MYSQL;

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(SqlType.DDL, basicResult.getSqlType());
    }

    @Test
    public void testDetermineSqlType_invalidSql_unknownType() {
        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM table1", 1, 1, "SELECT * FROM table1");
        DialectType dialectType = DialectType.MYSQL;

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(SqlType.UNKNOWN, basicResult.getSqlType());
    }

    @Test
    public void testDoAfterCompletion_noViolations_true() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        response.setStatus(SqlExecuteStatus.SUCCESS);

        assertTrue(sqlConsoleInterceptor.doAfterCompletion(response, session, context));
    }

    @Test
    public void testDoAfterCompletion_violationsPresent_false() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        Rule rule = mock(Rule.class);
        RuleViolation violation = mock(RuleViolation.class);

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET.getRuleName()))
                .thenReturn(Optional.of(rule));

        response.setStatus(SqlExecuteStatus.SUCCESS);

        assertFalse(sqlConsoleInterceptor.doAfterCompletion(response, session, context));
        verify(rule, times(1)).getViolation();
    }

    @Test
    public void testGetExecuteStageName_correctStageName() {
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, sqlConsoleInterceptor.getExecuteStageName());
    }
}