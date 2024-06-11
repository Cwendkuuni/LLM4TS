import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor.SqlAsyncExecuteResp;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;

@ActiveProfiles("test")
@ContextConfiguration(classes = SqlConsoleInterceptor.class)
public class SqlConsoleInterceptorTest {

 private ApplicationContext context;
 private AuthenticationFacade authenticationFacade;
 private RuleService ruleService;
 private SqlConsoleRuleService sqlConsoleRuleService;
 private SqlConsoleInterceptor sqlConsoleInterceptor;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 context = Mockito.mock(ApplicationContext.class);
 authenticationFacade = Mockito.mock(AuthenticationFacade.class);
 ruleService = Mockito.mock(RuleService.class);
 sqlConsoleRuleService = Mockito.mock(SqlConsoleRuleService.class);
 sqlConsoleInterceptor = new SqlConsoleInterceptor();
 sqlConsoleInterceptor.setApplicationContext(context);
 sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
 sqlConsoleInterceptor.setRuleService(ruleService);
 sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
 }

 @Test
 public void testHandle_true() {
 ConnectionSession session = Mockito.mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 Mockito.when(authenticationFacade.currentUser()).thenReturn(new Object());
 Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 Mockito.when(sqlConsoleRuleService.getProperties(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(SqlConsoleRules.MAX_RETURN_ROWS),
 ArgumentMatchers.eq(DialectType.MYSQL), ArgumentMatchers.eq(Integer.class))).thenReturn(Optional.of(10));

 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setSqlId("1");
 sqlTuple.setOriginalSql("SELECT * FROM users");

 SqlTuple sqlTuple2 = new SqlTuple();
 sqlTuple2.setSqlId("2");
 sqlTuple2.setOriginalSql("SELECT * FROM users limit 11");

 List<SqlTuple> sqlTuples = List.of(sqlTuple, sqlTuple2);
 response.setSqls(sqlTuples);

 Mockito.when(sqlConsoleRuleService.getListProperties(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(SqlConsoleRules.ALLOW_SQL_TYPES),
 ArgumentMatchers.eq(DialectType.MYSQL), ArgumentMatchers.eq(String.class))).thenReturn(Optional.of(List.of("SELECT")));

 Mockito.when(ruleService.getByRulesetIdAndName(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())))
 .thenReturn(Optional.of(new Rule()));

 Mockito.when(ruleService.getByRulesetIdAndName(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName())))
 .thenReturn(Optional.of(new Rule()));

 Mockito.when(response.getSqls()).thenReturn(sqlTuples);

 boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
 Assert.assertTrue(result);
 }

 @Test
 public void testHandle_false() {
 ConnectionSession session = Mockito.mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 Mockito.when(authenticationFacade.currentUser()).thenReturn(new Object());
 Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 Mockito.when(sqlConsoleRuleService.getProperties(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(SqlConsoleRules.MAX_RETURN_ROWS),
 ArgumentMatchers.eq(DialectType.MYSQL), ArgumentMatchers.eq(Integer.class))).thenReturn(Optional.empty());

 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setSqlId("1");
 sqlTuple.setOriginalSql("SELECT * FROM users");

 SqlTuple sqlTuple2 = new SqlTuple();
 sqlTuple2.setSqlId("2");
 sqlTuple2.setOriginalSql("SELECT * FROM users limit 11");

 List<SqlTuple> sqlTuples = List.of(sqlTuple, sqlTuple2);
 response.setSqls(sqlTuples);

 Mockito.when(ruleService.getByRulesetIdAndName(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName())))
 .thenReturn(Optional.empty());

 Mockito.when(response.getSqls()).thenReturn(sqlTuples);

 boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
 Assert.assertFalse(result);
 }

 @Test(expected = SyntaxErrorException.class)
 public void testHandle_sqlParseError() {
 ConnectionSession session = Mockito.mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 Mockito.when(authenticationFacade.currentUser()).thenReturn(new Object());
 Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setSqlId("1");
 sqlTuple.setOriginalSql("CREATE TABLE users (id int, name varchar(10));DROP TABLE users;");
 sqlTuple.setAst(Mockito.mock(AbstractSyntaxTree.class));

 List<SqlTuple> sqlTuples = List.of(sqlTuple);
 response.setSqls(sqlTuples);

 Mockito.when(sqlConsoleRuleService.getListProperties(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(SqlConsoleRules.ALLOW_SQL_TYPES),
 ArgumentMatchers.eq(DialectType.MYSQL), ArgumentMatchers.eq(String.class))).thenReturn(Optional.of(List.of("CREATE TABLE")));

 Mockito.when(ruleService.getByRulesetIdAndName(ArgumentMatchers.eq(1L), ArgumentMatchers.eq(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName())))
 .thenReturn(Optional.of(new Rule()));

 Mockito.when(response.getSqls()).thenReturn(sqlTuples);

 boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
 Assert.assertFalse(result);
 }

 @Test
 public void testDoAfterCompletion_true() {
 ConnectionSession session = Mockito.mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();
 SqlExecuteResult response = new SqlExecuteResult();
 response.setAllowExport(true);

 Mockito.when(authenticationFacade.currentUser()).thenReturn(new Object());
 Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

 Mockito.when(sqlConsoleRuleService.isForbidden(ArgumentMatchers.eq(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET), ArgumentMatchers.eq(session)))
 .thenReturn(false);

 boolean result = sqlConsoleInterceptor.doAfterCompletion(response, session, context);
 Assert.assertTrue(result);
 }

 @Test
 public void testDoAfterCompletion_false() {
 ConnectionSession session = Mockito.mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();
 SqlExecuteResult response = new SqlExecuteResult();
 response.setAllowExport(false);

 Mockito.when(authenticationFacade.currentUser()).thenReturn(new Object());
 Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

 Mockito.when(sqlConsoleRuleService.isForbidden(ArgumentMatchers.eq(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET), ArgumentMatchers.eq(session)))
 .thenReturn(true);

 boolean result = sqlConsoleInterceptor.doAfterCompletion(response, session, context);
 Assert.assertFalse(result);
 }

}