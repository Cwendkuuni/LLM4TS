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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.SqlConsoleInterceptor;
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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test for method doPreHandle when no rule set id is present and isIndividualTeam is true
     */
    @Test
    public void testDoPreHandleNoRuleSetIdAndIndividualTeam() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());
        assertTrue(result);
    }

    /**
     * Test for method doPreHandle when no rule set id is present and isIndividualTeam is false
     */
    @Test
    public void testDoPreHandleNoRuleSetIdAndNotIndividualTeam() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.CORPORATE);

        Long ruleSetId = null;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());
        assertTrue(result);
    }

    /**
     * Test for method doPreHandle when ruleSetId is present and isIndividualTeam is true
     */
    @Test
    public void testDoPreHandleRuleSetIdAndIndividualTeam() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());
        assertTrue(result);
    }

    /**
     * Test for method doPreHandle when ruleSetId is present and context contains NEED_SQL_CONSOLE_CHECK as false
     */
    @Test
    public void testDoPreHandleRuleSetIdAndNEED_SQL_CONSOLE_CHECK_is_false() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.CORPORATE);

        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    /**
     * Test for method handle when there is no violation
     */
    @Test
    public void testHandleNoViolation() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.CORPORATE);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        BasicResult parseResult = new BasicResult(SqlType.SELECT);
        SqlTuple sqlTuple = new SqlTuple(parseResult, null, null);
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(1));

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    /**
     * Test for method handle when there is a violation for MAX_EXECUTE_SQLS
     */
    @Test
    public void testHandleMaxExecuteSqlsViolation() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.CORPORATE);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        BasicResult parseResult = new BasicResult(SqlType.SELECT);
        SqlTuple sqlTuple = new SqlTuple(parseResult, null, null);
        sqlTuples.add(sqlTuple);
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL,
                Integer.class)).thenReturn(Optional.of(1));

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertFalse(result);
    }

    /**
     * Test for method handle when there is a violation for NOT_ALLOWED_CREATE_PL
     */
    @Test
    public void testHandleNotAllowedCreatePlViolation() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.CORPORATE);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        BasicResult parseResult = new BasicResult(SqlType.CREATE_PROCEDURE);
        SqlTuple sqlTuple = new SqlTuple(parseResult, null, null);
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertFalse(result);
    }

    /**
     * Test for method handle when there is a violation for ALLOW_SQL_TYPES
     */
    @Test
    public void testHandleAllowSqlTypesViolation() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.CORPORATE);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        List<SqlTuple> sqlTuples = new ArrayList<>();
        BasicResult parseResult = new BasicResult(SqlType.INSERT);
        SqlTuple sqlTuple = new SqlTuple(parseResult, null, null);
        sqlTuples.add(sqlTuple);
        response.setSqls(sqlTuples);

        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MYSQL,
                String.class)).thenReturn(Optional.of(new ArrayList<>(List.of("SELECT"))));

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertFalse(result);
    }

    /**
     * Test for method doAfterCompletion when resultSet is allowed to be exported
     */
    @Test
    public void testDoAfterCompletionAllowExport() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setAllowExport(true);

        sqlConsoleInterceptor.doAfterCompletion(result, mock(ConnectionSession.class), new HashMap<>());
        assertTrue(result.isAllowExport());
    }

    /**
     * Test for method doAfterCompletion when resultSet is not allowed to be exported
     */
    @Test
    public void testDoAfterCompletionNotAllowExport() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setAllowExport(false);

        sqlConsoleInterceptor.doAfterCompletion(result, mock(ConnectionSession.class), new HashMap<>());
        assertFalse(result.isAllowExport());
    }

    /**
     * Test for method determineSqlType when parseResult is null
     */
    @Test
    public void testDetermineSqlTypeNullParseResult() {
        SqlTuple sqlTuple = new SqlTuple(null, null, null);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        assertEquals(SqlType.UNKNOWN, result.getSqlType());
    }

    /**
     * Test for method determineSqlType when parseResult is not null
     */
    @Test
    public void testDetermineSqlTypeNotNullParseResult() {
        SqlTuple sqlTuple = new SqlTuple(new BasicResult(SqlType.SELECT), null, null);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        assertEquals(SqlType.SELECT, result.getSqlType());
    }
}