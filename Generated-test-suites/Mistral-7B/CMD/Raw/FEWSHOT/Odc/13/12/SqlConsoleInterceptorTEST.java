package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.model.AuthenticatedUser;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Lebie
 * @Date: 2023/7/17 15:37
 * @Description: Test class for SqlConsoleInterceptor
 */
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
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private SqlExecuteResult executeResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();
        executeResult = new SqlExecuteResult();
    }

    @Test
    public void testDoPreHandle_whenRuleSetIdIsNull_thenReturnTrue() {
        when(authenticationFacade.currentUser()).thenReturn(new AuthenticatedUser());

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, Collections.emptyMap()));
    }

    @Test
    public void testDoPreHandle_whenRuleSetIdIsPresent_thenHandleMethodCalled() {
        when(authenticationFacade.currentUser()).thenReturn(new AuthenticatedUser());

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        sqlTuple.setSqlTuple(new SqlTuple());
        sqlTuples.add(sqlTuple);
        resp.setSqls(sqlTuples);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(10));

        sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());

        verify(sqlConsoleRuleService, times(1)).getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class);
    }

    @Test
    public void testHandle_whenMaxSqlSizeRuleIsViolated_thenReturnFalse() {
        when(authenticationFacade.currentUser()).thenReturn(new AuthenticatedUser());

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        sqlTuple.setSqlTuple(new SqlTuple());
        sqlTuple.setViolatedRules(Collections.singletonList(new Rule()));
        sqlTuples.add(sqlTuple);
        resp.setSqls(sqlTuples);
        Rule maxSqlSizeRule = new Rule();
        maxSqlSizeRule.setRuleName(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
        maxSqlSizeRule.setProperties(Collections.singletonMap("propertyName", "value"));

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(maxSqlSizeRule));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Optional.empty());

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap()));
    }

    @Test
    public void testHandle_whenForbiddenToCreatePl_thenReturnFalse() {
        when(authenticationFacade.currentUser()).thenReturn(new AuthenticatedUser());

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        sqlTuple.setSqlTuple(new SqlTuple());
        sqlTuple.setViolatedRules(Collections.emptyList());
        sqlTuples.add(sqlTuple);
        resp.setSqls(sqlTuples);
        Rule notAllowedCreatePlRule = new Rule();
        notAllowedCreatePlRule.setRuleName(SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName());
        notAllowedCreatePlRule.setProperties(Collections.singletonMap("propertyName", "value"));

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName()))
                .thenReturn(Optional.of(notAllowedCreatePlRule));

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap()));
    }

    @Test
    public void testHandle_whenSqlTypeIsForbidden_thenReturnFalse() {
        when(authenticationFacade.currentUser()).thenReturn(new AuthenticatedUser());

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        sqlTuple.setSqlTuple(new SqlTuple());
        sqlTuple.setViolatedRules(Collections.emptyList());
        sqlTuples.add(sqlTuple);
        resp.setSqls(sqlTuples);
        Rule allowSqlTypesRule = new Rule();
        allowSqlTypesRule.setRuleName(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName());
        allowSqlTypesRule.setProperties(Collections.singletonMap("propertyName", "value"));

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.of(allowSqlTypesRule));

        SqlType forbiddenSqlType = SqlType.PL;
        BasicResult basicResult = new BasicResult(forbiddenSqlType);

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.empty());

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap()));
    }

    @Test
    public void testHandle_whenNoViolation_thenReturnTrue() {
        when(authenticationFacade.currentUser()).thenReturn(new AuthenticatedUser());

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        sqlTuple.setSqlTuple(new SqlTuple());
        sqlTuple.setViolatedRules(Collections.emptyList());
        sqlTuples.add(sqlTuple);
        resp.setSqls(sqlTuples);

        Rule allowSqlTypesRule = new Rule();
        allowSqlTypesRule.setRuleName(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName());
        allowSqlTypesRule.setProperties(Collections.singletonMap("propertyName", "value"));

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.of(allowSqlTypesRule));

        SqlType allowedSqlType = SqlType.SELECT;
        BasicResult allowedBasicResult = new BasicResult(allowedSqlType);

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(10));

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap()));
    }

    @Test
    public void testDetermineSqlType_whenSqlIsValid_thenReturnValidBasicResult() {
        SqlType expectedSqlType = SqlType.SELECT;
        SqlTuple sqlTuple = new SqlTuple();
        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(sqlTuple.getAst()).thenReturn(ast);
        when(ast.getParseResult()).thenReturn(new BasicResult(expectedSqlType));

        BasicResult actualBasicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        assertEquals(expectedSqlType, actualBasicResult.getSqlType());
    }

    @Test
    public void testDetermineSqlType_whenSqlHasSyntaxError_thenReturnUnknownBasicResult() {
        SqlType expectedSqlType = SqlType.UNKNOWN;
        SqlTuple sqlTuple = new SqlTuple();
        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(sqlTuple.getAst()).thenReturn(ast);
        when(ast.getParseResult()).thenThrow(new SyntaxErrorException("test error message"));

        BasicResult actualBasicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        assertEquals(expectedSqlType, actualBasicResult.getSqlType());
        assertEquals("test error message", actualBasicResult.getErrorMessage());
    }

    @Test
    public void testDoAfterCompletion_whenForbiddenToEditResultSet_thenMakeResultSetUneditable() {
        when(authenticationFacade.currentUser()).thenReturn(new AuthenticatedUser());

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        SqlExecuteResult executeResult = new SqlExecuteResult();
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, Collections.emptyMap());

        assertTrue(executeResult.getResultSetMetaData().isReadOnly());
    }

    @Test
    public void testDoAfterCompletion_whenAllowedToEditResultSet_thenMakeResultSetEditable() {
        when(authenticationFacade.currentUser()).thenReturn(new AuthenticatedUser());

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        SqlExecuteResult executeResult = new SqlExecuteResult();
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, Collections.emptyMap());

        assertFalse(executeResult.getResultSetMetaData().isReadOnly());
    }
}