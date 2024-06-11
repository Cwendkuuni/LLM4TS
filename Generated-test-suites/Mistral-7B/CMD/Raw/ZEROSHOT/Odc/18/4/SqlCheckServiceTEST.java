package com.oceanbase.odc.service.sqlcheck.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlCheckReq;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

/**
 * {@link SqlCheckServiceTest}
 *
 * @author yh263208
 * @date 2023-03-13 11:01
 * @since ODC_release_4.1.0
 */
@Slf4j
public class SqlCheckServiceTest {

    private static final String RULE_NAME = "testRule";
    private static final String SCRIPT_CONTENT = "SELECT * FROM testTable WHERE id = ?";
    private static final String VIOLATION_TYPE = "testViolationType";
    private static final int RISK_LEVEL = 5;

    @Test
    public void testCheck() {
        // given
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(SCRIPT_CONTENT);

        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName(RULE_NAME);
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        List<Rule> rules = Collections.singletonList(rule);

        JdbcOperations jdbc = mock(JdbcOperations.class);
        when(jdbc.queryForObject(ArgumentMatchers.anyString(), ArgumentMatchers.anyClass())).thenReturn(null);

        RuleService ruleService = mock(RuleService.class);
        when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        EnvironmentService environmentService = mock(EnvironmentService.class);

        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        when(factory.getDataSource()).thenReturn(dataSource);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        SqlCheckService sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
        sqlCheckService.setFactory(factory);

        // when
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // then
        assertNotNull(checkResults);
        assertEquals(1, checkResults.size());
        CheckResult checkResult = checkResults.get(0);
        assertEquals(RISK_LEVEL, checkResult.getCheckViolations().get(0).getLevel());
    }

    @Test
    public void testCheckWithEnvironment() {
        // given
        Long environmentId = 1L;
        String databaseName = "testDatabase";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString(SCRIPT_CONTENT, 0));
        ConnectionConfig config = new ConnectionConfig();

        Environment env = new Environment();
        env.setRulesetId(1L);

        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName(RULE_NAME);
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        List<Rule> rules = Collections.singletonList(rule);

        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        when(factory.getDataSource()).thenReturn(dataSource);

        JdbcOperations jdbc = mock(JdbcOperations.class);
        List<SqlCheckRuleFactory> candidates = mock(List.class);
        when(jdbc.queryForObject(ArgumentMatchers.anyString(), ArgumentMatchers.anyClass())).thenReturn(null);
        when(candidates.get(0).createCheckRule(ArgumentMatchers.any(Rule.class), ArgumentMatchers.any(DialectType.class),
                ArgumentMatchers.any(JdbcOperations.class))).thenReturn(new SqlCheckRule());

        SqlCheckService sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(mock(RuleService.class));
        sqlCheckService.setEnvironmentService(mock(EnvironmentService.class));
        sqlCheckService.setFactory(factory);

        // when
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // then
        assertNotNull(checkViolations);
        assertEquals(1, checkViolations.size());
        CheckViolation checkViolation = checkViolations.get(0);
        assertEquals(RISK_LEVEL, checkViolation.getLevel());
    }

    @Test
    public void testGetRules() {
        // given
        List<Rule> rules = new ArrayList<>();
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = mock(JdbcOperations.class);

        SqlCheckService sqlCheckService = new SqlCheckService();

        // when
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);

        // then
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithSession() {
        // given
        List<Rule> rules = new ArrayList<>();
        ConnectionSession session = mock(ConnectionSession.class);
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = mock(JdbcOperations.class);

        SqlCheckService sqlCheckService = new SqlCheckService();

        // when
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session, dialectType, jdbc);

        // then
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithJdbc() {
        // given
        List<Rule> rules = new ArrayList<>();
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = mock(JdbcOperations.class);

        SqlCheckService sqlCheckService = new SqlCheckService();

        // when
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);

        // then
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        // given
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();

        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName(RULE_NAME);
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rules.add(rule);

        CheckViolation violation = new CheckViolation();
        violation.setType(new RuleViolation(VIOLATION_TYPE, StringUtils.EMPTY, null));
        violations.add(violation);

        SqlCheckService sqlCheckService = new SqlCheckService();

        // when
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        // then
        assertEquals(1, violatedRules.size());
        Rule violatedRule = violatedRules.get(0);
        assertEquals(RISK_LEVEL, violatedRule.getLevel());
    }
}