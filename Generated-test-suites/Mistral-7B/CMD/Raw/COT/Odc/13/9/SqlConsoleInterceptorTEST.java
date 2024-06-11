import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class SqlConsoleInterceptorTest {

 private SqlConsoleInterceptor sqlConsoleInterceptor;
 private AuthenticationFacade authenticationFacade;
 private RuleService ruleService;
 private SqlConsoleRuleService sqlConsoleRuleService;

 @Before
 public void setUp() {
 authenticationFacade = mock(AuthenticationFacade.class);
 ruleService = mock(RuleService.class);
 sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
 sqlConsoleInterceptor = new SqlConsoleInterceptor();
 sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
 sqlConsoleInterceptor.setRuleService(ruleService);
 sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
 }

 @Test
 public void testDoPreHandle_noRuleSetId_true() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 when(authenticationFacade.currentUser().getRuleSetId()).thenReturn(null);
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

 assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
 }

 @Test
 public void testDoPreHandle_ruleSetId_true() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 Long ruleSetId = 1L;
 when(authenticationFacade.currentUser().getRuleSetId()).thenReturn(ruleSetId);
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

 when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, ArgumentMatchers.eq(DialectType.MYSQL),
 Integer.class)).thenReturn(Optional.of(10));

 SqlTuplesWithViolation sqlTuples = new SqlTuplesWithViolation();
 sqlTuples.setSqlTuple(new SqlTuple());
 sqlTuples.setViolatedRules(Collections.singletonList(createRule()));
 response.setSqls(Collections.singletonList(sqlTuples));

 assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
 }

 @Test
 public void testDoPreHandle_handleFalse_false() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

 Long ruleSetId = 1L;
 when(authenticationFacade.currentUser().getRuleSetId()).thenReturn(ruleSetId);
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

 when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, ArgumentMatchers.eq(DialectType.MYSQL),
 Integer.class)).thenReturn(Optional.of(10));

 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, ArgumentMatchers.eq(session))).thenReturn(true);

 SqlTuplesWithViolation sqlTuples = new SqlTuplesWithViolation();
 sqlTuples.setSqlTuple(new SqlTuple());
 sqlTuples.setViolatedRules(Collections.singletonList(createRule()));
 response.setSqls(Collections.singletonList(sqlTuples));

 assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
 }

 @Test
 public void testHandle_noRuleSetId_true() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 when(authenticationFacade.currentUser().getRuleSetId()).thenReturn(null);
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

 assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
 }

 @Test
 public void testHandle_ruleSetId_true() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 Long ruleSetId = 1L;
 when(authenticationFacade.currentUser().getRuleSetId()).thenReturn(ruleSetId);
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

 Optional<Integer> queryLimit = Optional.of(10);
 when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, ArgumentMatchers.eq(DialectType.MYSQL),
 Integer.class)).thenReturn(queryLimit);
 request.setQueryLimit(15);

 SqlTuplesWithViolation sqlTuples = new SqlTuplesWithViolation();
 sqlTuples.setSqlTuple(new SqlTuple());
 sqlTuples.setViolatedRules(Collections.singletonList(createRule()));
 response.setSqls(Collections.singletonList(sqlTuples));

 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setSqlId(1L);
 sqlTuple.setOriginalSql("SELECT * FROM table1");
 sqlTuple.setAst(null);
 Optional<BasicResult> basicResultOpt = Optional.of(new BasicResult(SqlType.SQL, null, null, null, null, null));
 when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, ArgumentMatchers.eq(DialectType.MYSQL),
 String.class)).thenReturn(Optional.of(new String[]{"SQL"}));
 when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS, ArgumentMatchers.eq(DialectType.MYSQL),
 Integer.class)).thenReturn(Optional.of(1));
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, ArgumentMatchers.eq(session))).thenReturn(false);
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName(), ArgumentMatchers.eq(session))).thenReturn(false);
 when(sqlConsoleInterceptor.determineSqlType(ArgumentMatchers.eq(sqlTuple), ArgumentMatchers.eq(DialectType.MYSQL)))
 .thenReturn(basicResultOpt.get());

 assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
 }

 @Test
 public void testHandle_forbiddenToCreatePl_false() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 Long ruleSetId = 1L;
 when(authenticationFacade.currentUser().getRuleSetId()).thenReturn(ruleSetId);
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

 Optional<Integer> queryLimit = Optional.of(10);
 when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, ArgumentMatchers.eq(DialectType.MYSQL),
 Integer.class)).thenReturn(queryLimit);
 request.setQueryLimit(15);

 SqlTuplesWithViolation sqlTuples = new SqlTuplesWithViolation();
 sqlTuples.setSqlTuple(new SqlTuple());
 sqlTuples.setViolatedRules(Collections.singletonList(createRule()));
 response.setSqls(Collections.singletonList(sqlTuples));

 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setSqlId(1L);
 sqlTuple.setOriginalSql("CREATE PROCEDURE procedure1()");
 sqlTuple.setAst(null);
 Optional<BasicResult> basicResultOpt = Optional.of(new BasicResult(SqlType.PLDDL, null, null, null, null, null));
 when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, ArgumentMatchers.eq(DialectType.MYSQL),
 String.class)).thenReturn(Optional.of(new String[]{"SQL"}));
 when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS, ArgumentMatchers.eq(DialectType.MYSQL),
 Integer.class)).thenReturn(Optional.of(1));
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, ArgumentMatchers.eq(session))).thenReturn(true);
 when(sqlConsoleInterceptor.determineSqlType(ArgumentMatchers.eq(sqlTuple), ArgumentMatchers.eq(DialectType.MYSQL)))
 .thenReturn(basicResultOpt.get());

 assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
 }

 @Test
 public void testHandle_allowSqlTypes_false() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 Long ruleSetId = 1L;
 when(authenticationFacade.currentUser().getRuleSetId()).thenReturn(ruleSetId);
 when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

 Optional<Integer> queryLimit = Optional.of(10);
 when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_RETURN_ROWS, ArgumentMatchers.eq(DialectType.MYSQL),
 Integer.class)).thenReturn(queryLimit);
 request.setQueryLimit(15);

 SqlTuplesWithViolation sqlTuples = new SqlTuplesWithViolation();
 sqlTuples.setSqlTuple(new SqlTuple());
 sqlTuples.setViolatedRules(Collections.singletonList(createRule()));
 response.setSqls(Collections.singletonList(sqlTuples));

 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setSqlId(1L);
 sqlTuple.setOriginalSql("SELECT * FROM table1");
 sqlTuple.setAst(null);
 Optional<BasicResult> basicResultOpt = Optional.of(new BasicResult(SqlType.SQL, null, null, null, null, null));
 when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.ALLOW_SQL_TYPES, ArgumentMatchers.eq(DialectType.MYSQL),
 String.class)).thenReturn(Optional.of(new String[]{"DDL"}));
 when(sqlConsoleRuleService.getProperties(ruleSetId, SqlConsoleRules.MAX_EXECUTE_SQLS, ArgumentMatchers.eq(DialectType.MYSQL),
 Integer.class)).thenReturn(Optional.of(1));
 when(sqlConsoleInterceptor.determineSqlType(ArgumentMatchers.eq(sqlTuple), ArgumentMatchers.eq(DialectType.MYSQL)))
 .thenReturn(basicResultOpt.get());

 assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
 }

 @Test
 public void testGetExecuteStageName() {
 assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, sqlConsoleInterceptor.getExecuteStageName());
 }

 @Test
 public void testDoAfterCompletion_success_true() {
 SqlExecuteResult response = new SqlExecuteResult();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 assertTrue(sqlConsoleInterceptor.doAfterCompletion(response, session, context));
 }

 @Test
 public void testDoAfterCompletion_failure_false() {
 SqlExecuteResult response = new SqlExecuteResult();
 response.setStatus(SqlExecuteStatus.FAILURE);
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 assertFalse(sqlConsoleInterceptor.doAfterCompletion(response, session, context));
 }

 @Test
 public void testIsIndividualTeam_true() {
 assertFalse(sqlConsoleInterceptor.isIndividualTeam());
 }

 @Test
 public void testIsIndividualTeam_false() {
 assertTrue(sqlConsoleInterceptor.isIndividualTeam());
 }

 @Test
 public void testDetermineSqlType_sqlTypeUnknown_sqlTypeOthers() {
 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setOriginalSql("SELECT * FROM table1");
 sqlTuple.setAst(null);

 BasicResult basicResult = new BasicResult(SqlType.UNKNOWN, null, null, null, null, null);
 assertEquals(SqlType.OTHERS, sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL).getSqlType());
 }

 private Rule createRule() {
 Rule rule = new Rule();
 RuleViolation violation = new RuleViolation();
 violation.setLevel(1);
 violation.setLocalizedMessage("Rule message");
 rule.setViolation(violation);
 return rule;
 }
}