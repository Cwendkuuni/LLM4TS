import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class SqlConsoleInterceptorTest {

 private SqlConsoleInterceptor sqlConsoleInterceptor;
 private ConnectionSession connectionSession;
 private AuthenticationFacade authenticationFacade;
 private RuleService ruleService;
 private SqlConsoleRuleService sqlConsoleRuleService;

 @Autowired
 public void setUpMocks(ConnectionSession connectionSession, AuthenticationFacade authenticationFacade,
 RuleService ruleService, SqlConsoleRuleService sqlConsoleRuleService) {
 this.connectionSession = connectionSession;
 this.authenticationFacade = authenticationFacade;
 this.ruleService = ruleService;
 this.sqlConsoleRuleService = sqlConsoleRuleService;
 sqlConsoleInterceptor = new SqlConsoleInterceptor();
 sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
 sqlConsoleInterceptor.setRuleService(ruleService);
 sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
 }

 @BeforeEach
 public void setupMocks() {
 connectionSession = mock(ConnectionSession.class);
 authenticationFacade = mock(AuthenticationFacade.class);
 ruleService = mock(RuleService.class);
 sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
 }

 @Test
 public void testDoPreHandle_SqlConsoleRuleNotSet_True() {
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
 when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(null);

 assertTrue(sqlConsoleInterceptor.doPreHandle(any(SqlAsyncExecuteReq.class), any(SqlAsyncExecuteResp.class),
 connectionSession, anyMap()));
 }

 @Test
 public void testDoPreHandle_SqlConsoleRuleSetNotSet_True() {
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
 when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(DialectType.class),
 Integer.class)).thenReturn(Optional.empty());

 assertTrue(sqlConsoleInterceptor.doPreHandle(any(SqlAsyncExecuteReq.class), any(SqlAsyncExecuteResp.class),
 connectionSession, anyMap()));
 }

 @Test
 public void testDoPreHandle_SqlConsoleRulesSet_LimitExceeds_QueryLimitSet() {
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
 when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(DialectType.class),
 Integer.class)).thenReturn(Optional.of(50));

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setQueryLimit(100);

 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(
 new SqlTuple("id", "sql", 1, "sql", "sqlId", "originalSql"), null, null)));

 assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, anyMap()));
 assertTrue(response.isSqlConsoleIntercepted());
 assertEquals(50, response.getSqls().get(0).getQueryLimit());
 }

 @Test
 public void testDoPreHandle_SqlConsoleRulesSet_LimitNotExceeds_QueryLimitNotSet() {
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
 when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, any(DialectType.class),
 Integer.class)).thenReturn(Optional.of(50));

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setQueryLimit(null);

 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(
 new SqlTuple("id", "sql", 1, "sql", "sqlId", "originalSql"), null, null)));

 assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, anyMap()));
 assertFalse(response.isSqlConsoleIntercepted());
 assertNull(response.getSqls().get(0).getQueryLimit());
 }

 @Test
 public void testDoPreHandle_SqlConsoleRulesSet_MaxSqlsExceeds_MaxSqlsSet() {
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
 when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, any(DialectType.class),
 Integer.class)).thenReturn(Optional.of(3));

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSqls(Arrays.asList(new SqlAsyncExecuteResp.SqlTuple("id1", "sql1"), new SqlAsyncExecuteResp.SqlTuple("id2", "sql2")));

 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(
 new SqlTuple("id1", "sql1", 1, "sql1", "sqlId1", "originalSql1"), null, null)));

 assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, anyMap()));
 assertTrue(response.isSqlConsoleIntercepted());
 assertEquals(3, response.getSqls().size());
 }

 @Test
 public void testDoPreHandle_SqlConsoleRulesSet_MaxSqlsNotExceeds_MaxSqlsNotSet() {
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
 when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS, any(DialectType.class),
 Integer.class)).thenReturn(Optional.empty());

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSqls(Arrays.asList(new SqlAsyncExecuteResp.SqlTuple("id1", "sql1"), new SqlAsyncExecuteResp.SqlTuple("id2", "sql2")));

 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(Arrays.asList(new SqlTuplesWithViolation(
 new SqlTuple("id1", "sql1", 1, "sql1", "sqlId1", "originalSql1"), null, null),
 new SqlTuplesWithViolation(new SqlTuple("id2", "sql2", 2, "sql2", "sqlId2", "originalSql2"), null, null)));

 assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, anyMap()));
 assertFalse(response.isSqlConsoleIntercepted());
 assertEquals(2, response.getSqls().size());
 }

 @Test
 public void testDoPreHandle_ForbiddenToCreatePl_Forbidden() {
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
 when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession)).thenReturn(true);

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSqls(Collections.singletonList(new SqlAsyncExecuteResp.SqlTuple("id", "CREATE PROCEDURE p1()", 1, "sql", "sqlId", "originalSql")));

 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(
 new SqlTuple("id", "CREATE PROCEDURE p1()", 1, "sql", "sqlId", "originalSql"),
 new RuleViolation(RuleLevel.ERROR, "Not Allowed to Create PL"))));

 assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, anyMap()));
 assertTrue(response.isSqlConsoleIntercepted());
 }

 @Test
 public void testDoPreHandle_AllowSqlTypes_Allowed() {
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
 when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(DialectType.class),
 String.class)).thenReturn(Optional.of(Arrays.asList("DDL")));

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSqls(Collections.singletonList(new SqlAsyncExecuteResp.SqlTuple("id", "CREATE TABLE table1 (id INT)", 1, "sql", "sqlId", "originalSql")));

 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(
 new SqlTuple("id", "CREATE TABLE table1 (id INT)", 1, "sql", "sqlId", "originalSql"), null, null)));

 assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, anyMap()));
 assertFalse(response.isSqlConsoleIntercepted());
 }

 @Test
 public void testDoPreHandle_AllowSqlTypes_NotAllowed() {
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
 when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES, any(DialectType.class),
 String.class)).thenReturn(Optional.of(Arrays.asList("DDL", "DML")));

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSqls(Collections.singletonList(new SqlAsyncExecuteResp.SqlTuple("id", "SELECT * FROM table1", 1, "sql", "sqlId", "originalSql")));

 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(Collections.singletonList(new SqlTuplesWithViolation(
 new SqlTuple("id", "SELECT * FROM table1", 1, "sql", "sqlId", "originalSql"),
 new RuleViolation(RuleLevel.ERROR, "Not Allowed to Execute SQL Type: DML"))));

 assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, anyMap()));
 assertTrue(response.isSqlConsoleIntercepted());
 }

 @Test
 public void testDoAfterCompletion_NotForbiddenToEditResultSet_True() {
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
 when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, connectionSession)).thenReturn(false);

 SqlExecuteResult response = new SqlExecuteResult();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 response.setAllowExport(true);
 response.setResultSetMetaData(new Object(){});

 sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, anyMap());
 assertTrue(response.isAllowExport());
 }

 @Test
 public void testDoAfterCompletion_ForbiddenToEditResultSet_False() {
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
 when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, connectionSession)).thenReturn(true);

 SqlExecuteResult response = new SqlExecuteResult();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 response.setAllowExport(false);
 response.setResultSetMetaData(new Object(){});

 sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, anyMap());
 assertFalse(response.isAllowExport());
 }
}