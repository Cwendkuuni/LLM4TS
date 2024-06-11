package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlCheckReq;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.SqlCheckContext;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

 @Mock
 private RuleService ruleService;
 @Mock
 private EnvironmentService environmentService;
 @Mock
 private JdbcTemplate jdbcTemplate;
 @Mock
 private SingleConnectionDataSource dataSource;
 @Mock
 private ConnectionSession connectionSession;
 @Mock
 private ConnectionConfig connectionConfig;
 @Mock
 private OBConsoleDataSourceFactory factory;

 @InjectMocks
 private SqlCheckService sqlCheckService;

 private List<Rule> ruleList;
 private List<SqlCheckRule> sqlCheckRules;
 private RuleMetadata ruleMetadata;
 private List<CheckViolation> checkViolations;
 private CheckResult checkResult;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 ruleList = Arrays.asList(
 new Rule() {{
 setMetadata(ruleMetadata = new RuleMetadata() {{
 setName("Rule1");
 setEnabled(true);
 setLevel(1);
 setType(RuleType.SQL_CHECK);
 }});
 setViolation(new RuleViolation() {{
 setMessage("Message1");
 }});
 }},
 new Rule() {{
 setMetadata(ruleMetadata = new RuleMetadata() {{
 setName("Rule2");
 setEnabled(true);
 setLevel(2);
 setType(RuleType.SQL_CHECK);
 }});
 setViolation(null);
 }}
 );
 sqlCheckRules = Arrays.asList(
 new SqlCheckRule() {{
 setName("Rule1");
 setRule(Objects.requireNonNull(ruleList.get(0)));
 setSqlCheckRuleFactory(mock(SqlCheckRuleFactory.class));
 setEnabled(true);
 }},
 new SqlCheckRule() {{
 setName("Rule2");
 setRule(Objects.requireNonNull(ruleList.get(1)));
 setSqlCheckRuleFactory(mock(SqlCheckRuleFactory.class));
 setEnabled(false);
 }}
 );
 checkViolations = Arrays.asList(
 new CheckViolation() {{
 setType(RuleType.SQL_CHECK);
 setMessage("Message1");
 setLevel(1);
 }},
 new CheckViolation() {{
 setType(RuleType.SQL_CHECK);
 setMessage("Message2");
 setLevel(2);
 }}
 );
 checkResult = new CheckResult() {{
 setCheckViolations(checkViolations);
 }};
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(ruleList);
 when(environmentService.detail(anyLong())).thenReturn(new Environment());
 when(jdbcTemplate.query(anyString(), anyArray())).thenReturn(new Object[0]);
 when(dataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);
 when(factory.getDataSource()).thenReturn(dataSource);
 }

 @Test
 public void testCheckWithConnectionSession() {
 // Arrange
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = new SqlCheckReq() {{
 setScriptContent("Content");
 setDelimiter(";");
 }};
 when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
 when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);

 // Act
 List<CheckResult> results = sqlCheckService.check(connectionSession, req);

 // Assert
 assertNotNull(results);
 assertEquals(1, results.size());
 assertNotNull(results.get(0).getCheckViolations());
 assertEquals(2, results.get(0).getCheckViolations().size());
 assertEquals("Message1", results.get(0).getCheckViolations().get(0).getMessage());
 assertEquals(1, results.get(0).getCheckViolations().get(0).getLevel());
 assertEquals("Message2", results.get(0).getCheckViolations().get(1).getMessage());
 assertEquals(2, results.get(0).getCheckViolations().get(1).getLevel());
 }

 @Test
 public void testCheckWithEnvironmentId() {
 // Arrange
 List<OffsetString> sqls = Arrays.asList(new OffsetString("Content1"), new OffsetString("Content2"));
 String databaseName = "Database";
 ConnectionConfig config = new ConnectionConfig();
 config.setDialectType(DialectType.MYSQL);

 // Act
 List<CheckViolation> violations = sqlCheckService.check(1L, databaseName, sqls, config);

 // Assert
 assertNotNull(violations);
 assertEquals(2, violations.size());
 assertEquals("Message1", violations.get(0).getMessage());
 assertEquals(1, violations.get(0).getLevel());
 assertEquals("Message2", violations.get(1).getMessage());
 assertEquals(2, violations.get(1).getLevel());
 }

 @Test
 public void testGetRulesEmpty() {
 // Arrange
 List<Rule> rules = Collections.emptyList();

 // Act
 List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

 // Assert
 assertNotNull(sqlCheckRules);
 assertEquals(0, sqlCheckRules.size());
 }

 @Test
 public void testGetRules() {
 // Arrange
 String dialectType = "MySQL";
 JdbcOperations jdbc = mock(JdbcOperations.class);

 // Act
 List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(ruleList, dialectType, jdbc);

 // Assert
 assertNotNull(sqlCheckRules);
 assertEquals(2, sqlCheckRules.size());
 assertEquals("Rule1", sqlCheckRules.get(0).getName());
 assertFalse(sqlCheckRules.get(0).isEnabled());
 assertEquals("Rule2", sqlCheckRules.get(1).getName());
 assertFalse(sqlCheckRules.get(1).isEnabled());
 }

 @Test
 public void testFullFillRiskLevelEmpty() {
 // Arrange
 List<CheckViolation> violations = Collections.emptyList();

 // Act
 List<Rule> rules = sqlCheckService.fullFillRiskLevel(Collections.emptyList(), violations);

 // Assert
 assertNotNull(rules);
 assertEquals(0, rules.size());
 }

 @Test
 public void testFullFillRiskLevel() {
 // Arrange
 List<CheckViolation> violations = Arrays.asList(new CheckViolation() {{
 setType(RuleType.SQL_CHECK);
 setMessage("Message1");
 setLevel(1);
 }},
 new CheckViolation() {{
 setType(RuleType.SQL_CHECK);
 setMessage("Message2");
 setLevel(2);
 }}
 );
 Rule rule1 = new Rule() {{
 setMetadata(ruleMetadata);
 setLevel(1);
 setEnabled(true);
 setViolation(new RuleViolation() {{
 setMessage("Message3");
 }});
 }};
 Rule rule2 = new Rule() {{
 setMetadata(ruleMetadata);
 setLevel(2);
 setEnabled(true);
 setViolation(null);
 }};
 ruleList = Arrays.asList(rule1, rule2);

 // Act
 List<Rule> rules = sqlCheckService.fullFillRiskLevel(ruleList, violations);

 // Assert
 assertNotNull(rules);
 assertEquals(2, rules.size());
 assertEquals("Rule1", rules.get(0).getMetadata().getName());
 assertEquals("Message1", rules.get(0).getViolation().getMessage());
 assertEquals(1, rules.get(0).getViolation().getLevel());
 assertEquals("Rule2", rules.get(1).getMetadata().getName());
 assertNull(rules.get(1).getViolation());
 assertEquals(2, rules.get(1).getViolation().getLevel());
 }
}