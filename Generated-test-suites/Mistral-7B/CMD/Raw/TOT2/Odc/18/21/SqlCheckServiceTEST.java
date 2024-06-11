import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import static com.oceanbase.odc.service.sqlcheck.SqlCheckServiceTest.getSampleRules;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

private ConnectionSession session;
private RuleService ruleService;
private EnvironmentService environmentService;
private JdbcTemplate jdbcTemplate;
private SqlCheckService sqlCheckService;

@Before
public void setup() {
 MockitoAnnotations.initMocks(this);
 session = Mockito.mock(ConnectionSession.class);
 ruleService = mock(RuleService.class);
 environmentService = mock(EnvironmentService.class);
 jdbcTemplate = mock(JdbcTemplate.class);
 sqlCheckService = new SqlCheckService();
 sqlCheckService.setRuleService(ruleService);
 sqlCheckService.setEnvironmentService(environmentService);
 sqlCheckService.setJdbcTemplate(jdbcTemplate);
}

@Test
public void testCheck() {
 // Given
 ConnectionConfig config = new ConnectionConfig();
 ConnectionSession session = Mockito.mock(ConnectionSession.class);
 SqlCheckReq req = new SqlCheckReq();
 req.setScriptContent("SELECT * FROM users WHERE age > 20");

 when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(getSampleRules());
 when(environmentService.detail(Mockito.anyLong())).thenReturn(new Environment());
 when(session.getDialectType()).thenReturn(DialectType.MYSQL);
 when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);

 // When
 List<CheckResult> checkResults = sqlCheckService.check(session, req);

 // Then
 List<CheckViolation> checkViolations = checkResults.get(0).getCheckViolations();
 List<Rule> rules = getSampleRules();
 List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session.getDialectType(), jdbcTemplate);
 DefaultSqlChecker sqlChecker = new DefaultSqlChecker(session.getDialectType(), req.getDelimiter(), sqlCheckRules);
 List<CheckViolation> expectedViolations = sqlChecker.check(Collections.singletonList(req.getScriptContent()));

 // Check violations matching
 for (CheckViolation actualViolation : checkViolations) {
 RuleMetadata expectedMetadata = rules.stream().filter(rule -> rule.getMetadata().getName().equals(actualViolation.getType().getName()))
 .findFirst().get().getMetadata();
 RuleViolation expectedViolation = expectedMetadata.getDefaultViolation();
 assert actualViolation.getMessage().contains(expectedViolation.getMessage());
 }

 // Check number of violations
 int expectedSize = sqlCheckRules.size();
 assertEquals(expectedSize, checkViolations.size());
}

@Test
public void testCheckWithEnvironment() {
 // Given
 Long environmentId = 1L;
 String databaseName = "test_db";
 List<OffsetString> sqls = new ArrayList<>();
 sqls.add(new OffsetString("SELECT * FROM users WHERE age > 20"));
 ConnectionConfig config = new ConnectionConfig();
 config.setEnvironmentId(environmentId);
 config.setDatabaseName(databaseName);

 // When
 List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config).stream()
 .flatMap(checkResult -> checkResult.getCheckViolations().stream())
 .collect(Collectors.toList());

 // Then
 List<Rule> rules = getSampleRules();
 List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, config.getDialectType(), jdbcTemplate);
 DefaultSqlChecker sqlChecker = new DefaultSqlChecker(config.getDialectType(), null, sqlCheckRules);
 List<CheckViolation> expectedViolations = sqls.stream().map(sql -> sqlChecker.check(Collections.singletonList(sql)).get(0))
 .collect(Collectors.toList());

 // Check violations matching
 for (CheckViolation actualViolation : checkViolations) {
 RuleMetadata expectedMetadata = rules.stream().filter(rule -> rule.getMetadata().getName().equals(actualViolation.getType().getName()))
 .findFirst().get().getMetadata();
 RuleViolation expectedViolation = expectedMetadata.getDefaultViolation();
 assert actualViolation.getMessage().contains(expectedViolation.getMessage());
 }

 // Check number of violations
 int expectedSize = sqlCheckRules.size();
 assertEquals(expectedSize, checkViolations.size());
}

private List<Rule> getSampleRules() {
 List<Rule> rules = new ArrayList<>();
 rules.add(new Rule());
 rules.add(new Rule());
 return rules;
}
}