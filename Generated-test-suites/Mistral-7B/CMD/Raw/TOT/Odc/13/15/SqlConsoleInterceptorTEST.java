import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testDoPreHandle_ValidQuery_True() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        when(authenticationFacade.currentUser()).thenReturn(new Object());
        doReturn(Optional.empty()).when(sqlConsoleRuleService).getProperties(anyLong(), anyString(), any(), anyClass());
        when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenReturn(Optional.empty());
        when(ruleService.getByName(anyString())).thenReturn(Optional.empty());
        when(sqlConsoleRuleService.isForbidden(anyString(), any())).thenReturn(false);
        when(sqlExecuteResult.getResultSetMetaData()).thenReturn(null);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        Assertions.assertTrue(result);
        verify(sqlConsoleInterceptor, times(1)).handle(request, response, session, context);
    }

    @Test
    void testDoPreHandle_InvalidQuery_False() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.ERROR);
        when(authenticationFacade.currentUser()).thenReturn(new Object());
        doReturn(Optional.of(1)).when(sqlConsoleRuleService).getProperties(anyLong(), anyString(), any(), anyClass());
        Rule rule = mock(Rule.class);
        when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenReturn(Optional.of(rule));
        when(rule.getLevel()).thenReturn(1);
        when(rule.getProperties().get(0).getValue()).thenReturn("MAX_EXECUTE_SQLS");
        when(rule.getProperties().get(1).getValue()).thenReturn("10");
        when(sqlConsoleRuleService.isForbidden(anyString(), any())).thenReturn(true);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        Assertions.assertFalse(result);
        verify(sqlConsoleInterceptor, never()).handle(request, response, session, context);
    }

    @Test
    void testDoPreHandle_NoRulesetId_True() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        when(authenticationFacade.currentUser()).thenReturn(new Object());
        doReturn(Optional.empty()).when(sqlConsoleRuleService).getProperties(anyLong(), anyString(), any(), anyClass());
        doReturn(Optional.empty()).when(ruleService).getByRulesetIdAndName(anyLong(), anyString());

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        Assertions.assertTrue(result);
        verify(sqlConsoleInterceptor, never()).handle(request, response, session, context);
    }

    @Test
    void testHandle_ValidQuery_AllowExecute() throws Exception {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        when(authenticationFacade.currentUser()).thenReturn(new Object());
        doReturn(Optional.empty()).when(sqlConsoleRuleService).getProperties(anyLong(), anyString(), any(), anyClass());
        doReturn(sqlTuples).when(response).getSqls();
        doReturn(Optional.of(1)).when(sqlConsoleRuleService).getProperties(anyLong(), anyString(), any(), Integer.class);
        doReturn(ruleService).when(sqlConsoleInterceptor).getRuleService();
        Rule rule = mock(Rule.class);
        when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenReturn(Optional.of(rule));
        when(rule.getLevel()).thenReturn(0);
        when(rule.getProperties().get(0).getValue()).thenReturn("MAX_EXECUTE_SQLS");
        when(rule.getProperties().get(1).getValue()).thenReturn("10");
        when(sqlConsoleRuleService.isForbidden(anyString(), any())).thenReturn(false);
        when(ruleService.getByName(anyString())).thenReturn(Optional.of(rule));
        when(rule.getMetadata().getPropertyMetadatas().get(0).getName()).thenReturn("MAX_EXECUTE_SQLS");
        when(rule.getViolations()).thenReturn(Arrays.asList(rule));

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        Assertions.assertTrue(result);
        verify(sqlConsoleInterceptor,times(1)).determineSqlType(any(SqlTuple.class), any(DialectType.class));
        verify(response, times(1)).setSqls(eq(sqlTuples));
        verify(response, times(1)).setViolatedRules(eq(Arrays.asList(rule)));
    }

    @Test
    void testHandle_ValidQuery_ForbiddenExecute() throws Exception {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        when(authenticationFacade.currentUser()).thenReturn(new Object());
        doReturn(Optional.empty()).when(sqlConsoleRuleService).getProperties(anyLong(), anyString(), any(), anyClass());
        doReturn(sqlTuples).when(response).getSqls();
        doReturn(Optional.of(1)).when(sqlConsoleRuleService).getProperties(anyLong(), anyString(), any(), Integer.class);
        doReturn(ruleService).when(sqlConsoleInterceptor).getRuleService();
        Rule rule = mock(Rule.class);
        when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenReturn(Optional.of(rule));
        when(rule.getLevel()).thenReturn(1);
        when(rule.getProperties().get(0).getValue()).thenReturn("MAX_EXECUTE_SQLS");
        when(rule.getProperties().get(1).getValue()).thenReturn("10");
        when(sqlConsoleRuleService.isForbidden(anyString(), any())).thenReturn(true);
        when(ruleService.getByName(anyString())).thenReturn(Optional.of(rule));
        when(rule.getMetadata().getPropertyMetadatas().get(0).getName()).thenReturn("MAX_EXECUTE_SQLS");
        when(rule.getViolations()).thenReturn(Arrays.asList(rule));

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        Assertions.assertFalse(result);
        verify(response, never()).setSqls(eq(sqlTuples));
        verify(response, never()).setViolatedRules(eq(Arrays.asList(rule)));
    }

    @Test
    void testHandle_InvalidQuery_NoViolations() throws Exception {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        when(authenticationFacade.currentUser()).thenReturn(new Object());
        doReturn(Optional.empty()).when(sqlConsoleRuleService).getProperties(anyLong(), anyString(), any(), anyClass());
        doReturn(Optional.empty()).when(sqlConsoleRuleService).getListProperties(anyLong(), anyString(), any(), anyClass());
        doReturn(sqlTuples).when(response).getSqls();
        doReturn(ruleService).when(sqlConsoleInterceptor).getRuleService();
        when(ruleService.getByName(anyString())).thenReturn(Optional.empty());

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        Assertions.assertTrue(result);
        verify(response, never()).setSqls(eq(sqlTuples));
        verify(response, never()).setViolatedRules(any());
    }

    @Test
    void testHandle_InvalidQuery_Violations() throws Exception {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        when(authenticationFacade.currentUser()).thenReturn(new Object());
        doReturn(Optional.empty()).when(sqlConsoleRuleService).getProperties(anyLong(), anyString(), any(), anyClass());
        doReturn(Optional.of(1)).when(sqlConsoleRuleService).getListProperties(anyLong(), anyString(), any(), String.class);
        doReturn(sqlTuples).when(response).getSqls();
        doReturn(ruleService).when(sqlConsoleInterceptor).getRuleService();
        Rule rule = mock(Rule.class);
        RuleViolation violation = mock(RuleViolation.class);
        when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenReturn(Optional.of(rule));
        when(rule.getLevel()).thenReturn(1);
        when(rule.getProperties().get(0).getValue()).thenReturn("ALLOW_SQL_TYPES");
        when(rule.getProperties().get(1).getValue()).thenReturn("SELECT");
        when(sqlConsoleRuleService.isForbidden(anyString(), any())).thenReturn(false);
        when(ruleService.getByName(anyString())).thenReturn(Optional.of(rule));
        when(violation.getLevel()).thenReturn(1);
        when(violation.getLocalizedMessage()).thenReturn("Test Violation Message");
        when(rule.getViolations()).thenReturn(Arrays.asList(violation));

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        Assertions.assertFalse(result);
        verify(response, times(1)).setSqls(eq(sqlTuples));
        verify(response, times(1)).setViolatedRules(eq(Arrays.asList(rule)));
    }

    @Test
    void testDetermineSqlType_ValidQuery_CorrectType() throws Exception {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlTuple sqlTuple = new SqlTuple();
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sqlTuple.getSqlId()).thenReturn(1);
        when(sqlTuple.getAst()).thenReturn(null);
        BasicResult expectedResult = new BasicResult(SqlType.SELECT);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, session.getDialectType());
        Assertions.assertEquals(expectedResult, result);
        verify(sqlConsoleInterceptor, times(1)).initAst(any(ConnectionSession.class), anyInt());
    }

    @Test
    void testDetermineSqlType_InvalidQuery_UnknownType() throws Exception {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlTuple sqlTuple = new SqlTuple();
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sqlTuple.getSqlId()).thenReturn(1);
        when(sqlTuple.getAst()).thenReturn(null);
        doThrow(new Exception()).when(sqlConsoleInterceptor).initAst(any(ConnectionSession.class), anyInt());
        BasicResult expectedResult = new BasicResult(SqlType.UNKNOWN);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, session.getDialectType());
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void testGetRuleService() {
        when(ruleService).thenReturn(ruleService);
        RuleService result = sqlConsoleInterceptor.getRuleService();
        Assertions.assertSame(ruleService, result);
        verify(sqlConsoleInterceptor, times(1)).ruleService;
    }
}