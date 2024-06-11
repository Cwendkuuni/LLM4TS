package com.oceanbase.odc.service.session.interceptor;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.mockito.annotations.Mock;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.core.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model. SqlTuplesWithViolation;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;;

@DisplayName("SqlConsoleInterceptor Test")
public class SqlConsoleInterceptorTest {

 @Mock
 private AuthenticationFacade authenticationFacade;
 @Mock
 private RuleService ruleService;
 @Mock
 private SqlConsoleRuleService sqlConsoleRuleService;

 @BeforeEach
 public void initMocks() {
 MockitoAnnotations.openMocks(this);
 }

 @Test
 public void testHandle_true() {
 // Given
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 when(authenticationFacade.currentUser()).thenReturn(new Object());
 when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenAnswer(new Answer<Rule>() {
 @Override
 public Rule answer(InvocationOnMock invocation) throws Throwable {
 return new Rule();
 }
 });
 doReturn(Collections.emptyList()).when(sqlConsoleRuleService).getListProperties(anyLong(), anyString(), any(), eq(String.class));
 doReturn(Optional.of(1L)).when(sqlConsoleRuleService).getProperties(anyLong(), eq(SqlConsoleRules.MAX_RETURN_ROWS), any(), anyClass());
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

 // When
 boolean result = new SqlConsoleInterceptor().handle(request, response, session, context);

 // Then
 verify(session, never()).setRuleSetId(anyLong());
 verify(sqlConsoleRuleService, never()).isForbidden(anyString(), eq(session));
 verify(response, never()).setQueryLimit(anyInt());
 assertTrue(result);
 }

 @Test
 public void testHandle_false() {
 // Given
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 when(authenticationFacade.currentUser()).thenReturn(new Object());
 when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenAnswer(new Answer<Rule>() {
 @Override
 public Rule answer(InvocationOnMock invocation) throws Throwable {
 return new Rule();
 }
 });
 doReturn(Arrays.asList(new Rule())).when(sqlConsoleRuleService).getListProperties(anyLong(), anyString(), any(), eq(String.class));
 doReturn(Optional.of(1L)).when(sqlConsoleRuleService).getProperties(anyLong(), eq(SqlConsoleRules.MAX_RETURN_ROWS), any(), anyClass());
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

 // When
 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setSqlId(1L);
 sqlTuple.setOriginalSql("select * from test");
 List<SqlTuple> sqlTuples = Arrays.asList(sqlTuple);
 AbstractSyntaxTree ast = Mockito.mock(AbstractSyntaxTree.class);
 BasicResult basicResult = new BasicResult("select", SqlType.SELECT);
 doReturn(basicResult).when(ast).getParseResult();
 doReturn(ast).when(sqlTuple).getAst();
 doReturn(sqlTuples).when(response).getSqls();
 doReturn(Collections.emptyList()).when(response).getViolatedRules();

 // When
 boolean result = new SqlConsoleInterceptor().handle(request, response, session, context);

 // Then
 verify(session, never()).setRuleSetId(anyLong());
 verify(sqlConsoleRuleService).isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, eq(session));
 verify(response, times(1)).setViolatedRules(anyList());
 assertFalse(result);
 }

 @Test
 public void testHandle_maxSqlSizeRuleViolated() {
 // Given
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 when(authenticationFacade.currentUser()).thenReturn(new Object());
 when(ruleService.getByRulesetIdAndName(anyLong(), anyString())).thenAnswer(new Answer<Rule>() {
 @Override
 public Rule answer(InvocationOnMock invocation) throws Throwable {
 Rule rule = new Rule();
 rule.setName(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
 rule.setProperties(Map.of("max_execute_sqls", 1L));
 return rule;
 }
 });
 doReturn(Arrays.asList(new Rule())).when(sqlConsoleRuleService).getListProperties(anyLong(), anyString(), any(), eq(String.class));
 doReturn(Optional.of(1L)).when(sqlConsoleRuleService).getProperties(anyLong(), eq(SqlConsoleRules.MAX_RETURN_ROWS), any(), anyClass());
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

 // When
 SqlTuple sqlTuple1 = new SqlTuple();
 sqlTuple1.setSqlId(1L);
 sqlTuple1.setOriginalSql("select 1 from test");
 List<SqlTuple> sqlTuples1 = Arrays.asList(sqlTuple1);
 AbstractSyntaxTree ast1 = Mockito.mock(AbstractSyntaxTree.class);
 BasicResult basicResult1 = new BasicResult("select", SqlType.SELECT);
 doReturn(basicResult1).when(ast1).getParseResult();
 doReturn(ast1).when(sqlTuple1).getAst();
 doReturn(sqlTuples1).when(response).getSqls();
 doReturn(Collections.emptyList()).when(response).getViolatedRules();

 SqlTuple sqlTuple2 = new SqlTuple();
 sqlTuple2.setSqlId(2L);
 sqlTuple2.setOriginalSql("select 1 from test union all select 1 from test");
 List<SqlTuple> sqlTuples2 = Arrays.asList(sqlTuple2);
 AbstractSyntaxTree ast2 = Mockito.mock(AbstractSyntaxTree.class);
 BasicResult basicResult2 = new BasicResult("select", SqlType.SELECT);
 doReturn(basicResult2).when(ast2).getParseResult();
 doReturn(ast2).when(sqlTuple2).getAst();
 doReturn(sqlTuples2).when(response).getSqls();
 doReturn(Collections.emptyList()).when(response).getViolatedRules();

 // When
 doAnswer(invocation -> {
 throw new RuntimeException("test exception");
 }).when(sqlConsoleRuleService).isForbidden(anyString(), eq(session));

 // When & Then
 Executable executable = () -> new SqlConsoleInterceptor().handle(request, response, session, context);
 assertThrows(RuntimeException.class, executable);
 }

 @Test
 public void testGetExecuteStageName() {
 // Given

 // When
 String result = new SqlConsoleInterceptor().getExecuteStageName();

 // Then
 assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, result);
 }

 @Test
 public void testDoAfterCompletion_true() {
 // Given
 SqlExecuteResult response = new SqlExecuteResult();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(false);
 doReturn(true).when(response).isSuccess();
 doReturn(Arrays.asList(new Rule())).when(ruleService).getByRulesetIdAndName(anyLong(), anyString());

 // When
 new SqlConsoleInterceptor().doAfterCompletion(response, session, context);

 // Then
 verify(session, never()).setRuleSetId(anyLong());
 verify(response, never()).setAllowExport(anyBoolean());
 }

 @Test
 public void testDoAfterCompletion_false() {
 // Given
 SqlExecuteResult response = new SqlExecuteResult();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).thenReturn(true);
 doReturn(true).when(response).isSuccess();
 doReturn(Collections.emptyList()).when(ruleService).getByRulesetIdAndName(anyLong(), anyString());

 // When
 new SqlConsoleInterceptor().doAfterCompletion(response, session, context);

 // Then
 verify(session, never()).setRuleSetId(anyLong());
 verify(response, times(1)).setAllowExport(anyBoolean());
 }

 @Test
 public void testDetermineSqlType() {
 // Given
 SqlTuple sqlTuple = new SqlTuple();
 when(sqlTuple.getOriginalSql()).thenReturn("select * from test");
 when(sqlTuple.getAst()).thenReturn(Mockito.mock(AbstractSyntaxTree.class));

 // When
 AbstractSyntaxTree ast = Mockito.mock(AbstractSyntaxTree.class);
 doReturn(new BasicResult("select", SqlType.SELECT)).when(ast).getParseResult();
 doReturn(ast).when(sqlTuple).getAst();

 // Then
 assertEquals(SqlType.SELECT, new SqlConsoleInterceptor().determineSqlType(sqlTuple, DialectType.MYSQL));
 }

 @Test
 public void testDetermineSqlType_syntaxError() {
 // Given
 SqlTuple sqlTuple = new SqlTuple();
 when(sqlTuple.getOriginalSql()).thenReturn("select * test from test");
 when(sqlTuple.getAst()).thenReturn(Mockito.mock(AbstractSyntaxTree.class));
 doReturn(new BasicResult(null, SqlType.UNKNOWN)).when(sqlTuple.getAst()).getParseResult();

 // Then
 assertEquals(SqlType.UNKNOWN, new SqlConsoleInterceptor().determineSqlType(sqlTuple, DialectType.MYSQL));
 }

 @Test
 public void testDetermineSqlType_otherSqlType() {
 // Given
 SqlTuple sqlTuple = new SqlTuple();
 when(sqlTuple.getOriginalSql()).thenReturn("create table test (id int primary key)");
 when(sqlTuple.getAst()).thenReturn(Mockito.mock(AbstractSyntaxTree.class));
 doReturn(new BasicResult("create table", SqlType.CREATE_TABLE)).when(sqlTuple.getAst()).getParseResult();

 // Then
 assertEquals(SqlType.CREATE_TABLE, new SqlConsoleInterceptor().determineSqlType(sqlTuple, DialectType.MYSQL));
 }
}