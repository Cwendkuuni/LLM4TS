package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResp;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuplesWithViolation;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation.SqlTuple;
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

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle_NoRulesetId_ReturnsTrue() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        doReturn(Optional.empty()).when(sqlConsoleRuleService).getProperties(any(), any(), any(), any());
        doReturn(true).when(sqlConsoleInterceptor).handle(request, response, session, context);
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_RulesetIdExists_ReturnsFalse() {
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Long ruleSetId = 1L;
        doReturn(Optional.of(ruleSetId)).when(sqlConsoleRuleService).getProperties(any(), any(), any(), any());
        doReturn(false).when(sqlConsoleInterceptor).handle(request, response, session, context);
        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testHandle_NoViolations_ReturnsTrue() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        doReturn(Optional.of(false)).when(sqlConsoleInterceptor).isIndividualTeam();
        doReturn(Optional.empty()).when(sqlConsoleRuleService).getProperties(anyLong(), anyString(), any(), any());
        doReturn(new ArrayList<>()).when(response).getSqls();
        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_ForbiddenToCreatePl_ReturnsFalse() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        doReturn(Optional.of(true)).when(sqlConsoleInterceptor).isIndividualTeam();
        doReturn(Optional.of(true)).when(sqlConsoleRuleService).isForbidden(anyString(), session);
        doReturn(new ArrayList<>()).when(response).getSqls();
        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_ForbiddenSqlType_ReturnsFalse() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        doReturn(Optional.of(true)).when(sqlConsoleInterceptor).isIndividualTeam();
        doReturn(Optional.of(List.of("FORBIDDEN_SQL_TYPE"))).when(sqlConsoleRuleService)
                .getListProperties(anyLong(), anyString(), any(), any());
        doReturn(new BasicResult(SqlType.INSERT)).when(sqlConsoleInterceptor).determineSqlType(any(SqlTuple.class), any());
        doReturn(new ArrayList<>()).when(response).getSqls();
        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_SyntaxError_ReturnsFalse() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        doReturn(Optional.of(true)).when(sqlConsoleInterceptor).isIndividualTeam();
        doReturn(new ArrayList<>()).when(response).getSqls();
        doReturn(new BasicResult(SqlType.INSERT)).when(sqlConsoleInterceptor)
                .determineSqlType(any(SqlTuple.class), any());
        doReturn(true).when(sqlConsoleInterceptor).handle(request, response, session, context);
        SqlTuple sqlTuple = mock(SqlTuple.class);
        doReturn(sqlTuple).when(response).getSqls().get(0);
        doThrow(new SyntaxErrorException("Syntax error"))
                .when(sqlTuple)
                .initAst(AbstractSyntaxTreeFactories.getAstFactory(any(DialectType.class), anyInt()));
        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_MaxSqlSizeExceeded_ReturnsFalse() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        doReturn(Optional.of(true)).when(sqlConsoleInterceptor).isIndividualTeam();
        doReturn(Optional.of(1)).when(sqlConsoleRuleService).getProperties(anyLong(), anyString(), any(), anyInt());
        doReturn(new ArrayList<>()).when(response).getSqls();
        doReturn(new BasicResult(SqlType.SELECT, null, null, null, null, null, null, null, new Object[0], null, false))
                .when(sqlConsoleInterceptor)
                .determineSqlType(any(SqlTuple.class), any());
        doReturn(List.of(new Rule()))
                .when(ruleService)
                .getByRulesetIdAndName(anyLong(), anyString());
        doReturn(new ArrayList<>()).when(sqlConsoleInterceptor).getExecutedRules(any(SqlAsyncExecuteResp.class), any());
        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletion_NoViolations_ReturnsNull() {
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        doReturn(new SqlExecuteResult(SqlExecuteStatus.SUCCESS, null, null, null, null, null, null, null, null, null))
                .when(response).getResult();
        assertNull(sqlConsoleInterceptor.doAfterCompletion(response, session, context));
    }

    @Test
    public void testDoAfterCompletion_ViolationsExist_ReturnsRule() {
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult(SqlExecuteStatus.SUCCESS, null, null, null,
                List.of(new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM test", null), List.of(new Rule()))),
                null, null, null, null, null, null);
        doReturn(sqlExecuteResult).when(response).getResult();
        Rule rule = new Rule();
        RuleViolation violation = new RuleViolation();
        violation.setLevel("ERROR");
        violation.setLocalizedMessage("Message for violation");
        rule.setViolation(violation);
        doReturn(List.of(rule)).when(sqlConsoleInterceptor).getExecutedRules(response);
        assertEquals(rule, sqlConsoleInterceptor.doAfterCompletion(response, session, context));
    }

    @Test
    public void testGetExecuteStageName_ReturnsName() {
        assertEquals("SQL_CONSOLE_RULE", sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testIsIndividualTeam_ReturnsTrue() {
        assertTrue(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testIsIndividualTeam_ReturnsFalse() {
        assertFalse(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType_ParsesCorrectly() {
        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM test", null);
        BasicResult expectedResult = new BasicResult(SqlType.SELECT, null, null, null, null, null, null, null, null, null, null,
                null);
        assertEquals(expectedResult, sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL));
    }

    @Test
    public void testDetermineSqlType_ReturnsUnknownType() {
        SqlTuple sqlTuple = new SqlTuple("CREATE TABLE test (id INT PRIMARY KEY)", null);
        BasicResult expectedResult = new BasicResult(SqlType.UNKNOWN);
        assertEquals(expectedResult, sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL));
    }
}