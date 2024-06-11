package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.model.User;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @Mock
    private ConnectionSessionFactory sessionFactory;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private RuleService ruleService;
    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;
    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private ConnectionSession session;
    private User user;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(authenticationFacade.currentUser()).thenReturn(user);
        session = mock(ConnectionSession.class);
        when(sessionFactory.getConnectionSession()).thenReturn(session);
    }

    @Test
    public void testHandleTrue() {
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL, Integer.class))
                .thenReturn(Optional.ofNullable(10));
        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1);
        SqlExecuteResult sqlExecuteResult = mock(SqlExecuteResult.class);
        SqlExecuteStatus sqlExecuteStatus = SqlExecuteStatus.SUCCESS;
        when(sqlExecuteResult.getStatus()).thenReturn(sqlExecuteStatus);
        resp.setSqls(Arrays.asList(new SqlTuplesWithViolation(sqlExecuteResult, sqlTuple)));

        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.handle(req, resp, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandleFalse() {
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL, Integer.class))
                .thenReturn(Optional.ofNullable(10));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL, Integer.class))
                .thenReturn(Optional.ofNullable(5));

        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        SqlTuple sqlTuple1 = new SqlTuple();
        sqlTuple1.setSqlId(1);
        SqlTuple sqlTuple2 = new SqlTuple();
        sqlTuple2.setSqlId(6);
        List<SqlTuplesWithViolation> sqls = Arrays.asList(new SqlTuplesWithViolation(null, sqlTuple1),
                new SqlTuplesWithViolation(null, sqlTuple2));
        resp.setSqls(sqls);

        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.handle(req, resp, session, context);
        assertFalse(result);

        Rule rule = mock(Rule.class);
        when(rule.getLevel()).thenReturn(1);
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.ofNullable(rule));

        RuleViolation violation = mock(RuleViolation.class);
        when(violation.getLevel()).thenReturn(1);
        when(violation.getLocalizedMessage())
                .thenAnswer(invocation -> SqlConsoleRules.MAX_EXECUTE_SQLS.getLocalizedMessage(new Object[] {"1"}));
        when(violation.getOffset()).thenReturn(1);
        when(violation.getStart()).thenReturn(1);
        when(violation.getStop()).thenReturn(10);

        Rule violationRule = mock(Rule.class);
        when(violationRule.getViolation()).thenReturn(violation);

        sqls.get(1).setViolatedRules(Arrays.asList(violationRule));
        context.clear();
        result = sqlConsoleInterceptor.handle(req, resp, session, context);
        assertFalse(result);
    }

    @Test
    public void testGetOrder() {
        int result = sqlConsoleInterceptor.getOrder();
        assertEquals(2, result);
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = new SqlTuple();
        AbstractSyntaxTree ast = new AbstractSyntaxTree() {};
        sqlTuple.setAst(ast);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL);
        assertEquals(SqlType.SQL, result.getSqlType());
    }

    @Test(expected = SyntaxErrorException.class)
    public void testDetermineSqlTypeWithSyntaxError() {
        SqlTuple sqlTuple = new SqlTuple();
        AbstractSyntaxTree ast = new AbstractSyntaxTree() {
            @Override
            public boolean isSyntaxError() {
                return true;
            }
        };
        sqlTuple.setAst(ast);

        sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL);
    }
}