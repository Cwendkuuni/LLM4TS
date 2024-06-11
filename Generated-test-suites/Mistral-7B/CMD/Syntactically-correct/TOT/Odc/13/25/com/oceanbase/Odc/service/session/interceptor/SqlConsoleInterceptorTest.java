package com.oceanbase.odc.service.session.interceptor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle_noRuleSetId_returnTrue() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        AtomicBoolean allowExecute = new AtomicBoolean(true);
        when(session.getRuleSetId()).thenReturn(null);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        Assertions.assertTrue(result);
        verify(session, times(1)).getRuleSetId();
        verifyNoInteractions(authenticationFacade, ruleService, sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_ruleSetId_returnTrue() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        AtomicBoolean allowExecute = new AtomicBoolean(true);

        Long ruleSetId = 1L;
        when(session.getRuleSetId()).thenReturn(ruleSetId);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        Assertions.assertTrue(result);
        verify(session, times(1)).getRuleSetId();
        verifyNoInteractions(authenticationFacade, ruleService, sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_ruleSetId_returnFalse_forbiddenToCreatePL() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        AtomicBoolean allowExecute = new AtomicBoolean(true);

        Long ruleSetId = 1L;
        when(session.getRuleSetId()).thenReturn(ruleSetId);
        when(session.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL)).thenReturn(true);

        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1L);
        sqlTuple.setOriginalSql("CREATE PROCEDURE p AS SELECT 1;");
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        Assertions.assertFalse(result);
        verify(session, times(1)).getRuleSetId();
        verify(session, times(1)).isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL);
        verifyNoInteractions(authenticationFacade, ruleService, sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_ruleSetId_returnFalse_maxSqlSizeExceeded() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        AtomicBoolean allowExecute = new AtomicBoolean(true);

        Long ruleSetId = 1L;
        when(session.getRuleSetId()).thenReturn(ruleSetId);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        Integer maxSqlSize = 1;
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS, any(), Integer.class))
                .thenReturn(Optional.of(maxSqlSize));

        SqlTuple sqlTuple1 = new SqlTuple();
        sqlTuple1.setSqlId(1L);
        sqlTuple1.setOriginalSql("SELECT * FROM table1;");
        List<SqlTuple> sqlTuples1 = new ArrayList<>();
        sqlTuples1.add(sqlTuple1);
        response.setSqls(sqlTuples1);

        Boolean sqlConsoleIntercepted = sqlConsoleInterceptor.handle(request, response, session, context);
        Assertions.assertFalse(sqlConsoleIntercepted);
        verify(session, times(1)).getRuleSetId();
        verify(session, times(1)).getProperties(eq(ruleSetId), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(), eq(Integer.class));
        verifyNoInteractions(authenticationFacade, ruleService, sqlConsoleRuleService);

        SqlTuple sqlTuple2 = new SqlTuple();
        sqlTuple2.setSqlId(2L);
        sqlTuple2.setOriginalSql("SELECT * FROM table2;");
        List<SqlTuple> sqlTuples2 = new ArrayList<>();
        sqlTuples2.add(sqlTuple2);
        response.setSqls(sqlTuples1.stream().concat(sqlTuples2.stream()));

        Boolean sqlConsoleIntercepted2 = sqlConsoleInterceptor.handle(request, response, session, context);
        Assertions.assertFalse(sqlConsoleIntercepted2);
        verify(session, times(1)).getRuleSetId();
        verify(session, times(1)).getProperties(eq(ruleSetId), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(), eq(Integer.class));
        verifyNoMoreInteractions(session, authenticationFacade, ruleService, sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_ruleSetId_returnTrue_noViolation() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        AtomicBoolean allowExecute = new AtomicBoolean(true);

        Long ruleSetId = 1L;
        when(session.getRuleSetId()).thenReturn(ruleSetId);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, any(), String.class))
                .thenReturn(Optional.of(new ArrayList<>()));

        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1L);
        sqlTuple.setOriginalSql("SELECT * FROM table1;");
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        Boolean sqlConsoleIntercepted = sqlConsoleInterceptor.handle(request, response, session, context);
        Assertions.assertTrue(sqlConsoleIntercepted);
        verify(session, times(1)).getRuleSetId();
        verifyNoInteractions(authenticationFacade, ruleService, sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_ruleSetId_returnTrue_allowSqlType() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        AtomicBoolean allowExecute = new AtomicBoolean(true);

        Long ruleSetId = 1L;
        when(session.getRuleSetId()).thenReturn(ruleSetId);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        List<String> allowSqlTypes = new ArrayList<>();
        allowSqlTypes.add(SqlType.DDL.name());
        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, any(), String.class))
                .thenReturn(Optional.of(allowSqlTypes));

        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1L);
        sqlTuple.setOriginalSql("CREATE TABLE table1 (id INT PRIMARY KEY);");
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        Boolean sqlConsoleIntercepted = sqlConsoleInterceptor.handle(request, response, session, context);
        Assertions.assertTrue(sqlConsoleIntercepted);
        verify(session, times(1)).getRuleSetId();
        verify(session, times(1)).getListProperties(eq(ruleSetId), eq(SqlConsoleRules.ALLOW_SQL_TYPES), any(), eq(String.class));
        verifyNoMoreInteractions(session, authenticationFacade, ruleService, sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_ruleSetId_returnFalse_violation() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        AtomicBoolean allowExecute = new AtomicBoolean(true);

        Long ruleSetId = 1L;
        when(session.getRuleSetId()).thenReturn(ruleSetId);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        List<String> allowSqlTypes = new ArrayList<>();
        allowSqlTypes.add(SqlType.DDL.name());
        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, any(), String.class))
                .thenReturn(Optional.of(allowSqlTypes));

        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1L);
        sqlTuple.setOriginalSql("CREATE TABLE table1 (id INT PRIMARY KEY, name VARCHAR(100));");
        List<SqlTuple> sqlTuples = new ArrayList<>();
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        List<Rule> violatedRules = new ArrayList<>();
        Rule rule = new Rule();
        RuleViolation violation = new RuleViolation();
        violation.setLevel(1);
        violation.setLocalizedMessage("The maximum number of SQLs allowed to be executed at once has been reached.");
        violation.setOffset(0);
        violation.setStart(0);
        violation.setStop(sqlTuple.getOriginalSql().length());
        rule.setViolation(violation);
        violatedRules.add(rule);
        when(ruleService.getByRulesetIdAndName(eq(ruleSetId), anyString())).thenReturn(Optional.of(rule));

        Boolean sqlConsoleIntercepted = sqlConsoleInterceptor.handle(request, response, session, context);
        Assertions.assertFalse(sqlConsoleIntercepted);
        verify(session, times(1)).getRuleSetId();
        verify(session, times(1)).getListProperties(eq(ruleSetId), eq(SqlConsoleRules.ALLOW_SQL_TYPES), any(), eq(String.class));
        verify(session, times(1)).getByRulesetIdAndName(eq(ruleSetId), anyString());
        verifyNoMoreInteractions(session, authenticationFacade, ruleService, sqlConsoleRuleService);
    }

    @Test
    public void testDoAfterCompletion_noRuleSetId_returnNull() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlExecuteResult response = new SqlExecuteResult();
        Map<String, Object> context = new HashMap<>();

        when(session.getRuleSetId()).thenReturn(null);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        Assertions.assertNull(response.getAllowExport());
        verify(session, times(1)).getRuleSetId();
        verifyNoInteractions(authenticationFacade, ruleService, sqlConsoleRuleService);
    }

    @Test
    public void testDoAfterCompletion_ruleSetId_returnFalse_forbiddenToExport() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlExecuteResult response = new SqlExecuteResult();
        Map<String, Object> context = new HashMap<>();

        Long ruleSetId = 1L;
        when(session.getRuleSetId()).thenReturn(ruleSetId);
        when(session.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET)).thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        Assertions.assertFalse(response.getAllowExport());
        verify(session, times(1)).getRuleSetId();
        verify(session, times(1)).isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET);
        verifyNoInteractions(authenticationFacade, ruleService, sqlConsoleRuleService);
    }

    @Test
    public void testDoAfterCompletion_ruleSetId_returnTrue() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlExecuteResult response = new SqlExecuteResult();
        Map<String, Object> context = new HashMap<>();

        Long ruleSetId = 1L;
        when(session.getRuleSetId()).thenReturn(ruleSetId);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        Assertions.assertTrue(response.getAllowExport());
        verify(session, times(1)).getRuleSetId();
        verifyNoInteractions(authenticationFacade, ruleService, sqlConsoleRuleService);
    }
}