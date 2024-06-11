package com.oceanbase.odc.service.session.interceptor.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuplesWithViolation;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle_true() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(connectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, SqlType.MYSQL, Integer.class))
                .thenReturn(Optional.of(10));

        boolean actual = sqlConsoleInterceptor.doPreHandle(new SqlAsyncExecuteReq(), response, session, context);
        assertTrue(actual);
        verify(sqlConsoleRuleService, times(1)).getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, SqlType.MYSQL,
                Integer.class);
    }

    @Test
    public void testDoPreHandle_false() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(connectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        boolean actual = sqlConsoleInterceptor.doPreHandle(new SqlAsyncExecuteReq(), response, session, context);
        assertTrue(actual);
        verify(sqlConsoleRuleService, never()).getProperties(anyLong(), any(), any(), anyClass());
    }

    @Test
    public void testHandle_true() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(connectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1L);
        sqlTuple.setOriginalSql("select * from table1 where id = 1");
        sqlTuples.add(new SqlTuplesWithViolation(sqlTuple, new ArrayList<>()));
        response.setSqls(sqlTuples);

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, SqlType.MYSQL, Integer.class))
                .thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, SqlType.MYSQL,
                String.class)).thenReturn(Optional.of(List.of("SELECT")));

        Optional<Integer> queryLimit = sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                SqlType.MYSQL, Integer.class);
        queryLimit.ifPresent(limit -> {
            request.setQueryLimit(limit);
        });

        boolean actual = sqlConsoleInterceptor.handle(request, response, session, context);
        assertTrue(actual);
        verify(sqlConsoleRuleService, times(2)).getProperties(1L, any(), any(), anyClass());
    }

    @Test
    public void testHandle_false() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new Object());
        when(connectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1L);
        sqlTuple.setOriginalSql("create procedure p()");
        sqlTuples.add(new SqlTuplesWithViolation(sqlTuple, new ArrayList<>()));
        response.setSqls(sqlTuples);

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, SqlType.MYSQL, Integer.class))
                .thenReturn(Optional.of(10));

        boolean actual = sqlConsoleInterceptor.handle(request, response, session, context);
        assertFalse(actual);
        verify(sqlConsoleRuleService, times(1)).getProperties(1L, any(), any(), anyClass());
    }

    @Test
    public void testDetermineSqlType_valid() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1L);
        sqlTuple.setOriginalSql("select * from table1 where id = 1");

        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(sqlTuple.getAst()).thenReturn(ast);

        BasicResult expectedResult = new BasicResult(SqlType.SELECT);

        BasicResult actualResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, SqlType.MYSQL);

        assertEquals(expectedResult.getSqlType(), actualResult.getSqlType());
    }

    @Test
    public void testDetermineSqlType_invalid() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1L);
        sqlTuple.setOriginalSql("select * from table1 where id = 1");

        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(sqlTuple.getAst()).thenThrow(new SyntaxErrorException());

        BasicResult expectedResult = new BasicResult(SqlType.UNKNOWN);
        expectedResult.setSyntaxError(true);

        BasicResult actualResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, SqlType.MYSQL);

        assertEquals(expectedResult.getSqlType(), actualResult.getSqlType());
        assertTrue(expectedResult.isSyntaxError());
        assertTrue(actualResult.isSyntaxError());
    }
}