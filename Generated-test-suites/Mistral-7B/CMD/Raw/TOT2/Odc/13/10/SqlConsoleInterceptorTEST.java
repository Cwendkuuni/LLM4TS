import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Stories;
import org.mockito.quality.Strictness;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SqlConsoleInterceptorTest {

 private static final String TEST_RULE_SET_ID = "123456";
 private static final String TEST_SQL = "SELECT * FROM testTable;";
 private static final String TEST_INVALID_SQL = "SELECT * FROM nonExistentTable;";
 private static final String TEST_PL_SQL = "CREATE PROCEDURE testProc() BEGIN SELECT * FROM testTable; END;";
 private static final int TEST_MAX_EXECUTE_SQLS = 3;
 private static final int TEST_MAX_RETURN_ROWS = 10;
 private static final DialectType TEST_DIALECT_TYPE = DialectType.MYSQL;

 @Mock private ConnectionSession session;
 @Mock private AuthenticationFacade authenticationFacade;
 @Mock private RuleService ruleService;
 @Mock private SqlConsoleRuleService sqlConsoleRuleService;

 @InjectMocks private SqlConsoleInterceptor sqlConsoleInterceptor;

 @BeforeAll
 static void initMocks() {
 MockitoAnnotations.initMocks(Stories.defaultInstance());
 Mockito.lenient().when(session.getRuleSetId()).thenReturn(TEST_RULE_SET_ID);
 Mockito.when(authenticationFacade.currentUser()).thenReturn(Stories.mock(User.class));
 }

 @BeforeEach
 void setUp() {
 Mockito.when(session.getDialectType()).thenReturn(TEST_DIALECT_TYPE);
 }

 @ParameterizedTest(name = "{0} Interceptor: doPreHandle() - SQL: {1}")
 @MethodSource("getSqlTestCases")
 void testDoPreHandle_Sql(SqlAsyncExecuteReq request, boolean expectedResult) throws Exception {
 given()
 .givenSqlConsoleRuleService_getProperties_returns(TEST_MAX_EXECUTE_SQLS, TEST_MAX_RETURN_ROWS)
 .givenSqlConsoleRuleService_isForbidden_returns(false)
 .givenRuleService_getByRulesetIdAndName_returns(null)
 .givenRuleService_getByRulesetIdAndName_returns(Stories.mock(Rule.class))
 .givenRuleService_getByRulesetIdAndName_returns(null)
 .when(sqlConsoleInterceptor.doPreHandle(request, null, session, Map.of()))
 .thenReturn(expectedResult);

 then()
 .assertThat(sqlConsoleInterceptor.doPreHandle(request, null, session, Map.of()))
 .isEqualTo(expectedResult);
 }

 @DisplayName("SqlConsoleInterceptor: doPreHandle() - SQL: {0} - Max SQL limit reached")
 @Test
 void testDoPreHandle_SqlLimitReached() throws Exception {
 given()
 .givenSqlConsoleRuleService_getProperties_returns(TEST_MAX_EXECUTE_SQLS, TEST_MAX_RETURN_ROWS)
 .givenSqlConsoleRuleService_getProperties_returns(TEST_MAX_EXECUTE_SQLS)
 .givenRuleService_getByRulesetIdAndName_returns(null)
 .givenRuleService_getByRulesetIdAndName_returns(Stories.mock(Rule.class))
 .givenRuleService_getByRulesetIdAndName_returns(null)
 .givenRequest_hasMoreThanExpectedSqls()
 .when(sqlConsoleInterceptor.doPreHandle(request, null, session, Map.of()))
 .thenReturn(false);

 then()
 .assertThat(sqlConsoleInterceptor.doPreHandle(request, null, session, Map.of()))
 .isFalse();
 }

 @DisplayName("SqlConsoleInterceptor: doPreHandle() - SQL: {0} - SQL type not allowed")
 @Test
 void testDoPreHandle_SqlTypeNotAllowed() throws Exception {
 given()
 .givenSqlConsoleRuleService_getListProperties_returns(Arrays.asList("TEXT"))
 .givenRuleService_getByRulesetIdAndName_returns(Stories.mock(Rule.class))
 .givenRuleService_getByRulesetIdAndName_returns(null)
 .givenRuleService_getByRulesetIdAndName_returns(Stories.mock(Rule.class))
 .givenRequest_hasInvalidSqlType()
 .when(sqlConsoleInterceptor.doPreHandle(request, null, session, Map.of()))
 .thenReturn(false);

 then()
 .assertThat(sqlConsoleInterceptor.doPreHandle(request, null, session, Map.of()))
 .isFalse();
 }

 @DisplayName("SqlConsoleInterceptor: doPreHandle() - PL SQL: {0} - CREATE PROCEDURE not allowed")
 @Test
 void testDoPreHandle_CreateProcedureNotAllowed() throws Exception {
 given()
 .givenSqlConsoleRuleService_isForbidden_returns(true)
 .givenRuleService_getByRulesetIdAndName_returns(Stories.mock(Rule.class))
 .givenRuleService_getByRulesetIdAndName_returns(null)
 .givenRuleService_getByRulesetIdAndName_returns(null)
 .givenRequest_hasPlSql()
 .when(sqlConsoleInterceptor.doPreHandle(request, null, session, Map.of()))
 .thenReturn(false);

 then()
 .assertThat(sqlConsoleInterceptor.doPreHandle(request, null, session, Map.of()))
 .isFalse();
 }

 @DisplayName("SqlConsoleInterceptor: doAfterCompletion() - SQL: {0} - Expected result set export")
 @Test
 void testDoAfterCompletion_ExpectedResultSetExport() throws Exception, SQLException {
 given()
 .givenSqlConsoleRuleService_isForbidden_returns(false)
 .givenRuleService_getByRulesetIdAndName_returns(null)
 .givenRuleService_getByRulesetIdAndName_returns(Stories.mock(Rule.class))
 .givenRuleService_getByRulesetIdAndName_returns(null)
 .givenSqlConsoleInterceptor_getExecuteStageName_returns("SQL_CONSOLE_RULE")
 .givenSqlConsoleInterceptor_handle_returns(true)
 .givenSqlConsoleInterceptor_doAfterCompletion_returns(null)
 .givenSqlConsoleInterceptor_doAfterCompletion_returns(true)
 .givenResponse_withExpectedResultSetExport()
 .when(sqlConsoleInterceptor.doAfterCompletion(Mockito.any(SqlExecuteResult.class), Mockito.any(ConnectionSession.class),
 Mockito.anyMap()))
 .thenReturn(null);

 then()
 .assertThat(sqlConsoleInterceptor.doAfterCompletion(Mockito.any(SqlExecuteResult.class), Mockito.any(ConnectionSession.class),
 Mockito.anyMap()))
 .isNull();
 }

 @DisplayName("SqlConsoleInterceptor: doAfterCompletion() - SQL: {0} - Not allowed to export result set")
 @Test
 void testDoAfterCompletion_NotAllowedToExportResultSet() throws Exception, SQLException {
 given()
 .givenSqlConsoleRuleService_isForbidden_returns(true)
 .givenRuleService_getByRulesetIdAndName_returns(Stories.mock(Rule.class))
 .givenRuleService_getByRulesetIdAndName_returns(null)
 .givenRuleService_getByRulesetIdAndName_returns(null)
 .givenSqlConsoleInterceptor_getExecuteStageName_returns("SQL_CONSOLE_RULE")
 .givenSqlConsoleInterceptor_handle_returns(true)
 .givenSqlConsoleInterceptor_doAfterCompletion_returns(null)
 .givenSqlConsoleInterceptor_doAfterCompletion_returns(false)
 .givenResponse_withExpectedResultSet()
 .when(sqlConsoleInterceptor.doAfterCompletion(Mockito.any(SqlExecuteResult.class), Mockito.any(ConnectionSession.class),
 Mockito.anyMap()))
 .thenReturn(null);

 then()
 .assertThat(sqlConsoleInterceptor.doAfterCompletion(Mockito.any(SqlExecuteResult.class), Mockito.any(ConnectionSession.class),
 Mockito.anyMap()))
 .isNull();
 }

 @ParameterizedTest
 @DisplayName("SqlConsoleInterceptor: getExecuteStageName() - {0}")
 @MethodSource("getExecuteStageNameTestCases")
 void testGetExecuteStageName(String expected) {
 assertTrue(Objects.equals(expected, sqlConsoleInterceptor.getExecuteStageName()));
 }

 private static Stream<Arguments> getSqlTestCases() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSql(TEST_SQL);
 request.setSqlType(SqlType.SELECT);
 request.setQueryLimit(TEST_MAX_RETURN_ROWS);
 return Stream.of(Arguments.arguments(request, true));
 }

 private static Stream<Arguments> getExecuteStageNameTestCases() {
 return Stream.of(Arguments.arguments("SQL_CONSOLE_RULE"));
 }

 private void givenSqlConsoleRuleService_getProperties_returns(int numSqls, int maxRows) {
 Mockito.when(sqlConsoleRuleService.getProperties(TEST_RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS,
 TEST_DIALECT_TYPE, Integer.class))
 .thenReturn(Optional.of(numSqls));
 Mockito.when(sqlConsoleRuleService.getProperties(TEST_RULE_SET_ID, SqlConsoleRules.MAX_RETURN_ROWS,
 TEST_DIALECT_TYPE, Integer.class))
 .thenReturn(Optional.of(maxRows));
 }

 private void givenSqlConsoleRuleService_isForbidden_returns(boolean isForbidden) {
 Mockito.when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session))
 .thenReturn(isForbidden);
 }

 private void givenRuleService_getByRulesetIdAndName_returns(Rule rule) {
 Mockito.when(ruleService.getByRulesetIdAndName(TEST_RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
 .thenReturn(rule);
 }

 private void givenRuleService_getByRulesetIdAndName_returns(Rule rule1) {
 Mockito.when(ruleService.getByRulesetIdAndName(TEST_RULE_SET_ID, SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()))
 .thenReturn(rule1);
 }

 private void givenRuleService_getByRulesetIdAndName_returns(Rule rule2) {
 Mockito.when(ruleService.getByRulesetIdAndName(TEST_RULE_SET_ID, SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()))
 .thenReturn(rule2);
 }

 private void givenRequest_hasMoreThanExpectedSqls() {
 Mockito.when(request.getSqls()).thenReturn(Arrays.asList(TEST_SQL, TEST_SQL, TEST_SQL));
 }

 private void givenRequest_hasInvalidSqlType() {
 Mockito.when(request.getSqls()).thenReturn(Arrays.asList(new SqlAsyncExecuteReq(TEST_INVALID_SQL)));
 }

 private void givenRequest_hasPlSql() {
 Mockito.when(request.getSqls()).thenReturn(Arrays.asList(new SqlAsyncExecuteReq(TEST_PL_SQL)));
 }

 private void givenResponse_withExpectedResultSetExport() throws Exception {
 SqlExecuteResult response = Mockito.mock(SqlExecuteResult.class);
 response.setStatus(SqlExecuteStatus.SUCCESS);
 response.setAllowExport(true);
 }

 private void givenResponse_withExpectedResultSet() throws Exception {
 SqlExecuteResult response = Mockito.mock(SqlExecuteResult.class);
 response.setStatus(SqlExecuteStatus.SUCCESS);
 }
}