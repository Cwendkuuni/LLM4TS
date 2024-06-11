package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.parse.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parse.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.impl.CurrentUserImpl;
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
    public void testDoPreHandle_NoRuleSetId_True() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        SqlAsyncExecuteReq sqlAsyncExecuteReq = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp sqlAsyncExecuteResp = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new CurrentUserImpl());
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(null);

        assertTrue(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession,
                context));
    }

    @Test
    public void testDoPreHandle_RuleSetId_False() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        SqlAsyncExecuteReq sqlAsyncExecuteReq = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp sqlAsyncExecuteResp = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new CurrentUserImpl());
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
                .thenReturn(false);

        assertFalse(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession,
                context));
    }

    @Test
    public void testDoPreHandle_RuleSetId_True() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        SqlAsyncExecuteReq sqlAsyncExecuteReq = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp sqlAsyncExecuteResp = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(new CurrentUserImpl());
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
                .thenReturn(true);

        assertFalse(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession,
                context));
    }

    @Test
    public void testHandle_NoViolatedRules_True() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        SqlAsyncExecuteReq sqlAsyncExecuteReq = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp sqlAsyncExecuteResp = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = new HashMap<>();
        List<SqlTuple> sqlTuples = new ArrayList<>();

        when(authenticationFacade.currentUser()).thenReturn(new CurrentUserImpl());
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                DialectType.MySQL, Integer.class)).thenReturn(Optional.of(10));

        SqlTuple sqlTuple1 = new SqlTuple();
        sqlTuple1.setSqlId(1);
        sqlTuple1.setOriginalSql("select * from table1 limit 11");
        sqlTuple1.setOffset(0);
        sqlTuple1.setSqlType(SqlType.SELECT);
        sqlTuples.add(sqlTuple1);

        SqlTuple sqlTuple2 = new SqlTuple();
        sqlTuple2.setSqlId(2);
        sqlTuple2.setOriginalSql("select * from table1 limit 10");
        sqlTuple2.setOffset(0);
        sqlTuple2.setSqlType(SqlType.SELECT);
        sqlTuples.add(sqlTuple2);

        SqlAsyncExecuteResp sqlAsyncExecuteRespMock = mock(SqlAsyncExecuteResp.class);
        when(sqlAsyncExecuteRespMock.getSqls()).thenReturn(sqlTuples);

        assertTrue(sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context));
    }

    @Test
    public void testHandle_ViolatedRules_False() {
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        SqlAsyncExecuteReq sqlAsyncExecuteReq = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp sqlAsyncExecuteResp = mock(SqlAsyncExecuteResp.class);
        Map<String, Object> context = new HashMap<>();
        List<SqlTuple> sqlTuples = new ArrayList<>();

        when(authenticationFacade.currentUser()).thenReturn(new CurrentUserImpl());
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS,
                DialectType.MySQL, Integer.class)).thenReturn(Optional.of(10));

        SqlTuple sqlTuple1 = new SqlTuple();
        sqlTuple1.setSqlId(1);
        sqlTuple1.setOriginalSql("select * from table1 limit 11");
        sqlTuple1.setOffset(0);
        sqlTuple1.setSqlType(SqlType.SELECT);
        sqlTuples.add(sqlTuple1);

        SqlTuple sqlTuple2 = new SqlTuple();
        sqlTuple2.setSqlId(2);
        sqlTuple2.setOriginalSql("select * from table1 limit 10");
        sqlTuple2.setOffset(0);
        sqlTuple2.setSqlType(SqlType.SELECT);
        sqlTuples.add(sqlTuple2);

        Rule rule1 = new Rule();
        rule1.setName(SqlConsoleRules.MAX_RETURN_ROWS.getRuleName());
        rule1.setProperties(new HashMap<>());
        rule1.getProperties().put("max_return_rows", 10);
        RuleViolation violation1 = new RuleViolation();
        violation1.setLevel(1);
        violation1.setLocalizedMessage("Message 1");
        violation1.setOffset(0);
        violation1.setStart(0);
        violation1.setStop(sqlTuple1.getOriginalSql().length());
        rule1.setViolation(violation1);

        Rule rule2 = new Rule();
        rule2.setName(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
        rule2.getProperties().put("max_execute_sqls", 1);
        RuleViolation violation2 = new RuleViolation();
        violation2.setLevel(1);
        violation2.setLocalizedMessage("Message 2");
        violation2.setOffset(0);
        violation2.setStart(0);
        violation2.setStop(sqlTuple1.getOriginalSql().length());
        rule2.setViolation(violation2);

        Rule rule3 = new Rule();
        rule3.setName(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName());
        rule3.getProperties().put("allowed_sql_types", "DDL");
        RuleViolation violation3 = new RuleViolation();
        violation3.setLevel(1);
        violation3.setLocalizedMessage("Message 3");
        violation3.setOffset(0);
        violation3.setStart(0);
        violation3.setStop(sqlTuple1.getOriginalSql().length());
        rule3.setViolation(violation3);

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_RETURN_ROWS.getRuleName()))
                .thenReturn(Optional.of(rule1));
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(rule2));
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.of(rule3));

        SqlAsyncExecuteResp sqlAsyncExecuteRespMock = mock(SqlAsyncExecuteResp.class);
        when(sqlAsyncExecuteRespMock.getSqls()).thenReturn(sqlTuples);

        assertFalse(sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context));
    }

    @Test
    public void testDetermineSqlType_SelectQuery_SqlType_SELECT() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("select * from table1 limit 10");
        sqlTuple.setOffset(0);
        sqlTuple.setSqlType(null);

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL);

        assertTrue(basicResult.getSqlType() == SqlType.SELECT);
    }

    @Test
    public void testDetermineSqlType_DDLQuery_SqlType_DDL() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("create table table1 (id int primary key, name varchar(255))");
        sqlTuple.setOffset(0);
        sqlTuple.setSqlType(null);

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL);

        assertTrue(basicResult.getSqlType() == SqlType.DDL);
    }

    @Test
    public void testDetermineSqlType_InvalidQuery_SqlType_OTHERS() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("invalid query");
        sqlTuple.setOffset(0);
        sqlTuple.setSqlType(null);

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySQL);

        assertTrue(basicResult.getSqlType() == SqlType.OTHERS);
    }
}