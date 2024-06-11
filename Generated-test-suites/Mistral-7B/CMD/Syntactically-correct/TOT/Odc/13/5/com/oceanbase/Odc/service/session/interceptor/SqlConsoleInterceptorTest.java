package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
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
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@SuppressWarnings("unchecked")
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @InjectMocks
    private SqlConsoleInterceptor interceptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle_ruleSetIsNull_returnTrue() {
        // Setup
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        // Execute
        boolean result = interceptor.doPreHandle(request, response, session, context);

        // Verify
        assertFalse(result);
    }

    @Test
    public void testDoPreHandle_ruleSetIsIndividualTeam_returnTrue() {
        // Setup
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        doReturn(OrganizationType.INDIVIDUAL).when(authenticationFacade).currentUser();

        // Execute
        boolean result = interceptor.doPreHandle(request, response, session, context);

        // Verify
        assertFalse(result);
    }

    @Test
    public void testDoPreHandle_needSqlConsoleCheckIsFalse_returnTrue() {
        // Setup
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        doReturn(Boolean.FALSE).when(context).get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK);

        // Execute
        boolean result = interceptor.doPreHandle(request, response, session, context);

        // Verify
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_handleIsFalse_returnFalse() {
        // Setup
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        doReturn(false).when(interceptor).handle(request, response, session, context);

        // Execute
        boolean result = interceptor.doPreHandle(request, response, session, context);

        // Verify
        assertFalse(result);
    }

    @Test
    public void testHandle_ruleSetIdIsNull_returnTrue() {
        // Setup
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        // Execute
        boolean result = interceptor.handle(request, response, session, context);

        // Verify
        assertTrue(result);
    }

    @Test
    public void testHandle_queryLimitIsGreaterThanRequest_setRequestLimitToLimit() {
        // Setup
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQueryLimit(10);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        Long ruleSetId = 1L;
        doReturn(ruleSetId).when(ConnectionSessionUtil).getRuleSetId(session);
        doReturn(Optional.of(5L)).when(sqlConsoleRuleService).getProperties(ruleSetId,
                SqlConsoleRules.MAX_RETURN_ROWS, session.getDialectType(), Integer.class);

        // Execute
        boolean result = interceptor.handle(request, response, session, context);

        // Verify
        assertEquals(5, request.getQueryLimit());
        assertTrue(result);
    }

    @Test
    public void testHandle_maxSqlSizeRuleIsPresentAndViolated_returnFalse() {
        // Setup
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        Long ruleSetId = 1L;
        doReturn(ruleSetId).when(ConnectionSessionUtil).getRuleSetId(session);
        doReturn(Optional.of(SqlConsoleRules.MAX_EXECUTE_SQLS)).when(sqlConsoleRuleService)
                .getProperties(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS, session.getDialectType(),
                        String.class);
        doReturn(Optional.of(new Rule())).when(ruleService).getByRulesetIdAndName(ruleSetId,
                SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1L);
        sqlTuple.setOriginalSql("SELECT * FROM table1 LIMIT 10");
        sqlTuples.add(sqlTuple);
        doReturn(sqlTuples).when(response).getSqls();

        // Execute
        boolean result = interceptor.handle(request, response, session, context);

        // Verify
        assertFalse(result);
    }

    @Test
    public void testHandle_forbiddenToCreatePl_returnFalse() {
        // Setup
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        Long ruleSetId = 1L;
        doReturn(ruleSetId).when(ConnectionSessionUtil).getRuleSetId(session);
        doReturn(true).when(sqlConsoleRuleService).isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session);

        // Execute
        boolean result = interceptor.handle(request, response, session, context);

        // Verify
        assertFalse(result);
    }

    @Test
    public void testHandle_allowSqlTypesIsPresentAndViolated_returnFalse() {
        // Setup
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        Long ruleSetId = 1L;
        doReturn(ruleSetId).when(ConnectionSessionUtil).getRuleSetId(session);
        doReturn(Optional.of("SELECT")).when(sqlConsoleRuleService)
                .getListProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, session.getDialectType(), String.class);
        doReturn(Optional.of(new Rule())).when(ruleService).getByRulesetIdAndName(ruleSetId,
                SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName());

        List<SqlTuple> sqlTuples = new ArrayList<>();
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1L);
        sqlTuple.setOriginalSql("UPDATE table1 SET column1 = 'value'");
        sqlTuples.add(sqlTuple);
        doReturn(sqlTuples).when(response).getSqls();

        // Execute
        boolean result = interceptor.handle(request, response, session, context);

        // Verify
        assertFalse(result);
    }

    @Test
    public void testDoAfterCompletion_resultSetMetaDataIsNotNull_setEditableToFalse() {
        // Setup
        SqlExecuteResult result = new SqlExecuteResult();
        ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
        doReturn(resultSetMetaData).when(result).getResultSetMetaData();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        // Execute
        interceptor.doAfterCompletion(result, session, context);

        // Verify
        verify(resultSetMetaData).setEditable(false);
    }

    @Test
    public void testDoAfterCompletion_isForbiddenToEditResultSet_setAllowExportToFalse() {
        // Setup
        SqlExecuteResult result = new SqlExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        doReturn(true).when(sqlConsoleRuleService).isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session);

        // Execute
        interceptor.doAfterCompletion(result, session, context);

        // Verify
        assertFalse(result.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_isNotForbiddenToEditResultSet_setAllowExportToTrue() {
        // Setup
        SqlExecuteResult result = new SqlExecuteResult();
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        doReturn(false).when(sqlConsoleRuleService).isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session);

        // Execute
        interceptor.doAfterCompletion(result, session, context);

        // Verify
        assertTrue(result.isAllowExport());
    }

    @Test
    public void testDetermineSqlType_sqlIsValid_setSqlType() {
        // Setup
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM table1");
        ConnectionSession session = mock(ConnectionSession.class);
        DialectType dialectType = DialectType.MYSQL;

        // Execute
        BasicResult result = interceptor.determineSqlType(sqlTuple, dialectType);

        // Verify
        assertEquals(SqlType.SELECT, result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_sqlHasSyntaxError_setSqlTypeToUnknown() {
        // Setup
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM table1 FROM");
        ConnectionSession session = mock(ConnectionSession.class);
        DialectType dialectType = DialectType.MYSQL;

        // Execute
        BasicResult result = interceptor.determineSqlType(sqlTuple, dialectType);

        // Verify
        assertEquals(SqlType.UNKNOWN, result.getSqlType());
    }
}