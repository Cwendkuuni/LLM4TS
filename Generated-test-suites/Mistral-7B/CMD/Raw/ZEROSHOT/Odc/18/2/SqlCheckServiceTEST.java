package com.oceanbase.odc.service.sqlcheck;

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
import org.apache.commons.dbcp2.SqlRowMapper;
import org.apache.commons.dbcp2.SimpleDriver;
import org.apache.commons.jdbc.JdbcContexts;
import org.apache.commons.jdbc.RowMapper;
import org.apache.commons.jdbc.SQLExceptionFactory;
import org.apache.commons.jdbc.SqlExceptionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.DataSourceUtils;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private ConnectionSession session;
    private ConnectionConfig config;
    private Environment env;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        session = new ConnectionSession("userId", "userName", "password", "localhost", 1111, DialectType.MYSQL);
        config = new ConnectionConfig("dsName", "dsUsername", "dsPassword", "dsJdbcUrl", "dsDriverClass", null, null,
                DialectType.MYSQL, null, null);
        env = new Environment();
        env.setRulesetId(1L);

        DataSourceFactory dataSourceFactory = new DataSourceFactory() {
            @Override
            public DataSource createDataSource() {
                BasicDataSource ds = new BasicDataSource();
                ds.setDriverClassName(config.getDriverClass());
                ds.setUrl(config.getJdbcUrl());
                ds.setUsername(config.getUsername());
                ds.setPassword(config.getPassword());
                ds.setConnectionInitSqls(Collections.singletonList("CREATE DATABASE IF NOT EXISTS test"));
                return ds;
            }
        };

        DataSourceUtils.setUpDataSource(dataSourceFactory);

        SqlRowMapper sqlRowMapper = JdbcContexts.newSqlRowMapper(new RowMapper<Object>() {
            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getObject("name");
            }
        });
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
                new JdbcTemplate(new SingleConnectionDataSource(DataSourceUtils.getDataSource())));
        namedParameterJdbcTemplate.setSqlExceptionTranslator(new SQLExceptionFactory() {
            @Override
            public SqlException translateException(Class<? extends Throwable> exceptionClass, Throwable original) {
                return SqlExceptionUtils.createSqlException(exceptionClass, original.getMessage());
            }
        });
        namedParameterJdbcTemplate.setRowMapper(sqlRowMapper);
        namedParameterJdbcTemplate.setJdbcOperations(new JdbcTemplate(namedParameterJdbcTemplate.getDataSource()));
        sqlCheckService.setJdbcTemplate(namedParameterJdbcTemplate);
    }

    @After
    public void teardown() {
        DataSourceUtils.shutdownDataSource();
    }

    @Test
    public void testCheck() {
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("Rule1");
        metadata1.setType(RuleType.SQL_CHECK);
        Rule rule1 = new Rule();
        rule1.setEnabled(true);
        rule1.setMetadata(metadata1);
        List<Rule> rules = Arrays.asList(rule1);

        SqlCheckReq req = new SqlCheckReq();
        req.setDelimiter(";");
        req.setScriptContent("SELECT 1;");

        List<CheckResult> results = sqlCheckService.check(session, req);
        assertNotNull(results);
        assertTrue(results.isEmpty());

        List<SqlCheckRule> rulesForSession = sqlCheckService.getRules(rules, session);
        assertNotNull(rulesForSession);
        assertTrue(rulesForSession.isEmpty());

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(RuleType.SQL_CHECK);
        violation1.setLevel(1);
        violation1.setMessage("Test violation 1");
        violation1.setProperty("testProperty1");
        violations.add(violation1);

        List<Rule> rulesWithViolations = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(rulesWithViolations);
        assertFalse(rulesWithViolations.isEmpty());
        Rule ruleWithViolation = rulesWithViolations.get(0);
        assertEquals(RuleType.SQL_CHECK, ruleWithViolation.getMetadata().getType());
        assertEquals(1, ruleWithViolation.getLevel());
        assertEquals("Rule1", ruleWithViolation.getMetadata().getName());
        RuleViolation violation = ruleWithViolation.getViolation();
        assertEquals(RuleViolation.fromCheckViolation(violation1), violation);
    }

    @Test
    public void testCheckWithEnvironment() {
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT 1;"), new OffsetString("SELECT 2;"));

        List<CheckResult> results = sqlCheckService.check(env.getId(), "test", sqls, config);
        assertNotNull(results);
        assertFalse(results.isEmpty());

        List<SqlCheckRule> rulesForEnvironment = sqlCheckService.getRules(rules, config.getDialectType(),
                sqlCheckService.getJdbcTemplate());
        assertNotNull(rulesForEnvironment);
        assertTrue(rulesForEnvironment.isEmpty());

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(RuleType.SQL_CHECK);
        violation1.setLevel(1);
        violation1.setMessage("Test violation 1");
        violation1.setProperty("testProperty1");
        violations.add(violation1);

        List<Rule> rulesWithViolations = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(rulesWithViolations);
        assertFalse(rulesWithViolations.isEmpty());
        Rule ruleWithViolation = rulesWithViolations.get(0);
        assertEquals(RuleType.SQL_CHECK, ruleWithViolation.getMetadata().getType());
        assertEquals(1, ruleWithViolation.getLevel());
        assertEquals("Rule1", ruleWithViolation.getMetadata().getName());
        RuleViolation violation = ruleWithViolation.getViolation();
        assertEquals(RuleViolation.fromCheckViolation(violation1), violation);
    }

    @Test(expected = RuntimeException.class)
    public void testGetRulesWithEmptyRules() {
        sqlCheckService.getRules(Collections.emptyList(), config.getDialectType(),
                sqlCheckService.getJdbcTemplate());
    }

    @Test(expected = RuntimeException.class)
    public void testGetRulesWithNullDialectType() {
        sqlCheckService.getRules(rules, null, sqlCheckService.getJdbcTemplate());
    }

    @Test(expected = RuntimeException.class)
    public void testGetRulesWithNullJdbcTemplate() {
        sqlCheckService.getRules(rules, config.getDialectType(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithEmptyScripts() {
        sqlCheckService.check(env.getId(), "test", Collections.emptyList(), config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullDatabaseName() {
        sqlCheckService.check(env.getId(), null, Collections.singletonList(new OffsetString("SELECT 1;")), config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullConnectionConfig() {
        sqlCheckService.check(env.getId(), "test", Collections.singletonList(new OffsetString("SELECT 1;")), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullDialectType() {
        sqlCheckService.check(env.getId(), "test", Collections.singletonList(new OffsetString("SELECT 1;")), config, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullJdbcTemplate() {
        sqlCheckService.check(env.getId(), "test", Collections.singletonList(new OffsetString("SELECT 1;")), config,
                (JdbcOperations) null);
    }

}