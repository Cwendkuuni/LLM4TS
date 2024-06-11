package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

/**
 * Test class for {@link SqlConsoleInterceptor}
 */
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
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle_allowExecute() throws Exception {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(),
                eq(Integer.class))).thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(),
                eq(String.class))).thenReturn(Optional.of(List.of("DDL")));

        doAnswer(invocation -> {
            SqlAsyncExecuteResp result = invocation.getArgument(2);
            result.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM TEST_TABLE;"))));
            return null;
        }).when(sqlConsoleInterceptor).handle(eq(request), eq(response), eq(session), eq(context));

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_doesNotAllowExecute() throws Exception {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(),
                eq(Integer.class))).thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(),
                eq(String.class))).thenReturn(Optional.of(List.of("DDL")));

        doAnswer(invocation -> {
            SqlAsyncExecuteResp result = invocation.getArgument(2);
            result.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple("CREATE TABLE TEST_TABLE (ID INT);"))));
            return null;
        }).when(sqlConsoleInterceptor).handle(eq(request), eq(response), eq(session), eq(context));

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_noRuleSetId() throws Exception {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_individualTeam() throws Exception {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(ConnectionSessionUtil.isIndividualTeam(authenticationFacade.currentUser())).thenReturn(true);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testHandle_noViolations() throws Exception {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        SqlConsoleInterceptor interceptor = spy(sqlConsoleInterceptor);
        doReturn(true).when(interceptor).handle(request, response, session, context);

        interceptor.handle(request, response, session, context);

        verifyNoMoreInteractions(interceptor);
    }

    @Test
    public void testHandle_violations() throws Exception {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        SqlConsoleInterceptor interceptor = spy(sqlConsoleInterceptor);
        doReturn(false).when(interceptor).handle(request, response, session, context);

        List<Rule> violatedRules = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleViolation violation1 = new RuleViolation();
        violation1.setLevel(1);
        violation1.setLocalizedMessage("Rule 1 message");
        violation1.setOffset(1);
        violation1.setStart(1);
        violation1.setStop(10);
        rule1.setViolation(violation1);
        Rule rule2 = new Rule();
        RuleViolation violation2 = new RuleViolation();
        violation2.setLevel(2);
        violation2.setLocalizedMessage("Rule 2 message");
        violation2.setOffset(11);
        violation2.setStart(11);
        violation2.setStop(20);
        rule2.setViolation(violation2);
        violatedRules.add(rule1);
        violatedRules.add(rule2);

        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM TEST_TABLE WHERE ID = 1;");
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(), eq(Integer.class)))
                .thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.isForbidden(anyString(), session)).thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(), eq(String.class)))
                .thenReturn(Optional.of(List.of("DDL")));

        when(request.getQueryLimit()).thenReturn(null);

        doAnswer(invocation -> {
            SqlAsyncExecuteResp result = invocation.getArgument(2);
            result.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM TEST_TABLE WHERE ID = 1;"),
                    violatedRules)));
            return null;
        }).when(sqlConsoleInterceptor).handle(request, response, session, context);

        interceptor.handle(request, response, session, context);

        verify(ruleService, times(2)).getByRulesetIdAndName(1L, anyString());
    }

    @Test
    public void testDetermineSqlType_validSql() throws Exception {
        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM TEST_TABLE WHERE ID = 1;");
        DialectType dialectType = DialectType.MYSQL;

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(SqlType.SELECT, basicResult.getSqlType());
    }

    @Test
    public void testDetermineSqlType_invalidSql() throws Exception {
        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM TEST_TABLE WHERE ID = ID;");
        DialectType dialectType = DialectType.MYSQL;

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(SqlType.OTHERS, basicResult.getSqlType());
    }

    @Test
    public void testDetermineSqlType_syntaxError() throws Exception {
        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM TEST_TABLE WHERE ID = 1; SELECT * FROM TEST_TABLE;");
        DialectType dialectType = DialectType.MYSQL;

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(SqlType.OTHERS, basicResult.getSqlType());
        assertTrue(basicResult.isSyntaxError());
    }
}