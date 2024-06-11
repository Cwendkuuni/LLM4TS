import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.CheckResult;
import com.oceanbase.odc.service.sqlcheck.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.Model.CheckResult.CheckViolationType;
import com.oceanbase.odc.service.sqlcheck.Model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

 private SqlCheckService sqlCheckService;
 private RuleService ruleService;
 private EnvironmentService environmentService;
 private JdbcTemplate jdbcTemplate;
 private ConnectionSession session;
 private ConnectionConfig connectionConfig;
 private Environment environment;
 private static final String RULE_SET_ID = "ruleSetId";
 private static final String DATABASE_NAME = "databaseName";
 private static final List<OffsetString> SQLS = Collections.singletonList(new OffsetString("SELECT * FROM tableName;"));

 @Before
 public void setup() {
 sqlCheckService = new SqlCheckService();
 ruleService = mock(RuleService.class);
 environmentService = mock(EnvironmentService.class);
 jdbcTemplate = mock(JdbcTemplate.class);
 session = mock(ConnectionSession.class);
 connectionConfig = new ConnectionConfig();
 environment = new Environment();
 }

 @Test
 public void testCheck() {
 // Given
 when(ruleService.list(RULE_SET_ID, ArgumentMatchers.any(QueryRuleMetadataParams.class)))
 .thenReturn(createRules());
 when(environmentService.detail(ArgumentMatchers.anyLong()))
 .thenReturn(environment);
 when(session.getDialectType()).thenReturn(DialectType.MYSQL);
 when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);

 // When
 List<CheckResult> checkResults = sqlCheckService.check(session, new SqlCheckReq("scriptContent"));

 // Then
 Assert.assertEquals(1, checkResults.size());
 CheckResult checkResult = checkResults.get(0);
 // Check for basic functionality
 Assert.assertEquals("scriptContent", checkResult.getScriptContent());
 Assert.assertEquals(CheckViolationType.WARNING, checkResult.getViolations().get(0).getType());
 // Check for edge cases and exception handling
 // Add test cases for other methods and edge cases here
 }

 // Add test cases for other methods and edge cases here

 private List<Rule> createRules() {
 List<Rule> rules = new ArrayList<>();
 Rule rule1 = new Rule();
 rule1.setMetadata(new RuleMetadata("rule1", RuleType.SQL_CHECK, 0));
 Rule rule2 = new Rule();
 rule2.setMetadata(new RuleMetadata("rule2", RuleType.SQL_CHECK, 1));
 rules.add(rule1);
 rules.add(rule2);
 return rules;
 }
}