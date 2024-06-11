package com.oceanbase.odc.service.session.interceptor.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuplesWithViolation;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeMock;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleServiceMock;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleServiceMock;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;

/**
 * Class: SqlConsoleInterceptorTest
 * Description: Test class for SqlConsoleInterceptor
 */
@Slf4j
public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private ConnectionSession connectionSession;
    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private SqlExecuteResult sqlExecuteResult;

    @Before
    public void setup() {
        connectionSession = mock(ConnectionSession.class);
        authenticationFacade = new AuthenticationFacadeMock();
        ruleService = new RuleServiceMock();
        sqlConsoleRuleService = new SqlConsoleRuleServiceMock();
        sqlConsoleInterceptor = new SqlConsoleInterceptor() {
            @Override
            @Autowired
            public void setAuthenticationFacade(AuthenticationFacade authenticationFacade) {
                this.authenticationFacade = authenticationFacade;
            }

            @Override
            @Autowired
            public void setRuleService(RuleService ruleService) {
                this.ruleService = ruleService;
            }

            @Override
            @Autowired
            public void setSqlConsoleRuleService(SqlConsoleRuleService sqlConsoleRuleService) {
                this.sqlConsoleRuleService = sqlConsoleRuleService;
            }
        };
        sqlExecuteResult = new SqlExecuteResult();
    }

    @Test
    public void testDoPreHandle_NoRuleSetId_ReturnsTrue() {
        when(connectionSession.getRuleSetId()).thenReturn(null);
        assertTrue(sqlConsoleInterceptor.doPreHandle(Mockito.mock(SqlAsyncExecuteReq.class),
                Mockito.mock(SqlAsyncExecuteResp.class), connectionSession, new HashMap<>()));
    }

    @Test
    public void testDoPreHandle_RuleSetIdPresent_ReturnsTrue_WhenNotNeedToCheck() {
        Long ruleSetId = 1L;
        when(connectionSession.getRuleSetId()).thenReturn(ruleSetId);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(Boolean.FALSE);
        assertTrue(sqlConsoleInterceptor.doPreHandle(Mockito.mock(SqlAsyncExecuteReq.class),
                Mockito.mock(SqlAsyncExecuteResp.class), connectionSession, new HashMap<>()));
    }

    @Test
    public void testDoPreHandle_RuleSetIdPresent_ReturnsTrue_WhenCheckPasses() {
        Long ruleSetId = 1L;
        when(connectionSession.getRuleSetId()).thenReturn(ruleSetId);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(Boolean.TRUE);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS,
                DialectType.MYSQL, Integer.class)).thenReturn(Optional.of(10));
        when(request.getQueryLimit()).thenReturn(null);
        assertTrue(sqlConsoleInterceptor.doPreHandle(Mockito.mock(SqlAsyncExecuteReq.class),
                Mockito.mock(SqlAsyncExecuteResp.class), connectionSession, new HashMap<>()));
    }

    @Test(expected = AssertionError.class)
    public void testDoPreHandle_RuleSetIdPresent_ReturnsFalse_WhenQueryLimitExceedsLimit() {
        Long ruleSetId = 1L;
        when(connectionSession.getRuleSetId()).thenReturn(ruleSetId);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(Boolean.TRUE);
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS,
                DialectType.MYSQL, Integer.class)).thenReturn(Optional.of(10));
        when(request.getQueryLimit()).thenReturn(11);
        assertFalse(sqlConsoleInterceptor.doPreHandle(Mockito.mock(SqlAsyncExecuteReq.class),
                Mockito.mock(SqlAsyncExecuteResp.class), connectionSession, new HashMap<>()));
    }

    @Test
    public void testHandle_NoRulesetId_ReturnsTrue() {
        when(connectionSession.getRuleSetId()).thenReturn(null);
        assertTrue(sqlConsoleInterceptor.handle(Mockito.mock(SqlAsyncExecuteReq.class),
                Mockito.mock(SqlAsyncExecuteResp.class), connectionSession, new HashMap<>()));
    }

    @Test
    public void testHandle_RulesetIdPresent_MaxSqlSizeRuleViolation_ReturnsFalse() {
        Long ruleSetId = 1L;
        when(connectionSession.getRuleSetId()).thenReturn(ruleSetId);
        List<SqlTuple> sqlTuples = Arrays.asList(Mockito.mock(SqlTuple.class));
        SqlAsyncExecuteResp sqlExecuteResp = Mockito.mock(SqlAsyncExecuteResp.class);
        when(sqlExecuteResp.getSqls()).thenReturn(sqlTuples);
        AtomicBoolean allowExecute = new AtomicBoolean(true);
        Rule maxSqlSizeRule = Mockito.mock(Rule.class);
        RuleViolation maxSqlSizeViolation = Mockito.mock(RuleViolation.class);
        when(maxSqlSizeViolation.getLevel()).thenReturn(1);
        when(maxSqlSizeRule.getViolation()).thenReturn(maxSqlSizeViolation);
        when(ruleService.getByRulesetIdAndName(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(maxSqlSizeRule));
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS,
                DialectType.MYSQL, Integer.class)).thenReturn(Optional.of(5));
        when(sqlTuples.get(0).getViolatedRules()).thenReturn(Arrays.asList(maxSqlSizeRule));
        assertFalse(sqlConsoleInterceptor.handle(Mockito.mock(SqlAsyncExecuteReq.class), sqlExecuteResp,
                connectionSession, new HashMap<>()));
    }

    @Test
    public void testHandle_RulesetIdPresent_ForbiddenToCreatePl_ReturnsFalse() {
        Long ruleSetId = 1L;
        when(connectionSession.getRuleSetId()).thenReturn(ruleSetId);
        List<SqlTuple> sqlTuples = Arrays.asList(Mockito.mock(SqlTuple.class));
        SqlAsyncExecuteResp sqlExecuteResp = Mockito.mock(SqlAsyncExecuteResp.class);
        when(sqlExecuteResp.getSqls()).thenReturn(sqlTuples);
        AtomicBoolean allowExecute = new AtomicBoolean(true);
        Rule notAllowedCreatePlRule = Mockito.mock(Rule.class);
        RuleViolation notAllowedCreatePlViolation = Mockito.mock(RuleViolation.class);
        when(notAllowedCreatePlViolation.getLevel()).thenReturn(1);
        when(notAllowedCreatePlRule.getViolation()).thenReturn(notAllowedCreatePlViolation);
        when(ruleService.getByRulesetIdAndName(ruleSetId, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName()))
                .thenReturn(Optional.of(notAllowedCreatePlRule));
        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.NOT_ALLOWED_CREATE_PL,
                DialectType.MYSQL, String.class)).thenReturn(Optional.of("PL"));
        when(sqlTuples.get(0).getAst().getParseResult().getSqlType().equals(SqlType.PL))
                .thenReturn(true);
        assertFalse(sqlConsoleInterceptor.handle(Mockito.mock(SqlAsyncExecuteReq.class), sqlExecuteResp,
                connectionSession, new HashMap<>()));
    }

    @Test
    public void testHandle_RulesetIdPresent_AllowSqlTypesRuleViolation_ReturnsFalse() {
        Long ruleSetId = 1L;
        when(connectionSession.getRuleSetId()).thenReturn(ruleSetId);
        List<SqlTuple> sqlTuples = Arrays.asList(Mockito.mock(SqlTuple.class));
        SqlAsyncExecuteResp sqlExecuteResp = Mockito.mock(SqlAsyncExecuteResp.class);
        when(sqlExecuteResp.getSqls()).thenReturn(sqlTuples);
        AtomicBoolean allowExecute = new AtomicBoolean(true);
        Rule allowSqlTypesRule = Mockito.mock(Rule.class);
        RuleViolation allowSqlTypesViolation = Mockito.mock(RuleViolation.class);
        when(allowSqlTypesViolation.getLevel()).thenReturn(1);
        when(allowSqlTypesRule.getViolation()).thenReturn(allowSqlTypesViolation);
        List<String> allowedSqlTypes = Arrays.asList("SELECT");
        when(ruleService.getByRulesetIdAndName(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.of(allowSqlTypesRule));
        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES,
                DialectType.MYSQL, String.class)).thenReturn(Optional.of(allowedSqlTypes));
        when(sqlTuples.get(0).getAst().getParseResult().getSqlType()).thenReturn(SqlType.UPDATE);
        assertFalse(sqlConsoleInterceptor.handle(Mockito.mock(SqlAsyncExecuteReq.class), sqlExecuteResp,
                connectionSession, new HashMap<>()));
    }

    @Test
    public void testDoAfterCompletion_AllowExport_ReturnsTrue() {
        sqlExecuteResult.setAllowExport(true);
        assertTrue(sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>()));
    }

    @Test
    public void testDoAfterCompletion_ForbiddenExport_ReturnsFalse() {
        sqlExecuteResult.setAllowExport(false);
        assertFalse(sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>()));
    }
}