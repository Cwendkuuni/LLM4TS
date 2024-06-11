package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parse.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parse.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

/**
 * Test class for SqlConsoleInterceptor.
 */
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

    private ConnectionSession session;
    private SqlAsyncExecuteReq sqlAsyncExecuteReq;
    private SqlAsyncExecuteResp sqlAsyncExecuteResp;
    private Map<String, Object> context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
        sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
        context = new HashMap<>();
    }

    @After
    public void tearDown() {
        reset(authenticationFacade, ruleService, sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_whenRuleSetIdNull_thenReturnTrue() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        assertFalse(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context));
    }

    @Test
    public void testDoPreHandle_whenIndividualTeam_thenReturnTrue() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        assertFalse(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context));
    }

    @Test
    public void testDoPreHandle_whenNeedSqlConsoleCheckFalse_thenReturnTrue() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
        assertTrue(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context));
    }

    @Test
    public void testHandle_whenRuleSetIdNull_thenReturnTrue() {
        SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();
        assertFalse(sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context));
    }

    @Test
    public void testHandle_whenLimitNull_thenReturnTrue() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySql,
                Integer.class)).thenReturn(Optional.empty());
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        assertFalse(sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context));
    }

    @Test
    public void testHandle_whenMaxSqlSizeExceeded_thenReturnFalse() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySql,
                Integer.class)).thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySql,
                String.class)).thenReturn(Optional.of(Collections.singletonList("DDL")));

        SqlTuplesWithViolation sqlTupleWithViolation = new SqlTuplesWithViolation();
        sqlTupleWithViolation.setSqlTuple(new SqlTuple());
        sqlTupleWithViolation.getSqlTuple().setOriginalSql("CREATE TABLE myTable (id INT PRIMARY KEY, name VARCHAR(50))");
        sqlAsyncExecuteResp.getSqls().add(sqlTupleWithViolation);

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);
        assertFalse(sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context));
    }

    @Test
    public void testHandle_whenMaxSqlSizeNotExceeded_thenReturnTrue() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySql,
                Integer.class)).thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySql,
                String.class)).thenReturn(Optional.of(Collections.singletonList("DDL")));

        SqlTuplesWithViolation sqlTupleWithViolation = new SqlTuplesWithViolation();
        sqlTupleWithViolation.setSqlTuple(new SqlTuple());
        sqlTupleWithViolation.getSqlTuple().setOriginalSql("CREATE TABLE myTable (id INT PRIMARY KEY, name VARCHAR(50))");
        sqlAsyncExecuteResp.getSqls().add(sqlTupleWithViolation);

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        assertTrue(sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context));
    }

    @Test
    public void testHandle_whenSqlTypeNotAllowed_thenReturnFalse() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySql,
                Integer.class)).thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySql,
                String.class)).thenReturn(Optional.of(Collections.singletonList("DDL")));

        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT 1 FROM dual");
        SqlTuplesWithViolation sqlTupleWithViolation = new SqlTuplesWithViolation();
        sqlTupleWithViolation.setSqlTuple(sqlTuple);
        sqlAsyncExecuteResp.getSqls().add(sqlTupleWithViolation);

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySql,
                String.class)).thenReturn(Optional.of(Collections.singletonList("DDL_QUERY")));

        assertFalse(sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context));
    }

    @Test
    public void testHandle_whenSqlTypeAllowed_thenReturnTrue() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySql,
                Integer.class)).thenReturn(Optional.of(5));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySql,
                String.class)).thenReturn(Optional.of(Collections.singletonList("DDL_QUERY")));

        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT 1 FROM dual");
        SqlTuplesWithViolation sqlTupleWithViolation = new SqlTuplesWithViolation();
        sqlTupleWithViolation.setSqlTuple(sqlTuple);
        sqlAsyncExecuteResp.getSqls().add(sqlTupleWithViolation);

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySql,
                String.class)).thenReturn(Optional.of(Collections.singletonList("DDL_QUERY")));

        assertTrue(sqlConsoleInterceptor.handle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, session, context));
    }

    @Test
    public void testDoAfterCompletion_whenStatusNotSuccess_thenReturn() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.ERROR);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);
    }

    @Test
    public void testDoAfterCompletion_whenNotAllowedEditResultSet_thenSetFalse() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        sqlExecuteResult.setResultSetMetaData(new Object() {});
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);
        assertFalse(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_whenAllowedEditResultSet_thenSetTrue() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        sqlExecuteResult.setResultSetMetaData(new Object() {});
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(false);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);
        assertTrue(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam_whenIndividualTeam_thenReturnTrue() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        assertTrue(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testIsIndividualTeam_whenTeam_thenReturnFalse() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        assertFalse(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType_whenNoParseError_thenReturnBasicResult() {
        SqlTuple sqlTuple = new SqlTuple();
        AbstractSyntaxTree ast = new AbstractSyntaxTree();
        sqlTuple.setAst(ast);
        BasicResult basicResult = new BasicResult();
        basicResult.setSqlType(SqlType.DDL_QUERY);

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        assertEquals(basicResult, sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySql));
    }

    @Test
    public void testDetermineSqlType_whenParseError_thenReturnBasicResult() {
        SqlTuple sqlTuple = new SqlTuple();
        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(ast.getParseResult().isSyntaxError()).thenReturn(true);

        BasicResult basicResult = new BasicResult();
        basicResult.setSqlType(SqlType.OTHERS);

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        assertEquals(basicResult, sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MySql));
    }
}