package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabaseInitializer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@ContextConfiguration(classes = { SqlCheckService.class, SqlCheckRules.class })
@SpringJUnitConfig
public class SqlCheckServiceTest {

    private static final String DB_INIT_SCRIPT = "/db-schema.sql";
    private static final String DB_DATA_SCRIPT = "/db-data.sql";

    @Autowired
    private RuleService ruleService;
    @Autowired
    private EnvironmentService environmentService;
    @Autowired
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;
    private ConnectionConfig connectionConfig;
    private JdbcTemplate jdbcTemplate;
    private PlatformTransactionManager transactionManager;

    @Before
    public void setUp() throws Exception {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/testdb?createDatabaseIfNotExist=true");
        ds.setUsername("root");
        ds.setPassword("password");
        ds.setInitialSize(1);
        ds.setTestOnBorrow(false);

        DataSourceInitializer initializer = new DataSourceInitializer();
        ResourceDatabaseInitializer factory = new ResourceDatabaseInitializer();
        factory.setLocation(DB_INIT_SCRIPT);
        initializer.setDataSource(ds);
        initializer.afterPropertiesSet();

        BasicDataSource testDs = new BasicDataSource();
        testDs.setDriverClassName("com.mysql.jdbc.Driver");
        testDs.setUrl("jdbc:mysql://localhost:3306/testdb");
        testDs.setUsername("root");
        testDs.setPassword("password");
        testDs.setInitialSize(1);
        testDs.setTestOnBorrow(false);

        jdbcTemplate = new JdbcTemplate(testDs);
        transactionManager = new DataSourceTransactionManager(testDs);

        connectionConfig = new ConnectionConfig();
        connectionConfig.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("password");
        connectionConfig.setDialectType(DialectType.MYSQL);

        session = new ConnectionSession();
        session.setDataSource(new SingleConnectionDataSource(jdbcTemplate, transactionManager));
        session.setConnectionConfig(connectionConfig);
    }

    @After
    public void tearDown() throws Exception {
        session = null;
        connectionConfig = null;
        jdbcTemplate.destroyDatabase();
    }

    @Test
    @Transactional
    public void testCheck() {
        Long ruleSetId = environmentService.createRuleSet("test_rule_set", null);
        Rule rule = ruleService.createRule("test_rule", "SELECT 1;", new HashMap<>(),
                RuleType.SQL_CHECK, 1, null, null, ruleSetId);
        List<Rule> rules = Arrays.asList(rule);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        List<CheckViolation> violations = sqlCheckService.check(session, new SqlCheckReq("SELECT 1;", "test_rule_set"))
                .getCheckViolations();
        assertEquals(1, violations.size());
        assertEquals(RuleType.SQL_CHECK, violations.get(0).getType());
        assertEquals(1, violations.get(0).getRiskLevel());
        assertEquals("test_rule", violations.get(0).getRuleName());

        CheckResult result = sqlCheckService.check(session, new SqlCheckReq("SELECT 2;", "test_rule_set")).getCheckResult();
        assertNull(result.getCheckViolations());
    }

    @Test
    @Transactional
    public void testCheckWithEnvironment() throws SQLException {
        Long environmentId = environmentService.createEnvironment("test_environment", null, null);
        Environment env = environmentService.detail(environmentId);
        Long ruleSetId = env.getRulesetId();
        Rule rule = ruleService.createRule("test_rule", "SELECT 1;", new HashMap<>(),
                RuleType.SQL_CHECK, 1, null, null, ruleSetId);
        List<Rule> rules = Arrays.asList(rule);

        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/test_environment?createDatabaseIfNotExist=true");
        ds.setUsername("root");
        ds.setPassword("password");
        ds.setInitialSize(1);
        ds.setTestOnBorrow(false);

        DataSourceInitializer initializer = new DataSourceInitializer();
        ResourceDatabaseInitializer factory = new ResourceDatabaseInitializer();
        factory.setLocation(DB_INIT_SCRIPT);
        initializer.setDataSource(ds);
        initializer.afterPropertiesSet();

        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.setType(EmbeddedDatabaseType.HSQL);
        builder.setScriptEncoding("UTF-8");
        builder.addScript(DB_DATA_SCRIPT);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session.getDialectType(),
                new SimpleJdbcTemplate(new EmbeddedDatabaseBuilder.Builder()
                        .setType(EmbeddedDatabaseType.HSQL)
                        .addScript(DB_DATA_SCRIPT)
                        .build()));

        List<CheckViolation> violations = sqlCheckService.check(environmentId, "test_database",
                Arrays.asList(new OffsetString("SELECT 1;")), connectionConfig)
                .getCheckViolations();
        assertEquals(1, violations.size());
        assertEquals(RuleType.SQL_CHECK, violations.get(0).getType());
        assertEquals(1, violations.get(0).getRiskLevel());
        assertEquals("test_rule", violations.get(0).getRuleName());

        List<CheckViolation> violations2 = sqlCheckService.check(environmentId, "test_database",
                Arrays.asList(new OffsetString("SELECT 2;")), connectionConfig)
                .getCheckViolations();
        assertNull(violations2);

        ds.destroyContainer();
    }

    @Test
    @Transactional
    public void testGetRules() {
        List<Rule> rules = Arrays.asList(ruleService.createRule("test_rule1", "SELECT 1;", new HashMap<>(),
                RuleType.SQL_CHECK, 1, null, null, null),
                ruleService.createRule("test_rule2", "SELECT 2;", new HashMap<>(),
                        RuleType.SQL_CHECK, 2, null, null, null));
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
        assertEquals(2, sqlCheckRules.size());
        assertEquals(rule.getClass(), sqlCheckRules.get(0).getClass());
        assertEquals("test_rule1", sqlCheckRules.get(0).getRuleName());
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), session);
        assertNull(sqlCheckRules);
    }

    @Test
    public void testGetRulesWithEmptyRuleset() {
        Long ruleSetId = 0L;
        List<Rule> rules = Arrays.asList(ruleService.createRule("test_rule", "SELECT 1;", new HashMap<>(),
                RuleType.SQL_CHECK, 1, null, null, ruleSetId));
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithDisabledRule() {
        List<Rule> rules = Arrays.asList(ruleService.createRule("test_rule", "SELECT 1;", new HashMap<>(),
                RuleType.SQL_CHECK, 1, null, null, null),
                ruleService.createRule("test_rule2", "SELECT 2;", new HashMap<>(),
                        RuleType.SQL_CHECK, 2, null, false, null));
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
        assertEquals(1, sqlCheckRules.size());
        assertEquals("test_rule2", sqlCheckRules.get(0).getRuleName());
    }

    @Test
    public void testGetRulesWithDifferentDialect() {
        List<Rule> rules = Arrays.asList(ruleService.createRule("test_rule1", "SELECT 1;", new HashMap<>(),
                RuleType.SQL_CHECK, 1, null, null, null),
                ruleService.createRule("test_rule2", "SELECT 2;", new HashMap<>(),
                        RuleType.SQL_CHECK, 2, null, null, null));
        List<SqlCheckRule> sqlCheckRulesMySql = sqlCheckService.getRules(rules, session);
        List<SqlCheckRule> sqlCheckRulesOracle = sqlCheckService.getRules(rules, DialectType.ORACLE, jdbcTemplate);
        assertNotNull(sqlCheckRulesMySql);
        assertNotNull(sqlCheckRulesOracle);
        assertEquals(2, sqlCheckRulesMySql.size());
        assertEquals(2, sqlCheckRulesOracle.size());
        assertFalse(sqlCheckRulesMySql.get(0).getClass().isInstance(sqlCheckRulesOracle.get(0)));
    }

    @Test
    public void testGetRulesWithEmptyDataSource() {
        List<Rule> rules = Arrays.asList(ruleService.createRule("test_rule", "SELECT 1;", new HashMap<>(),
                RuleType.SQL_CHECK, 1, null, null, null));
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session, null);
        assertNull(sqlCheckRules);
    }
}