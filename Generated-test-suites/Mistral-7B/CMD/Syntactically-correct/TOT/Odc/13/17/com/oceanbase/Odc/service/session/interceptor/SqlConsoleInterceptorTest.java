package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@SuppressWarnings("unchecked")
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
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(authenticationFacade.currentUser()).thenReturn(mockUser());
    }

    @Test
    public void testDoPreHandle_AllowExecution_NoRuleSet() {
        ConnectionSession session = mockConnectionSession();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenReturn(Optional.empty());
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), eq(Integer.class)))
                .thenReturn(Optional.empty());

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_AllowExecution_NoCheckNeeded() {
        ConnectionSession session = mockConnectionSession();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenReturn(Optional.of(mockRule()));
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), eq(Boolean.class)))
                .thenReturn(Optional.of(Boolean.FALSE));

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_AllowExecution_WithinLimit() {
        ConnectionSession session = mockConnectionSession();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = Arrays.asList(mockSqlTuples());
        when(response.getSqls()).thenReturn(sqlTuples);

        when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenReturn(Optional.of(mockRule()));
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), eq(Integer.class)))
                .thenReturn(Optional.of(10));

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_DenyExecution_ExceedLimit() {
        ConnectionSession session = mockConnectionSession();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = Arrays.asList(mockSqlTuples());
        when(response.getSqls()).thenReturn(sqlTuples);

        when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenReturn(Optional.of(mockRule()));
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), eq(Integer.class)))
                .thenReturn(Optional.of(10));

        when(sqlConsoleRuleService.getListProperties(anyLong(), anyString(), any(DialectType.class), eq(String.class)))
                .thenReturn(Optional.of(Collections.singletonList("SELECT")));

        when(sqlConsoleRuleService.isForbidden(anyString(), any())).thenReturn(true);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());

        assertFalse(result);
    }

    @Test
    public void testDoPreHandle_DenyExecution_ForbiddenSqlType() {
        ConnectionSession session = mockConnectionSession();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = Arrays.asList(mockSqlTuples());
        when(response.getSqls()).thenReturn(sqlTuples);

        when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenReturn(Optional.of(mockRule()));
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), eq(Integer.class)))
                .thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(anyLong(), anyString(), any(DialectType.class), eq(String.class)))
                .thenReturn(Optional.of(new HashSet<>(Arrays.asList("SELECT", "DROP TABLE"))));

        when(sqlConsoleRuleService.isForbidden(anyString(), any()))
                .thenReturn(sqlTuple -> sqlTuple.getSqlTuple().getAst().getParseResult().getSqlType() != SqlType.SELECT);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());

        assertFalse(result);
    }

    @Test
    public void testDoPreHandle_DenyExecution_ForbiddenCreatePL() throws SQLException {
        ConnectionSession session = mockConnectionSession();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = Arrays.asList(mockSqlTuples());
        when(response.getSqls()).thenReturn(sqlTuples);

        when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenReturn(Optional.of(mockRule()));
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), eq(Boolean.class)))
                .thenReturn(Optional.of(Boolean.FALSE));
        when(sqlConsoleRuleService.getListProperties(anyLong(), anyString(), any(DialectType.class), eq(String.class)))
                .thenReturn(Optional.of(new HashSet<>(Arrays.asList("CREATE PROCEDURE"))));

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName(), any()))
                .thenReturn(true);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());

        assertFalse(result);
    }

    @Test
    public void testDoAfterCompletion_AllowExport_NoRuleSet() {
        SqlExecuteResult result = new SqlExecuteResult();
        ConnectionSession session = mockConnectionSession();

        result.setAllowExport(sqlConsoleInterceptor.doAfterCompletion(result, session, new HashMap<>()));

        assertTrue(result.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_DenyExport_RuleSetPresent() {
        SqlExecuteResult result = new SqlExecuteResult();
        ConnectionSession session = mockConnectionSession();

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET.getRuleName(), any()))
                .thenReturn(true);

        result.setAllowExport(sqlConsoleInterceptor.doAfterCompletion(result, session, new HashMap<>()));

        assertFalse(result.isAllowExport());
    }

    @Test
    public void testGetExecuteStageName_SqlConsoleRuleStage() {
        String stageName = sqlConsoleInterceptor.getExecuteStageName();

        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE.getName(), stageName);
    }

    private ConnectionSession mockConnectionSession() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        return session;
    }

    private Rule mockRule() {
        Rule rule = mock(Rule.class);
        when(rule.getLevel()).thenReturn(1);
        when(rule.getProperties()).thenReturn(Collections.singletonMap("ruleName", "Rule1"));
        return rule;
    }

    private List<SqlTuple> mockSqlTuples() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM test");
        sqlTuple.setSqlId("TestId1");
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(sqlTuple);
        return sqlTuples;
    }

    private User mockUser() {
        User user = mock(User.class);
        when(user.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        return user;
    }

    private static class User {
        private OrganizationType organizationType;

        public OrganizationType getOrganizationType() {
            return organizationType;
        }

    }

}