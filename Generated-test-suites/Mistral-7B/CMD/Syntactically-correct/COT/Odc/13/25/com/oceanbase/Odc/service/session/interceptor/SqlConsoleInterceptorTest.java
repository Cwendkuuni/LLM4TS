package com.oceanbase.odc.service.session.interceptor.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResp;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlCheckInterceptor;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

/**
 * Test class for SqlConsoleInterceptor
 */
@Slf4j
public class SqlConsoleInterceptorTest {

    @Mock
    private ConnectionSession session;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private BaseTimeConsumingInterceptor baseTimeConsumingInterceptor;

    @Mock
    private SqlCheckInterceptor sqlCheckInterceptor;

    @Test
    public void testHandle_true() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL,
                Integer.class)).thenReturn(Optional.of(10));

        // When
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Boolean sqlConsoleIntercepted = sqlConsoleInterceptor.handle(request, response, session, new HashMap<>());

        // Then
        assertTrue(sqlConsoleIntercepted);
    }

    @Test
    public void testHandle_false() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        // When
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Boolean sqlConsoleIntercepted = sqlConsoleInterceptor.handle(request, response, session, new HashMap<>());

        // Then
        assertFalse(sqlConsoleIntercepted);
    }

    @Test
    public void testDoPreHandle_true() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlCheckInterceptor.doPreHandle(eq(request), eq(response), eq(session), eq(context)))
                .thenReturn(true);

        // When
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        Boolean sqlConsoleIntercepted = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        // Then
        assertTrue(sqlConsoleIntercepted);
    }

    @Test
    public void testDoPreHandle_false() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlCheckInterceptor.doPreHandle(eq(request), eq(response), eq(session), eq(context)))
                .thenReturn(false);

        // When
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        Boolean sqlConsoleIntercepted = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        // Then
        assertFalse(sqlConsoleIntercepted);
    }

    @Test
    public void testHandle_maxSqlSize_false() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL,
                Integer.class)).thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL,
                String.class)).thenReturn(Optional.of(new ArrayList<>()));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(new ArrayList<>());
        response.getSqls().add(new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM tab1"), null));
        response.getSqls().add(new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM tab2"), null));

        List<SqlTuple> sqlTuples = response.getSqls().stream().map(SqlTuplesWithViolation::getSqlTuple)
                .collect(Collectors.toList());

        // When
        Optional<Integer> maxSqlSize = sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                DialectType.MySQL, Integer.class);
        AtomicBoolean allowExecute = new AtomicBoolean(true);
        AtomicReference<Rule> maxSqlSizeRule = new AtomicReference<>();
        if (maxSqlSize.isPresent()) {
            if (sqlTuples.size() > maxSqlSize.get()) {
                sqlConsoleInterceptor.handle(request, response, session, new HashMap<>());
                allowExecute.set(false);
            }
        }

        // Then
        assertTrue(maxSqlSize.isPresent());
        assertTrue(allowExecute.get());
    }

    @Test
    public void testHandle_maxSqlSize_true() {
        // Given
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL,
                Integer.class)).thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL,
                String.class)).thenReturn(Optional.of(new ArrayList<>()));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL,
                Integer.class)).thenReturn(Optional.of(5));

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(new ArrayList<>());
        response.getSqls().add(new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM tab1"), null));
        response.getSqls().add(new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM tab2"), null));
        response.getSqls().add(new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM tab3"), null));

        List<SqlTuple> sqlTuples = response.getSqls().stream().map(SqlTuplesWithViolation::getSqlTuple)
                .collect(Collectors.toList());

        // When
        Optional<Integer> maxSqlSize = sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
                DialectType.MySQL, Integer.class);
        AtomicBoolean allowExecute = new AtomicBoolean(true);
        AtomicReference<Rule> maxSqlSizeRule = new AtomicReference<>();
        if (maxSqlSize.isPresent()) {
            if (sqlTuples.size() > maxSqlSize.get()) {
                sqlConsoleInterceptor.handle(request, response, session, new HashMap<>());
                allowExecute.set(false);
            }
        }

        // Then
        assertTrue(maxSqlSize.isPresent());
        assertFalse(allowExecute.get());
    }

    @Test
    public void testDetermineSqlType() {
        // Given
        String sql = "SELECT * FROM tab1 WHERE id = 1";
        AbstractSyntaxTree ast = AbstractSyntaxTreeFactories.getAstFactory(DialectType.MySQL, 0).parse(sql);

        // When
        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(new SqlTuple(sql), DialectType.MySQL);

        // Then
        assertEquals(SqlType.SQL, basicResult.getSqlType());
        assertFalse(basicResult.isPlDdl());
        assertFalse(basicResult.isSyntaxError());
    }

    @Test
    public void testDetermineSqlType_pl_ddl() {
        // Given
        String sql = "CREATE TABLE tab1 (id INT PRIMARY KEY)";
        AbstractSyntaxTree ast = AbstractSyntaxTreeFactories.getAstFactory(DialectType.MySQL, 0).parse(sql);

        // When
        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(new SqlTuple(sql), DialectType.MySQL);

        // Then
        assertEquals(SqlType.PL, basicResult.getSqlType());
        assertTrue(basicResult.isPlDdl());
        assertFalse(basicResult.isSyntaxError());
    }

    @Test
    public void testDetermineSqlType_syntax_error() {
        // Given
        String sql = "SELECT a FROM tab1 WHERE id = a";
        AbstractSyntaxTree ast = AbstractSyntaxTreeFactories.getAstFactory(DialectType.MySQL, 0).parse(sql);

        // When
        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(new SqlTuple(sql), DialectType.MySQL);

        // Then
        assertEquals(SqlType.UNKNOWN, basicResult.getSqlType());
        assertTrue(basicResult.isSyntaxError());
    }

    @Test
    public void testGetOrder() {
        // Then
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
}