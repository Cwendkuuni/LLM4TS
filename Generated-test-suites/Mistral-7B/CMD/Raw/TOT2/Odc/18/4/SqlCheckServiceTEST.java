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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
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
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;

/**
 * {@link SqlCheckService} test cases.
 *
 * @author yh263208
 * @since ODC_release_4.1.0
 */
@Slf4j
@Validated
public class SqlCheckServiceTest {

    private static final String TEST_DATABASE_NAME = "test_database";
    private static final String TEST_RULE_SET_ID = "test_rule_set_id";
    private static final String TEST_RULE_NAME = "test_rule";

    @Mock
    private ConnectionSession session;
    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private SingleConnectionDataSource dataSource;
    @Mock
    private JdbcOperations jdbc;
    @Mock
    private OBConsoleDataSourceFactory factory;

    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(session.getDialectType()).thenReturn(DialectType.MySQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbc);
        when(ruleService.list(TEST_RULE_SET_ID, QueryRuleMetadataParams.builder().build())).thenReturn(Arrays.asList(
                createTestRule()));
        when(environmentService.detail(anyLong())).thenReturn(createTestEnvironment());
        when(factory.getDataSource()).thenReturn(dataSource);

        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
    }

    @Test
    public void testCheck() {
        // Given
        SqlCheckReq req = SqlCheckReq.builder().build();
        when(session.getRuleSetId()).thenReturn(TEST_RULE_SET_ID);
        List<Rule> rules = Arrays.asList(createTestRule());
        List<SqlCheckRule> sqlCheckRules = createTestCheckRules(rules);
        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MySQL, null, sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check(Collections.singletonList("SELECT * FROM test_table;"));
        when(ruleService.list(TEST_RULE_SET_ID, QueryRuleMetadataParams.builder().build())).thenReturn(rules);

        // When
        List<CheckResult> results = sqlCheckService.check(session, req);

        // Then
        assertEquals(1, results.size());
        assertEquals("test_rule", results.get(0).getRuleName());
        assertEquals(RuleViolation.FROM_CHECK_VIOLATION, results.get(0).getViolation().getType());
        assertTrue(results.get(0).getViolation().getMessage().contains("Test message for test rule"));
    }

    @Test
    public void testCheckWithEnvironment() {
        // Given
        List<OffsetString> sqls = Arrays.asList(OffsetString.of("SELECT * FROM test_table;"));
        ConnectionConfig config = ConnectionConfig.builder().build();
        Environment env = createTestEnvironment();
        when(environmentService.detail(anyLong())).thenReturn(env);
        List<Rule> rules = Arrays.asList(createTestRule());
        List<SqlCheckRule> sqlCheckRules = createTestCheckRules(rules);
        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MySQL, null, sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check(Collections.singletonList("SELECT * FROM test_table;"));
        List<Rule> fullfilledRules = fullFillRiskLevel(rules, checkViolations);

        // When
        List<CheckResult> results = sqlCheckService.check(TEST_RULE_SET_ID, TEST_DATABASE_NAME, sqls, config);

        // Then
        assertEquals(1, results.size());
        assertEquals("test_rule", results.get(0).getRuleName());
        assertEquals(RuleViolation.FROM_CHECK_VIOLATION, results.get(0).getViolation().getType());
        assertTrue(results.get(0).getViolation().getMessage().contains("Test message for test rule"));
        assertEquals(RuleViolation.RISK_LEVEL_HIGH, results.get(0).getViolation().getLevel());
    }

    @Test
    public void testEmptyCheck() {
        // Given
        when(session.getRuleSetId()).thenReturn(TEST_RULE_SET_ID);
        List<Rule> rules = Arrays.asList(createTestRule());
        when(ruleService.list(TEST_RULE_SET_ID, QueryRuleMetadataParams.builder().build())).thenReturn(rules);

        // When
        List<CheckResult> results = sqlCheckService.check(session, SqlCheckReq.builder().build());

        // Then
        assertEquals(0, results.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSession() {
        // Given
        SqlCheckReq req = SqlCheckReq.builder().build();
        when(session.getRuleSetId()).thenReturn(TEST_RULE_SET_ID);
        List<Rule> rules = Arrays.asList(createTestRule());

        // When
        sqlCheckService.check(null, req);

        // Then
        fail("Should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullReq() {
        // Given
        ConnectionSession session = mock(ConnectionSession.class);

        // When
        sqlCheckService.check(session, null);

        // Then
        fail("Should throw IllegalArgumentException");
    }

    // Helper methods
    private Rule createTestRule() {
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName(TEST_RULE_NAME);
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rule.setViolation(RuleViolation.fromCheckViolation(null));
        rule.setLevel(RuleViolation.RISK_LEVEL_LOW);
        return rule;
    }

    private Environment createTestEnvironment() {
        Environment env = new Environment();
        env.setRulesetId(TEST_RULE_SET_ID);
        return env;
    }

    private List<SqlCheckRule> createTestCheckRules(List<Rule> rules) {
        return SqlCheckRules.getAllFactories(DialectType.MySQL, jdbc).stream()
                .map(f -> f.createCheckRule(rules.get(0)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Rule> fullFillRiskLevel(List<Rule> rules, List<CheckViolation> violations) {
        return rules.stream().peek(r -> {
            r.setViolation(RuleViolation.fromCheckViolation(null));
            r.setLevel(RuleViolation.RISK_LEVEL_LOW);
        }).map(r -> {
            Rule newRule = new Rule();
            newRule.setLevel(r.getLevel());
            newRule.setViolation(RuleViolation.fromCheckViolation(null));
            for (CheckViolation violation : violations) {
                if (violation.getType().getName().equals(r.getMetadata().getName())) {
                    newRule.setViolation(violation);
                    break;
                }
            }
            return newRule;
        }).collect(Collectors.toList());
    }
}