package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
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
import org.apache.commons.jdbc.JdbcTemplate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcOperations;

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
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata.Level;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation.Type;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.service.DefaultSqlChecker;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private ConnectionSession session;
    private JdbcOperations jdbcOperations;
    private SingleConnectionDataSource dataSource;

    @Before
    public void setup() {
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        session = Mockito.spy(ConnectionSession.class);
        SqlCheckRules sqlCheckRules = new SqlCheckRules();
        sqlCheckService = new SqlCheckService() {
            {
                setRuleService(ruleService);
                setEnvironmentService(environmentService);
            }
        };

        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        JdbcTemplate jdbcTemplate = new JdbcTemplate(new BasicDataSource());
        jdbcOperations = jdbcTemplate;
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);

        List<SqlCheckRuleFactory> candidates = Collections.singletonList(mock(SqlCheckRuleFactory.class));
        when(ruleService.list(anyLong(), any())).thenReturn(Arrays.asList(new Rule()));
        when(ruleService.list(anyLong(), QueryRuleMetadataParams.builder().build())).thenReturn(Arrays.asList(new Rule()));
        when(ruleService.list(anyLong(), any())).thenReturn(Arrays.asList(new Rule(), new Rule()));
        when(SqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class))).thenReturn(candidates);
        when(SqlCheckRules.createByRule(anyList(), any(DialectType.class), any(Rule.class))).thenReturn(mock(SqlCheckRule.class));

        sqlCheckService.setDialectType(DialectType.MYSQL);
    }

    @Test
    public void testCheck() {
        List<Rule> rules = Arrays.asList(new Rule() {{
            RuleMetadata metadata = new RuleMetadata();
            metadata.setName("TestRule1");
            metadata.setType(RuleType.SQL_CHECK);
            metadata.setLevel(Level.LOW);
            setMetadata(metadata);
        }},
                new Rule() {{
                    RuleMetadata metadata = new RuleMetadata();
                    metadata.setName("TestRule2");
                    metadata.setType(RuleType.SQL_CHECK);
                    metadata.setLevel(Level.MEDIUM);
                    setMetadata(metadata);
                }});
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcOperations);

        CheckReq req = new CheckReq();
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), checkResults);

        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("TestRule1");
        metadata1.setType(RuleType.SQL_CHECK);
        RuleTestRule1 rule1 = new RuleTestRule1();
        rule1.setMetadata(metadata1);
        RuleViolation violation1 = new RuleTestViolation();
        violation1.setType(Type.QUERY);
        violation1.setMessage("Test violation 1");
        violation1.setRiskLevel(Level.LOW);
        rule1.addViolation(violation1);
        RuleViolation violation2 = new RuleTestViolation();
        violation2.setType(Type.QUERY);
        violation2.setMessage("Test violation 2");
        violation2.setRiskLevel(Level.MEDIUM);
        rule1.addViolation(violation2);
        Rule rule = new Rule();
        rule.setMetadata(metadata1);
        rule.setViolation(RuleViolation.fromCheckViolation(violation1));
        when(ruleService.list(anyLong(), any())).thenReturn(Arrays.asList(rule));

        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setType(Type.QUERY);
        checkViolation.setMessage("Test query");
        List<CheckViolation> checkViolations = Arrays.asList(checkViolation, checkViolation);

        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(2, results.size());
        CheckResult result1 = results.get(0);
        assertEquals("TestRule1", result1.getRuleName());
        assertEquals(Level.LOW, result1.getRiskLevel());
        assertEquals(2, result1.getViolations().size());
        CheckViolation violation = result1.getViolations().get(0);
        assertEquals("Test violation 1", violation.getMessage());
        assertEquals(Level.LOW, violation.getRiskLevel());
        assertEquals("Test violation 2", results.get(1).getViolations().get(0).getMessage());
        assertEquals(Level.MEDIUM, results.get(1).getRiskLevel());

        verify(ruleService, times(1)).list(anyLong(), any());
        verify(ruleService, times(1)).list(anyLong(), QueryRuleMetadataParams.builder().build());
        verify(ruleService, times(1)).list(anyLong(), any());
        verifyNoMoreInteractions(ruleService);
        verify(SqlCheckRules, times(1)).getAllFactories(DialectType.MYSQL, jdbcOperations);
        verify(SqlCheckRules, times(1)).createByRule(candidates, DialectType.MYSQL, rule1);
        verify(session, times(1)).getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY);
    }

    @Test
    public void testCheckWithEnvironment() {
        List<Rule> rules = Arrays.asList(new Rule() {{
            RuleMetadata metadata = new RuleMetadata();
            metadata.setName("TestRule1");
            metadata.setType(RuleType.SQL_CHECK);
            metadata.setLevel(Level.LOW);
            setMetadata(metadata);
        }},
                new Rule() {{
                    RuleMetadata metadata = new RuleMetadata();
                    metadata.setName("TestRule2");
                    metadata.setType(RuleType.SQL_CHECK);
                    metadata.setLevel(Level.MEDIUM);
                    setMetadata(metadata);
                }});

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcOperations);

        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> checkResults = sqlCheckService.check(1L, "test", Arrays.asList(new OffsetString("SELECT * FROM test")), new ConnectionConfig());
        assertEquals(Collections.emptyList(), checkResults);

        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("TestRule1");
        metadata1.setType(RuleType.SQL_CHECK);
        RuleTestRule1 rule1 = new RuleTestRule1();
        rule1.setMetadata(metadata1);
        RuleViolation violation1 = new RuleTestViolation();
        violation1.setType(Type.QUERY);
        violation1.setMessage("Test violation 1");
        violation1.setRiskLevel(Level.LOW);
        rule1.addViolation(violation1);
        RuleViolation violation2 = new RuleTestViolation();
        violation2.setType(Type.QUERY);
        violation2.setMessage("Test violation 2");
        violation2.setRiskLevel(Level.MEDIUM);
        rule1.addViolation(violation2);
        Rule rule = new Rule();
        rule.setMetadata(metadata1);
        rule.setViolation(RuleViolation.fromCheckViolation(violation1));
        when(ruleService.list(anyLong(), any())).thenReturn(Arrays.asList(rule));

        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setType(Type.QUERY);
        checkViolation.setMessage("Test query");
        List<CheckViolation> checkViolations = Arrays.asList(checkViolation, checkViolation);

        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        DataSource dataSource = mock(DataSource.class);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        when(dataSource.getConnection()).thenReturn(mock(Connection.class));
        List<SqlCheckRule> checkRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);

        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, checkRules);
        List<CheckViolation> violations = sqlChecker.check(Collections.singletonList(new OffsetString("SELECT * FROM test")), new SqlCheckContext(1));

        fullFillRiskLevel(rules, violations);

        List<CheckResult> results = sqlCheckService.check(1L, "test", Arrays.asList(new OffsetString("SELECT * FROM test")), new ConnectionConfig());
        assertEquals(2, results.size());
        CheckResult result1 = results.get(0);
        assertEquals("TestRule1", result1.getRuleName());
        assertEquals(Level.LOW, result1.getRiskLevel());
        assertEquals(2, result1.getViolations().size());
        CheckViolation violation = result1.getViolations().get(0);
        assertEquals("Test violation 1", violation.getMessage());
        assertEquals(Level.LOW, violation.getRiskLevel());
        assertEquals("Test violation 2", results.get(1).getViolations().get(0).getMessage());
        assertEquals(Level.MEDIUM, results.get(1).getRiskLevel());

        verify(environmentService, times(1)).detail(anyLong());
        verify(ruleService, times(1)).list(anyLong(), any());
        verify(ruleService, times(1)).list(anyLong(), QueryRuleMetadataParams.builder().build());
        verify(ruleService, times(1)).list(anyLong(), any());
        verify(SqlCheckRules, times(1)).getAllFactories(DialectType.MYSQL, jdbcOperations);
        verify(SqlCheckRules, times(1)).createByRule(candidates, DialectType.MYSQL, rule1);
        verify(dataSource, times(1)).getConnection();
        verify(session, times(1)).getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY);
    }

    private void fullFillRiskLevel(List<Rule> rules, List<CheckViolation> violations) {
        List<Rule> violatedRules = new ArrayList<>();
        Map<String, Rule> name2RuleMap = new HashMap<>();
        for (Rule rule : rules) {
            name2RuleMap.put(rule.getMetadata().getName(), rule);
        }
        for (CheckViolation violation : violations) {
            String name = "${" + violation.getType().getName() + "}";
            Rule rule = name2RuleMap.getOrDefault(name, null);
            if (Objects.nonNull(rule)) {
                violation.setLevel(rule.getLevel());
                Rule newRule = new Rule();
                newRule.setLevel(rule.getLevel());
                newRule.setViolation(RuleViolation.fromCheckViolation(violation));
                violatedRules.add(newRule);
            }
        }
        assertEquals(violatedRules, sqlCheckService.fullFillRiskLevel(rules, violations));
    }
}