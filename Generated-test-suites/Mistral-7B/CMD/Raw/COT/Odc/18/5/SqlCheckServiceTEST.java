package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;
import com.oceanbase.odc.test.utils.DialectTypeMock;
import com.oceanbase.odc.test.utils.MockRuleService;
import com.oceanbase.odc.test.utils.MockRuleViolation;
import com.oceanbase.odc.test.utils.MockSqlCheckRule;
import com.oceanbase.odc.test.utils.MockSqlCheckRuleFactory;
import com.oceanbase.odc.test.utils.MockSqlCheckRuleFactoryMock;
import com.oceanbase.odc.test.utils.MockSqlCheckRuleMock;
import com.oceanbase.odc.test.utils.MockEnvironment;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    private ConnectionSession session;
    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private JdbcTemplate jdbcTemplate;
    private SingleConnectionDataSource dataSource;
    private OBConsoleDataSourceFactory factory;
    private MockSqlCheckRuleFactoryMock ruleFactoryMock;
    private MockRuleService ruleServiceMock;
    private MockEnvironment environmentMock;

    @Before
    public void setUp() {
        session = mock(ConnectionSession.class);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleServiceMock);
        sqlCheckService.setEnvironmentService(environmentService);
        ruleService = mockRuleService();
        environmentService = mockEnvironmentService();
        jdbcTemplate = mock(JdbcTemplate.class);
        dataSource = mock(SingleConnectionDataSource.class);
        factory = new OBConsoleDataSourceFactory(null, true, false);
        factory.setDataSource(dataSource);
        when(session.getDialectType()).thenReturn(DialectTypeMock.ORACLE);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        when(jdbcTemplate.queryForList(anyString(), any())).thenReturn(Collections.emptyList());
        ruleFactoryMock = mock(MockSqlCheckRuleFactoryMock.class);
        sqlCheckService.setSqlCheckRules(SqlCheckRules.getInstance());
        sqlCheckService.getSqlCheckRules().setSqlCheckRuleFactory(ruleFactoryMock);
        environmentMock = mockEnvironment();
    }

    @Test
    public void testCheck() {
        List<Rule> rules = Collections.singletonList(mockRule());
        when(ruleService.list(anyLong(), any())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertEquals(1, sqlCheckRules.size());

        String scriptContent = "SELECT * FROM users WHERE age < 18";
        List<CheckViolation> checkViolations = new ArrayList<>();
        SqlChecker sqlChecker = new DefaultSqlChecker(DialectTypeMock.ORACLE, null, sqlCheckRules);
        CheckViolation violation = new MockSqlCheckRuleMock().getViolation();
        checkViolations.add(violation);
        List<CheckResult> checkResults = sqlCheckService.check(session, new SqlCheckReq("test", scriptContent));
        assertEquals(1, checkResults.size());
        assertEquals(violation.getMessage(), checkResults.get(0).getMessage());
        assertEquals(RuleType.SQL_CHECK, checkResults.get(0).getType());
    }

    @Test
    public void testCheckWithEmptyRules() {
        when(ruleService.list(anyLong(), any())).thenReturn(Collections.emptyList());
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), session);
        assertNull(sqlCheckRules);

        List<CheckViolation> checkViolations = new ArrayList<>();
        String scriptContent = "SELECT * FROM users WHERE age < 18";
        List<CheckResult> checkResults = sqlCheckService.check(session, new SqlCheckReq("test", scriptContent));
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEnvironment() {
        List<Rule> rules = Collections.singletonList(mockRule());
        when(ruleService.list(anyLong(), any())).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(environmentMock);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectTypeMock.ORACLE, jdbcTemplate);
        assertEquals(1, sqlCheckRules.size());

        String databaseName = "test";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users WHERE age < 18"));
        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation = new MockSqlCheckRuleMock().getViolation();
        checkViolations.add(violation);

        List<CheckResult> checkResults = sqlCheckService.check(1L, databaseName, sqls, new ConnectionConfig());
        assertEquals(1, checkResults.size());
        assertEquals(violation.getMessage(), checkResults.get(0).getMessage());
        assertEquals(RuleType.SQL_CHECK, checkResults.get(0).getType());
    }

    @Test
    public void testCheckWithEmptySqls() {
        List<Rule> rules = Collections.singletonList(mockRule());
        when(ruleService.list(anyLong(), any())).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectTypeMock.ORACLE, jdbcTemplate);
        assertEquals(1, sqlCheckRules.size());

        List<CheckViolation> checkViolations = new ArrayList<>();
        List<CheckResult> checkResults = sqlCheckService.check(1L, "test", Collections.emptyList(), new ConnectionConfig());
        assertEquals(0, checkResults.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testCheckWithNullEnvironmentId() {
        List<Rule> rules = Collections.singletonList(mockRule());
        when(ruleService.list(anyLong(), any())).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectTypeMock.ORACLE, jdbcTemplate);
        assertEquals(1, sqlCheckRules.size());

        List<CheckViolation> checkViolations = new ArrayList<>();
        List<CheckResult> checkResults = sqlCheckService.check(null, "test", Collections.singletonList(new OffsetString("SELECT * FROM users WHERE age < 18")), new ConnectionConfig());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullDatabaseName() {
        List<Rule> rules = Collections.singletonList(mockRule());
        when(ruleService.list(anyLong(), any())).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectTypeMock.ORACLE, jdbcTemplate);
        assertEquals(1, sqlCheckRules.size());

        List<CheckViolation> checkViolations = new ArrayList<>();
        List<CheckResult> checkResults = sqlCheckService.check(1L, null, Collections.singletonList(new OffsetString("SELECT * FROM users WHERE age < 18")), new ConnectionConfig());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullSqls() {
        List<Rule> rules = Collections.singletonList(mockRule());
        when(ruleService.list(anyLong(), any())).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectTypeMock.ORACLE, jdbcTemplate);
        assertEquals(1, sqlCheckRules.size());

        List<CheckViolation> checkViolations = new ArrayList<>();
        List<CheckResult> checkResults = sqlCheckService.check(1L, "test", null, new ConnectionConfig());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullConnectionConfig() {
        List<Rule> rules = Collections.singletonList(mockRule());
        when(ruleService.list(anyLong(), any())).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectTypeMock.ORACLE, jdbcTemplate);
        assertEquals(1, sqlCheckRules.size());

        List<CheckViolation> checkViolations = new ArrayList<>();
        List<CheckResult> checkResults = sqlCheckService.check(1L, "test", Collections.singletonList(new OffsetString("SELECT * FROM users WHERE age < 18")), null);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = mockRule();
        RulesViolation violation1 = new MockRuleViolation().getViolation();
        rule1.getViolations().add(violation1);
        rules.add(rule1);

        Rule rule2 = mockRule();
        RulesViolation violation2 = new MockRuleViolation().getViolation();
        rule2.getViolations().add(violation2);
        rules.add(rule2);

        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(new MockSqlCheckRuleMock().getViolation());
        checkViolations.add(new MockSqlCheckRuleMock().getViolation());

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, checkViolations);
        assertEquals(2, result.size());
        assertEquals(RuleType.SQL_CHECK, result.get(0).getType());
        assertEquals(RuleType.SQL_CHECK, result.get(1).getType());
    }

    @Test
    public void testFullFillRiskLevelWithEmptyCheckViolations() {
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = mockRule();
        Rule rule2 = mockRule();
        rules.add(rule1);
        rules.add(rule2);

        List<CheckViolation> checkViolations = Collections.emptyList();

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, checkViolations);
        assertEquals(2, result.size());
        assertEquals(RuleType.SQL_CHECK, result.get(0).getType());
        assertEquals(RuleType.SQL_CHECK, result.get(1).getType());
    }

    @Test
    public void testFullFillRiskLevelWithEmptyRules() {
        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(new MockSqlCheckRuleMock().getViolation());
        checkViolations.add(new MockSqlCheckRuleMock().getViolation());

        List<Rule> result = sqlCheckService.fullFillRiskLevel(Collections.emptyList(), checkViolations);
        assertEquals(0, result.size());
    }

    private Rule mockRule() {
        Rule rule = mock(Rule.class);
        RuleMetadata metadata = mock(RuleMetadata.class);
        when(metadata.getName()).thenReturn("testRule");
        when(metadata.getType()).thenReturn(RuleType.SQL_CHECK);
        when(rule.getMetadata()).thenReturn(metadata);
        return rule;
    }

    private MockRuleService mockRuleService() {
        return new MockRuleService();
    }

    private MockEnvironment mockEnvironment() {
        return new MockEnvironment();
    }
}