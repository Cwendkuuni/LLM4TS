package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.exception.OceanbaseException;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.model.SqlTuple;
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
import com.oceanbase.tools.sqlparser.parser.AbstractSyntaxTree;
import com.oceanbase.tools.sqlparser.parser.AbstractSyntaxTreeFactories;

@Slf4j
public class SqlConsoleInterceptorTest {

    @Mock
    private ConnectionSession session;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @InjectMocks
    private SqlConsoleInterceptor interceptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(authenticationFacade.currentUser()).thenReturn(new User());
        interceptor.setAuthenticationFacade(authenticationFacade);
        interceptor.setRuleService(ruleService);
        interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testHandle_allowedExecution() throws OceanbaseException, SQLException {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getRuleSetId()).thenReturn(1L);

        interceptor.handle(request, response, session, Collections.emptyMap());

        verify(sqlConsoleRuleService, times(2)).getProperties(1L, any(), eq(DialectType.MYSQL), anyClass());
        verify(session, times(1)).getRuleSetId();
        verify(session, times(1)).getDialectType();
        verify(response, times(1)).setSqls(anyList());

        SqlTuplesWithViolation sqlTuples = new SqlTuplesWithViolation();
        sqlTuples.setSqlTuple(new SqlTuple(1L, "SELECT * FROM table1", null, null));
        response.setSqls(Arrays.asList(sqlTuples));

        interceptor.handle(request, response, session, Collections.emptyMap());

        verify(sqlConsoleRuleService, times(1)).isForbidden(anyString(), eq(session));
        verify(sqlConsoleRuleService, times(1)).getListProperties(1L, anyString(), eq(DialectType.MYSQL), anyClass());
        verify(sqlTuple, times(1)).getOriginalSql();
        verify(sqlTuple, times(1)).getSqlId();
        verify(sqlTuple, times(1)).getOffset();
    }

    @Test
    public void testHandle_forbiddenExecution_maxSqlSize() throws OceanbaseException, SQLException {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getRuleSetId()).thenReturn(1L);

        List<Rule> violatedRules = new ArrayList<>();
        Rule rule = new Rule();
        RuleViolation violation = new RuleViolation();
        violation.setLevel(RuleLevel.ERROR);
        violation.setLocalizedMessage("Maximum number of SQLs exceeded");
        violation.setOffset(0);
        violation.setStart(0);
        violation.setStop(0);
        rule.setViolation(violation);
        violatedRules.add(rule);

        doReturn(Optional.of(10)).when(sqlConsoleRuleService).getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                DialectType.MYSQL, Integer.class);
        doReturn(Optional.of(Arrays.asList("SELECT * FROM table1", "SELECT * FROM table2")))
                .when(response).getSqls();

        interceptor.handle(request, response, session, Collections.emptyMap());

        RuleViolation violationExpected = new RuleViolation();
        violationExpected.setLevel(RuleLevel.ERROR);
        violationExpected.setLocalizedMessage("Maximum number of SQLs exceeded");
        violationExpected.setOffset(0);
        violationExpected.setStart(0);
        violationExpected.setStop(0);
        Rule ruleExpected = new Rule();
        ruleExpected.setViolation(violationExpected);
        List<Rule> violatedRulesExpected = Arrays.asList(ruleExpected);

        verify(sqlConsoleRuleService, times(1)).isForbidden(anyString(), eq(session));
        verify(sqlConsoleRuleService, times(1)).getListProperties(1L, anyString(), eq(DialectType.MYSQL), anyClass());
        verify(sqlConsoleRuleService, times(1)).getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                DialectType.MYSQL, Integer.class);
        verify(sqlConsoleRuleService, times(1)).getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                DialectType.MYSQL, Integer.class);
        verify(authenticationFacade, times(1)).currentUser();
        verify(ruleService, times(1)).getByRulesetIdAndName(1L, anyString());
        verify(response, times(1)).setSqls(violatedRulesExpected);
    }

    @Test
    public void testHandle_forbiddenExecution_maxSqlSize_noViolation() throws OceanbaseException, SQLException {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getRuleSetId()).thenReturn(1L);

        List<Rule> violatedRules = new ArrayList<>();
        Rule rule = new Rule();
        RuleViolation violation = new RuleViolation();
        violation.setLevel(RuleLevel.ERROR);
        violation.setLocalizedMessage("Maximum number of SQLs exceeded");
        violation.setOffset(0);
        violation.setStart(0);
        violation.setStop(0);
        rule.setViolation(violation);
        violatedRules.add(rule);

        doReturn(Optional.of(10)).when(sqlConsoleRuleService).getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                DialectType.MYSQL, Integer.class);
        doReturn(Optional.of(11)).when(response).getSqls().size();

        interceptor.handle(request, response, session, Collections.emptyMap());

        RuleViolation violationExpected = new RuleViolation();
        violationExpected.setLevel(RuleLevel.ERROR);
        violationExpected.setLocalizedMessage("Maximum number of SQLs exceeded");
        violationExpected.setOffset(0);
        violationExpected.setStart(0);
        violationExpected.setStop(0);
        Rule ruleExpected = new Rule();
        ruleExpected.setViolation(violationExpected);
        List<Rule> violatedRulesExpected = Arrays.asList(ruleExpected);

        verify(sqlConsoleRuleService, times(1)).isForbidden(anyString(), eq(session));
        verify(sqlConsoleRuleService, times(1)).getListProperties(1L, anyString(), eq(DialectType.MYSQL), anyClass());
        verify(sqlConsoleRuleService, times(1)).getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                DialectType.MYSQL, Integer.class);
        verify(authenticationFacade, times(1)).currentUser();
        verify(ruleService, times(0)).getByRulesetIdAndName(1L, anyString());
        verify(response, times(1)).setSqls(violatedRulesExpected);
    }

    @Test
    public void testHandle_forbiddenExecution_sqlType() {
        // TODO: Implement this test
    }

    @Test
    public void testDoAfterCompletion_allowedExport() throws OceanbaseException, SQLException {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        when(session.getRuleSetId()).thenReturn(1L);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.isAllowedToWrite()).thenReturn(true);

        interceptor.doAfterCompletion(result, session, Collections.emptyMap());

        verify(result, times(1)).setAllowExport(true);
    }

    @Test
    public void testDoAfterCompletion_notAllowedExport() throws OceanbaseException, SQLException {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        when(session.getRuleSetId()).thenReturn(1L);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.isAllowedToWrite()).thenReturn(false);

        interceptor.doAfterCompletion(result, session, Collections.emptyMap());

        verify(result, times(1)).setAllowExport(false);
    }

    @Test
    public void testDoAfterCompletion_notAllowedEditResultSet() throws OceanbaseException, SQLException {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        result.setResultSetMetaData(new Object());
        when(session.getRuleSetId()).thenReturn(1L);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session))
                .thenReturn(true);

        interceptor.doAfterCompletion(result, session, Collections.emptyMap());

        verify(result, times(1)).getResultSetMetaData().setEditable(false);
    }

    @Test
    public void testDoAfterCompletion_allowedEditResultSet() throws OceanbaseException, SQLException {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        result.setResultSetMetaData(new Object());
        when(session.getRuleSetId()).thenReturn(1L);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session))
                .thenReturn(false);

        interceptor.doAfterCompletion(result, session, Collections.emptyMap());

        verify(result, times(1)).getResultSetMetaData().setEditable(true);
    }
}