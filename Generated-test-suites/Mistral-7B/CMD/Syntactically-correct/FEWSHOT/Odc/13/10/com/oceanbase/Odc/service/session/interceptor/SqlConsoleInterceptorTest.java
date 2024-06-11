package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.core.sql.parser.SyntaxErrorException;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

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
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private Map<String, Object> context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();
        context = new HashMap<>();
    }

    @Test
    public void testHandle_noRuleSetId_returnTrue() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_isIndividualTeam_returnTrue() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_needSqlConsoleCheck_returnFalse() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, Boolean.FALSE);
        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandle_handleSqlTuples_returnFalse() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL, Integer.class))
                .thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL,
                        String.class)).thenReturn(Optional.of(Arrays.asList("SELECT")));

        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("SELECT * FROM table1", 1, ""));
        response.setSqls(sqlTuples);

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(false);

        Rule violationRule = mock(Rule.class);
        RuleViolation violation = new RuleViolation();
        violation.setLevel(Rule.Level.ERROR);
        violation.setLocalizedMessage("Message");
        violationRule.setViolation(violation);

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.of(new Rule()));
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(new Rule()));

        List<Rule> violatedRules = Arrays.asList(violationRule);

        SqlTuple sqlTuple = sqlTuples.get(0);
        BasicResult parseResult = new BasicResult(SqlType.SELECT);
        when(determineSqlType(sqlTuple, DialectType.MySQL)).thenReturn(parseResult);

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        sqlConsoleInterceptor.handle(request, response, session, context);

        verify(ruleService, times(2)).getByRulesetIdAndName(1L, anyString());
        verify(sqlConsoleRuleService, times(1)).isForbidden(anyString(), session);
        verify(sqlConsoleRuleService, times(1)).getListProperties(1L, anyString(), DialectType.MySQL, any());
        verify(ruleService, times(1)).getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName());
        verify(ruleService, never()).getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
        verify(response, never()).setSqls(anyList());
        verify(context, never()).put(anyString(), any());
        assertFalse(allowExecute.get());
    }

    @Test
    public void testHandle_handleSqlTuples_returnTrue() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MySQL, Integer.class))
                .thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL,
                        String.class)).thenReturn(Optional.of(Collections.singletonList("SELECT")));

        List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("SELECT * FROM table1", 1, ""),
                new SqlTuple("CREATE TABLE table2 (id int)", 2, ""));
        response.setSqls(sqlTuples);

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(false);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(false);

        Rule violationRule = mock(Rule.class);
        RuleViolation violation = new RuleViolation();
        violation.setLevel(Rule.Level.ERROR);
        violation.setLocalizedMessage("Message");
        violationRule.setViolation(violation);

        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
                .thenReturn(Optional.of(new Rule()));
        when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
                .thenReturn(Optional.of(new Rule()));

        List<Rule> violatedRules = Collections.emptyList();

        SqlTuple sqlTuple1 = sqlTuples.get(0);
        BasicResult parseResult1 = new BasicResult(SqlType.SELECT);
        when(determineSqlType(sqlTuple1, DialectType.MySQL)).thenReturn(parseResult1);

        SqlTuple sqlTuple2 = sqlTuples.get(1);
        BasicResult parseResult2 = new BasicResult(SqlType.CREATE_TABLE);
        when(determineSqlType(sqlTuple2, DialectType.MySQL)).thenReturn(parseResult2);

        AtomicBoolean allowExecute = new AtomicBoolean(true);
        sqlConsoleInterceptor.handle(request, response, session, context);

        verify(ruleService, times(2)).getByRulesetIdAndName(1L, anyString());
        verify(sqlConsoleRuleService, times(1)).isForbidden(anyString(), session);
        verify(sqlConsoleRuleService, times(1)).getListProperties(1L, anyString(), DialectType.MySQL, any());
        verify(ruleService, times(1)).getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName());
        verify(ruleService, times(1)).getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
        verify(response, times(1)).setSqls(anyList());
        verify(context, never()).put(anyString(), any());
        assertTrue(allowExecute.get());
    }

    @Test
    public void testDoAfterCompletion_notAllowedExportResultSet() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(true);

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlExecuteResult.setAllowExport(true);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);

        verify(sqlExecuteResult, times(1)).setAllowExport(false);
    }

    @Test
    public void testDoAfterCompletion_allowedExportResultSet() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(false);

        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);

        verify(sqlExecuteResult, times(1)).setAllowExport(true);
    }

    @Test
    public void testDetermineSqlType_sqlTypeUnknown_setSqlTypeOthers() {
        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM table1", 1, "");
        DialectType dialectType = DialectType.MySQL;
        BasicResult basicResult = new BasicResult(SqlType.UNKNOWN);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(SqlType.OTHERS, result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_sqlTypeValid_returnBasicResult() {
        SqlTuple sqlTuple = new SqlTuple("SELECT * FROM table1", 1, "");
        DialectType dialectType = DialectType.MySQL;
        BasicResult expectedBasicResult = new BasicResult(SqlType.SELECT);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(expectedBasicResult, result);
    }

    @Test(expected = SyntaxErrorException.class)
    public void testDetermineSqlType_syntaxError_returnUnknownType() {
        SqlTuple sqlTuple = new SqlTuple("CREATE TABLE table1", 1, "");
        DialectType dialectType = DialectType.MySQL;
        BasicResult expectedBasicResult = new BasicResult(SqlType.UNKNOWN);

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(expectedBasicResult, result);
    }
}