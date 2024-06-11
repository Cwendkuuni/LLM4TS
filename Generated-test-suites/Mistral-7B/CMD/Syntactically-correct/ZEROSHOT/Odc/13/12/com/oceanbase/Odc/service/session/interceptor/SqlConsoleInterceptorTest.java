package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
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
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

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

    private ConnectionSession session;
    private User user;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        user = new User();
        when(authenticationFacade.currentUser()).thenReturn(user);
        session = mock(ConnectionSession.class);
        ConnectionSessionUtil.setRuleSetId(session, 1L);
    }

    @Test
    public void testDoPreHandle_true() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(20));

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, Collections.emptyMap());
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_false() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.empty());

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, Collections.emptyMap());
        assertFalse(result);
    }

    @Test
    public void testHandle_true() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(new SqlTuple("select * from t", "1",
                null, null, null), null, new ArrayList<>())));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(1));

        boolean result = sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
        assertTrue(result);
    }

    @Test
    public void testHandle_false() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(new SqlTuple("select * from t", "1",
                null, null, null), null, null)));
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(1));
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(new Rule()));

        boolean result = sqlConsoleInterceptor.handle(request, response, session, Collections.emptyMap());
        assertFalse(result);
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = new SqlTuple("select * from t", "1", null, null, null);
        AbstractSyntaxTree ast = AbstractSyntaxTreeFactories.getAstFactory(DialectType.MYSQL, 0)
                .parse("select * from t");
        sqlTuple.setAst(ast);
        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
        assertEquals(SqlType.SELECT, result.getSqlType());
    }

    @Test
    public void testDoAfterCompletion_true() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        sqlConsoleInterceptor.doAfterCompletion(response, session, Collections.emptyMap());
        assertTrue(response.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_false() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);
        sqlConsoleInterceptor.doAfterCompletion(response, session, Collections.emptyMap());
        assertFalse(response.isAllowExport());
    }

}