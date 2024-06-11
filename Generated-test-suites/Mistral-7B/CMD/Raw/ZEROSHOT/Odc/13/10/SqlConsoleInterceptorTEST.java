package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
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
import com.oceanbase.odc.service.regulation.ruleset.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
    }

    @Test
    public void testDoPreHandle_true() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(10));

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, Collections.emptyMap()));
    }

    @Test
    public void testDoPreHandle_false() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.empty());

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, Collections.emptyMap()));
    }

    @Test
    public void testHandle_true() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple("id:1, sql:create table test (id int)", 1, "create table test (id int)");
        sqlTuples.add(sqlTuple);
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(1));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        AtomicReference<Rule> maxSqlSizeRule = new AtomicReference<>();

        SqlConsoleInterceptor interceptor = spy(sqlConsoleInterceptor);
        doAnswer(invocationOnMock -> {
            Rule rule = new Rule();
            RuleViolation violation = new RuleViolation();
            violation.setLevel(RuleViolation.Level.ERROR);
            violation.setOffset(sqlTuple.getOffset());
            violation.setStart(0);
            violation.setStop(sqlTuple.getOriginalSql().length());
            rule.setViolation(violation);
            maxSqlSizeRule.set(rule);
            return null;
        }).when(interceptor).getRuleByRuleName(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());

        doAnswer(invocationOnMock -> {
            Rule rule = new Rule();
            RuleViolation violation = new RuleViolation();
            violation.setLevel(RuleViolation.Level.ERROR);
            violation.setOffset(sqlTuple.getOffset());
            violation.setStart(0);
            violation.setStop(sqlTuple.getOriginalSql().length());
            rule.setViolation(violation);
            return rule;
        }).when(ruleService).getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());

        doReturn(sqlTuples).when(response).getSqls();

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap()));
        verify(interceptor).getRuleByRuleName(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
        verify(ruleService).getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
        verifyNoMoreInteractions(interceptor);
        verifyNoMoreInteractions(ruleService);
    }

    @Test
    public void testHandle_false() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple("id:1, sql:create table test (id int)", 1, "create table test (id int)");
        sqlTuples.add(sqlTuple);
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.empty());
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        AtomicReference<Rule> maxSqlSizeRule = new AtomicReference<>();

        SqlConsoleInterceptor interceptor = spy(sqlConsoleInterceptor);
        doAnswer(invocationOnMock -> {
            Rule rule = new Rule();
            RuleViolation violation = new RuleViolation();
            violation.setLevel(RuleViolation.Level.ERROR);
            violation.setOffset(sqlTuple.getOffset());
            violation.setStart(0);
            violation.setStop(sqlTuple.getOriginalSql().length());
            rule.setViolation(violation);
            maxSqlSizeRule.set(rule);
            return null;
        }).when(interceptor).getRuleByRuleName(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());

        doAnswer(invocationOnMock -> {
            Rule rule = new Rule();
            RuleViolation violation = new RuleViolation();
            violation.setLevel(RuleViolation.Level.ERROR);
            violation.setOffset(sqlTuple.getOffset());
            violation.setStart(0);
            violation.setStop(sqlTuple.getOriginalSql().length());
            rule.setViolation(violation);
            return rule;
        }).when(ruleService).getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());

        doReturn(sqlTuples).when(response).getSqls();

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap()));
        verify(interceptor).getRuleByRuleName(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
        verify(ruleService).getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
        verify(interceptor).getRuleByRuleName(SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName());
        verify(ruleService).getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName());
    }

    @Test
    public void testDoAfterCompletion_true() {
        SqlExecuteResult response = new SqlExecuteResult();
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(response, session, Collections.emptyMap());
        verifyNoInteractions(sqlConsoleRuleService);
    }

    @Test
    public void testDoAfterCompletion_false() {
        SqlExecuteResult response = new SqlExecuteResult();
        when(authenticationFacade.currentUser()).thenReturn(mock(Object.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(response, session, Collections.emptyMap());
        verify(response).setEditable(false);
        verifyNoMoreInteractions(sqlConsoleRuleService);
    }
}