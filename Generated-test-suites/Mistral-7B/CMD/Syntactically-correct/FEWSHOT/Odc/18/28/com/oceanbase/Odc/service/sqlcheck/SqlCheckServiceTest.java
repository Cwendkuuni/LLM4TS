package com.oceanbase.odc.service.sqlcheck;

import static com.oceanbase.odc.service.sqlcheck.model.RuleType.SQL_CHECK;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.ModelRule;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

/**
 * {@link SqlCheckService} tests.
 *
 * @author yh263208
 * @date 2023-03-01 14:52
 */
@Slf4j
public class SqlCheckServiceTest {

    private static final String RULE_NAME = "sql_check_rule";
    private static final String SQL = "select * from users where age < 18";
    private static final String VIOLATION_MESSAGE = "Age check violation.";
    private static final int RISK_LEVEL = 5;

    private EnvironmentService environmentService;
    private RuleService ruleService;
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.environmentService = mock(EnvironmentService.class);
        this.ruleService = mock(RuleService.class);
        this.sqlCheckService = new SqlCheckService() {
            @Override
            @Autowired
            public void setRuleService(RuleService ruleService) {
                super.setRuleService(ruleService);
                this.ruleService = ruleService;
            }

            @Override
            @Autowired
            public void setEnvironmentService(EnvironmentService environmentService) {
                super.setEnvironmentService(environmentService);
                this.environmentService = environmentService;
            }
        };
    }

    @Test
    public void testCheckWithEmptyRules() {
        when(this.ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        List<CheckResult> results = this.sqlCheckService.check(mock(ConnectionSession.class), new SqlCheckReq());
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithEmptyScript() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        List<Rule> rules = Arrays.asList(createRule(RULE_NAME, SQL_CHECK, RISK_LEVEL, VIOLATION_MESSAGE));
        List<SqlCheckRule> checkRules = this.sqlCheckService.getRules(rules, session);
        assertTrue(checkRules.isEmpty());
        List<CheckResult> results = this.sqlCheckService.check(session, new SqlCheckReq());
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithNoViolation() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        List<Rule> rules = Arrays.asList(createRule(RULE_NAME, SQL_CHECK, RISK_LEVEL, VIOLATION_MESSAGE));
        List<SqlCheckRule> checkRules = this.sqlCheckService.getRules(rules, session);
        assertEquals(1, checkRules.size());
        SqlCheckRule checkRule = checkRules.get(0);
        assertFalse(checkRule.isViolated(SQL));
        List<CheckResult> results = this.sqlCheckService.check(session, new SqlCheckReq(SQL));
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithViolation() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        List<Rule> rules = Arrays.asList(createRule(RULE_NAME, SQL_CHECK, RISK_LEVEL, VIOLATION_MESSAGE));
        List<SqlCheckRule> checkRules = this.sqlCheckService.getRules(rules, session);
        assertEquals(1, checkRules.size());
        SqlCheckRule checkRule = checkRules.get(0);
        assertTrue(checkRule.isViolated(SQL));
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(checkRule.getType());
        violation.setMessage(VIOLATION_MESSAGE);
        violation.setRiskLevel(RISK_LEVEL);
        violations.add(violation);
        List<CheckResult> results = this.sqlCheckService.check(session, new SqlCheckReq(SQL));
        assertEquals(1, results.size());
        CheckResult result = results.get(0);
        assertEquals(CheckResult.Type.VIOLATION, result.getType());
        assertEquals(violations, result.getViolations());
    }

    @Test
    public void testCheckWithMultipleViolations() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        List<Rule> rules = Arrays.asList(createRule(RULE_NAME, SQL_CHECK, RISK_LEVEL, VIOLATION_MESSAGE),
                createRule("another_rule_name", SQL_CHECK, RISK_LEVEL, VIOLATION_MESSAGE));
        List<SqlCheckRule> checkRules = this.sqlCheckService.getRules(rules, session);
        assertEquals(2, checkRules.size());
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(checkRules.get(0).getType());
        violation1.setMessage(VIOLATION_MESSAGE);
        violation1.setRiskLevel(RISK_LEVEL);
        violations.add(violation1);
        CheckViolation violation2 = new CheckViolation();
        violation2.setType(checkRules.get(1).getType());
        violation2.setMessage(VIOLATION_MESSAGE);
        violation2.setRiskLevel(RISK_LEVEL);
        violations.add(violation2);
        List<CheckResult> results = this.sqlCheckService.check(session, new SqlCheckReq(SQL));
        assertEquals(1, results.size());
        CheckResult result = results.get(0);
        assertEquals(CheckResult.Type.VIOLATIONS, result.getType());
        assertEquals(violations, result.getViolations());
    }

    @Test
    public void testCheckWithEmptyDataSource() {
        ConnectionConfig config = new ConnectionConfig();
        List<OffsetString> sqls = Collections.emptyList();
        List<CheckResult> results = this.sqlCheckService.check(null, config, sqls);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithEmptyScripts() {
        ConnectionConfig config = new ConnectionConfig();
        List<OffsetString> sqls = Arrays.asList(OffsetString.EMPTY);
        List<CheckResult> results = this.sqlCheckService.check(mock(Environment.class), config, sqls);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithOneViolation() {
        Environment env = mock(Environment.class);
        when(this.environmentService.detail(anyLong())).thenReturn(env);
        ConnectionConfig config = new ConnectionConfig();
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM users WHERE age < 18"));
        List<Rule> rules = Arrays.asList(createRule(RULE_NAME, SQL_CHECK, RISK_LEVEL, VIOLATION_MESSAGE));
        List<SqlCheckRule> checkRules = this.sqlCheckService.getRules(rules, DialectType.MYSQL, new JdbcTemplate(mock(JdbcOperations.class)));
        assertEquals(1, checkRules.size());
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(checkRules.get(0).getType());
        violation.setMessage(VIOLATION_MESSAGE);
        violation.setRiskLevel(RISK_LEVEL);
        violations.add(violation);
        when(env.getRulesetId()).thenReturn(1L);
        when(this.ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<CheckResult> results = this.sqlCheckService.check(env, config, sqls);
        assertEquals(1, results.size());
        CheckResult result = results.get(0);
        assertEquals(CheckResult.Type.VIOLATIONS, result.getType());
        assertEquals(violations, result.getViolations());
    }

    @Test
    public void testCheckWithMultipleViolations() {
        Environment env = mock(Environment.class);
        when(this.environmentService.detail(anyLong())).thenReturn(env);
        ConnectionConfig config = new ConnectionConfig();
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM users WHERE age < 18"),
                new OffsetString("UPDATE users SET age = 17 WHERE id = 1"));
        List<Rule> rules = Arrays.asList(createRule(RULE_NAME, SQL_CHECK, RISK_LEVEL, VIOLATION_MESSAGE),
                createRule("another_rule_name", SQL_CHECK, RISK_LEVEL, VIOLATION_MESSAGE));
        List<SqlCheckRule> checkRules = this.sqlCheckService.getRules(rules, DialectType.MYSQL, new JdbcTemplate(mock(JdbcOperations.class)));
        assertEquals(2, checkRules.size());
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(checkRules.get(0).getType());
        violation1.setMessage(VIOLATION_MESSAGE);
        violation1.setRiskLevel(RISK_LEVEL);
        violations.add(violation1);
        CheckViolation violation2 = new CheckViolation();
        violation2.setType(checkRules.get(1).getType());
        violation2.setMessage(VIOLATION_MESSAGE);
        violation2.setRiskLevel(RISK_LEVEL);
        violations.add(violation2);
        when(env.getRulesetId()).thenReturn(1L);
        when(this.ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<CheckResult> results = this.sqlCheckService.check(env, config, sqls);
        assertEquals(2, results.size());
        CheckResult result1 = results.get(0);
        assertEquals(CheckResult.Type.VIOLATIONS, result1.getType());
        assertEquals(violations, result1.getViolations());
        CheckResult result2 = results.get(1);
        assertEquals(CheckResult.Type.VIOLATIONS, result2.getType());
        assertEquals(violations, result2.getViolations());
    }

    private Rule createRule(String name, RuleType ruleType, int riskLevel, String violationMessage) {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName(name);
        metadata.setType(ruleType);
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        RuleViolation violation = new RuleViolation();
        violation.setMessage(violationMessage);
        violation.setRiskLevel(riskLevel);
        rule.setViolation(violation);
        return rule;
    }
}