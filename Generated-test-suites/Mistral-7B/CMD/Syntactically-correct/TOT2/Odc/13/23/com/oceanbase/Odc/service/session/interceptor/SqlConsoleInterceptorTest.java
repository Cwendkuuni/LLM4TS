package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;

import java.util.Arrays;
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
import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;

@Slf4j
public class SqlConsoleInterceptorTest {

 private static final String RULE_SET_ID = "ruleSetId";
 private static final List<String> ALLOW_SQL_TYPES = Arrays.asList("DDL", "DML");

 @Mock
 private AuthenticationFacade authenticationFacade;
 @Mock
 private RuleService ruleService;
 @Mock
 private SqlConsoleRuleService sqlConsoleRuleService;

 @InjectMocks
 private SqlConsoleInterceptor sqlConsoleInterceptor;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testDoPreHandle_SqlConsoleIntercepted_WhenSqlConsoleCheckIsNeeded() {
 // given
 ConnectionSession session = createConnectionSession();
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 given(authenticationFacade.currentUser()).willReturn(createUser());
 given(sqlConsoleRuleService.getRuleSetId(session)).willReturn(Optional.of(RULE_SET_ID));
 given(sqlConsoleRuleService.getProperties(RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL,
 Integer.class)).willReturn(Optional.of(10));
 given(sqlConsoleRuleService.getListProperties(RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL,
 String.class)).willReturn(Optional.of(ALLOW_SQL_TYPES));
 given(ruleService.getByRulesetIdAndName(RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
 .willReturn(Optional.of(createRule(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName(), "Max execute sqls", 1, 1)));
 given(ruleService.getByRulesetIdAndName(RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
 .willReturn(Optional.of(createRule(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName(), "Allow sql types", 1, 2)));

 // when
 boolean sqlConsoleIntercepted = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 // then
 assertTrue(sqlConsoleIntercepted);
 }

 @Test
 public void testDoPreHandle_NotIntercepted_WhenNoRulesetId() {
 // given
 ConnectionSession session = createConnectionSession();
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 given(authenticationFacade.currentUser()).willReturn(createUser());
 given(sqlConsoleRuleService.getRuleSetId(session)).willReturn(Optional.empty());

 // when
 boolean sqlConsoleIntercepted = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 // then
 assertTrue(sqlConsoleIntercepted);
 }

 @Test
 public void testDoPreHandle_NotIntercepted_WhenSqlConsoleCheckIsNotNeeded() {
 // given
 ConnectionSession session = createConnectionSession();
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 given(authenticationFacade.currentUser()).willReturn(createUser());
 given(sqlConsoleRuleService.getRuleSetId(session)).willReturn(Optional.of(RULE_SET_ID));
 given(sqlConsoleRuleService.getProperties(RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL,
 Integer.class)).willReturn(Optional.empty());

 // when
 boolean sqlConsoleIntercepted = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 // then
 assertTrue(sqlConsoleIntercepted);
 }

 @Test
 public void testDoPreHandle_NotIntercepted_WhenQueryLimitIsSetInRequest() {
 // given
 ConnectionSession session = createConnectionSession();
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setQueryLimit(15);
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 given(authenticationFacade.currentUser()).willReturn(createUser());
 given(sqlConsoleRuleService.getRuleSetId(session)).willReturn(Optional.of(RULE_SET_ID));
 given(sqlConsoleRuleService.getProperties(RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL,
 Integer.class)).willReturn(Optional.of(10));

 // when
 boolean sqlConsoleIntercepted = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 // then
 assertFalse(sqlConsoleIntercepted);
 }

 @Test(expected = AssertionError.class)
 public void testHandle_SqlConsoleIntercepted_WhenForbiddenToCreatePl() {
 // given
 ConnectionSession session = createConnectionSession();
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 given(authenticationFacade.currentUser()).willReturn(createUser());
 given(sqlConsoleRuleService.getRuleSetId(session)).willReturn(Optional.of(RULE_SET_ID));
 given(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).willReturn(true);
 given(sqlConsoleRuleService.getProperties(RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL,
 Integer.class)).willReturn(Optional.of(1));
 given(ruleService.getByRulesetIdAndName(RULE_SET_ID, SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName()))
 .willReturn(Optional.of(createRule(SqlConsoleRules.NOT_ALLOWED_CREATE_PL.getRuleName(), "Not allowed create pl", 1, 1)));

 List<SqlTuple> sqlTuples = Arrays.asList(new SqlTuple(new byte[1024], "CREATE PROCEDURE proc()", 1, 1));
 given(response.getSqls()).willReturn(sqlTuples);

 // when
 boolean sqlConsoleIntercepted = sqlConsoleInterceptor.handle(request, response, session, context);

 // then
 fail();
 }

 @Test
 public void testHandle_NotIntercepted_WhenNotForbiddenToCreatePl() {
 // given
 ConnectionSession session = createConnectionSession();
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 given(authenticationFacade.currentUser()).willReturn(createUser());
 given(sqlConsoleRuleService.getRuleSetId(session)).willReturn(Optional.of(RULE_SET_ID));
 given(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).willReturn(false);
 given(sqlConsoleRuleService.getProperties(RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL,
 Integer.class)).willReturn(Optional.of(1));
 given(response.getSqls()).willReturn(Arrays.asList(new SqlTuple(new byte[1024], "SELECT * FROM table", 1, 1)));

 // when
 boolean sqlConsoleIntercepted = sqlConsoleInterceptor.handle(request, response, session, context);

 // then
 assertFalse(sqlConsoleIntercepted);
 }

 @Test
 public void testHandle_NotIntercepted_WhenAllowSqlTypes() {
 // given
 ConnectionSession session = createConnectionSession();
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 given(authenticationFacade.currentUser()).willReturn(createUser());
 given(sqlConsoleRuleService.getRuleSetId(session)).willReturn(Optional.of(RULE_SET_ID));
 given(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).willReturn(false);
 given(sqlConsoleRuleService.getProperties(RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL,
 Integer.class)).willReturn(Optional.of(1));
 given(sqlConsoleRuleService.getListProperties(RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL,
 String.class)).willReturn(Optional.of(ALLOW_SQL_TYPES));
 given(ruleService.getByRulesetIdAndName(RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
 .willReturn(Optional.of(createRule(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName(), "Allow sql types", 1, 2)));
 given(response.getSqls()).willReturn(Arrays.asList(new SqlTuple(new byte[1024], "SELECT * FROM table", 1, 1),
 new SqlTuple(new byte[1024], "CREATE TABLE table2 (id INT)", 1, 1)));

 // when
 boolean sqlConsoleIntercepted = sqlConsoleInterceptor.handle(request, response, session, context);

 // then
 assertFalse(sqlConsoleIntercepted);
 }

 @Test
 public void testHandle_NotIntercepted_WhenSqlTypesAreNotAllowed() {
 // given
 ConnectionSession session = createConnectionSession();
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 given(authenticationFacade.currentUser()).willReturn(createUser());
 given(sqlConsoleRuleService.getRuleSetId(session)).willReturn(Optional.of(RULE_SET_ID));
 given(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).willReturn(false);
 given(sqlConsoleRuleService.getProperties(RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS, DialectType.MySQL,
 Integer.class)).willReturn(Optional.of(1));
 given(sqlConsoleRuleService.getListProperties(RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES, DialectType.MySQL,
 String.class)).willReturn(Optional.of(Arrays.asList("DDL")));
 given(ruleService.getByRulesetIdAndName(RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
 .willReturn(Optional.of(createRule(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName(), "Allow sql types", 1, 2)));
 given(response.getSqls()).willReturn(Arrays.asList(new SqlTuple(new byte[1024], "SELECT * FROM table", 1, 1),
 new SqlTuple(new byte[1024], "CREATE PROCEDURE proc()", 1, 1)));

 // when
 boolean sqlConsoleIntercepted = sqlConsoleInterceptor.handle(request, response, session, context);

 // then
 assertFalse(sqlConsoleIntercepted);
 }

 @Test
 public void testDoAfterCompletion_AllowExport() {
 // given
 ConnectionSession session = createConnectionSession();
 Map<String, Object> context = new HashMap<>();
 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 given(authenticationFacade.currentUser()).willReturn(createUser());
 given(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).willReturn(false);

 // when
 sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);

 // then
 assertTrue(sqlExecuteResult.isAllowExport());
 }

 @Test
 public void testDoAfterCompletion_NotAllowExport() {
 // given
 ConnectionSession session = createConnectionSession();
 Map<String, Object> context = new HashMap<>();
 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 given(authenticationFacade.currentUser()).willReturn(createUser());
 given(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session)).willReturn(true);

 // when
 sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);

 // then
 assertFalse(sqlExecuteResult.isAllowExport());
 }

 private ConnectionSession createConnectionSession() {
 return new ConnectionSession();
 }

 private User createUser() {
 return new User();
 }

 private Rule createRule(String ruleName, String ruleDescription, int ruleId, int metadataId) {
 Rule rule = new Rule();
 rule.setName(ruleName);
 rule.setDescription(ruleDescription);
 rule.setId(ruleId);
 rule.setMetadata(new Metadata());
 rule.getMetadata().addPropertyMetadata(new PropertyMetadata("ruleId", String.valueOf(metadataId), null));
 return rule;
 }

 private static class User {
 // empty
 }

 private static class Metadata {
 // empty
 }

}