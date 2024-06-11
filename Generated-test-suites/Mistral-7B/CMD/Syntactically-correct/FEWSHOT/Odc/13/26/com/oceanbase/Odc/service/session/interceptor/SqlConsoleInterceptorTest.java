package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@Slf4j
public class SqlConsoleInterceptorTest {

    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private ConnectionSession connectionSession;
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setup() {
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        connectionSession = mock(ConnectionSession.class);
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_ReturnTrue_WhenRuleSetIdIsNull() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(connectionSession.getRuleSetId()).thenReturn(null);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, new HashMap<>());
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_ReturnTrue_WhenIsIndividualTeam() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(connectionSession.getRuleSetId()).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, new HashMap<>());
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_ReturnFalse_WhenNeedSqlConsoleCheckIsFalse() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(connectionSession.getRuleSetId()).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(false);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, new HashMap<>());
        assertFalse(result);
    }

    @Test
    public void testHandle_ReturnTrue_WhenRuleSetIdIsNull() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle_ReturnTrue_WhenRuleSetIdIsNotNullButQueryLimitIsNull() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(connectionSession.getRuleSetId()).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);

        Optional<Integer> queryLimitOptional = Optional.empty();
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.ORACLE,
                Integer.class)).thenReturn(queryLimitOptional);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle_ReturnFalse_WhenMaxSqlSizeIsReached() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(connectionSession.getRuleSetId()).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.ORACLE,
                Integer.class)).thenReturn(Optional.of(1));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1);
        sqlTuple.setOriginalSql("test sql");
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testHandle_ReturnFalse_WhenSqlTypeIsForbidden() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(connectionSession.getRuleSetId()).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.ORACLE,
                String.class)).thenReturn(Optional.of(List.of("DDL")));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1);
        sqlTuple.setOriginalSql("CREATE TABLE test (id INT PRIMARY KEY)");
        sqlTuple.setAst(mock(AbstractSyntaxTree.class));
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testHandle_ReturnFalse_WhenSyntaxError() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(connectionSession.getRuleSetId()).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1);
        sqlTuple.setOriginalSql("test sql");
        sqlTuple.setAst(mock(AbstractSyntaxTree.class));
        sqlTuple.setParseResult(new BasicResult(SqlType.UNKNOWN));
        sqlTuple.setSyntaxError(true);
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testHandle_ReturnTrue_WhenSqlTypeIsAllowed() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(connectionSession.getRuleSetId()).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.ORACLE,
                String.class)).thenReturn(Optional.of(List.of("DDL", "DML")));

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1);
        sqlTuple.setOriginalSql("CREATE TABLE test (id INT PRIMARY KEY)");
        sqlTuple.setAst(mock(AbstractSyntaxTree.class));
        sqlTuple.setParseResult(new BasicResult(SqlType.DDL));
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDetermineSqlType_ReturnBasicResult_WhenSyntaxError() {
        SqlTuple sqlTuple = new SqlTuple();
        when(sqlTuple.getAst()).thenReturn(mock(AbstractSyntaxTree.class));
        when(sqlTuple.getParseResult().isSyntaxError()).thenReturn(true);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.ORACLE);
        assertEquals(SqlType.UNKNOWN, result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_ReturnBasicResult_WhenNotDdlOrDml() {
        SqlTuple sqlTuple = new SqlTuple();
        when(sqlTuple.getAst()).thenReturn(mock(AbstractSyntaxTree.class));
        when(sqlTuple.getParseResult().getSqlType()).thenReturn(SqlType.OTHERS);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.ORACLE);
        assertEquals(SqlType.OTHERS, result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_ReturnBasicResult_WhenDdlOrDml() {
        SqlTuple sqlTuple = new SqlTuple();
        when(sqlTuple.getAst()).thenReturn(mock(AbstractSyntaxTree.class));
        when(sqlTuple.getParseResult().getSqlType()).thenReturn(SqlType.DDL);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.ORACLE);
        assertEquals(SqlType.DDL, result.getSqlType());
    }

    @Test
    public void testGetExecuteStageName_ReturnSqlConsoleRule() {
        String result = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, result);
    }

    @Test
    public void testDoAfterCompletion_Return_WhenStatusIsNotSuccess() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.ERROR);

        sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, new HashMap<>());
    }

    @Test
    public void testDoAfterCompletion_AllowExport_WhenNotForbiddenToEditResultset() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setAllowExport(true);

        sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, new HashMap<>());
    }

    @Test
    public void testDoAfterCompletion_NotAllowExport_WhenForbiddenToEditResultset() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setAllowExport(false);

        sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, new HashMap<>());
    }
}