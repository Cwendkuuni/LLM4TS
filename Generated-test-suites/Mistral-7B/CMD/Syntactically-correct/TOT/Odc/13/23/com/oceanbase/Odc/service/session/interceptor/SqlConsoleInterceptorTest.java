package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult.Status;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

/**
 * Test for {@link SqlConsoleInterceptor}.
 */
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private ConnectionSession session;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @Mock
    private User currentUser;

    @InjectMocks
    private SqlConsoleInterceptor interceptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(SqlConsoleInterceptor.class);
        interceptor = context.getBean(SqlConsoleInterceptor.class);
        when(authenticationFacade.currentUser()).thenReturn(currentUser);
        when(currentUser.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenReturn(Optional.ofNullable(null));
        when(sqlConsoleRuleService.getProperties(anyLong(), anyString(), eq(DialectType.MYSQL), any()))
                .thenReturn(Optional.ofNullable(1L));
        when(sqlConsoleRuleService.isForbidden(anyString(), any()))
                .thenAnswer(answer -> Boolean.FALSE);
        when(sqlConsoleRuleService.getListProperties(anyLong(), anyString(), eq(DialectType.MYSQL), any()))
                .thenReturn(Optional.of(Collections.singletonList(String.valueOf(SqlType.DDL.ordinal()))));
    }

    @Test
    public void testDoPreHandle_ValidRequest_AllowExecute() {
        // Arrange
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        ConnectionSessionUtil.setRuleSetId(session, 1L);

        // Act
        boolean result = interceptor.doPreHandle(request, new SqlAsyncExecuteResp(), session, Collections.emptyMap());

        // Assert
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_InvalidRequest_NotAllowExecute() {
        // Arrange
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        ConnectionSessionUtil.setRuleSetId(session, 1L);
        request.setQueryLimit(2);

        // Act
        Optional<Integer> queryLimit = Optional.of(1);
        when(sqlConsoleRuleService.getProperties(anyLong(), SqlConsoleRules.MAX_RETURN_ROWS, eq(DialectType.MYSQL),
                Integer.class)).thenReturn(queryLimit);

        // Assert
        assertFalse(interceptor.doPreHandle(request, new SqlAsyncExecuteResp(), session, Collections.emptyMap()));
    }

    @Test
    public void testDoPreHandle_ValidRequest_IgnoreCheck() {
        // Arrange
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        ConnectionSessionUtil.setRuleSetId(session, 1L);
        request.setNeedSqlConsoleCheck(Boolean.FALSE);

        // Act
        boolean result = interceptor.doPreHandle(request, new SqlAsyncExecuteResp(), session, Collections.emptyMap());

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHandle_ValidResponse_AllowExecute() {
        // Arrange
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        ConnectionSessionUtil.setRuleSetId(session, 1L);

        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1L);
        sqlTuple.setOriginalSql("SELECT * FROM test WHERE id = ?");

        List<SqlTuple> sqlTuples = Arrays.asList(sqlTuple);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(sqlTuples);

        // Act
        boolean result = interceptor.handle(request, response, session, Collections.emptyMap());

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHandle_InvalidResponse_ForbiddenExecute() {
        // Arrange
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        ConnectionSessionUtil.setRuleSetId(session, 1L);

        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setSqlId(1L);
        sqlTuple.setOriginalSql("SELECT * FROM test WHERE id = ?");

        List<SqlTuple> sqlTuples = Arrays.asList(sqlTuple);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(sqlTuples);

        Optional<Integer> maxSqlSize = Optional.of(1);
        Rule maxSqlSizeRule = new Rule();
        RuleViolation violation = new RuleViolation();
        violation.setLevel(Rule.Level.ERROR);
        violation.setLocalizedMessage("Maximum number of SQLs executed exceeded");
        maxSqlSizeRule.setViolation(violation);

        when(sqlConsoleRuleService.getProperties(anyLong(), SqlConsoleRules.MAX_EXECUTE_SQLS, eq(DialectType.MYSQL),
                Integer.class)).thenReturn(maxSqlSize);

        // Act
        boolean result = interceptor.handle(request, response, session, Collections.emptyMap());

        // Assert
        assertFalse(result);
    }

    @Test
    public void testDetermineSqlType_SqlTypeKnown_ReturnParsedResult() throws SyntaxErrorException {
        // Arrange
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM test WHERE id = ?");

        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        BasicResult basicResult = new BasicResult(SqlType.DDL);

        when(sqlTuple.getAst()).thenReturn(ast);
        when(ast.parse()).thenReturn(basicResult);

        // Act
        BasicResult result = interceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        // Assert
        assertEquals(basicResult, result);
    }

    @Test
    public void testDetermineSqlType_SqlTypeUnknown_ReturnOther() {
        // Arrange
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM test WHERE id = ?");

        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        BasicResult basicResult = new BasicResult(SqlType.OTHERS);

        when(sqlTuple.getAst()).thenReturn(ast);

        // Act
        BasicResult result = interceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        // Assert
        assertEquals(basicResult, result);
    }

    @Test
    public void testDoAfterCompletion_SuccessResult_AllowExport() {
        // Arrange
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(Status.SUCCESS);

        // Act
        interceptor.doAfterCompletion(result, session, Collections.emptyMap());

        // Assert
        assertTrue(result.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_FailedResult_NotAllowExport() {
        // Arrange
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(Status.FAILED);

        // Act
        interceptor.doAfterCompletion(result, session, Collections.emptyMap());

        // Assert
        assertFalse(result.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_SuccessResultWithEditableResultSet_NotAllowExport() {
        // Arrange
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(Status.SUCCESS);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.isEditable()).thenReturn(true);
        result.setResultSetMetaData(metaData);

        // Act
        interceptor.doAfterCompletion(result, session, Collections.emptyMap());

        // Assert
        assertFalse(result.isAllowExport());
    }
}