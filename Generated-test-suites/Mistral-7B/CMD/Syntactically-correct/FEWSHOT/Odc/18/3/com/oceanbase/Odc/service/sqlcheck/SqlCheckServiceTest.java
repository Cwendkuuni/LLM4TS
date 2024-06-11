package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
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
import com.oceanbase.odc.service.sqlcheck.rule.DefaultSqlChecker;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtilTest;

/**
 * {@link SqlCheckService} Tests
 *
 * @author yh263208
 * @date 2023-03-27 11:10
 * @since ODC_release_4.1.0
 */
@Slf4j
public class SqlCheckServiceTest {

    @Component
    static class TestSqlCheckService {

        @Autowired
        private RuleService ruleService;
        @Autowired
        private EnvironmentService environmentService;

        public List<CheckResult> check(ConnectionSession session, @Valid SqlCheckReq req) {
            Long ruleSetId = 1L;
            List<Rule> rules = Collections.singletonList(Mockito.mock(Rule.class));
            List<SqlCheckRule> sqlCheckRules = getRules(rules, session);
            DefaultSqlChecker sqlChecker = new DefaultSqlChecker(session.getDialectType(), req.getDelimiter(), sqlCheckRules);
            List<CheckViolation> checkViolations = sqlChecker.check(req.getScriptContent());
            fullFillRiskLevel(rules, checkViolations);
            return SqlCheckUtil.buildCheckResults(checkViolations);
        }

        public List<CheckViolation> check(Long environmentId, String databaseName, List<OffsetString> sqls, ConnectionConfig config) {
            Environment env = Mockito.mock(Environment.class);
            List<Rule> rules = Collections.singletonList(Mockito.mock(Rule.class));
            OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true, false);
            SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);
            when(factory.getDataSource()).thenReturn(dataSource);
            JdbcTemplate jdbc = Mockito.mock(JdbcTemplate.class);
            when(dataSource.getJdbcTemplate()).thenReturn(jdbc);
            List<SqlCheckRule> checkRules = getRules(rules, config.getDialectType(), jdbc);
            DefaultSqlChecker sqlChecker = new DefaultSqlChecker(config.getDialectType(), null, checkRules);
            List<CheckViolation> checkViolations = new ArrayList<>();
            for (OffsetString sql : sqls) {
                List<CheckViolation> violations = sqlChecker.check(Collections.singletonList(sql), new SqlCheckContext(1));
                checkViolations.addAll(violations);
            }
            return checkViolations;
        }

        public List<SqlCheckRule> getRules(List<Rule> rules, @NonNull ConnectionSession session) {
            return getRules(rules, session.getDialectType(), session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY));
        }

        public List<SqlCheckRule> getRules(List<Rule> rules, @NonNull DialectType dialectType, @NonNull JdbcOperations jdbc) {
            if (CollectionUtils.isEmpty(rules)) {
                return Collections.emptyList();
            }
            List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(dialectType, jdbc);
            return rules.stream().filter(rule -> {
                RuleMetadata metadata = rule.getMetadata();
                if (metadata == null || !Boolean.TRUE.equals(rule.getEnabled())) {
                    return false;
                }
                return Objects.equals(metadata.getType(), RuleType.SQL_CHECK);
            }).map(rule -> {
                try {
                    return SqlCheckRules.createByRule(candidates, dialectType, rule);
                } catch (Exception e) {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }

        public List<Rule> fullFillRiskLevel(List<Rule> rules, @NonNull List<CheckViolation> violations) {
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
            return violatedRules;
        }
    }

    @Autowired
    private TestSqlCheckService sqlCheckService;

    @Before
    public void setup() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.singletonList(Mockito.mock(Rule.class)));
        when(environmentService.detail(anyLong())).thenReturn(Mockito.mock(Environment.class));
    }

    @After
    public void cleanup() {
        reset(ruleService, environmentService);
    }

    @Test
    public void testCheckWithConnectionSession() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = SqlCheckUtilTest.generateSqlCheckReq();
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(1, checkResults.size());
        assertTrue(checkResults.get(0).getViolations().size() > 0);
    }

    @Test
    public void testCheckWithEnvironment() {
        List<OffsetString> sqls = Collections.singletonList(OffsetString.of("select * from users where age < 18"));
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MySQL);
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "test_database", sqls, config);
        assertEquals(1, checkViolations.size());
        assertTrue(checkViolations.get(0).toString().contains("Age restriction violation"));
    }
}