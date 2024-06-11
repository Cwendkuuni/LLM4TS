package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
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

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName: SqlConsoleInterceptorTest <br/>
 * Description: <br/>
 *
 * @Author: Lebie
 * @CreateDate: 2023/7/17 15:37 <br/>
 * @Version: 1.0 <br/>
 */
@Slf4j
public class SqlConsoleInterceptorTest {

    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private ConnectionSession connectionSession;
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        authenticationFacade = Mockito.mock(AuthenticationFacade.class);
        ruleService = Mockito.mock(RuleService.class);
        sqlConsoleRuleService = Mockito.mock(SqlConsoleRuleService.class);
        connectionSession = Mockito.mock(ConnectionSession.class);
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_true() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);

        when(sqlConsoleRuleService.getProperties(Mockito.anyLong(), Mockito.eq(SqlConsoleRules.MAX_RETURN_ROWS),
                Mockito.eq(DialectType.MYSQL), Mockito.anyClass()))
                .thenAnswer(new Answer<Integer>() {
                    @Override
                    public Integer answer(InvocationOnMock invocation) throws Throwable {
                        return 10;
                    }
                });

        when(sqlConsoleRuleService.isForbidden(Mockito.anyString(), connectionSession)).thenAnswer(i -> false);

        when(sqlConsoleRuleService.getListProperties(Mockito.anyLong(), Mockito.eq(SqlConsoleRules.ALLOW_SQL_TYPES),
                Mockito.eq(DialectType.MYSQL), Mockito.anyClass()))
                .thenReturn(Optional.of(new ArrayList<>()));

        when(sqlConsoleRuleService.getProperties(Mockito.anyLong(), Mockito.eq(SqlConsoleRules.NOT_ALLOWED_CREATE_PL),
                Mockito.eq(DialectType.MYSQL), Mockito.anyClass()))
                .thenReturn(Optional.empty());

        when(sqlConsoleInterceptor.handle(request, response, connectionSession, new HashMap<>())).thenReturn(true);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, new HashMap<>());
        assertTrue(result);
        verify(sqlConsoleInterceptor, times(1)).handle(request, response, connectionSession, new HashMap<>());
    }

    @Test
    public void testDoPreHandle_false() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);

        when(sqlConsoleRuleService.getProperties(Mockito.anyLong(), Mockito.eq(SqlConsoleRules.MAX_RETURN_ROWS),
                Mockito.eq(DialectType.MYSQL), Mockito.anyClass()))
                .thenAnswer(new Answer<Integer>() {
                    @Override
                    public Integer answer(InvocationOnMock invocation) throws Throwable {
                        return null;
                    }
                });

        when(sqlConsoleInterceptor.handle(request, response, connectionSession, new HashMap<>())).thenReturn(false);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, new HashMap<>());
        assertFalse(result);
        verify(sqlConsoleInterceptor, times(1)).handle(request, response, connectionSession, new HashMap<>());
    }

    @Test
    public void testDoPreHandle_noRuleSetId() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);

        when(sqlConsoleInterceptor.handle(request, response, connectionSession, new HashMap<>())).thenReturn(false);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, new HashMap<>());
        assertFalse(result);
        verify(sqlConsoleInterceptor, times(0)).handle(request, response, connectionSession, new HashMap<>());
    }

    @Test(expected = NullPointerException.class)
    public void testDoPreHandle_nullSession() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(sqlConsoleInterceptor.handle(request, response, null, new HashMap<>()))
                .thenThrow(new NullPointerException("session is null"));

        sqlConsoleInterceptor.doPreHandle(request, response, null, new HashMap<>());
    }

    @Test
    public void testHandle_true() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();

        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1L);
        sqlTuple.setOriginalSql("select * from test");
        sqlTuple.setOffset(0);
        sqlTuple.setQueryLimit(10);
        sqlTuple.setAst(new AbstractSyntaxTree());
        sqlTuples.add(sqlTuple);

        response.setSqls(sqlTuples);

        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(10));

        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.NOT_ALLOWED_CREATE_PL, DialectType.MYSQL,
                String.class)).thenReturn(Optional.empty());

        when(sqlConsoleRuleService.getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Optional.of(new ArrayList<>()));

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
                .thenAnswer(i -> false);

        when(ruleService.getByRulesetIdAndName(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(new Rule()));

        when(ruleService.getByRulesetIdAndName(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.of(new Rule()));

        when(ruleService.getByRulesetIdAndName(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())
                .getProperties().get(ruleService.getByRulesetIdAndName(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())
                        .getMetadata().getPropertyMetadatas().get(0).getName())).thenReturn("maxExecuteSqls");

        when(sqlConsoleInterceptor.handle(request, response, session, new HashMap<>())).thenReturn(true);

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, new HashMap<>()));
        verify(sqlConsoleInterceptor, times(1)).handle(request, response, session, new HashMap<>());
    }

    @Test
    public void testHandle_false() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();

        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1L);
        sqlTuple.setOriginalSql("select * from test");
        sqlTuple.setOffset(0);
        sqlTuple.setQueryLimit(11);
        sqlTuple.setAst(new AbstractSyntaxTree());
        sqlTuples.add(sqlTuple);

        response.setSqls(sqlTuples);

        when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(10));

        when(sqlConsoleInterceptor.handle(request, response, session, new HashMap<>())).thenReturn(false);

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, new HashMap<>()));
        verify(sqlConsoleInterceptor, times(1)).handle(request, response, session, new HashMap<>());
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("select * from test");
        sqlTuple.setAst(new AbstractSyntaxTree());

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
        assertEquals(SqlType.SELECT, basicResult.getSqlType());
    }

    @Test
    public void testDetermineSqlType_parseError() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("select * from test select * from test");
        sqlTuple.setAst(new AbstractSyntaxTree());

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
        assertEquals(SqlType.OTHERS, basicResult.getSqlType());
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testDoAfterCompletion_true() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        response.setAllowExport(true);

        when(sqlConsoleInterceptor.isIndividualTeam()).thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, new HashMap<>());
        verify(response, times(1)).setAllowExport(true);
    }

    @Test
    public void testDoAfterCompletion_false() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        response.setAllowExport(false);

        when(sqlConsoleInterceptor.isIndividualTeam()).thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, new HashMap<>());
        verify(response, times(1)).setAllowExport(false);
    }

    @Test
    public void testDoAfterCompletion_individualTeam() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        response.setAllowExport(false);

        when(sqlConsoleInterceptor.isIndividualTeam()).thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, new HashMap<>());
        verify(response, times(1)).setAllowExport(true);
    }

}