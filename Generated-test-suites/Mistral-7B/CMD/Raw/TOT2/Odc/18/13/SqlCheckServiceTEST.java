import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

 @Mock
 private RuleService ruleService;
 @Mock
 private EnvironmentService environmentService;
 @Mock
 private JdbcOperations jdbcOperations;
 @Mock
 private ConnectionSession connectionSession;
 @Mock
 private SingleConnectionDataSource singleConnectionDataSource;
 @Mock
 private OBConsoleDataSourceFactory factory;
 @Mock
 private ConnectionConfig connectionConfig;

 @InjectMocks
 private SqlCheckService sqlCheckService;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testCheck_ValidConnectionSession_ValidSqlCheckReq_ReturnsCheckResults() {
 // Given
 List<Rule> rules = new ArrayList<>();
 Rule rule = new Rule();
 rule.setMetadata(new RuleMetadata());
 rule.setMetadata().setName("Rule1");
 rule.setEnabled(true);
 rule.setLevel(1);
 rules.add(rule);

 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

 String scriptContent = "SELECT * FROM Employees WHERE Salary > 5000";
 SqlCheckReq req = new SqlCheckReq();
 req.setDelimiter(";");
 req.setScriptContent(scriptContent);

 when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
 when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);

 when(environmentService.detail(anyLong())).thenReturn(new Environment());

 when(factory.getDataSource()).thenReturn(singleConnectionDataSource);
 when(singleConnectionDataSource.getJdbcOperations()).thenReturn(jdbcOperations);

 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

 List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

 DefaultSqlChecker sqlChecker = new DefaultSqlChecker(connectionSession.getDialectType(), req.getDelimiter(), sqlCheckRules);
 List<CheckViolation> checkViolations = sqlChecker.check(Collections.singletonList(req.getScriptContent()));
 List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

 List<CheckResult> expectedCheckResults = new ArrayList<>();
 expectedCheckResults.add(new CheckResult("Rule1", 1, Collections.singletonList(checkViolations.get(0))));

 // When
 List<CheckResult> actualCheckResults = sqlCheckService.check(connectionSession, req);

 // Then
 Assertions.assertEquals(expectedCheckResults, actualCheckResults);
 }

 @Test
 public void testCheck_EmptyRules_ReturnsEmptyCheckResults() {
 // Given
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

 String scriptContent = "SELECT * FROM Employees WHERE Salary > 5000";
 SqlCheckReq req = new SqlCheckReq();
 req.setDelimiter(";");
 req.setScriptContent(scriptContent);

 when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
 when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);

 // When
 List<CheckResult> actualCheckResults = sqlCheckService.check(connectionSession, req);

 // Then
 Assertions.assertEquals(Collections.emptyList(), actualCheckResults);
 }

 @Test
 public void testCheck_InvalidConnectionSession_ReturnsEmptyCheckResults() {
 // Given
 when(connectionSession.getDialectType()).thenThrow(new RuntimeException("Invalid connection session"));

 String scriptContent = "SELECT * FROM Employees WHERE Salary > 5000";
 SqlCheckReq req = new SqlCheckReq();
 req.setDelimiter(";");
 req.setScriptContent(scriptContent);

 // When
 List<CheckResult> actualCheckResults = sqlCheckService.check(connectionSession, req);
 // Then
 Assertions.assertEquals(Collections.emptyList(), actualCheckResults);
 }

 @Test
 public void testCheck_EnvironmentNotFound_ReturnsEmptyCheckResults() {
 // Given
 Long environmentId = 1L;
 when(environmentService.detail(environmentId)).thenThrow(new RuntimeException("Environment not found"));

 String databaseName = "EmployeesDB";
 List<OffsetString> sqls = new ArrayList<>();
 sqls.add(new OffsetString("SELECT * FROM Employees WHERE Salary > 5000"));
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.MYSQL);
 // When
 List<CheckResult> actualCheckResults = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);
 // Then
 Assertions.assertEquals(Collections.emptyList(), actualCheckResults);
 }

 @Test
 public void testCheck_InvalidJdbcOperations_ReturnsEmptyCheckResults() {
 // Given
 Long environmentId = 1L;
 String databaseName = "EmployeesDB";
 List<OffsetString> sqls = new ArrayList<>();
 sqls.add(new OffsetString("SELECT * FROM Employees WHERE Salary > 5000"));
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.MYSQL);

 when(environmentService.detail(environmentId)).thenReturn(new Environment());
 when(factory.getDataSource()).thenReturn(singleConnectionDataSource);
 when(singleConnectionDataSource.getJdbcOperations()).thenThrow(new RuntimeException("Invalid JDBC Operations"));

 // When
 List<CheckResult> actualCheckResults = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);
 // Then
 Assertions.assertEquals(Collections.emptyList(), actualCheckResults);
 }

 @Test
 public void testCheck_EmptySqls_ReturnsEmptyCheckResults() {
 // Given
 Long environmentId = 1L;
 String databaseName = "EmployeesDB";
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.MYSQL);
 List<OffsetString> sqls = Collections.emptyList();

 when(environmentService.detail(environmentId)).thenReturn(new Environment());
 when(factory.getDataSource()).thenReturn(singleConnectionDataSource);

 // When
 List<CheckResult> actualCheckResults = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);
 // Then
 Assertions.assertEquals(Collections.emptyList(), actualCheckResults);
 }

 @Test
 public void testFullFillRiskLevel_EmptyCheckViolations_ReturnsEmptyRules() {
 // Given
 List<Rule> rules = new ArrayList<>();
 List<CheckViolation> checkViolations = Collections.emptyList();

 // When
 List<Rule> actualRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

 // Then
 Assertions.assertEquals(Collections.emptyList(), actualRules);
 }

 @Test
 public void testFullFillRiskLevel_ValidCheckViolations_ReturnsRulesWithRiskLevel() {
 // Given
 List<Rule> rules = new ArrayList<>();
 Rule rule = new Rule();
 rule.setMetadata(new RuleMetadata());
 rule.setMetadata().setName("Rule1");
 rule.setEnabled(true);
 rule.setLevel(1);
 rules.add(rule);

 CheckViolation checkViolation = new CheckViolation();
 checkViolation.setType(RuleType.SQL_INJECTION);
 checkViolation.setMessage("SQL injection detected");
 checkViolation.setViolationMessage("' OR 1=1 --");
 checkViolation.setLineNumber(1);
 checkViolation.setColumnName("column_name");
 checkViolation.setStartIndex(10);
 checkViolation.setEndIndex(20);
 checkViolation.setLevel(2);

 // When
 List<Rule> actualRules = sqlCheckService.fullFillRiskLevel(rules, Collections.singletonList(checkViolation));

 // Then
 Rule expectedRule = rules.get(0);
 Assertions.assertEquals(expectedRule.getLevel(), actualRules.get(0).getLevel());
 }

 @Test
 public void testGetRules_EmptyRules_ReturnsEmptyRules() {
 // Given
 List<Rule> rules = new ArrayList<>();

 // When
 List<SqlCheckRule> actualRules = sqlCheckService.getRules(rules, connectionSession);

 // Then
 Assertions.assertEquals(Collections.emptyList(), actualRules);
 }

 @Test
 public void testGetRules_ValidRules_ReturnsRules() {
 // Given
 Rule rule1 = new Rule();
 RuleMetadata ruleMetadata1 = new RuleMetadata();
 ruleMetadata1.setName("Rule1");
 ruleMetadata1.setType(RuleType.SQL_CHECK);
 rule1.setMetadata(ruleMetadata1);
 rule1.setEnabled(true);
 List<Rule> rules = new ArrayList<>();
 rules.add(rule1);

 Rule rule2 = new Rule();
 RuleMetadata ruleMetadata2 = new RuleMetadata();
 ruleMetadata2.setName("Rule2");
 ruleMetadata2.setType(RuleType.SQL_CHECK);
 rule2.setMetadata(ruleMetadata2);
 rule2.setEnabled(false);
 rules.add(rule2);

 // When
 List<SqlCheckRule> actualRules = sqlCheckService.getRules(rules, connectionSession);

 // Then
 List<SqlCheckRule> expectedRules = new ArrayList<>();
 expectedRules.add(mock(SqlCheckRule.class));
 expectedRules.add(mock(SqlCheckRule.class));
 verify(ruleService, times(2)).list(anyLong(), any(QueryRuleMetadataParams.class));
 Assertions.assertIterableEquals(expectedRules, actualRules);
 }

 @Test
 public void testGetRules_EmptyRulesList_ReturnsEmptyRules() {
 // Given
 List<Rule> rules = Collections.emptyList();

 // When
 List<SqlCheckRule> actualRules = sqlCheckService.getRules(rules, connectionSession);

 // Then
 Assertions.assertEquals(Collections.emptyList(), actualRules);
 }

 @Test
 public void testGetRules_InvalidJdbcOperations_ReturnsEmptyRules() {
 // Given
 List<Rule> rules = new ArrayList<>();
 Rule rule = new Rule();
 rule.setMetadata(new RuleMetadata());
 rule.setMetadata().setName("Rule1");
 rule.setEnabled(true);
 rule.setLevel(1);
 rules.add(rule);

 when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenThrow(new RuntimeException("Invalid JDBC Operations"));

 // When
 List<SqlCheckRule> actualRules = sqlCheckService.getRules(rules, connectionSession);

 // Then
 Assertions.assertEquals(Collections.emptyList(), actualRules);
 }
}