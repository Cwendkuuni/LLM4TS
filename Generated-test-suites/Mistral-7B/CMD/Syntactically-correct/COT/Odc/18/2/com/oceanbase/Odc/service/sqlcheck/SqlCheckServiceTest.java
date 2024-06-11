package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;
    private ConnectionConfig config;
    private EnvironmentService environmentService;
    private RuleService ruleService;
    private JdbcTemplate jdbcTemplate;
    private SingleConnectionDataSource dataSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        session = Mockito.mock(ConnectionSession.class);
        config = Mockito.mock(ConnectionConfig.class);
        environmentService = Mockito.mock(EnvironmentService.class);
        ruleService = Mockito.mock(RuleService.class);
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        dataSource = Mockito.mock(SingleConnectionDataSource.class);

        Mockito.when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        Mockito.when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        Mockito.when(environmentService.detail(Mockito.anyLong())).thenReturn(new Environment());
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<>());

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM test_table");

        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class)))
                .thenReturn(new ArrayList<>());

        Mockito.when(jdbcTemplate.queryForList(Mockito.anyString(), Mockito.anyArray()))
                .thenReturn(Collections.emptyList());

        Mockito.when(dataSource.getConnection()).thenReturn(Mockito.mock(java.sql.Connection.class));
    }

    @Test
    public void testCheck() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rules.add(rule);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM test_table");

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check(Collections.singletonList(req.getScriptContent()),
                new SqlCheckContext(1));

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        assertEquals(1, violatedRules.size());
        assertEquals("testRule", violatedRules.get(0).getMetadata().getName());
    }

    @Test
    public void testCheckWithEmptyRules() {
        List<Rule> rules = new ArrayList<>();

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM test_table");

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check(Collections.singletonList(req.getScriptContent()),
                new SqlCheckContext(1));

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        assertTrue(violatedRules.isEmpty());
    }

    @Test
    public void testCheckWithEnvironment() {
        Environment env = new Environment();
        Long ruleSetId = 1L;
        List<Rule> rules = this.ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build());

        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql = new OffsetString("SELECT * FROM test_table");
        sqls.add(sql);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check(sqls, new SqlCheckContext(1));

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        assertEquals(sqls.size(), violatedRules.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullEnvironment() {
        List<Rule> rules = this.ruleService.list(Mockito.anyLong(), QueryRuleMetadataParams.builder().build());

        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql = new OffsetString("SELECT * FROM test_table");
        sqls.add(sql);

        SqlCheckContext checkContext = new SqlCheckContext((long) sqls.size());

        try {
            sqlCheckService.check(null, rules, sqls, config);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("environment cannot be null", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullDatabaseName() {
        Environment env = Mockito.mock(Environment.class);
        Mockito.when(environmentService.detail(Mockito.anyLong())).thenReturn(env);

        List<Rule> rules = this.ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build());

        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql = new OffsetString("SELECT * FROM test_table");
        sqls.add(sql);

        try {
            sqlCheckService.check(env, null, sqls, config);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("databaseName cannot be null", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullSqls() {
        Environment env = Mockito.mock(Environment.class);
        Mockito.when(environmentService.detail(Mockito.anyLong())).thenReturn(env);

        List<Rule> rules = this.ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build());

        try {
            sqlCheckService.check(env, "test_database", null, config);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("sqls cannot be null", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullConfig() {
        Environment env = Mockito.mock(Environment.class);
        Mockito.when(environmentService.detail(Mockito.anyLong())).thenReturn(env);

        List<Rule> rules = this.ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build());

        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql = new OffsetString("SELECT * FROM test_table");
        sqls.add(sql);

        try {
            sqlCheckService.check(env, "test_database", sqls, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("config cannot be null", e.getMessage());
        }
    }
}