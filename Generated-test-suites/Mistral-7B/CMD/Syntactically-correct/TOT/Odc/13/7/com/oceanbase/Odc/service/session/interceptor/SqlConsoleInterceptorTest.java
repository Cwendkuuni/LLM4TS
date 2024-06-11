package com.oceanbase.odc.service.session.interceptor;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtensions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
@Component
class SqlConsoleInterceptorTest {

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
    private Map<String, Object> context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();
        context = new HashMap<>();
    }

    @Test
    void testDoPreHandle_noRuleSetId_shouldReturnTrue() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        verify(sqlConsoleInterceptor).handle(request, response, session, context);
        Assertions.assertTrue(result);
    }

    @Test
    void testDoPreHandle_ruleSetId_shouldCallHandleMethod() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        sqlConsoleInterceptor.handle(request, response, session, context);
        verify(sqlConsoleInterceptor).doPreHandle(request, response, session, context);
    }

    @Test
    void testHandle_noViolation_shouldAllowExecute() throws SQLException {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        SqlTuplesWithViolation sqlTuplesWithViolation = new SqlTuplesWithViolation();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOffset(0);
        sqlTuple.setOriginalSql("SELECT * FROM users WHERE id = 1");
        sqlTuplesWithViolation.setSqlTuple(sqlTuple);
        List<Rule> violatedRules = Collections.emptyList();
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), any()))
                .thenReturn(Optional.empty());
        when(sqlConsoleRuleService.getListProperties(anyLong(), anyString(), any(DialectType.class), any()))
                .thenReturn(Optional.of(Collections.singletonList("DDL")));

        when(ruleService.getByRulesetIdAndName(anyLong(), anyString()))
                .thenReturn(Optional.empty());

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        verify(sqlConsoleInterceptor, times(1)).handle(request, response, session, context);
        Assertions.assertTrue(result);
    }

    @Test
    void testHandle_violation_shouldReturnFalse() throws SQLException {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        SqlTuplesWithViolation sqlTuplesWithViolation = new SqlTuplesWithViolation();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOffset(0);
        sqlTuple.setOriginalSql("CREATE PROCEDURE my_procedure()");
        sqlTuplesWithViolation.setSqlTuple(sqlTuple);
        List<Rule> violatedRules = Arrays.asList(mock(Rule.class));

        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), any()))
                .thenReturn(Optional.empty());
        when(sqlConsoleRuleService.getListProperties(anyLong(), anyString(), any(DialectType.class), any()))
                .thenReturn(Optional.empty());

        when(ruleService.getByRulesetIdAndName(anyLong(), anyString()))
                .thenReturn(Optional.of(mock(Rule.class)));

        doThrow(new RuntimeException("Test exception"))
                .when(sqlConsoleInterceptor)
                .handle(request, response, session, context);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        verify(sqlConsoleInterceptor, times(1)).handle(request, response, session, context);

        // Verify that the exception is not thrown
        Assertions.assertFalse(result);
    }

    @Test
    void testHandle_multipleViolations_shouldReturnFalse() throws SQLException {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        SqlTuplesWithViolation sqlTuplesWithViolation1 = new SqlTuplesWithViolation();
        SqlTuple sqlTuple1 = new SqlTuple();
        sqlTuple1.setOffset(0);
        sqlTuple1.setOriginalSql("SELECT * FROM users WHERE id = 1");
        sqlTuplesWithViolation1.setSqlTuple(sqlTuple1);
        List<Rule> violatedRules1 = Arrays.asList(mock(Rule.class), mock(Rule.class));

        SqlTuplesWithViolation sqlTuplesWithViolation2 = new SqlTuplesWithViolation();
        SqlTuple sqlTuple2 = new SqlTuple();
        sqlTuple2.setOffset(0);
        sqlTuple2.setOriginalSql("CREATE PROCEDURE my_procedure()");
        sqlTuplesWithViolation2.setSqlTuple(sqlTuple2);
        List<Rule> violatedRules2 = Collections.emptyList();

        List<SqlTuplesWithViolation> sqlTuplesWithViolations = Arrays.asList(sqlTuplesWithViolation1, sqlTuplesWithViolation2);
        response.setSqls(sqlTuplesWithViolations);

        Map<String, BasicResult> sqlId2BasicResult = new HashMap<>();
        sqlId2BasicResult.put("1", mock(BasicResult.class));

        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), any(DialectType.class), any()))
                .thenReturn(Optional.empty());
        when(sqlConsoleRuleService.getListProperties(anyLong(), anyString(), any(DialectType.class), any()))
                .thenReturn(Optional.of(Collections.singletonList("DDL")));

        when(ruleService.getByRulesetIdAndName(anyLong(), anyString()))
                .thenAnswer(invocation -> {
                    Rule rule = new Rule();
                    RuleViolation violation1 = new RuleViolation();
                    violation1.setLevel(1);
                    violation1.setLocalizedMessage("Test violation 1");
                    violation1.setOffset(sqlTuple1.getOffset());
                    violation1.setStart(0);
                    violation1.setStop(sqlTuple1.getOriginalSql().length());
                    violation1.setRuleName("Test rule 1");
                    violation1.setSql(sqlTuple1.getOriginalSql());
                    rule.setViolation(violation1);

                    RuleViolation violation2 = new RuleViolation();
                    violation2.setLevel(1);
                    violation2.setLocalizedMessage("Test violation 2");
                    violation2.setOffset(sqlTuple2.getOffset());
                    violation2.setStart(0);
                    violation2.setStop(sqlTuple2.getOriginalSql().length());
                    violation2.setRuleName("Test rule 2");
                    violation2.setSql(sqlTuple2.getOriginalSql());
                    rule.setViolation(violation2);
                    return Optional.of(rule);
                });

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        verify(sqlConsoleInterceptor, times(1)).handle(request, response, session, context);

        // Verify that the result is false
        Assertions.assertFalse(result);
    }

    @Test
    void testDoAfterCompletion_noViolation_shouldAllowExport() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        sqlConsoleInterceptor.doAfterCompletion(new SqlExecuteResult(), session, context);

        verify(sqlConsoleInterceptor, times(1)).doAfterCompletion(any(SqlExecuteResult.class), any(ConnectionSession.class), anyMap());
        Assertions.assertTrue(context.getOrDefault(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED, false));
        Assertions.assertTrue(response.isAllowExport());
    }

    @Test
    void testDoAfterCompletion_violation_shouldNotAllowExport() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        when(sqlConsoleRuleService.isForbidden(anyString(), any()))
                .thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(new SqlExecuteResult(), session, context);

        verify(sqlConsoleInterceptor, times(1)).doAfterCompletion(any(SqlExecuteResult.class), any(ConnectionSession.class), anyMap());
        Assertions.assertTrue(context.getOrDefault(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED, false));
        Assertions.assertFalse(response.isAllowExport());
    }

    @Test
    void testGetExecuteStageName_shouldReturnExpectedStageName() {
        String result = sqlConsoleInterceptor.getExecuteStageName();
        Assertions.assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, result);
    }

    @Test
    void testGetOrder_shouldReturnExpectedOrder() {
        int result = sqlConsoleInterceptor.getOrder();
        Assertions.assertEquals(2, result);
    }
}