package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.rule.DefaultSqlChecker;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;

/**
 * Test class for {@link SqlCheckService}
 *
 * @author yh263208
 */
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ConnectionSession session;

    @Mock
    private SingleConnectionDataSource dataSource;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        // given
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        List<Rule> rules = Arrays.asList(
                createRule("TestRule1", RuleType.SQL_CHECK, RuleLevel.LOW, null),
                createRule("TestRule2", RuleType.SQL_CHECK, RuleLevel.MEDIUM, null)
        );
        when(environmentService.detail(1L)).thenReturn(new Environment());
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        when(jdbcTemplate.queryForList("SELECT 1", new Object[0])).thenReturn(Collections.emptyList());
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        SqlCheckReq req = new SqlCheckReq("SELECT 1;", "delimiter;");

        // when
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // then
        assertEquals(2, checkResults.size());
        CheckViolation violation1 = checkResults.get(0).getViolations().get(0);
        CheckViolation violation2 = checkResults.get(1).getViolations().get(0);
        assertEquals(RuleViolation.INJECT_SQL, violation1.getType());
        assertEquals(RuleViolation.INJECT_SQL, violation2.getType());
        assertEquals(RuleLevel.LOW, violation1.getLevel());
        assertEquals(RuleLevel.MEDIUM, violation2.getLevel());
    }

    @Test
    public void testCheckWithExternalDataSource() {
        // given
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        List<Rule> rules = Arrays.asList(
                createRule("TestRule1", RuleType.SQL_CHECK, RuleLevel.LOW, null),
                createRule("TestRule2", RuleType.SQL_CHECK, RuleLevel.MEDIUM, null)
        );
        Environment env = new Environment();
        env.setRulesetId(1L);
        when(environmentService.detail(1L)).thenReturn(env);
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true, false);
        when(factory.getDataSource()).thenReturn(dataSource);
        SqlCheckContext checkContext = new SqlCheckContext(1);
        when(dataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);
        String sql = "SELECT 1;";
        List<OffsetString> sqls = Arrays.asList(OffsetString.of(sql, 0));

        // when
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "test_database", sqls, config);

        // then
        assertEquals(2, checkViolations.size());
        CheckViolation violation1 = checkViolations.get(0);
        CheckViolation violation2 = checkViolations.get(1);
        assertEquals(RuleViolation.INJECT_SQL, violation1.getType());
        assertEquals(RuleViolation.INJECT_SQL, violation2.getType());
        assertEquals(RuleLevel.LOW, violation1.getLevel());
        assertEquals(RuleLevel.MEDIUM, violation2.getLevel());
    }

    private Rule createRule(String name, RuleType type, RuleLevel level, String description) {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName(name);
        metadata.setType(type);
        metadata.setLevel(level);
        metadata.setDescription(description);
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        return rule;
    }

    private List<SqlCheckRule> getRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbc) {
        List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(dialectType, jdbc);
        return rules.stream().map(rule -> {
            RuleMetadata metadata = rule.getMetadata();
            if (!RuleType.SQL_CHECK.equals(metadata.getType()) || !Boolean.TRUE.equals(metadata.isEnabled())) {
                return null;
            }
            try {
                return SqlCheckRules.createByRule(candidates, dialectType, rule);
            } catch (Exception e) {
                log.error("Error creating SQL check rule: {}", e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private RuleLevel stringToRuleLevel(String levelString) {
        if (StringUtils.isBlank(levelString)) {
            return RuleLevel.UNDEFINED;
        }
        return RuleLevel.valueOf(levelString.toUpperCase());
    }

    private static class RuleLevel {
        public static final RuleLevel LOW = RuleLevel.valueOf("LOW");
        public static final RuleLevel MEDIUM = RuleLevel.valueOf("MEDIUM");
        public static final RuleLevel HIGH = RuleLevel.valueOf("HIGH");
        public static final RuleLevel CRITICAL = RuleLevel.valueOf("CRITICAL");
        public static final RuleLevel UNDEFINED = new Enum<>("UNDEFINED") {
            @Override
            public String toString() {
                return "UNDEFINED";
            }
        };
    }
}