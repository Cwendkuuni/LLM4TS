package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.DelegatingConnection;
import org.apache.commons.dbcp2.DelegatingPreparedStatement;
import org.apache.commons.dbcp2.DelegatingResultSet;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.dbcp2.util.DBUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.datasource.init.DataSourceInitializerUtils;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.oceanbase.odc.core.authority.util.SkipAuthorization;
import com.oceanbase.odc.core.datasource.DataSourceBuilder;
import com.oceanbase.odc.core.datasource.DataSourceProperties;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSourceFactory;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckContext;

/**
 * Test class for {@link SqlCheckService}
 */
public class SqlCheckServiceTest {

    private static final String TEST_DATABASE_NAME = "test_database";
    private static final String TEST_SCRIPT = "SELECT 1 FROM DUAL";
    private static final String TEST_RULE = "test_rule";

    private Environment env;
    private Rule rule;
    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private ConnectionConfig connectionConfig;
    private DataSourceProperties dataSourceProperties;
    private DataSourceBuilder dataSourceBuilder;
    private SingleConnectionDataSourceFactory singleConnectionDataSourceFactory;
    private SingleConnectionDataSource ds;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private SimpleJdbcTemplate simpleJdbcTemplate;
    private PlatformTransactionManager transactionManager;

    @Autowired
    public void setSqlCheckService(SqlCheckService sqlCheckService) {
        this.sqlCheckService = sqlCheckService;
    }

    @Autowired
    public void setRuleService(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @Autowired
    public void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @Before
    public void setup() {
        this.env = new Environment();
        this.env.setName("test_env");
        this.env.setRulesetId(1L);
        this.environmentService.save(this.env);
        this.rule = new Rule();
        this.rule.setName(TEST_RULE);
        this.rule.setMetadata(new RuleMetadata());
        this.rule.getMetadata().setType(RuleType.SQL_CHECK);
        this.rule.getMetadata().setEnabled(true);
        this.rule.setLevel(1);
        this.ruleService.save(this.rule);

        this.dataSourceProperties = new DataSourceProperties();
        this.dataSourceProperties.setDriverClassName("com.mysql.jdbc.Driver");
        this.dataSourceProperties.setUrl("jdbc:mysql://localhost:3306/test_database");
        this.dataSourceProperties.setUsername("root");
        this.dataSourceProperties.setPassword("password");

        this.dataSourceBuilder = new DataSourceBuilder();
        this.dataSourceBuilder.initializeDataSource(this.dataSourceProperties);
        this.ds = (SingleConnectionDataSource) this.dataSourceBuilder.build();
        this.jdbcTemplate = new JdbcTemplate(this.ds);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.ds);
        this.simpleJdbcTemplate = new SimpleJdbcTemplate(this.ds);
        this.transactionManager = new DataSourceTransactionManager(this.ds);
        this.connectionConfig = new ConnectionConfig();
        this.connectionConfig.setDialectType(DialectType.MySQL);
        this.connectionConfig.setDataSource(this.ds);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = new ConnectionSession();
        session.setConnectionConfig(this.connectionConfig);
        CheckResult result = this.sqlCheckService.check(session, new SqlCheckReq());
        assertNotNull(result);
        assertTrue(result.getCheckResults().isEmpty());

        List<CheckViolation> checkViolations = this.createCheckViolations();
        CheckResult resultWithViolations = this.sqlCheckService.check(session, new SqlCheckReq());
        assertNotNull(resultWithViolations);
        assertFalse(resultWithViolations.getCheckResults().isEmpty());
        assertEquals(checkViolations.size(), resultWithViolations.getCheckResults().size());
        checkViolations.forEach(checkViolation -> {
            CheckViolation checkViolationResult = resultWithViolations.getCheckResults().get(0);
            assertEquals(checkViolation.getType().getName(), checkViolationResult.getType().getName());
            assertEquals(checkViolation.getMessage(), checkViolationResult.getMessage());
            assertEquals(checkViolation.getLevel(), checkViolationResult.getLevel());
        });
    }

    @Test
    public void testCheckEnvironment() {
        List<OffsetString> sqls = Arrays.asList(new OffsetString(TEST_SCRIPT, 0));
        CheckResult result = this.sqlCheckService.check(this.env.getId(), TEST_DATABASE_NAME, sqls, this.connectionConfig);
        assertNotNull(result);
        assertTrue(result.getCheckResults().isEmpty());

        List<CheckViolation> checkViolations = this.createCheckViolations();
        List<OffsetString> sqlsWithViolations = Arrays.asList(new OffsetString(TEST_SCRIPT, 0), new OffsetString(TEST_SCRIPT, 1));
        CheckResult resultWithViolations = this.sqlCheckService.check(this.env.getId(), TEST_DATABASE_NAME, sqlsWithViolations, this.connectionConfig);
        assertNotNull(resultWithViolations);
        assertFalse(resultWithViolations.getCheckResults().isEmpty());
        assertEquals(checkViolations.size(), resultWithViolations.getCheckResults().size());
        checkViolations.forEach(checkViolation -> {
            CheckViolation checkViolationResult = resultWithViolations.getCheckResults().get(0);
            assertEquals(checkViolation.getType().getName(), checkViolationResult.getType().getName());
            assertEquals(checkViolation.getMessage(), checkViolationResult.getMessage());
            assertEquals(checkViolation.getLevel(), checkViolationResult.getLevel());
        });
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = this.ruleService.list(this.env.getRulesetId(), QueryRuleMetadataParams.builder().build());
        List<SqlCheckRule> sqlCheckRules = this.sqlCheckService.getRules(rules, this.connectionConfig);
        assertNotNull(sqlCheckRules);
        assertTrue(sqlCheckRules.isEmpty());

        List<Rule> rulesWithCheckRules = Arrays.asList(this.rule);
        List<SqlCheckRule> sqlCheckRulesWithCheckRules = this.sqlCheckService.getRules(rulesWithCheckRules, this.connectionConfig.getDialectType(), this.jdbcTemplate);
        assertNotNull(sqlCheckRulesWithCheckRules);
        assertFalse(sqlCheckRulesWithCheckRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = this.createCheckViolations();
        List<Rule> rules = Arrays.asList(this.rule);
        this.sqlCheckService.fullFillRiskLevel(rules, violations);

        Map<String, Rule> name2RuleMap = new HashMap<>();
        name2RuleMap.put(this.rule.getMetadata().getName(), this.rule);
        List<CheckViolation> violationsWithFullFillRiskLevel = new ArrayList<>();
        for (CheckViolation violation : violations) {
            Rule rule = name2RuleMap.getOrDefault(violation.getType().getName(), null);
            assertNotNull(rule);
            assertEquals(rule.getLevel(), violation.getLevel());
            violation.setLevel(rule.getLevel());
            violationsWithFullFillRiskLevel.add(violation);
        }
        assertEquals(violations.size(), violationsWithFullFillRiskLevel.size());
    }

    @After
    public void tearDown() {
        if (this.env != null) {
            this.environmentService.delete(this.env.getId());
        }
    }

    private List<CheckViolation> createCheckViolations() {
        return Arrays.asList(new CheckViolation(new RuleViolation(), 1, "test_type", "test_message", 1));
    }

}