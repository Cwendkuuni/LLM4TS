package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
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
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

/**
 * Test class for SqlConsoleInterceptor
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
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(authenticationFacade.currentUser()).thenReturn(mockUser());
        when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenAnswer(new Answer<Optional<Rule>>() {
            @Override
            public Optional<Rule> answer(InvocationOnMock invocation) throws Throwable {
                String ruleName = (String) invocation.getArguments()[1];
                if (SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName().equals(ruleName)) {
                    Rule mockRule = mock(Rule.class);
                    when(mockRule.getLevel()).thenReturn(1);
                    when(mockRule.getProperties()).thenReturn(new HashMap<>());
                    return Optional.of(mockRule);
                }
                return Optional.empty();
            }
        });
    }

    @Test
    public void testHandleWithMaxExecuteSqlsRule() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqls = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple("SELECT 1 FROM DUAL;", 1, 1, 1);
        sqlTuple.setOffset(0);
        sqls.add(new SqlTuplesWithViolation(sqlTuple, new ArrayList<>()));
        response.setSqls(sqls);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        sqlConsoleInterceptor.handle(request, response, session, context);

        verify(ruleService, times(1)).getByRulesetIdAndName(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
        verify(ruleService, never()).getByRulesetIdAndName(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName());
    }

    @Test
    public void testHandleWithAllowSqlTypesRule() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqls = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple("SELECT 1 FROM DUAL;", 1, 1, 1);
        sqlTuple.setOffset(0);
        sqls.add(new SqlTuplesWithViolation(sqlTuple, new ArrayList<>()));
        response.setSqls(sqls);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, any(), anyInt()))
                .thenReturn(Optional.of(new ArrayList<>()));

        sqlConsoleInterceptor.handle(request, response, session, context);

        verify(ruleService, never()).getByRulesetIdAndName(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
        verify(ruleService, times(1)).getByRulesetIdAndName(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName());
    }

    @Test
    public void testHandleWithAllowedQueryLimit() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQueryLimit(500);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqls = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple("SELECT 1 FROM DUAL;", 1, 1, 1);
        sqlTuple.setOffset(0);
        sqls.add(new SqlTuplesWithViolation(sqlTuple, new ArrayList<>()));
        response.setSqls(sqls);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, any(), anyInt()))
                .thenReturn(Optional.of(100));

        sqlConsoleInterceptor.handle(request, response, session, context);

        assertEquals(100, request.getQueryLimit());
    }

    @Test
    public void testHandleWithForbiddenCreatePl() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqls = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple("CREATE PROCEDURE procedure_name()", 1, 1, 1);
        sqlTuple.setOffset(0);
        sqls.add(new SqlTuplesWithViolation(sqlTuple, new ArrayList<>()));
        response.setSqls(sqls);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        sqlConsoleInterceptor.handle(request, response, session, context);

        verify(sqlConsoleRuleService, times(1)).isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session);
    }

    @Test
    public void testHandleWithAllowedSqlType() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqls = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple("SELECT 1 FROM DUAL;", 1, 1, 1);
        sqlTuple.setOffset(0);
        sqls.add(new SqlTuplesWithViolation(sqlTuple, new ArrayList<>()));
        response.setSqls(sqls);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, any(), anyInt()))
                .thenReturn(Optional.of(new ArrayList<>()));
        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, any(), anyInt()))
                .thenReturn(Optional.of(List.of("DDL")));

        sqlConsoleInterceptor.handle(request, response, session, context);

        verify(sqlConsoleRuleService, times(2)).getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, any(),
                anyInt());
    }

    @Test
    public void testDoAfterCompletionWithForbiddenExportResultSet() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        verify(response, never()).setAllowExport(true);
        verify(response, times(1)).setAllowExport(false);
    }

    @Test
    public void testDoAfterCompletionWithAllowedExportResultSet() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        verify(response, times(1)).setAllowExport(true);
    }

    private User mockUser() {
        User user = new User();
        user.setOrganizationType(OrganizationType.TEAM);
        return user;
    }

}

class User {
    private OrganizationType organizationType;

    public OrganizationType getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(OrganizationType organizationType) {
        this.organizationType = organizationType;
    }
}