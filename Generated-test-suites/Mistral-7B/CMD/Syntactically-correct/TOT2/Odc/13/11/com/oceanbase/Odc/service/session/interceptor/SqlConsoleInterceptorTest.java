import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;
import static org.junit.Assert.*;

@ContextConfiguration(classes = SqlConsoleInterceptor.class)
@ActiveProfiles("test")
@SpringJUnitConfig(classes = { SqlConsoleInterceptor.class })
public class SqlConsoleInterceptorTest {

 private SqlConsoleInterceptor sqlConsoleInterceptor;
 private AuthenticationFacade authenticationFacade;
 private RuleService ruleService;
 private SqlConsoleRuleService sqlConsoleRuleService;
 private ConnectionSession connectionSession;

 @Autowired
 public void setUpMocks(AuthenticationFacade authenticationFacade, RuleService ruleService,
 SqlConsoleRuleService sqlConsoleRuleService) {
 this.authenticationFacade = authenticationFacade;
 this.ruleService = ruleService;
 this.sqlConsoleRuleService = sqlConsoleRuleService;
 }

 @Before
 public void setUp() {
 connectionSession = Mockito.mock(ConnectionSession.class);
 sqlConsoleInterceptor = new SqlConsoleInterceptor();
 sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
 sqlConsoleInterceptor.setRuleService(ruleService);
 sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
 }

 @Test
 public void testDoPreHandle_allowExecute() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 response.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple("test", "SELECT * FROM testTable"), sqlExecuteResult)));
 Map<String, Object> context = new HashMap<>();

 assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context));
 }

 @Test
 public void testDoPreHandle_forbiddenToCreatePl() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 SqlTuple sqlTuple = new SqlTuple("test", "CREATE PROCEDURE test_procedure()", null, null);
 List<SqlTuplesWithViolation> sqlTuples = List.of(new SqlTuplesWithViolation(sqlTuple, sqlExecuteResult));
 response.setSqls(sqlTuples);

 Mockito.when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
 Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
 .thenReturn(true);

 assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, new HashMap<>()));
 }

 @Test
 public void testDoPreHandle_maxSqlSize() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple("test1", "SELECT * FROM testTable", null, null),
 new SqlTuple("test2", "SELECT * FROM testTable", null, null), new SqlTuple("test3", "SELECT * FROM testTable", null, null));
 response.setSqls(sqlTuples.stream().map(SqlTuplesWithViolation::new).collect(Collectors.toList()));

 Mockito.when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
 Mockito.when(sqlConsoleRuleService.getProperties(ArgumentMatchers.anyLong(), SqlConsoleRules.MAX_EXECUTE_SQLS,
 Equals.anyObject(), Equals.anyInt()))
 .thenReturn(Optional.of(1));

 assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, new HashMap<>()));
 }

 @Test
 public void testDoPreHandle_allowSqlTypes() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 SqlTuple sqlTuple = new SqlTuple("test", "CREATE PROCEDURE test_procedure()", null, null);
 List<SqlTuplesWithViolation> sqlTuples = List.of(new SqlTuplesWithViolation(sqlTuple, sqlExecuteResult));
 response.setSqls(sqlTuples);

 Mockito.when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
 Mockito.when(sqlConsoleRuleService.getListProperties(ArgumentMatchers.anyLong(), SqlConsoleRules.ALLOW_SQL_TYPES,
 Equals.anyObject(), Equals.anyClass()))
 .thenReturn(Optional.of(Arrays.asList("DDL")));

 assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, new HashMap<>()));
 }

 @Test(expected = SyntaxErrorException.class)
 public void testDoPreHandle_syntaxError() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 SqlTuple sqlTuple = new SqlTuple("test", "CREATE PROCEDURE test_procedure()", null, null);
 List<SqlTuplesWithViolation> sqlTuples = List.of(new SqlTuplesWithViolation(sqlTuple, sqlExecuteResult));
 response.setSqls(sqlTuples);

 Mockito.when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
 sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, new HashMap<>());
 }

 @Test
 public void testDoAfterCompletion_allowExport() {
 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 SqlConsoleInterceptor sqlConsoleInterceptor = Mockito.spy(new SqlConsoleInterceptor());
 sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>());
 assertTrue(sqlExecuteResult.isAllowExport());
 }

 @Test
 public void testDoAfterCompletion_forbiddenToExport() {
 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, connectionSession))
 .thenReturn(true);
 SqlConsoleInterceptor sqlConsoleInterceptor = Mockito.spy(new SqlConsoleInterceptor());
 sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, new HashMap<>());
 assertFalse(sqlExecuteResult.isAllowExport());
 }

 @Test
 public void testDetermineSqlType_validSql() throws Exception {
 SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
 SqlTuple sqlTuple = new SqlTuple("test", "SELECT * FROM testTable", null, null);
 sqlAsyncExecuteResp.setSqls(List.of(sqlTuple));

 BasicResult expectedResult = new BasicResult(SqlType.SELECT);
 SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();
 BasicResult actualResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testDetermineSqlType_invalidSql() throws Exception {
 SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
 SqlTuple sqlTuple = new SqlTuple("test", "SELECT", null, null);
 sqlAsyncExecuteResp.setSqls(List.of(sqlTuple));

 BasicResult expectedResult = new BasicResult(SqlType.OTHERS);
 SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();
 BasicResult actualResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = SyntaxErrorException.class)
 public void testDetermineSqlType_syntaxError() throws Exception {
 SqlAsyncExecuteResp sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
 SqlTuple sqlTuple = new SqlTuple("test", "SELECT FROM testTable", null, null);
 sqlAsyncExecuteResp.setSqls(List.of(sqlTuple));

 SqlConsoleInterceptor sqlConsoleInterceptor = new SqlConsoleInterceptor();
 BasicResult actualResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
 fail("Should throw SyntaxErrorException");
 }
}