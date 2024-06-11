import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.ExecuteSqlResponse;
import com.oceanbase.odc.core.sql.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.model.SqlExecuteResult;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRules;
import com.oceanbase.odc.service.session.SessionTestUtils;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest
@ActiveProfiles("test")
public class SqlConsoleInterceptorTest {

 private static final String TEST_RULE_SET_ID = "test_rule_set_id";
 private static final boolean TEST_FORBIDDEN_TO_CREATE_PL = false;
 private static final String TEST_ALLOWED_SQL_TYPE = "SELECT";
 private static final String TEST_DISALLOWED_SQL_TYPE = "CREATE PROCEDURE";
 private static final String TEST_INVALID_SQL = "INVALID_SQL";
 private static final int TEST_MAX_EXECUTE_SQLS = 1;
 private static final String TEST_MAX_EXECUTE_SQLS_RULE_NAME = "Max execute sqls rule name";

 @Autowired
 private AuthenticationFacade authenticationFacade;
 @Autowired
 private RuleService ruleService;
 @Autowired
 private SqlConsoleInterceptor sqlConsoleInterceptor;

 @BeforeEach
 void setUp() {
 ConnectionSession session = SessionTestUtils.createConnectionSession();
 Mockito.when(authenticationFacade.currentUser()).thenReturn(SessionTestUtils.createTestUser("test_user"));
 Mockito.when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
 ConnectionSessionUtil.setRuleSetId(session, TEST_RULE_SET_ID);
 }

 @Test
 void testDoPreHandle_NotIndividualTeamWithoutRuleSetId_ReturnsTrue() {
 ConnectionSession session = SessionTestUtils.createConnectionSession();
 assertTrue(sqlConsoleInterceptor.doPreHandle(new SqlAsyncExecuteReq(), new SqlAsyncExecuteResp(), session,
 Map.of()));
 }

 @Test
 void testDoPreHandle_IndividualTeam_ReturnsTrue() {
 ConnectionSession session = SessionTestUtils.createConnectionSession();
 assertTrue(sqlConsoleInterceptor.doPreHandle(new SqlAsyncExecuteReq(), new SqlAsyncExecuteResp(), session,
 Map.of(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true)));
 }

 @Test
 void testHandle_NoRuleSetId_ReturnsTrue() {
 ExecuteSqlResponse response = new ExecuteSqlResponse();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 ConnectionSession session = SessionTestUtils.createConnectionSession();
 Map<String, Object> context = new HashMap<>();

 assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
 }

 @Test
 void testHandle_NotForbiddenToCreatePl_ReturnsTrue() {
 ExecuteSqlResponse response = new ExecuteSqlResponse();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM table1;", 1), null)));
 ConnectionSession session = SessionTestUtils.createConnectionSession();
 Map<String, Object> context = new HashMap<>();

 assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
 }

 @Test
 void testHandle_ForbiddenToCreatePl_ReturnsFalse() {
 ExecuteSqlResponse response = new ExecuteSqlResponse();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple("CREATE PROCEDURE p();", 1), null)));
 ConnectionSession session = SessionTestUtils.createConnectionSession();
 Map<String, Object> context = new HashMap<>();

 assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
 }

 @Test
 void testHandle_SqlTypeAllowed_ReturnsTrue() {
 ExecuteSqlResponse response = new ExecuteSqlResponse();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM table1;", 1), null)));
 ConnectionSession session = SessionTestUtils.createConnectionSession();
 Map<String, Object> context = new HashMap<>();

 assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
 }

 @Test
 void testHandle_SqlTypeDisallowed_ReturnsFalse() {
 ExecuteSqlResponse response = new ExecuteSqlResponse();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple("CREATE PROCEDURE p();", 1), null)));
 ConnectionSession session = SessionTestUtils.createConnectionSession();
 Map<String, Object> context = new HashMap<>();

 assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
 }

 @Test
 void testHandle_SyntaxError_ReturnsTrue() {
 ExecuteSqlResponse response = new ExecuteSqlResponse();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple(TEST_INVALID_SQL, 1),
 new SyntaxErrorException("Invalid SQL"))));
 ConnectionSession session = SessionTestUtils.createConnectionSession();
 Map<String, Object> context = new HashMap<>();

 assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
 }

 @Test
 @ParameterizedTest
 @ArgumentsSource(SqlConsoleInterceptorTest.SqlTypeArgsProvider.class)
 void testHandle_AllowedSqlTypes_ReturnsTrue(String sqlType) {
 ExecuteSqlResponse response = new ExecuteSqlResponse();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM table1;", 1), null)));
 ConnectionSession session = SessionTestUtils.createConnectionSession();
 Map<String, Object> context = new HashMap<>();

 Mockito.doReturn(TEST_ALLOWED_SQL_TYPE).when(ruleService)
 .getByRulesetIdAndName(TEST_RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName());

 assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
 }

 @Test
 @ParameterizedTest
 @ArgumentsSource(SqlConsoleInterceptorTest.MaxExecuteSqlsArgsProvider.class)
 void testHandle_MaxExecuteSqlsReached_ReturnsFalse(int maxSqlSize) {
 ExecuteSqlResponse response = new ExecuteSqlResponse();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM table1;", 1), null),
 new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM table2;", 1), null)));
 ConnectionSession session = SessionTestUtils.createConnectionSession();
 Map<String, Object> context = new HashMap<>();

 Mockito.doReturn(TEST_MAX_EXECUTE_SQLS).when(sqlConsoleRuleService)
 .getProperties(TEST_RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS, any(DialectType.class), eq(Integer.class));

 assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
 }

 @Test
 @DisplayName("testHandle_MaxExecuteSqlsReachedWithMaxSqlSizeRule_ReturnsFalse")
 void testHandle_MaxExecuteSqlsReachedWithMaxSqlSizeRule_ReturnsFalse() {
 ExecuteSqlResponse response = new ExecuteSqlResponse();
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSqls(List.of(new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM table1;", 1), null),
 new SqlTuplesWithViolation(new SqlTuple("SELECT * FROM table2;", 1), null)));
 ConnectionSession session = SessionTestUtils.createConnectionSession();
 Map<String, Object> context = new HashMap<>();

 Rule rule = Mockito.mock(Rule.class);
 RuleViolation violation = new RuleViolation();
 violation.setLevel(1);
 violation.setLocalizedMessage("Maximum number of execute sqls reached");
 Mockito.when(rule.getRuleName()).thenReturn(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName());
 violation.setOffset(0);
 violation.setStart(0);
 violation.setStop(TEST_INVALID_SQL.length());
 Mockito.when(rule.getProperties()).thenReturn(Map.of(SqlConsoleRules.MAX_EXECUTE_SQLS.getPropertyName(),
 TEST_MAX_EXECUTE_SQLS));
 Mockito.when(ruleService.getByRulesetIdAndName(TEST_RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
 .thenReturn(rule);

 assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
 }

 @Test
 void testDoAfterCompletion_NoRuleSetId_AllowsExport() {
 SqlExecuteResult result = new SqlExecuteResult();
 result.setAllowExport(true);
 ConnectionSession session = SessionTestUtils.createConnectionSession();
 Map<String, Object> context = new HashMap<>();

 sqlConsoleInterceptor.doAfterCompletion(new SqlExecuteResult(), session, context);
 }

 @Test
 void testDoAfterCompletion_RuleSetId_DisallowsEditResultSet() {
 ExecuteSqlResponse response = new ExecuteSqlResponse();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 response.setResultSetMetaData(Mockito.mock(SqlExecuteResult.ResultSetMetaData.class));
 ConnectionSession session = SessionTestUtils.createConnectionSession();
 Map<String, Object> context = new HashMap<>();

 Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session))
 .thenReturn(TEST_FORBIDDEN_TO_CREATE_PL);

 sqlConsoleInterceptor.doAfterCompletion(new SqlExecuteResult(), session, context);
 assertTrue(response.getResultSetMetaData().isEditable());
 }

 @Test
 void testDoAfterCompletion_RuleSetId_AllowsExport() {
 ExecuteSqlResponse response = new ExecuteSqlResponse();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 response.setResultSetMetaData(Mockito.mock(SqlExecuteResult.ResultSetMetaData.class));
 ConnectionSession session = SessionTestUtils.createConnectionSession();
 Map<String, Object> context = new HashMap<>();

 Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session))
 .thenReturn(false);

 sqlConsoleInterceptor.doAfterCompletion(new SqlExecuteResult(), session, context);
 assertTrue(response.isAllowExport());
 }

}

class SqlConsoleInterceptorTest {
 @Autowired
 private AuthenticationFacade authenticationFacade;
 @Autowired
 private RuleService ruleService;
 @Autowired
 private SqlConsoleInterceptor sqlConsoleInterceptor;
 @Autowired
 private SqlConsoleRuleService sqlConsoleRuleService;

 @Value("${ruleSetId}")
 private String ruleSetId;

 @Value("${forbiddenToCreatePl}")
 private boolean forbiddenToCreatePl;

 @Value("${allowedSqlType}")
 private String allowedSqlType;

 @Value("${disallowedSqlType}")
 private String disallowedSqlType;

 @Value("${invalidSql}")
 private String invalidSql;

 @Value("${maxExecuteSqls}")
 private int maxExecuteSqls;

 @Value("${maxExecuteSqlsRuleName}")
 private String maxExecuteSqlsRuleName;

 @ParameterizedTest
 @ArgumentsSource(SqlConsoleInterceptorTest.SqlTypeArgsProvider.class)
 static void sqlTypeTest(String sqlType) {
 // TODO: implement test code here
 }

 @ParameterizedTest
 @ArgumentsSource(SqlConsoleInterceptorTest.MaxExecuteSqlsArgsProvider.class)
 static void maxExecuteSqlsTest(int maxExecuteSqls) {
 // TODO: implement test code here
 }

 static Object[][] sqlTypeArgsProvider() {
 return new Object[][]{
 new Object[]{SqlType.SELECT},
 new Object[]{SqlType.UPDATE},
 new Object[]{SqlType.DELETE},
 new Object[]{SqlType.INSERT},
 new Object[]{SqlType.DDL},
 new Object[]{SqlType.DCL},
 new Object[]{SqlType.DDL_OTHERS},
 new Object[]{SqlType.DCL_OTHERS},
 new Object[]{SqlType.OTHERS}
 };
 }

 static Object[][] maxExecuteSqlsArgsProvider() {
 return new Object[][]{
 new Object[]{1},
 new Object[]{2},
 new Object[]{3}
 };
 }

}