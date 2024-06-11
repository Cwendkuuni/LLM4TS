package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.After;
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
import com.oceanbase.odc.core.sql.parse.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parse.AbstractSyntaxTreeFactories;
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
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

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
    private Map<String, Object> context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();
        context = new HashMap<>();
    }

    @After
    public void tearDown() {
        reset(authenticationFacade, ruleService, sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_returnTrue_whenRuleSetIdIsNull() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_returnTrue_whenRuleSetIdIsIndividualTeam() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_returnFalse_whenNeedSqlConsoleCheckIsFalse() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(false);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_returnFalse_whenQueryLimitIsExceeded() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL,
                Integer.class)).thenReturn(Optional.of(10));
        request.setQueryLimit(11);

        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoPreHandle_returnTrue_whenQueryLimitIsNotExceeded() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL,
                Integer.class)).thenReturn(Optional.of(Integer.MAX_VALUE));
        request.setQueryLimit(10);

        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testHandle_returnTrue_whenRuleSetIdIsNull() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_returnTrue_whenRuleSetIdIsIndividualTeam() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_returnFalse_whenMaxSqlSizeIsExceeded() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL,
                Integer.class)).thenReturn(Optional.of(2));
        request.setSqls(Arrays.asList(new SqlTuplesWithViolation[2]));

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_returnTrue_whenMaxSqlSizeIsNotExceeded() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL,
                Integer.class)).thenReturn(Optional.of(Integer.MAX_VALUE));
        request.setSqls(Arrays.asList(new SqlTuplesWithViolation[3]));

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_setMaxSqlSizeRule_whenMaxSqlSizeIsExceeded() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL,
                Integer.class)).thenReturn(Optional.of(2));
        request.setSqls(Arrays.asList(new SqlTuplesWithViolation[3]));

        SqlConsoleInterceptor sqlConsoleInterceptorSpy = spy(sqlConsoleInterceptor);
        doCallRealMethod(sqlConsoleInterceptorSpy, "handle", request, response, session, context);

        Rule maxSqlSizeRule = sqlConsoleInterceptorSpy.maxSqlSizeRule.get();
        assertEquals(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName(), maxSqlSizeRule.getName());
        assertEquals(RuleViolation.Level.ERROR, maxSqlSizeRule.getViolation().getLevel());
        assertEquals("max.execute.sqls.exceeded", maxSqlSizeRule.getViolation().getLocalizedMessageKey());
    }

    @Test
    public void testHandle_returnFalse_whenForbiddenToCreatePl() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);
        request.setSqls(Arrays.asList(new SqlTuplesWithViolation[1]));

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_returnFalse_whenAllowSqlTypesIsNotFound() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL,
                String.class)).thenReturn(Optional.empty());
        request.setSqls(Arrays.asList(new SqlTuplesWithViolation[1]));

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_returnFalse_whenAllowSqlTypesIsNotAllowed() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL,
                String.class)).thenReturn(Optional.of(Arrays.asList("DDL")));
        request.setSqls(Arrays.asList(new SqlTuplesWithViolation[1]));
        SqlTuple sqlTuple = request.getSqls().get(0).getSqlTuple();
        AbstractSyntaxTree ast = sqlTuple.getAst();
        ast.setSqlType(AbstractSyntaxTree.SqlType.DML);

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_returnTrue_whenAllowSqlTypesIsAllowed() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(true);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL,
                String.class)).thenReturn(Optional.of(Arrays.asList("DDL")));
        request.setSqls(Arrays.asList(new SqlTuplesWithViolation[1]));
        SqlTuple sqlTuple = request.getSqls().get(0).getSqlTuple();
        AbstractSyntaxTree ast = sqlTuple.getAst();
        ast.setSqlType(AbstractSyntaxTree.SqlType.DDL);

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletion_returnTrue_whenNotForbiddenToEditResultset() {
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(false);
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setResultSetMetaData(new Object() {});

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);

        assertTrue(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_returnFalse_whenForbiddenToEditResultset() {
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setResultSetMetaData(new Object() {});

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);

        assertFalse(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testGetExecuteStageName_returnSqlConsoleRule() {
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, sqlConsoleInterceptor.getExecuteStageName());
    }
}