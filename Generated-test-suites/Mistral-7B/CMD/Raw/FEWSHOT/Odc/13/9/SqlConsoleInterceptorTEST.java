package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.SqlTuplesWithViolation;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationResponse;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@Component
public class SqlConsoleInterceptorTest {

    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private SqlConsoleRuleService sqlConsoleRuleService;

    private User currentUser;
    private ConnectionSession session;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        currentUser = new User();
        session = mock(ConnectionSession.class);
        when(authenticationFacade.currentUser()).thenReturn(currentUser);
        when(session.getRuleSetId()).thenReturn(1L);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
    }

    @Test
    public void testDoPreHandle_NoRuleSetId_True() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        boolean result = new SqlConsoleInterceptor().doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_RuleSetIdPresent_True() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        boolean result = new SqlConsoleInterceptor().doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle_MaxReturnRowsNotExceeded_True() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        sqlTuple.setSqlTuple(new SqlTuple());
        sqlTuple.setViolatedRules(new ArrayList<>());
        response.setSqls(sqlTuples);
        Optional<Integer> queryLimit = Optional.of(50);
        assertEquals(true, new SqlConsoleInterceptor().handle(request, response, session, context).booleanValue());
    }

    @Test
    public void testHandle_MaxReturnRowsExceeded_False() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        sqlTuple.setSqlTuple(new SqlTuple());
        sqlTuple.setViolatedRules(new ArrayList<>());
        response.setSqls(sqlTuples);
        Optional<Integer> queryLimit = Optional.of(10);
        assertEquals(false, new SqlConsoleInterceptor().handle(request, response, session, context).booleanValue());
    }

    @Test
    public void testHandle_AllowSqlTypesPresent_True() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        sqlTuple.setSqlTuple(new SqlTuple());
        sqlTuple.setViolatedRules(new ArrayList<>());
        response.setSqls(sqlTuples);
        Optional<List<String>> allowSqlTypes = Optional.of(new ArrayList<String>() {{
            add("DDL");
        }});
        assertEquals(true, new SqlConsoleInterceptor().handle(request, response, session, context).booleanValue());
    }

    @Test
    public void testHandle_AllowSqlTypesNotPresent_False() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        sqlTuple.setSqlTuple(new SqlTuple());
        sqlTuple.setViolatedRules(new ArrayList<>());
        response.setSqls(sqlTuples);
        Optional<List<String>> allowSqlTypes = Optional.empty();
        assertEquals(false, new SqlConsoleInterceptor().handle(request, response, session, context).booleanValue());
    }

    @Test
    public void testHandle_ForbiddenToCreatePl_False() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        sqlTuple.setSqlTuple(new SqlTuple());
        sqlTuple.setViolatedRules(new ArrayList<>());
        response.setSqls(sqlTuples);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        assertEquals(true, new SqlConsoleInterceptor().handle(request, response, session, context).booleanValue());
    }

    @Test
    public void testHandle_ForbiddenToCreatePl_True() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
        sqlTuple.setSqlTuple(new SqlTuple());
        sqlTuple.setViolatedRules(new ArrayList<>());
        response.setSqls(sqlTuples);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);
        Rule violationRule = new Rule();
        RuleViolation violation = new RuleViolation();
        violation.setLevel(1);
        violation.setLocalizedMessage("Create PL is forbidden");
        violationRule.setViolation(violation);
        sqlTuple.getViolatedRules().add(violationRule);
        assertEquals(false, new SqlConsoleInterceptor().handle(request, response, session, context).booleanValue());
    }

    @Test
    public void testDoAfterCompletion_NotAllowEditResultSet_False() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setAllowExport(false);
        assertEquals(false, new SqlConsoleInterceptor().doAfterCompletion(response, session, new HashMap<>()).booleanValue());
    }

    @Test
    public void testDoAfterCompletion_AllowEditResultSet_True() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setAllowExport(true);
        assertEquals(true, new SqlConsoleInterceptor().doAfterCompletion(response, session, new HashMap<>()).booleanValue());
    }

    @Test
    public void testIsIndividualTeam_True() {
        User user = new User();
        user.setOrganizationType(OrganizationType.INDIVIDUAL);
        assertEquals(true, new SqlConsoleInterceptor().isIndividualTeam());
    }

    @Test
    public void testIsIndividualTeam_False() {
        User user = new User();
        user.setOrganizationType(OrganizationType.TEAM);
        assertEquals(false, new SqlConsoleInterceptor().isIndividualTeam());
    }

    @Test(expected = SyntaxErrorException.class)
    public void testDetermineSqlType_SyntaxError_ThrowException() {
        SqlTuple sqlTuple = new SqlTuple();
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        interceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
    }

    @Test
    public void testDetermineSqlType_DDL_DDL() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(mock(AbstractSyntaxTree.class));
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        when(sqlTuple.getAst().getParseResult().getSqlType()).thenReturn(SqlType.DDL);
        assertEquals(SqlType.DDL, interceptor.determineSqlType(sqlTuple, DialectType.MYSQL).getSqlType());
    }

    @Test
    public void testDetermineSqlType_DDL_OTHERS() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(mock(AbstractSyntaxTree.class));
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        when(sqlTuple.getAst().getParseResult().getSqlType()).thenReturn(SqlType.OTHERS);
        assertEquals(SqlType.OTHERS, interceptor.determineSqlType(sqlTuple, DialectType.MYSQL).getSqlType());
    }

    @Test
    public void testDetermineSqlType_DML_DDL() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(mock(AbstractSyntaxTree.class));
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        when(sqlTuple.getAst().getParseResult().getSqlType()).thenReturn(SqlType.DDL);
        assertEquals(SqlType.DDL, interceptor.determineSqlType(sqlTuple, DialectType.MYSQL).getSqlType());
    }

    @Test
    public void testDetermineSqlType_DML_DML() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(mock(AbstractSyntaxTree.class));
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        when(sqlTuple.getAst().getParseResult().getSqlType()).thenReturn(SqlType.DML);
        assertEquals(SqlType.DML, interceptor.determineSqlType(sqlTuple, DialectType.MYSQL).getSqlType());
    }

    @Test
    public void testDetermineSqlType_DML_OTHERS() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(mock(AbstractSyntaxTree.class));
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        when(sqlTuple.getAst().getParseResult().getSqlType()).thenReturn(SqlType.OTHERS);
        assertEquals(SqlType.OTHERS, interceptor.determineSqlType(sqlTuple, DialectType.MYSQL).getSqlType());
    }

    @Test
    public void testDetermineSqlType_Unknown_Unknown() {
        SqlTuple sqlTuple = new SqlTuple();
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        assertEquals(SqlType.OTHERS, interceptor.determineSqlType(sqlTuple, DialectType.MYSQL).getSqlType());
    }
}