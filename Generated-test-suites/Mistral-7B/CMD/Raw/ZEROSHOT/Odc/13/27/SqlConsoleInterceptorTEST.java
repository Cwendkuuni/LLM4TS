package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlConsoleInterceptorTest {

 private ConnectionSession session;
 private AuthenticationFacade authenticationFacade;
 private RuleService ruleService;
 private Rule rule;
 private SqlConsoleRuleService sqlConsoleRuleService;

 @Before
 public void init() {
 session = mock(ConnectionSession.class);
 authenticationFacade = mock(AuthenticationFacade.class);
 ruleService = mock(RuleService.class);
 sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
 when(session.getDialectType()).thenReturn(DialectType.MYSQL);
 }

 @Test
 public void testDoPreHandle_noRuleSet_true() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 when(authenticationFacade.currentUser()).thenReturn(new Object());

 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 interceptor.authenticationFacade = authenticationFacade;
 interceptor.ruleService = ruleService;
 interceptor.sqlConsoleRuleService = sqlConsoleRuleService;

 assertTrue(interceptor.doPreHandle(request, response, session, new HashMap<>()));
 }

 @Test
 public void testDoPreHandle_ruleSetExists_false() {
 Long ruleSetId = 1L;
 when(authenticationFacade.currentUser()).thenReturn(new Object());
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
 when(ruleService.getByRulesetIdAndName(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS.getName())).thenReturn(Optional.empty());

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 interceptor.authenticationFacade = authenticationFacade;
 interceptor.ruleService = ruleService;
 interceptor.sqlConsoleRuleService = sqlConsoleRuleService;

 assertFalse(interceptor.doPreHandle(request, response, session, new HashMap<>()));
 }

 @Test
 public void testDoPreHandle_ruleSetExists_true() {
 Long ruleSetId = 1L;
 when(authenticationFacade.currentUser()).thenReturn(new Object());
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
 when(ruleService.getByRulesetIdAndName(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS.getName())).thenReturn(Optional.of(rule));

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 interceptor.authenticationFacade = authenticationFacade;
 interceptor.ruleService = ruleService;
 interceptor.sqlConsoleRuleService = sqlConsoleRuleService;

 assertTrue(interceptor.doPreHandle(request, response, session, new HashMap<>()));
 }

 @Test
 @SneakyThrows
 public void testHandle_noViolation_true() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(new ArrayList<>());

 when(authenticationFacade.currentUser()).thenReturn(new Object());
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(sqlConsoleRuleService.getProperties(ArgumentMatchers.eq(1L), SqlConsoleRules.MAX_RETURN_ROWS.getName(),
 ArgumentMatchers.eq(DialectType.MYSQL), Eq.class)).thenReturn(Optional.of(10L));

 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 interceptor.authenticationFacade = authenticationFacade;
 interceptor.ruleService = ruleService;
 interceptor.sqlConsoleRuleService = sqlConsoleRuleService;

 assertTrue(interceptor.handle(request, response, session, new HashMap<>()).booleanValue());
 }

 @Test
 @SneakyThrows
 public void testHandle_maxSqlSizeViolation_false() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(new ArrayList<>());
 response.getSqls().add(new SqlTuplesWithViolation(new SqlTuple("sql1", ""), new ArrayList<>()));

 when(authenticationFacade.currentUser()).thenReturn(new Object());
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(sqlConsoleRuleService.getProperties(ArgumentMatchers.eq(1L), SqlConsoleRules.MAX_EXECUTE_SQLS.getName(),
 ArgumentMatchers.eq(DialectType.MYSQL), Eq.class)).thenReturn(Optional.of(5L));
 when(sqlConsoleRuleService.getListProperties(ArgumentMatchers.eq(1L), SqlConsoleRules.ALLOW_SQL_TYPES.getName(),
 ArgumentMatchers.eq(DialectType.MYSQL), Eq.class)).thenReturn(Optional.empty());
 when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getName())).thenReturn(Optional.of(rule));

 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 interceptor.authenticationFacade = authenticationFacade;
 interceptor.ruleService = ruleService;
 interceptor.sqlConsoleRuleService = sqlConsoleRuleService;

 assertFalse(interceptor.handle(request, response, session, new HashMap<>()).booleanValue());
 }

 @Test
 @SneakyThrows
 public void testHandle_allowSqlTypesViolation_false() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(new ArrayList<>());
 response.getSqls().add(new SqlTuplesWithViolation(new SqlTuple("sql1", ""), new ArrayList<>()));

 when(authenticationFacade.currentUser()).thenReturn(new Object());
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(sqlConsoleRuleService.getProperties(ArgumentMatchers.eq(1L), SqlConsoleRules.MAX_EXECUTE_SQLS.getName(),
 ArgumentMatchers.eq(DialectType.MYSQL), Eq.class)).thenReturn(Optional.of(10L));
 when(sqlConsoleRuleService.getListProperties(ArgumentMatchers.eq(1L), SqlConsoleRules.ALLOW_SQL_TYPES.getName(),
 ArgumentMatchers.eq(DialectType.MYSQL), Eq.class)).thenReturn(Optional.of(new String[] {"CREATE"}));
 when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.ALLOW_SQL_TYPES.getName())).thenReturn(Optional.of(rule));

 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 interceptor.authenticationFacade = authenticationFacade;
 interceptor.ruleService = ruleService;
 interceptor.sqlConsoleRuleService = sqlConsoleRuleService;

 assertFalse(interceptor.handle(request, response, session, new HashMap<>()).booleanValue());
 }

 @Test
 @SneakyThrows
 public void testHandle_noViolation_true_withParseError() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(new ArrayList<>());
 response.getSqls().add(new SqlTuplesWithViolation(new SqlTuple("sql1", ""), new ArrayList<>()));
 response.getSqls().get(0).getSqlTuple().setParseError(true);

 when(authenticationFacade.currentUser()).thenReturn(new Object());
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(sqlConsoleRuleService.getProperties(ArgumentMatchers.eq(1L), SqlConsoleRules.MAX_EXECUTE_SQLS.getName(),
 ArgumentMatchers.eq(DialectType.MYSQL), Eq.class)).thenReturn(Optional.of(10L));
 when(sqlConsoleRuleService.getListProperties(ArgumentMatchers.eq(1L), SqlConsoleRules.ALLOW_SQL_TYPES.getName(),
 ArgumentMatchers.eq(DialectType.MYSQL), Eq.class)).thenReturn(Optional.empty());
 when(ruleService.getByRulesetIdAndName(1L, SqlConsoleRules.MAX_EXECUTE_SQLS.getName())).thenReturn(Optional.of(rule));

 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 interceptor.authenticationFacade = authenticationFacade;
 interceptor.ruleService = ruleService;
 interceptor.sqlConsoleRuleService = sqlConsoleRuleService;

 assertTrue(interceptor.handle(request, response, session, new HashMap<>()).booleanValue());
 }

 @Test
 public void testGetExecuteStageName() {
 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 String result = interceptor.getExecuteStageName();
 assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE.getName(), result);
 }

 @Test
 public void testDoAfterCompletion_noViolation_true() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setStatus(SqlExecuteStatus.SUCCESS);

 SqlExecuteResult result = new SqlExecuteResult();
 result.setAllowExport(true);

 when(authenticationFacade.currentUser()).thenReturn(new Object());
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(sqlConsoleRuleService.isForbidden(ArgumentMatchings.anyString(), session)).thenReturn(false);

 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 interceptor.authenticationFacade = authenticationFacade;
 interceptor.ruleService = ruleService;
 interceptor.sqlConsoleRuleService = sqlConsoleRuleService;

 interceptor.doAfterCompletion(response, session, new HashMap<>());
 assertTrue(result.isAllowExport());
 }

 @Test
 public void testDoAfterCompletion_violation_false() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setStatus(SqlExecuteStatus.SUCCESS);

 SqlExecuteResult result = new SqlExecuteResult();
 result.setAllowExport(true);

 when(authenticationFacade.currentUser()).thenReturn(new Object());
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(sqlConsoleRuleService.isForbidden(ArgumentMatchings.anyString(), session)).thenReturn(true);

 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 interceptor.authenticationFacade = authenticationFacade;
 interceptor.ruleService = ruleService;
 interceptor.sqlConsoleRuleService = sqlConsoleRuleService;

 interceptor.doAfterCompletion(response, session, new HashMap<>());
 assertFalse(result.isAllowExport());
 }

 @Test
 public void testDetermineSqlType_validSql_validType() throws Exception {
 String sql = "SELECT * FROM emp";
 AbstractSyntaxTree ast = AbstractSyntaxTreeFactories.getDefaultAstFactory().build(sql);
 BasicResult basicResult = interceptor.determineSqlType(new SqlTuple(sql, null), DialectType.MYSQL);
 assertEquals(SqlType.SQL, basicResult.getSqlType());
 }

 @Test
 public void testDetermineSqlType_invalidSql_unknownType() throws Exception {
 String sql = "SELECT * FROM emp WHERE 1=2";
 AbstractSyntaxTree ast = AbstractSyntaxTreeFactories.getDefaultAstFactory().build(sql);
 BasicResult basicResult = interceptor.determineSqlType(new SqlTuple(sql, null), DialectType.MYSQL);
 assertEquals(SqlType.OTHERS, basicResult.getSqlType());
 }

 @Test(expected = SyntaxErrorException.class)
 public void testDetermineSqlType_sqlError_unknownType() throws Exception {
 String sql = "SELECT * FROM emp WHERE 1=2";
 AbstractSyntaxTree ast = AbstractSyntaxTreeFactories.getDefaultAstFactory().build(sql);
 ast.setParseError(true);
 BasicResult basicResult = interceptor.determineSqlType(new SqlTuple(sql, null), DialectType.MYSQL);
 }
}