package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedParameterNameAnnotationProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;

@Configuration
public class SqlCheckServiceTest {

    private static final String TEST_RULE_SET_ID = "test_rule_set_id";
    private static final String TEST_ENV_ID = "test_env_id";
    private static final String TEST_DATABASE_NAME = "test_database_name";
    private static final String TEST_CONNECTION_NAME = "test_connection_name";
    private static final String TEST_SCRIPT_CONTENT = "test_script_content";
    private static final String TEST_RULE_NAME = "test_rule_name";

    private ConnectionSession session;
    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(SqlCheckServiceTest.class);
        context.scan("com.oceanbase.odc.service.sqlcheck.*");
        context.refresh();

        session = mock(ConnectionSession.class);
        sqlCheckService = context.getBean(SqlCheckService.class);
        ruleService = context.getBean(RuleService.class);
        environmentService = context.getBean(EnvironmentService.class);
        jdbcTemplate = mock(JdbcTemplate.class);

        when(session.getDialectType()).thenReturn(DialectType.MySQL);
        when(session.getRuleSetId()).thenReturn(TEST_RULE_SET_ID);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
    }

    @Test
    public void testCheckNormal() {
        List<Rule> rules = Collections.singletonList(createRule());
        when(ruleService.list(TEST_RULE_SET_ID, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MySQL, jdbcTemplate);
        assertEquals(1, sqlCheckRules.size());

        CheckResult result = sqlCheckService.check(session, new SqlCheckReq());
        assertEquals(1, result.getCheckResults().size());
        assertEquals("Success", result.getCheckResults().get(0).getStatus());
    }

    @Test
    public void testCheckEmptyScriptContent() {
        List<Rule> rules = Collections.singletonList(createRule());
        when(ruleService.list(TEST_RULE_SET_ID, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MySQL, jdbcTemplate);
        assertEquals(1, sqlCheckRules.size());

        CheckResult result = sqlCheckService.check(session, new SqlCheckReq());
        assertEquals(0, result.getCheckResults().size());
    }

    @Test
    public void testCheckEmptyRules() {
        when(ruleService.list(TEST_RULE_SET_ID, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), DialectType.MySQL, jdbcTemplate);
        assertEquals(0, sqlCheckRules.size());

        CheckResult result = sqlCheckService.check(session, new SqlCheckReq());
        assertEquals(0, result.getCheckResults().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullSession() {
        sqlCheckService.check(null, new SqlCheckReq());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullSqlCheckReq() {
        sqlCheckService.check(session, null);
    }

    @Test
    public void testCheckEmptySqls() {
        Environment env = createEnvironment();
        ConnectionConfig config = new ConnectionConfig();
        config.setName(TEST_CONNECTION_NAME);
        config.setId(TEST_CONNECTION_NAME);
        when(environmentService.detail(TEST_ENV_ID)).thenReturn(env);

        List<OffsetString> sqls = Collections.emptyList();
        CheckResult result = sqlCheckService.check(TEST_ENV_ID, TEST_DATABASE_NAME, sqls, config);
        assertEquals(0, result.getCheckResults().size());
    }

    @Test
    public void testCheckSqls() {
        Environment env = createEnvironment();
        ConnectionConfig config = new ConnectionConfig();
        config.setName(TEST_CONNECTION_NAME);
        config.setId(TEST_CONNECTION_NAME);
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("test_sql"));
        when(environmentService.detail(TEST_ENV_ID)).thenReturn(env);

        List<Rule> rules = Collections.singletonList(createRule());
        when(ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MySQL, jdbcTemplate);

        CheckResult result = sqlCheckService.check(TEST_ENV_ID, TEST_DATABASE_NAME, sqls, config);
        assertEquals(1, result.getCheckResults().size());
        assertEquals("Success", result.getCheckResults().get(0).getStatus());
    }

    @Test
    public void testCheckSqlsWithViolations() {
        Environment env = createEnvironment();
        ConnectionConfig config = new ConnectionConfig();
        config.setName(TEST_CONNECTION_NAME);
        config.setId(TEST_CONNECTION_NAME);
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("test_sql_with_violations"));
        when(environmentService.detail(TEST_ENV_ID)).thenReturn(env);

        List<Rule> rules = Collections.singletonList(createRule());
        when(ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<RuleViolation> violations = new ArrayList<>();
        RuleViolation violation = new RuleViolation("test_violation_name", 1, "test_violation_message");
        violations.add(violation);

        when(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM test_table", Integer.class)).thenReturn(1);
        when(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM test_table WHERE test_column = 'test_value'", Integer.class))
                .thenReturn(0);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MySQL, jdbcTemplate);
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MySQL, null, sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check(sqls, new SqlCheckContext(1));
        fullFillRiskLevel(rules, checkViolations);

        CheckResult result = sqlCheckService.check(TEST_ENV_ID, TEST_DATABASE_NAME, sqls, config);
        assertEquals(1, result.getCheckResults().size());
        assertEquals("Failed", result.getCheckResults().get(0).getStatus());
        assertEquals(1, result.getCheckResults().get(0).getCheckViolations().size());
        assertEquals(violation.getMessage(), result.getCheckResults().get(0).getCheckViolations().get(0).getMessage());
    }

    private Rule createRule() {
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName(TEST_RULE_NAME);
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        return rule;
    }

    private Environment createEnvironment() {
        Environment env = new Environment();
        env.setRulesetId(TEST_RULE_SET_ID);
        return env;
    }

    private void fullFillRiskLevel(List<Rule> rules, List<CheckViolation> violations) {
        List<Rule> violatedRules = new ArrayList<>();
        Map<String, Rule> name2RuleMap = rules.stream().collect(Collectors.toMap(r -> r.getMetadata().getName(), r -> r));
        violations.forEach(c -> {
            String name = "${" + c.getType().getName() + "}";
            Rule rule = name2RuleMap.getOrDefault(name, null);
            if (Objects.nonNull(rule)) {
                c.setLevel(rule.getLevel());
                Rule newRule = new Rule();
                newRule.setLevel(rule.getLevel());
                newRule.setViolation(RuleViolation.fromCheckViolation(c));
                violatedRules.add(newRule);
            }
        });
        SqlCheckUtil.fillRiskLevel(violatedRules);
    }
}