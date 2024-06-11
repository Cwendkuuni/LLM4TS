import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {SqlConsoleInterceptor.class, BaseTimeConsumingInterceptor.class})
@Import({AuthenticationFacade.class, RuleService.class, SqlConsoleRuleService.class})
public class SqlConsoleInterceptorTest extends AbstractJUnit4SpringContextTests {

 private ConnectionSession session;
 private SqlConsoleInterceptor interceptor;
 private AuthenticationFacade authenticationFacade;
 private RuleService ruleService;
 private SqlConsoleRuleService sqlConsoleRuleService;

 @Autowired
 public void setUpMockObjects(AuthenticationFacade authenticationFacade, RuleService ruleService,
 SqlConsoleRuleService sqlConsoleRuleService) {
 this.authenticationFacade = authenticationFacade;
 this.ruleService = ruleService;
 this.sqlConsoleRuleService = sqlConsoleRuleService;
 }

 @Before
 public void setup() {
 session = Mockito.mock(ConnectionSession.class);
 interceptor = new SqlConsoleInterceptor();
 interceptor.setAuthenticationFacade(authenticationFacade);
 interceptor.setRuleService(ruleService);
 interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
 }

 @Test
 public void testDoPreHandle_SqlConsoleIntercepted_SqlCheckIntercepted() throws Exception {
 when(authenticationFacade.currentUser()).thenReturn(Mockito.mock(Object.class));
 when(session.getRuleSetId()).thenReturn(1L);
 when(session.getDialectType()).thenReturn(DialectType.ORACLE);
 when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.ORACLE, Integer.class)).thenReturn(Optional.of(10));
 when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.ORACLE, String.class))
 .thenReturn(Optional.of(List.of("DDL")));

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSql("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255))");
 request.setQueryLimit(15);

 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 response.setSqls(List.of(new SqlTuplesWithViolation(new BasicResult(SqlType.DDL), "test_sql_id")));

 Mockito.when(session.getDialectType()).thenReturn(DialectType.ORACLE);
 Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 Mockito.when(authenticationFacade.currentUser()).thenReturn(Mockito.mock(Object.class));

 assertEquals(true, interceptor.doPreHandle(request, response, session, null));

 Mockito.verify(session).getRuleSetId();
 Mockito.verify(session).getDialectType();
 Mockito.verify(authenticationFacade, Mockito.times(1)).currentUser();
 }

 @Test
 public void testDoPreHandle_SqlConsoleNotIntercepted() throws Exception {
 when(authenticationFacade.currentUser()).thenReturn(Mockito.mock(Object.class));
 when(session.getRuleSetId()).thenReturn(null);

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSql("SELECT * FROM test_table");
 request.setQueryLimit(15);

 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 response.setSqls(List.of(new SqlTuplesWithViolation(new BasicResult(SqlType.SELECT), "test_sql_id")));

 Mockito.when(session.getRuleSetId()).thenReturn(null);
 Mockito.when(authenticationFacade.currentUser()).thenReturn(Mockito.mock(Object.class));

 assertEquals(true, interceptor.doPreHandle(request, response, session, null));

 Mockito.verify(session, Mockito.never()).getRuleSetId();
 }

 @Test
 public void testDoPreHandle_SqlConsoleIntercepted_NoRuleSet() throws Exception {
 when(authenticationFacade.currentUser()).thenReturn(Mockito.mock(Object.class));
 when(session.getRuleSetId()).thenReturn(null);
 when(session.getDialectType()).thenReturn(DialectType.ORACLE);

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSql("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255))");
 request.setQueryLimit(15);

 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 response.setSqls(List.of(new SqlTuplesWithViolation(new BasicResult(SqlType.DDL), "test_sql_id")));

 Mockito.when(session.getRuleSetId()).thenReturn(null);
 Mockito.when(authenticationFacade.currentUser()).thenReturn(Mockito.mock(Object.class));

 assertFalse(interceptor.doPreHandle(request, response, session, null));

 Mockito.verify(session, Mockito.never()).getRuleSetId();
 }

 @Test
 public void testDoPreHandle_SqlConsoleIntercepted_RuleSetExists_MaxRowsExceeded() throws Exception {
 when(authenticationFacade.currentUser()).thenReturn(Mockito.mock(Object.class));
 when(session.getRuleSetId()).thenReturn(1L);
 when(session.getDialectType()).thenReturn(DialectType.ORACLE);

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSql("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255))");
 request.setQueryLimit(16);

 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 response.setSqls(List.of(new SqlTuplesWithViolation(new BasicResult(SqlType.DDL), "test_sql_id")));

 Mockito.when(session.getRuleSetId()).thenReturn(1L);
 Mockito.when(session.getDialectType()).thenReturn(DialectType.ORACLE);
 Mockito.when(authenticationFacade.currentUser()).thenReturn(Mockito.mock(Object.class));
 Mockito.when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.ORACLE, Integer.class))
 .thenReturn(Optional.of(10));
 when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.ORACLE, String.class))
 .thenReturn(Optional.of(List.of("DDL")));

 assertFalse(interceptor.doPreHandle(request, response, session, null));

 Mockito.verify(session).getRuleSetId();
 Mockito.verify(session).getDialectType();
 Mockito.verify(authenticationFacade, Mockito.times(1)).currentUser();
 }

 @Test
 public void testDoPreHandle_SqlConsoleIntercepted_RuleSetExists_MaxSqlsExceeded() throws Exception {
 when(authenticationFacade.currentUser()).thenReturn(Mockito.mock(Object.class));
 when(session.getRuleSetId()).thenReturn(1L);
 when(session.getDialectType()).thenReturn(DialectType.ORACLE);

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSql("SELECT * FROM test_table");
 request.setQueryLimit(15);

 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 response.setSqls(List.of(new SqlTuplesWithViolation(new BasicResult(SqlType.SELECT), "test_sql_id1"),
 new SqlTuplesWithViolation(new BasicResult(SqlType.SELECT), "test_sql_id2"),
 new SqlTuplesWithViolation(new BasicResult(SqlType.SELECT), "test_sql_id3"),
 new SqlTuplesWithViolation(new BasicResult(SqlType.SELECT), "test_sql_id4"),
 new SqlTuplesWithViolation(new BasicResult(SqlType.SELECT), "test_sql_id5")));

 Mockito.when(session.getRuleSetId()).thenReturn(1L);
 Mockito.when(session.getDialectType()).thenReturn(DialectType.ORACLE);
 Mockito.when(authenticationFacade.currentUser()).thenReturn(Mockito.mock(Object.class));
 Mockito.when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.ORACLE, Integer.class))
 .thenReturn(Optional.of(3));
 when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.ORACLE, String.class))
 .thenReturn(Optional.of(List.of("SELECT")));

 assertFalse(interceptor.doPreHandle(request, response, session, null));

 Mockito.verify(session).getRuleSetId();
 Mockito.verify(session).getDialectType();
 Mockito.verify(authenticationFacade, Mockito.times(1)).currentUser();
 }

 @Test
 public void testDoPreHandle_SqlConsoleIntercepted_RuleSetExists_ForbiddenSqlType() throws Exception {
 when(authenticationFacade.currentUser()).thenReturn(Mockito.mock(Object.class));
 when(session.getRuleSetId()).thenReturn(1L);
 when(session.getDialectType()).thenReturn(DialectType.ORACLE);

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSql("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255))");
 request.setQueryLimit(15);

 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 response.setSqls(List.of(new SqlTuplesWithViolation(new BasicResult(SqlType.DDL), "test_sql_id")));

 Mockito.when(session.getRuleSetId()).thenReturn(1L);
 Mockito.when(session.getDialectType()).thenReturn(DialectType.ORACLE);
 Mockito.when(authenticationFacade.currentUser()).thenReturn(Mockito.mock(Object.class));
 Mockito.when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.ORACLE, Integer.class))
 .thenReturn(Optional.of(10));
 when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.ORACLE, String.class))
 .thenReturn(Optional.of(List.of("INSERT")));

 assertFalse(interceptor.doPreHandle(request, response, session, null));

 Mockito.verify(session).getRuleSetId();
 Mockito.verify(session).getDialectType();
 Mockito.verify(authenticationFacade, Mockito.times(1)).currentUser();
 }

 @Test
 public void testDoPreHandle_SqlConsoleIntercepted_RuleSetExists_ForbiddenCreatePl() throws Exception {
 when(authenticationFacade.currentUser()).thenReturn(Mockito.mock(Object.class));
 when(session.getRuleSetId()).thenReturn(1L);
 when(session.getDialectType()).thenReturn(DialectType.ORACLE);

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSql("CREATE PROCEDURE test_procedure()");
 request.setQueryLimit(15);

 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 response.setSqls(List.of(new SqlTuplesWithViolation(new BasicResult(SqlType.DDL), "test_sql_id")));

 Mockito.when(session.getRuleSetId()).thenReturn(1L);
 Mockito.when(session.getDialectType()).thenReturn(DialectType.ORACLE);
 Mockito.when(authenticationFacade.currentUser()).thenReturn(Mockito.mock(Object.class));
 Mockito.when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.ORACLE, Integer.class))
 .thenReturn(Optional.of(10));
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);

 assertFalse(interceptor.doPreHandle(request, response, session, null));

 Mockito.verify(session).getRuleSetId();
 Mockito.verify(session).getDialectType();
 Mockito.verify(authenticationFacade, Mockito.times(1)).currentUser();
 }

}