import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class SqlCheckServiceTest {

  @Test
  public void testCheckWithSession() {
    // Mocking dependencies
    ConnectionSession session = mock(ConnectionSession.class);
    SqlCheckReq req = new SqlCheckReq();
    req.setScriptContent("SELECT * FROM table");

    RuleService ruleService = mock(RuleService.class);
    EnvironmentService environmentService = mock(EnvironmentService.class);
    SqlCheckService sqlCheckService = new SqlCheckService();
    sqlCheckService.setRuleService(ruleService);
    sqlCheckService.setEnvironmentService(environmentService);

    // Setting up mock behavior
    when(session.getDialectType()).thenReturn(DialectType.MYSQL);
    when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
    when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());
    
    // Calling the method under test
    List<CheckResult> checkResults = sqlCheckService.check(session, req);

    // Validating the results
    assertEquals(Collections.emptyList(), checkResults);
  }

  @Test
  public void testCheckWithEnvironmentDetails() {
    // Mocking dependencies
    Long environmentId = 1L;
    String databaseName = "test_db";
    List<OffsetString> sqls = new ArrayList<>();
    ConnectionConfig config = new ConnectionConfig();

    RuleService ruleService = mock(RuleService.class);
    EnvironmentService environmentService = mock(EnvironmentService.class);
    SqlCheckService sqlCheckService = new SqlCheckService();
    sqlCheckService.setRuleService(ruleService);
    sqlCheckService.setEnvironmentService(environmentService);

    // Setting up mock behavior
    when(environmentService.detail(environmentId)).thenReturn(new Environment());
    when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());

    // Calling the method under test
    List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

    // Validating the results
    assertEquals(Collections.emptyList(), checkViolations);
  }

  @Test
  public void testGetRulesWithSession() {
    // Mocking dependencies
    RuleService ruleService = mock(RuleService.class);
    ConnectionSession session = mock(ConnectionSession.class);
    JdbcOperations jdbcOperations = mock(JdbcTemplate.class);

    SqlCheckService sqlCheckService = new SqlCheckService();
    sqlCheckService.setRuleService(ruleService);

    // Setting up mock behavior
    when(session.getDialectType()).thenReturn(DialectType.MYSQL);
    when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);
    when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());

    // Calling the method under test
    List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(ruleService.list(1L, QueryRuleMetadataParams.builder().build()),
        session);

    // Validating the results
    assertEquals(Collections.emptyList(), sqlCheckRules);
  }

  @Test
  public void testGetRulesWithJdbc() {
    // Mocking dependencies
    RuleService ruleService = mock(RuleService.class);
    JdbcTemplate jdbcOperations = mock(JdbcTemplate.class);

    SqlCheckService sqlCheckService = new SqlCheckService();
    sqlCheckService.setRuleService(ruleService);

    // Setting up mock behavior
    when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());

    // Calling the method under test
    List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(ruleService.list(1L, QueryRuleMetadataParams.builder().build()),
        DialectType.MYSQL, jdbcOperations);

    // Validating the results
    assertEquals(Collections.emptyList(), sqlCheckRules);
  }

  @Test
  public void testFullFillRiskLevel() {
    // Mocking dependencies
    List<CheckViolation> violations = new ArrayList<>();
    Rule rule = new Rule();
    rule.setLevel(1);
    RuleViolation ruleViolation = new RuleViolation();
    rule.setViolation(ruleViolation);
    violations.add(new CheckViolation());
    
    SqlCheckService sqlCheckService = new SqlCheckService();

    // Calling the method under test
    List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(Collections.singletonList(rule), violations);

    // Validating the results
    assertEquals(Collections.singletonList(rule), violatedRules);
  }

}