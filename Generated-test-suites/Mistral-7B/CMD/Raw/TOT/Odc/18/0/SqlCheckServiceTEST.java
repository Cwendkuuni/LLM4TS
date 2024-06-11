package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.model.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckUtils;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.service.DefaultSqlChecker;

public class SqlCheckServiceTest {

    @Autowired
    private RuleService ruleService;
    @Autowired
    private EnvironmentService environmentService;

    private SqlCheckService sqlCheckService;
    private JdbcTemplate jdbcTemplate;
    private ConnectionSession session;
    private ConnectionConfig config;

    @Before
    public void setup() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("root");
        dataSource.setPassword("password");
        jdbcTemplate = new JdbcTemplate(new JdbcOperations() {
            @Override
            public int update(String sql) {
                return dataSource.update(sql);
            }
        });

        session = Mockito.mock(ConnectionSession.class);
        config = Mockito.mock(ConnectionConfig.class);
        sqlCheckService = new SqlCheckService() {
            @Override
            public List<Rule> fullFillRiskLevel(List<Rule> rules, List<CheckViolation> violations) {
                return Collections.emptyList();
            }
        };
        when(session.getDialectType()).thenReturn(DialectType.MySQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        when(config.getDialectType()).thenReturn(DialectType.MySQL);

        Rule rule = Mockito.mock(Rule.class);
        RuleMetadata metadata = Mockito.mock(RuleMetadata.class);
        when(metadata.getType()).thenReturn(RuleType.SQL_CHECK);
        when(metadata.getName()).thenReturn("TestRule");
        when(rule.getMetadata()).thenReturn(metadata);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Arrays.asList(rule));

        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        SqlCheckRuleFactory factory = Mockito.mock(SqlCheckRuleFactory.class);
        SqlCheckRule sqlCheckRule = Mockito.mock(SqlCheckRule.class);
        when(factory.createInstance(rule, session.getDialectType(), jdbcTemplate)).thenReturn(sqlCheckRule);
        when(SqlCheckRules.getAllFactories(eq(DialectType.MySQL), any(JdbcOperations.class)))
                .thenReturn(Arrays.asList(factory));

        when(SqlCheckRules.createByRule(Collections.singletonList(factory), eq(DialectType.MySQL), eq(rule)))
                .thenReturn(sqlCheckRule);
    }

    @Test
    public void testCheck() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM test_table WHERE id = 1");
        List<CheckViolation> checkViolations = sqlCheckService.check(session, req);
        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testCheckWithViolations() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM test_table WHERE id IN (1, '2', 3)");
        List<CheckViolation> checkViolations = Arrays.asList(
                new CheckViolation("TestRule", new RuleViolation("SQL_INJECTION", "INjection attack detected")),
                new CheckViolation("TestRule", new RuleViolation("COMMAND_INJECTION", "Command injection detected"))
        );
        when(session.getRuleSetId()).thenReturn(1L);
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Arrays.asList(Mockito.mock(Rule.class)));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Arrays.asList(Mockito.mock(Rule.class), rule));
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(1, results.size());
        assertEquals(2, results.get(0).getViolations().size());

        CheckViolation violation = results.get(0).getViolations().get(0);
        assertEquals("TestRule", violation.getType().getName());
        assertEquals("SQL_INJECTION", violation.getRuleViolation().getType());
        assertEquals("Injection attack detected", violation.getMessage());

        violation = results.get(0).getViolations().get(1);
        assertEquals("TestRule", violation.getType().getName());
        assertEquals("COMMAND_INJECTION", violation.getRuleViolation().getType());
        assertEquals("Command injection detected", violation.getMessage());
    }

    @Test
    public void testCheckWithEmptyRules() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM test_table WHERE id = 1");
        when(session.getRuleSetId()).thenReturn(null);
        List<CheckViolation> checkViolations = sqlCheckService.check(session, req);
        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testCheckWithEnvironment() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM test_table WHERE id = 1");
        Environment env = new Environment();
        env.setRulesetId(1L);
        when(environmentService.detail(anyLong())).thenReturn(env);
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testdb", Arrays.asList(new OffsetString("SELECT * FROM test_table WHERE id = 1")), config);
        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testCheckWithViolationsInEnvironment() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM test_table WHERE id IN (1, '2', 3)");
        Environment env = new Environment();
        env.setRulesetId(1L);
        List<Rule> rules = Arrays.asList(Mockito.mock(Rule.class), rule);
        when(environmentService.detail(anyLong())).thenReturn(env);
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Arrays.asList(Mockito.mock(Rule.class)));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(rule.getMetadata()).thenReturn(Mockito.mock(RuleMetadata.class));
        when(rule.getMetadata().getName()).thenReturn("TestRule");
        when(rule.getMetadata().getType()).thenReturn(RuleType.SQL_CHECK);
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testdb", Arrays.asList(new OffsetString("SELECT * FROM test_table WHERE id IN (1, '2', 3)")), config);
        assertEquals(2, checkViolations.size());

        CheckViolation violation = checkViolations.get(0);
        assertEquals("TestRule", violation.getType().getName());
        assertEquals("SQL_INJECTION", violation.getRuleViolation().getType());
        assertEquals("Injection attack detected", violation.getMessage());

        violation = checkViolations.get(1);
        assertEquals("TestRule", violation.getType().getName());
        assertEquals("COMMAND_INJECTION", violation.getRuleViolation().getType());
        assertEquals("Command injection detected", violation.getMessage());
    }
}