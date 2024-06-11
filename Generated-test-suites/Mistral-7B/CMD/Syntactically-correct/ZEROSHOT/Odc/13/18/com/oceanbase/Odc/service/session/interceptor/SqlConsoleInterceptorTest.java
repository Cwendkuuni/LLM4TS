package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OrganizationType;
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
 * Test class for SqlConsoleInterceptor
 */
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
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle_whenRuleSetIdIsNull_thenReturnTrue() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        boolean actual = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        assertTrue(actual);
    }

    @Test
    public void testDoPreHandle_whenIsIndividualTeam_thenReturnTrue() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        boolean actual = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        assertTrue(actual);
    }

    @Test
    public void testDoPreHandle_whenRuleSetIdIsPresent_thenCallRuleServiceGetByRulesetIdAndName() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        Rule rule = mock(Rule.class);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())).thenReturn(Optional.of(rule));

        sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        verify(ruleService).getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
    }

    @Test
    public void testDoPreHandle_whenMaxSqlSizeRuleIsPresent_thenSetMaxSqlSize() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        Rule rule = mock(Rule.class);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())).thenReturn(Optional.of(rule));
        when(rule.getProperties()).thenReturn(Collections.singletonMap("maxSqlSize", 5));

        sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        assertEquals(5, request.getQueryLimit());
    }

    @Test
    public void testDoPreHandle_whenIsNotAllowedCreatePl_thenSetAllowExecuteToFalse() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
                .thenReturn(true);

        sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        assertFalse(context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandle_whenAllowExecuteIsFalse_thenReturnFalse() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
                .thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.ORACLE,
                String.class)).thenReturn(Collections.singletonList("DDL"));

        sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        assertFalse(context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoPreHandle_whenAllowExecuteIsTrue_thenReturnTrue() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
                .thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.ORACLE,
                String.class)).thenReturn(Collections.singletonList("DDL"));

        sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        assertTrue(context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
    }

    @Test
    public void testDoAfterCompletion_whenStatusIsNotSuccess_thenReturn() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.ERROR);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);

        verify(connectionSession, times(0)).setAllowExport(true);
        verify(connectionSession, times(0)).setAllowExport(false);
    }

    @Test
    public void testDoAfterCompletion_whenIsIndividualTeam_thenAllowExportIsTrue() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);

        verify(connectionSession).setAllowExport(true);
    }

    @Test
    public void testDoAfterCompletion_whenIsTeam_thenCheckRules() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, connectionSession))
                .thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);

        verify(connectionSession).setAllowExport(false);
    }

    @Test
    public void testDetermineSqlType_whenSyntaxErrorIsPresent_thenSqlTypeIsUnknown() {
        DialectType dialectType = DialectType.ORACLE;
        SqlTuple sqlTuple = new SqlTuple();
        AbstractSyntaxTree abstractSyntaxTree = mock(AbstractSyntaxTree.class);
        when(abstractSyntaxTree.getParseResult().isSyntaxError()).thenReturn(true);
        sqlTuple.setAst(abstractSyntaxTree);

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(SqlType.UNKNOWN, basicResult.getSqlType());
    }

    @Test
    public void testDetermineSqlType_whenSqlTypeIsPresent_thenReturnSqlType() {
        DialectType dialectType = DialectType.ORACLE;
        SqlTuple sqlTuple = new SqlTuple();
        BasicResult basicResult = new BasicResult();
        basicResult.setSqlType(SqlType.SELECT);
        sqlTuple.setAst(null);
        sqlTuple.setSqlId("1");
        sqlTuple.setOriginalSql("select * from emp");

        BasicResult expectedResult = new BasicResult();
        expectedResult.setSqlType(SqlType.SELECT);

        BasicResult actualResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetExecuteStageName_thenReturnSqlConsoleRule() {
        String expected = SqlExecuteStages.SQL_CONSOLE_RULE;
        String actual = sqlConsoleInterceptor.getExecuteStageName();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsIndividualTeam_thenReturnTrue() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        boolean actual = sqlConsoleInterceptor.isIndividualTeam();

        assertTrue(actual);
    }

    @Test
    public void testIsIndividualTeam_thenReturnFalse() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        boolean actual = sqlConsoleInterceptor.isIndividualTeam();

        assertFalse(actual);
    }

}