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
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedField;
import org.springframework.stereotype.Component;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OrganizationType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

/**
 * Test class for SqlConsoleInterceptor
 */
@Component
public class SqlConsoleInterceptorTest {

    private static final DialectType DIALECT_TYPE = DialectType.MYSQL;
    private static final OrganizationType ORGANIZATION_TYPE = OrganizationType.TEAM;

    private AuthenticationFacade authenticationFacade;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.authenticationFacade = mock(AuthenticationFacade.class);
        this.sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        when(this.authenticationFacade.currentUser()).thenReturn(createMockUser());

        new AnnotatedFieldUtils().autowireField(this.sqlConsoleInterceptor, "authenticationFacade");
        new AnnotatedFieldUtils().autowireField(this.sqlConsoleInterceptor, "ruleService");
        new AnnotatedFieldUtils().autowireField(this.sqlConsoleInterceptor, "sqlConsoleRuleService");
    }

    @Test
    public void testDoPreHandle_whenRuleSetIdIsNullOrIndividualTeam_thenReturnTrue() {
        // Given
        ConnectionSession session = createMockConnectionSession();
        SqlAsyncExecuteReq request = createMockSqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = createMockSqlAsyncExecuteResp();

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());

        // Then
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_whenNeedSqlConsoleCheckIsFalse_thenReturnTrue() {
        // Given
        ConnectionSession session = createMockConnectionSession();
        session.put(NEED_SQL_CONSOLE_CHECK, Boolean.FALSE);
        SqlAsyncExecuteReq request = createMockSqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = createMockSqlAsyncExecuteResp();

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());

        // Then
        assertTrue(result);
    }

    @Test
    public void testHandle_whenRuleSetIdIsNullOrIndividualTeam_thenReturnTrue() {
        // Given
        SqlAsyncExecuteReq request = createMockSqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = createMockSqlAsyncExecuteResp();
        ConnectionSession session = createMockConnectionSession();
        Map<String, Object> context = new HashMap<>();

        when(this.sqlConsoleRuleService.getProperties(anyLong(), anyString(), eq(DIALECT_TYPE), any()))
                .thenReturn(Optional.empty());

        // When
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        // Then
        assertTrue(result);
    }

    @Test
    public void testHandle_whenContextGetNeedSqlConsoleCheckIsFalse_thenReturnTrue() {
        // Given
        SqlAsyncExecuteReq request = createMockSqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = createMockSqlAsyncExecuteResp();
        ConnectionSession session = createMockConnectionSession();
        Map<String, Object> context = new HashMap<>();
        context.put(NEED_SQL_CONSOLE_CHECK, Boolean.FALSE);

        // When
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        // Then
        assertTrue(result);
    }

    @Test
    public void testHandle_whenRuleSetIdIsNotNullAndRuleIsAllowed_thenReturnTrue() {
        // Given
        Long ruleSetId = 1L;
        SqlAsyncExecuteReq request = createMockSqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = createMockSqlAsyncExecuteResp();
        ConnectionSession session = createMockConnectionSession();
        Map<String, Object> context = new HashMap<>();

        when(this.sqlConsoleRuleService.getProperties(ruleSetId, anyString(), eq(DIALECT_TYPE), any()))
                .thenReturn(Optional.of(10));

        // When
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        // Then
        assertTrue(result);
    }

    @Test
    public void testHandle_whenSqlConsoleInterceptedIsTrueAndSqlCheckInterceptedIsTrue_thenReturnTrue() {
        // Given
        SqlAsyncExecuteReq request = createMockSqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = createMockSqlAsyncExecuteResp();
        ConnectionSession session = createMockConnectionSession();
        Map<String, Object> context = new HashMap<>();
        context.put(NEED_SQL_CONSOLE_CHECK, Boolean.TRUE);
        context.put(SQL_CONSOLE_INTERCEPTED, Boolean.TRUE);
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, Boolean.TRUE);

        // When
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        // Then
        assertTrue(result);
    }

    @Test
    public void testHandle_whenSqlConsoleInterceptedIsFalseAndSqlCheckInterceptedIsFalse_thenReturnFalse() {
        // Given
        SqlAsyncExecuteReq request = createMockSqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = createMockSqlAsyncExecuteResp();
        ConnectionSession session = createMockConnectionSession();
        Map<String, Object> context = new HashMap<>();
        context.put(NEED_SQL_CONSOLE_CHECK, Boolean.TRUE);
        context.put(SQL_CONSOLE_INTERCEPTED, Boolean.FALSE);
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, Boolean.FALSE);

        // When
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

        // Then
        assertFalse(result);
    }

    @Test
    public void testDoAfterCompletion_whenStatusIsNotSuccess_thenReturn() {
        // Given
        SqlExecuteResult result = createMockSqlExecuteResult();
        result.setStatus(SqlExecuteStatus.ERROR);
        ConnectionSession session = createMockConnectionSession();

        // When
        sqlConsoleInterceptor.doAfterCompletion(result, session, new HashMap<>());

        // Then
        // Nothing to assert since the method does not do anything when the status is not success
    }

    @Test
    public void testDoAfterCompletion_whenQueryLimitIsReached_thenSetQueryLimit() {
        // Given
        SqlAsyncExecuteReq request = createMockSqlAsyncExecuteReq();
        request.setQueryLimit(11);
        SqlAsyncExecuteResp response = createMockSqlAsyncExecuteResp();
        ConnectionSession session = createMockConnectionSession();
        Map<String, Object> context = new HashMap<>();

        when(this.sqlConsoleRuleService.getProperties(anyLong(), anyString(), eq(DIALECT_TYPE), eq(Integer.class)))
                .thenReturn(Optional.of(10));

        // When
        sqlConsoleInterceptor.handle(request, response, session, context);
        sqlConsoleInterceptor.doAfterCompletion(createMockSqlExecuteResult(), session, context);

        // Then
        assertEquals(10, request.getQueryLimit());
    }

    @Test
    public void testDoAfterCompletion_whenRuleIsForbidden_thenSetAllowExportToFalse() {
        // Given
        SqlAsyncExecuteReq request = createMockSqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = createMockSqlAsyncExecuteResp();
        ConnectionSession session = createMockConnectionSession();
        Map<String, Object> context = new HashMap<>();

        when(this.sqlConsoleRuleService.isForbidden(anyString(), eq(session)))
                .thenReturn(true);

        // When
        sqlConsoleInterceptor.handle(request, response, session, context);
        SqlExecuteResult result = createMockSqlExecuteResult();
        sqlConsoleInterceptor.doAfterCompletion(result, session, context);

        // Then
        assertFalse(result.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_whenRuleIsNotForbidden_thenSetAllowExportToTrue() {
        // Given
        SqlAsyncExecuteReq request = createMockSqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = createMockSqlAsyncExecuteResp();
        ConnectionSession session = createMockConnectionSession();
        Map<String, Object> context = new HashMap<>();

        // When
        sqlConsoleInterceptor.handle(request, response, session, context);
        SqlExecuteResult result = createMockSqlExecuteResult();
        sqlConsoleInterceptor.doAfterCompletion(result, session, context);

        // Then
        assertTrue(result.isAllowExport());
    }

    @Test
    public void testDetermineSqlType_whenParseResultIsNotNull_thenReturnParseResult() {
        // Given
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(new AbstractSyntaxTree());

        // When
        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DIALECT_TYPE);

        // Then
        assertEquals(sqlTuple.getAst().getParseResult().getSqlType(), result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_whenParseResultIsSyntaxError_thenReturnUnknown() {
        // Given
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(new AbstractSyntaxTree());
        AbstractSyntaxTree ast = sqlTuple.getAst();
        ast.setParseResult(new BasicResult(SqlType.UNKNOWN));
        ast.getParseResult().setSyntaxError(true);

        // When
        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DIALECT_TYPE);

        // Then
        assertEquals(SqlType.UNKNOWN, result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_whenParseResultIsOther_thenReturnOther() {
        // Given
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(new AbstractSyntaxTree());
        AbstractSyntaxTree ast = sqlTuple.getAst();
        ast.setParseResult(new BasicResult(SqlType.OTHERS));

        // When
        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DIALECT_TYPE);

        // Then
        assertEquals(SqlType.OTHERS, result.getSqlType());
    }

    @Test
    public void testGetExecuteStageName_thenReturnSqlConsoleRule() {
        // Given

        // When
        String result = sqlConsoleInterceptor.getExecuteStageName();

        // Then
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, result);
    }

    private ConnectionSession createMockConnectionSession() {
        return mock(ConnectionSession.class);
    }

    private SqlAsyncExecuteReq createMockSqlAsyncExecuteReq() {
        return mock(SqlAsyncExecuteReq.class);
    }

    private SqlAsyncExecuteResp createMockSqlAsyncExecuteResp() {
        return mock(SqlAsyncExecuteResp.class);
    }

    private SqlExecuteResult createMockSqlExecuteResult() {
        return mock(SqlExecuteResult.class);
    }

    private Map<String, Object> newHashMap() {
        return new HashMap<>();
    }

    private Map<String, Object> newAnnotatedFieldUtils() {
        return new AnnotatedFieldUtils();
    }

    private Object createMockUser() {
        return mock(Object.class);
    }
}