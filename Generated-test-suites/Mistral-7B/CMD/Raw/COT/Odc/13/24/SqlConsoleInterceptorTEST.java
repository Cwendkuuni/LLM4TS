import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private ConnectionSession connectionSession;
    private Map<String, Object> context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        connectionSession = mock(ConnectionSession.class);
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle_NoRuleSetId_ReturnTrue() {
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(null);
        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        boolean actualResult = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        Assert.assertTrue(actualResult);
    }

    @Test
    public void testDoPreHandle_RuleSetIdPresent_ReturnsTrueIfNotNeedCheck() {
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(context.get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK)).thenReturn(false);
        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        boolean actualResult = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
        Assert.assertTrue(actualResult);
    }

    @Test
    public void testHandle_NoViolations_ReturnsTrue() {
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, SqlType.MySQL, Integer.class))
                .thenReturn(Optional.ofNullable(10));
        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        when(response.getSqls()).thenReturn(new ArrayList<>());
        boolean actualResult = sqlConsoleInterceptor.handle(request, response, connectionSession, context);
        Assert.assertTrue(actualResult);
    }

    @Test
    public void testHandle_MaxSqlSizeExceeded_ReturnsFalse() {
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, SqlType.MySQL, Integer.class))
                .thenReturn(Optional.of(5));
        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        List<SqlTuplesWithViolation> sqls = new ArrayList<>();
        SqlTuplesWithViolation sql1 = Mockito.mock(SqlTuplesWithViolation.class);
        SqlTuplesWithViolation sql2 = Mockito.mock(SqlTuplesWithViolation.class);
        sqls.add(sql1);
        sqls.add(sql2);
        when(response.getSqls()).thenReturn(sqls);
        boolean actualResult = sqlConsoleInterceptor.handle(request, response, connectionSession, context);
        Assert.assertFalse(actualResult);
    }

    @Test
    public void testHandle_ForbiddenSqlType_ReturnsFalse() {
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, SqlType.MySQL, String.class))
                .thenReturn(Optional.of(new ArrayList<>()));
        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        List<SqlTuplesWithViolation> sqls = new ArrayList<>();
        SqlTuplesWithViolation sql1 = Mockito.mock(SqlTuplesWithViolation.class);
        SqlTuplesWithViolation sql2 = Mockito.mock(SqlTuplesWithViolation.class);
        sqls.add(sql1);
        sqls.add(sql2);
        when(response.getSqls()).thenReturn(sqls);
        when(determineSqlType(sql1, SqlType.MySQL)).thenReturn(new BasicResult(SqlType.DDL));
        boolean actualResult = sqlConsoleInterceptor.handle(request, response, connectionSession, context);
        Assert.assertFalse(actualResult);
    }

    @Test
    public void testHandle_NoViolations_ReturnsTrue_whenAllows() {
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, SqlType.MySQL, Integer.class))
                .thenReturn(Optional.ofNullable(10));
        SqlAsyncExecuteReq request = Mockito.mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = Mockito.mock(SqlAsyncExecuteResp.class);
        List<SqlTuplesWithViolation> sqls = new ArrayList<>();
        SqlTuplesWithViolation sql1 = Mockito.mock(SqlTuplesWithViolation.class);
        SqlTuplesWithViolation sql2 = Mockito.mock(SqlTuplesWithViolation.class);
        sqls.add(sql1);
        sqls.add(sql2);
        when(response.getSqls()).thenReturn(sqls);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession)).thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, SqlType.MySQL, String.class))
                .thenReturn(Optional.of(new ArrayList<>()));
        boolean actualResult = sqlConsoleInterceptor.handle(request, response, connectionSession, context);
        Assert.assertTrue(actualResult);
    }

    @Test
    public void testDoAfterCompletion_NoViolations_AllowExport() {
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, connectionSession))
                .thenReturn(false);
        SqlExecuteResult response = Mockito.mock(SqlExecuteResult.class);
        boolean actualResult = sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, context);
        Assert.assertTrue(actualResult);
    }

    @Test
    public void testDoAfterCompletion_WithViolations_DisableExport() {
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, connectionSession))
                .thenReturn(false);
        SqlExecuteResult response = Mockito.mock(SqlExecuteResult.class);
        RuleViolation violation = Mockito.mock(RuleViolation.class);
        Rule rule = Mockito.mock(Rule.class);
        when(rule.getViolation()).thenReturn(violation);
        List<Rule> violatedRules = new ArrayList<>();
        violatedRules.add(rule);
        when(response.getViolatedRules()).thenReturn(violatedRules);
        boolean actualResult = sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, context);
        Assert.assertFalse(actualResult);
    }

    @Test
    public void testGetExecuteStageName_ReturnsExpectedName() {
        String actualResult = sqlConsoleInterceptor.getExecuteStageName();
        Assert.assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, actualResult);
    }

    @Test
    public void testIsIndividualTeam_ReturnsTrue() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        boolean actualResult = sqlConsoleInterceptor.isIndividualTeam();
        Assert.assertTrue(actualResult);
    }

    @Test
    public void testIsIndividualTeam_ReturnsFalse() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.ENTERPRISE);
        boolean actualResult = sqlConsoleInterceptor.isIndividualTeam();
        Assert.assertFalse(actualResult);
    }

    @Test
    public void testDetermineSqlType_ReturnsExpectedType() {
        SqlTuple sqlTuple = Mockito.mock(SqlTuple.class);
        when(sqlTuple.getOriginalSql()).thenReturn("SELECT * FROM tablename");
        when(sqlTuple.getSqlId()).thenReturn(1L);
        DialectType dialectType = SqlType.MySQL;
        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);
        Assert.assertEquals(SqlType.SQL, basicResult.getSqlType());
    }

    @Test
    public void testDetermineSqlType_ReturnsExpectedType_DDL() {
        SqlTuple sqlTuple = Mockito.mock(SqlTuple.class);
        when(sqlTuple.getOriginalSql()).thenReturn("CREATE TABLE tablename (id INT PRIMARY KEY)");
        when(sqlTuple.getSqlId()).thenReturn(1L);
        DialectType dialectType = SqlType.MySQL;
        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);
        Assert.assertEquals(SqlType.DDL, basicResult.getSqlType());
    }

    @Test
    public void testDetermineSqlType_ReturnsExpectedType_DML() {
        SqlTuple sqlTuple = Mockito.mock(SqlTuple.class);
        when(sqlTuple.getOriginalSql()).thenReturn("SELECT * FROM tablename WHERE id = 1");
        when(sqlTuple.getSqlId()).thenReturn(1L);
        DialectType dialectType = SqlType.MySQL;
        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);
        Assert.assertEquals(SqlType.DML, basicResult.getSqlType());
    }

    private BasicResult determineSqlType(@NonNull SqlTuple sqlTuple, @NonNull DialectType dialectType) {
        return sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);
    }
}