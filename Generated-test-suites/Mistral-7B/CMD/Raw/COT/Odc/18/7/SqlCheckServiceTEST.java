package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
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
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.Model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.Model.CheckViolationType;
import com.oceanbase.odc.service.sqlcheck.Model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.factory.DefaultSqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.factory.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.factory.SqlCheckRuleFactoryRegistry;

@Slf4j
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;
    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = new ConnectionSession();
        ConnectionConfig config = new ConnectionConfig();
        SqlCheckReq req = new SqlCheckReq();
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        CheckResult result = new CheckResult();

        // Test empty session and empty request
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertTrue(checkResults.isEmpty());

        // Test empty rule set
        sqlCheckService = new SqlCheckService() {
            @Override
            List<Rule> getRules(List<Rule> rules, ConnectionSession session) {
                return Collections.emptyList();
            }
        };
        checkResults = sqlCheckService.check(session, req);
        assertTrue(checkResults.isEmpty());

        // Test valid scenario
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRule");
        metadata.setLevel(1);
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);
        RuleMetadata params = new QueryRuleMetadataParams.Builder().build();
        List<Rule> rulesFromService = new ArrayList<>();
        when(ruleService.list(anyLong(), params)).thenReturn(rulesFromService);
        when(ruleService.list(anyLong(), params)).thenReturn(rules);
        SqlCheckRuleFactoryRegistry registry = new SqlCheckRuleFactoryRegistry();
        registry.registerFactory(new DefaultSqlCheckRuleFactory());
        SqlCheckRules rulesEngine = new SqlCheckRules(registry);
        SqlCheckRuleFactory factory = rulesEngine.createFactory(DialectType.MySQL);
        SqlCheckRule sqlCheckRule = factory.createRule(rule);
        sqlCheckRules.add(sqlCheckRule);
        when(sqlCheckService.getRules(rules, DialectType.MySQL, mock(JdbcOperations.class)))
                .thenReturn(sqlCheckRules);
        OffsetString sql = new OffsetString("SELECT 1");
        CheckViolation violation = new CheckViolation();
        violation.setType(CheckViolationType.SQL_INJECTION);
        violation.setMessage("SQL injection detected");
        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(violation);
        CheckResult expectedResult = new CheckResult();
        expectedResult.setCheckViolations(checkViolations);

        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(1, results.size());
        assertEquals(expectedResult, results.get(0));
    }

    @Test
    public void testCheckWithEnvironment() {
        Environment environment = new Environment();
        ConnectionConfig config = new ConnectionConfig();
        List<OffsetString> sqls = new ArrayList<>();
        CheckResult result = new CheckResult();

        // Test empty sqls
        List<CheckResult> checkResults = sqlCheckService.check(1L, "test_database", sqls, config);
        assertTrue(checkResults.isEmpty());

        // Test valid scenario
        OffsetString sql = new OffsetString("SELECT 1");
        CheckViolation violation = new CheckViolation();
        violation.setType(CheckViolationType.SQL_INJECTION);
        violation.setMessage("SQL injection detected");
        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(violation);
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRule");
        metadata.setLevel(1);
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);
        RuleMetadata params = new QueryRuleMetadataParams.Builder().build();
        when(environmentService.detail(1L)).thenReturn(environment);
        when(ruleService.list(environment.getRulesetId(), params)).thenReturn(rules);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(DialectType.MySQL, jdbc);
        sqlCheckRules = rules.stream().map(r -> SqlCheckRules.createByRule(candidates, DialectType.MySQL, r)).collect(Collectors.toList());
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MySQL, null, sqlCheckRules);
        List<CheckViolation> violationList = sqlChecker.check(Collections.singletonList(sql), new SqlCheckContext(1));
        FullFillRiskLevel(rules, violationList);
        checkResults = sqlCheckService.check(1L, "test_database", sqls, config);
        assertEquals(1, checkResults.size());
        assertEquals(result, checkResults.get(0));
    }

    private void FullFillRiskLevel(List<Rule> rules, List<CheckViolation> violations) {
        List<Rule> violatedRules = new ArrayList<>();
        Map<String, Rule> name2RuleMap = CollectionUtils.isEmpty(rules)
                ? new HashMap<>()
                : rules.stream().collect(Collectors.toMap(r -> r.getMetadata().getName(), r -> r));
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
        SqlCheckService sqlCheckService = new SqlCheckService();
        sqlCheckService.fullFillRiskLevel(rules, violations);
    }
}