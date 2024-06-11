import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.Service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;

public class SqlCheckServiceTest {

 private static final String RULE_SET_ID = "123";
 private static final String ENV_ID = "456";
 private static final String DB_NAME = "testDB";
 private static final String CONNECTION_NAME = "testConnection";
 private static final String SCRIPT_CONTENT = "SELECT * FROM testTable;";
 private static final String SCRIPT_CONTENT_WITH_ERROR = "SELECT * FROM testTable WHERE id < 0;";
 private static final String RULE_NAME = "TestRule";
 private static final String CHECK_VIOLATION_TYPE = "TestType";
 private static final int CHECK_VIOLATION_LEVEL = 1;
 private static final String CHECK_VIOLATION_MESSAGE = "TestMessage";

 @Mock
 private SqlCheckService sqlCheckService;
 @Mock
 private RuleService ruleService;
 @Mock
 private EnvironmentService environmentService;
 @Mock
 private JdbcOperations jdbcOperations;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(sqlCheckService.ruleService).thenReturn(ruleService);
 when(sqlCheckService.environmentService).thenReturn(environmentService);
 when(ruleService.list(eq(RULE_SET_ID), any())).thenReturn(Arrays.asList(getRule()));
 when(environmentService.detail(eq(ENV_ID))).thenReturn(getEnvironment());
 when(jdbcOperations.queryForList(anyString(), any(Map.class.getClass()))).thenReturn(Collections.emptyList());
 }

 @Test
 public void testCheckWithValidConnectionSessionAndValidScriptContent() {
 ConnectionSession session = Mockito.mock(ConnectionSession.class);
 SqlCheckReq req = new SqlCheckReq();
 CheckResult result = new CheckResult();
 result.setCheckViolations(Collections.emptyList());
 when(ruleService.list(eq(RULE_SET_ID), any())).thenReturn(Arrays.asList(getRule()));
 when(environmentService.detail(eq(ENV_ID))).thenReturn(getEnvironment());
 when(sqlCheckService.getRules(anyList(), any(ConnectionSession.class))).thenReturn(getCheckRules());
 SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, getCheckRules());
 List<CheckViolation> checkViolations = sqlChecker.check(Collections.singletonList(SCRIPT_CONTENT));
 result.setCheckViolations(checkViolations);
 when(sqlCheckService.fullFillRiskLevel(anyList(), anyList())).thenReturn(Collections.emptyList());
 when(sqlCheckService.check(eq(session), eq(req))).thenReturn(result);
 ConnectionSession mockSession = Mockito.spy(session);
 Mockito.doReturn(mockSession).when(sqlCheckService).getSession();
 SqlCheckUtil mockUtil = mock(SqlCheckUtil.class);
 when(SqlCheckUtil.buildCheckResults(anyList())).thenReturn(result);

 SqlCheckReq reqTest = new SqlCheckReq();
 reqTest.setScriptContent(SCRIPT_CONTENT);

 assertEquals(Collections.emptyList(), sqlCheckService.check(session, reqTest).getCheckViolations());
 }

 @Test
 public void testCheckWithValidConnectionSessionAndScriptContentWithError() {
 ConnectionSession session = Mockito.mock(ConnectionSession.class);
 SqlCheckReq req = new SqlCheckReq();
 CheckResult result = new CheckResult();
 result.setCheckViolations(Collections.singletonList(new CheckViolation()));
 result.getCheckViolations().setType(CHECK_VIOLATION_TYPE);
 result.getCheckViolations().setLevel(CHECK_VIOLATION_LEVEL);
 result.getCheckViolations().setMessage(CHECK_VIOLATION_MESSAGE);
 when(ruleService.list(eq(RULE_SET_ID), any())).thenReturn(Arrays.asList(getRule()));
 when(environmentService.detail(eq(ENV_ID))).thenReturn(getEnvironment());
 when(sqlCheckService.getRules(anyList(), any(ConnectionSession.class))).thenReturn(getCheckRules());
 SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, getCheckRules());
 List<CheckViolation> checkViolations = sqlChecker.check(Collections.singletonList(SCRIPT_CONTENT_WITH_ERROR));
 result.setCheckViolations(checkViolations);
 when(sqlCheckService.fullFillRiskLevel(anyList(), anyList())).thenReturn(Collections.emptyList());
 when(sqlCheckService.check(eq(session), eq(req))).thenReturn(result);
 ConnectionSession mockSession = Mockito.spy(session);
 Mockito.doReturn(mockSession).when(sqlCheckService).getSession();

 SqlCheckReq reqTest = new SqlCheckReq();
 reqTest.setScriptContent(SCRIPT_CONTENT_WITH_ERROR);

 List<CheckViolation> expectedViolations = Arrays.asList(new CheckViolation());
 expectedViolations.get(0).setType(CHECK_VIOLATION_TYPE);
 expectedViolations.get(0).setLevel(CHECK_VIOLATION_LEVEL);
 expectedViolations.get(0).setMessage(CHECK_VIOLATION_MESSAGE);

 assertEquals(expectedViolations, sqlCheckService.check(session, reqTest).getCheckViolations());
 }

 @Test
 public void testCheckWithValidEnvironmentAndValidScriptContent() {
 ConnectionConfig config = new ConnectionConfig();
 config.setDialectType(DialectType.MYSQL);
 SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);
 JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
 when(dataSource.getSingleConnection()).thenReturn(jdbcOperations);
 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true, false);
 when(factory.getDataSource()).thenReturn(dataSource);
 List<SqlCheckRule> checkRules = getCheckRules();
 SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, checkRules);
 List<CheckViolation> checkViolations = new ArrayList<>();
 CheckViolation violation = new CheckViolation();
 violation.setType(CHECK_VIOLATION_TYPE);
 violation.setLevel(CHECK_VIOLATION_LEVEL);
 violation.setMessage(CHECK_VIOLATION_MESSAGE);
 checkViolations.add(violation);
 when(sqlChecker.check(anyList(), any(SqlCheckContext.class))).thenReturn(checkViolations);
 when(sqlCheckService.fullFillRiskLevel(anyList(), anyList())).thenReturn(Collections.emptyList());

 assertEquals(Collections.emptyList(), sqlCheckService.check(ENV_ID, DB_NAME, Arrays.asList(new OffsetString(SCRIPT_CONTENT)), config).getCheckViolations());
 }

 @Test
 public void testCheckWithValidEnvironmentAndScriptContentWithError() {
 ConnectionConfig config = new ConnectionConfig();
 config.setDialectType(DialectType.MYSQL);
 SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);
 JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
 when(dataSource.getSingleConnection()).thenReturn(jdbcOperations);
 OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true, false);
 when(factory.getDataSource()).thenReturn(dataSource);
 List<SqlCheckRule> checkRules = getCheckRules();
 SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, checkRules);
 List<CheckViolation> checkViolations = new ArrayList<>();
 CheckViolation violation = new CheckViolation();
 violation.setType(CHECK_VIOLATION_TYPE);
 violation.setLevel(CHECK_VIOLATION_LEVEL);
 violation.setMessage(CHECK_VIOLATION_MESSAGE);
 checkViolations.add(violation);
 when(sqlChecker.check(anyList(), any(SqlCheckContext.class))).thenReturn(checkViolations);
 when(sqlCheckService.fullFillRiskLevel(anyList(), anyList())).thenReturn(Collections.emptyList());

 List<CheckViolation> expectedViolations = Arrays.asList(new CheckViolation());
 expectedViolations.get(0).setType(CHECK_VIOLATION_TYPE);
 expectedViolations.get(0).setLevel(CHECK_VIOLATION_LEVEL);
 expectedViolations.get(0).setMessage(CHECK_VIOLATION_MESSAGE);

 assertEquals(expectedViolations, sqlCheckService.check(ENV_ID, DB_NAME, Arrays.asList(new OffsetString(SCRIPT_CONTENT_WITH_ERROR)), config).getCheckViolations());
 }

 private Rule getRule() {
 Rule rule = new Rule();
 RuleMetadata metadata = new RuleMetadata();
 metadata.setType(RuleType.SQL_CHECK);
 metadata.setName(RULE_NAME);
 rule.setMetadata(metadata);
 return rule;
 }

 private Environment getEnvironment() {
 Environment env = new Environment();
 env.setRulesetId(RULE_SET_ID);
 return env;
 }

 private List<SqlCheckRule> getCheckRules() {
 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 sqlCheckRules.add(new SqlCheckRule(getRule()));
 return sqlCheckRules;
 }
}