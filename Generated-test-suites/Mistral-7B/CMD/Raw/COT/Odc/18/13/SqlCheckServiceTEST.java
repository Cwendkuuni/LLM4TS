package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;

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
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata.RuleLevel;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.DefaultSqlChecker;
import com.oceanbase.odc.service.sqlcheck.service.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckUtil;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckServiceTestData;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        sqlCheckService = new SqlCheckService();
        ruleService = Mockito.mock(RuleService.class);
        environmentService = Mockito.mock(EnvironmentService.class);
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        SqlCheckService.setRuleService(ruleService);
        SqlCheckService.setEnvironmentService(environmentService);
        SqlCheckService.setJdbcTemplate(jdbcTemplate);
    }

    @Test
    public void testCheckWithValidConnectionSessionAndValidSqlCheckReq() {
        ConnectionSession session = SqlCheckServiceTestData.getConnectionSession();
        SqlCheckReq req = SqlCheckServiceTestData.getSqlCheckReq();
        List<Rule> rules = SqlCheckServiceTestData.getRules();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        List<CheckViolation> expectedCheckViolations = Arrays.asList(
                new CheckViolation(SqlCheckRuleFactory.RULE_ID_1, "error message 1"),
                new CheckViolation(SqlCheckRuleFactory.RULE_ID_2, "error message 2"));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(jdbcTemplate.query(anyString(), any(Map.class), any(ResultSetHandler.class))).thenAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            String sql = (String) args[0];
            Map<String, Object> parameters = (Map<String, Object>) args[1];
            ResultSetHandler resultSetHandler = (ResultSetHandler) args[2];
            return resultSetHandler.handleResults(jdbcTemplate.query(sql, parameters));
        });
        List<CheckViolation> checkViolations = sqlCheckService.check(session, req);
        assertEquals(expectedCheckViolations.size(), checkViolations.size());
        for (int i = 0; i < expectedCheckViolations.size(); i++) {
            CheckViolation expectedCheckViolation = expectedCheckViolations.get(i);
            CheckViolation checkViolation = checkViolations.get(i);
            assertEquals(expectedCheckViolation.getType().getId(), checkViolation.getType().getId());
            assertEquals(expectedCheckViolation.getMessage(), checkViolation.getMessage());
        }
        Mockito.verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        Mockito.verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        Mockito.verify(jdbcTemplate, times(req.getScriptContent().size())).query(anyString(), any(Map.class),
                any(ResultSetHandler.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullConnectionSession() {
        sqlCheckService.check(null, new SqlCheckReq(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullSqlCheckReq() {
        ConnectionSession session = SqlCheckServiceTestData.getConnectionSession();
        sqlCheckService.check(session, null);
    }

    @Test
    public void testCheckWithEmptyRulesList() {
        ConnectionSession session = SqlCheckServiceTestData.getConnectionSession();
        SqlCheckReq req = new SqlCheckReq("");
        List<Rule> rules = Collections.emptyList();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<CheckViolation> checkViolations = sqlCheckService.check(session, req);
        assertEquals(0, checkViolations.size());
        Mockito.verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheckWithEmptySqlCheckRulesList() {
        ConnectionSession session = SqlCheckServiceTestData.getConnectionSession();
        SqlCheckReq req = new SqlCheckReq("");
        List<Rule> rules = Arrays.asList(new Rule());
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertEquals(0, sqlCheckRules.size());
        Mockito.verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithEmptyScriptContent() {
        ConnectionSession session = SqlCheckServiceTestData.getConnectionSession();
        SqlCheckReq req = new SqlCheckReq("");
        List<Rule> rules = Arrays.asList(new Rule());
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        List<CheckViolation> checkViolations = sqlCheckService.check(session, req);
        assertEquals(0, checkViolations.size());
        Mockito.verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        Mockito.verify(jdbcTemplate, never()).query(anyString(), any(Map.class), any(ResultSetHandler.class));
    }

    @Test
    public void testCheckWithEnvironmentIdAndValidDatabaseNameListAndValidScriptContentListAndValidConnectionConfig() {
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM users WHERE id = 1"));
        ConnectionConfig config = new ConnectionConfig();
        List<Rule> rules = Arrays.asList(new Rule());
        Environment env = new Environment();
        when(environmentService.detail(environmentId)).thenReturn(env);
        List<SqlCheckRule> checkRules = sqlCheckService.getRules(rules, config.getDialectType(), jdbcTemplate);
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(config.getDialectType(), null, checkRules);
        List<CheckViolation> expectedCheckViolations = Arrays.asList(
                new CheckViolation(SqlCheckRuleFactory.RULE_ID_1, "error message 1"),
                new CheckViolation(SqlCheckRuleFactory.RULE_ID_2, "error message 2"));
        when(jdbcTemplate.query(anyString(), any(Map.class), any(ResultSetHandler.class))).thenAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            String sql = (String) args[0];
            Map<String, Object> parameters = (Map<String, Object>) args[1];
            ResultSetHandler resultSetHandler = (ResultSetHandler) args[2];
            return resultSetHandler.handleResults(sqlChecker.check(Collections.singletonList(sql), new SqlCheckContext(1)));
        });
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(expectedCheckViolations.size(), checkViolations.size());
        for (int i = 0; i < expectedCheckViolations.size(); i++) {
            CheckViolation expectedCheckViolation = expectedCheckViolations.get(i);
            CheckViolation checkViolation = checkViolations.get(i);
            assertEquals(expectedCheckViolation.getType().getId(), checkViolation.getType().getId());
            assertEquals(expectedCheckViolation.getMessage(), checkViolation.getMessage());
        }
        Mockito.verify(environmentService, times(1)).detail(environmentId);
        Mockito.verify(ruleService, times(1)).list(eq(env.getRulesetId()), any(QueryRuleMetadataParams.class));
        Mockito.verify(jdbcTemplate, times(sqls.size())).query(anyString(), any(Map.class), any(ResultSetHandler.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullEnvironmentId() {
        String databaseName = "testdb";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM users WHERE id = 1"));
        ConnectionConfig config = new ConnectionConfig();
        sqlCheckService.check(null, databaseName, sqls, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullDatabaseName() {
        Long environmentId = 1L;
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM users WHERE id = 1"));
        ConnectionConfig config = new ConnectionConfig();
        sqlCheckService.check(environmentId, null, sqls, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithEmptyDatabaseNameList() {
        Long environmentId = 1L;
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM users WHERE id = 1"));
        ConnectionConfig config = new ConnectionConfig();
        sqlCheckService.check(environmentId, "", sqls, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithEmptyScriptContentList() {
        Long environmentId = 1L;
        String databaseName = "testdb";
        ConnectionConfig config = new ConnectionConfig();
        sqlCheckService.check(environmentId, databaseName, Collections.emptyList(), config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullConnectionConfig() {
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM users WHERE id = 1"));
        sqlCheckService.check(environmentId, databaseName, sqls, null);
    }

    @Test
    public void testFullFillRiskLevelWithValidCheckViolationsAndValidRulesList() {
        List<Rule> rules = Arrays.asList(new Rule());
        List<CheckViolation> violations = Arrays.asList(
                new CheckViolation(SqlCheckRuleFactory.RULE_ID_1, "error message 1"),
                new CheckViolation(SqlCheckRuleFactory.RULE_ID_2, "error message 2"));
        Rule rule = new Rule();
        rule.setLevel(RuleLevel.LOW);
        rule.setViolation(new RuleViolation(RuleViolation.Type.SQL_INJECTION, "test"));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(expectedViolatedRulesSize(), violatedRules.size());
        for (int i = 0; i < violations.size(); i++) {
            CheckViolation violation = violations.get(i);
            RuleViolation violationType = violation.getType();
            RuleViolation expectedViolationType = new RuleViolation(RuleViolation.Type.SQL_INJECTION, "test");
            Rule expectedRule = new Rule();
            expectedRule.setLevel(RuleLevel.LOW);
            expectedRule.setViolation(expectedViolationType);
            assertEquals(expectedRule.getLevel(), violatedRules.get(i).getLevel());
            assertEquals(expectedRule.getViolation(), violatedRules.get(i).getViolation());
        }
        Mockito.verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testFullFillRiskLevelWithEmptyCheckViolationsList() {
        List<Rule> rules = Arrays.asList(new Rule());
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, Collections.emptyList());
        assertEquals(0, violatedRules.size());
    }

    private static int expectedViolatedRulesSize() {
        return 2;
    }
}