package com.oceanbase.odc.service.sqlcheck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.rule.DefaultSqlChecker;

class SqlCheckServiceTest {

 @Mock
 private EnvironmentService environmentService;
 @Mock
 private RuleService ruleService;
 @Mock
 private JdbcOperations jdbcTemplate;
 @Mock
 private ConnectionSession session;
 @Mock
 private ConnectionConfig connectionConfig;
 @Mock
 private SingleConnectionDataSource singleConnectionDataSource;
 @Mock
 private OBConsoleDataSourceFactory factory;

 @InjectMocks
 private SqlCheckService sqlCheckService;

 @Test
 void testCheck() {
 // Given
 Rule rule = new Rule();
 rule.setMetadata(new RuleMetadata("test_rule", RuleType.SQL_CHECK, null));
 when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(Collections.singletonList(rule));
 List<SqlCheckRuleFactory> sqlCheckRuleFactories = SqlCheckRules.getAllFactories(DialectType.MySQL, jdbcTemplate);
 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 sqlCheckRules.add(SqlCheckRules.createByRule(sqlCheckRuleFactories, DialectType.MySQL, rule));
 when(session.getDialectType()).thenReturn(DialectType.MySQL);
 when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
 when(factory.getDataSource()).thenReturn(singleConnectionDataSource);
 when(singleConnectionDataSource.getConnection()).thenReturn(mock(javax.sql.DataSource.class));
 when(environmentService.detail(anyLong())).thenReturn(new Environment());

 SqlCheckReq req = new SqlCheckReq();
 List<CheckResult> expectedResults = new ArrayList<>();
 CheckResult expectedResult = new CheckResult();
 expectedResult.setRuleId(rule.getId());
 expectedResult.setViolations(Collections.singletonList(new CheckViolation()));
 expectedResults.add(expectedResult);
 when(sqlChecker.check(eq(Collections.singletonList(anyString())), any())).thenReturn(Collections.singletonList(new CheckViolation()));

 // When
 List<CheckResult> results = sqlCheckService.check(session, req);

 // Then
 assertNotNull(results);
 assertEquals(1, results.size());
 assertEquals(expectedResults, results);
 }

 @Test
 void testCheckWithEnvironment() {
 // Given
 Rule rule = new Rule();
 rule.setMetadata(new RuleMetadata("test_rule", RuleType.SQL_CHECK, null));
 when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(Collections.singletonList(rule));
 List<SqlCheckRuleFactory> sqlCheckRuleFactories = SqlCheckRules.getAllFactories(DialectType.MySQL, jdbcTemplate);
 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 sqlCheckRules.add(SqlCheckRules.createByRule(sqlCheckRuleFactories, DialectType.MySQL, rule));
 when(session.getDialectType()).thenReturn(DialectType.MySQL);
 when(connectionConfig.getDialectType()).thenReturn(DialectType.MySQL);
 when(factory.getDataSource()).thenReturn(singleConnectionDataSource);
 when(singleConnectionDataSource.getConnection()).thenReturn(mock(javax.sql.DataSource.class));
 when(environmentService.detail(anyLong())).thenReturn(new Environment());

 List<OffsetString> sqls = new ArrayList<>();
 OffsetString sql = new OffsetString("SELECT * FROM test_table");
 sqls.add(sql);
 List<CheckViolation> violations = new ArrayList<>();
 CheckViolation violation = new CheckViolation();
 violation.setType(rule.getMetadata().getType());
 violation.setMessage("Test violation");
 violations.add(violation);
 when(sqlChecker.check(eq(Collections.singletonList(sql.getScriptContent())), any())).thenReturn(violations);

 // When
 List<CheckViolation> checkViolations = sqlCheckService.check(1L, "test_database", sqls, connectionConfig);

 // Then
 assertNotNull(checkViolations);
 assertEquals(1, checkViolations.size());
 assertEquals(violations, checkViolations);
 }

 @Test
 void testGetRules() {
 // Given
 Rule rule = new Rule();
 rule.setMetadata(new RuleMetadata("test_rule", RuleType.SQL_CHECK, null));
 List<Rule> rules = new ArrayList<>();
 rules.add(rule);
 when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
 when(session.getDialectType()).thenReturn(DialectType.MySQL);
 when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);

 // When
 List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(session);

 // Then
 assertNotNull(sqlCheckRules);
 assertEquals(1, sqlCheckRules.size());
 }

}