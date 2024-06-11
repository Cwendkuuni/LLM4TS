package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

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
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
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

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testCheck() {
        ConnectionSession session = new ConnectionSession();
        ConnectionConfig config = new ConnectionConfig();
        SqlCheckReq req = new SqlCheckReq();
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        List<CheckViolation> checkViolations = new ArrayList<>();

        // Test with empty rules
        assertEquals(Collections.emptyList(), sqlCheckService.check(session, req));

        // Test with valid rules
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("testRule1");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        rules.add(rule1);
        SqlCheckRuleFactory ruleFactory1 = new SqlCheckRuleFactory() {
            @Override
            public SqlCheckRule createRuleInstance(@NonNull ConnectionSession session,
                    @NonNull DialectType dialectType, @NonNull JdbcOperations jdbc) {
                return new SqlCheckRule() {
                    @Override
                    public List<CheckViolation> check(String sql) {
                        CheckViolation violation = new CheckViolation();
                        violation.setType(new RuleViolation.Type("TestViolation"));
                        violation.setMessage("Test violation message");
                        checkViolations.add(violation);
                        return Collections.singletonList(violation);
                    }
                };
            }
        };
        sqlCheckRules.add(SqlCheckRules.createByRule(Collections.singletonList(ruleFactory1), DialectType.MYSQL, jdbcTemplate));
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("testRule2");
        metadata2.setType(RuleType.SQL_CHECK);
        Rule rule2 = new Rule();
        rule2.setMetadata(metadata2);
        rules.add(rule2);
        SqlCheckRuleFactory ruleFactory2 = new SqlCheckRuleFactory() {
            @Override
            public SqlCheckRule createRuleInstance(@NonNull ConnectionSession session,
                    @NonNull DialectType dialectType, @NonNull JdbcOperations jdbc) {
                return new SqlCheckRule() {
                    @Override
                    public List<CheckViolation> check(String sql) {
                        return Collections.emptyList();
                    }
                };
            }
        };
        sqlCheckRules.add(SqlCheckRules.createByRule(Collections.singletonList(ruleFactory2), DialectType.MYSQL, jdbcTemplate));

        assertEquals(2, sqlCheckService.check(session, req).size());
        assertEquals(2, checkViolations.size());
        assertTrue(checkViolations.get(0).getMessage().contains("Test violation message"));

        // Test with empty session
        assertEquals(Collections.emptyList(), sqlCheckService.check(null, req));

        // Test with null req
        assertEquals(Collections.emptyList(), sqlCheckService.check(session, null));

        // Test with empty req scriptContent
        SqlCheckReq emptyReq = new SqlCheckReq();
        assertEquals(Collections.emptyList(), sqlCheckService.check(session, emptyReq));

        // Test with valid scriptContent
        String scriptContent = "SELECT * FROM test_table;";
        SqlCheckReq validReq = new SqlCheckReq();
        validReq.setScriptContent(scriptContent);
        List<CheckResult> checkResults = sqlCheckService.check(session, validReq);
        assertTrue(checkResults.isEmpty());

        // Test check with exception
        RuleMetadata metadata3 = new RuleMetadata();
        metadata3.setName("testRule3");
        metadata3.setType(RuleType.SQL_CHECK);
        Rule rule3 = new Rule();
        rule3.setMetadata(metadata3);
        rules.add(rule3);
        SqlCheckRuleFactory ruleFactory3 = new SqlCheckRuleFactory() {
            @Override
            public SqlCheckRule createRuleInstance(@NonNull ConnectionSession session,
                    @NonNull DialectType dialectType, @NonNull JdbcOperations jdbc) {
                throw new RuntimeException("Test exception");
            }
        };
        sqlCheckRules.add(SqlCheckRules.createByRule(Collections.singletonList(ruleFactory3), DialectType.MYSQL, jdbcTemplate));
        assertThrows(RuntimeException.class, () -> sqlCheckService.check(session, req));
    }

    @Test
    public void testCheckWithEnvironment() {
        Long environmentId = 1L;
        String databaseName = "test_database";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM test_table;"));
        ConnectionConfig config = new ConnectionConfig();

        // Test with empty sqls
        assertEquals(Collections.emptyList(), sqlCheckService.check(environmentId, databaseName, sqls, config));

        // Test with valid sqls
        List<CheckViolation> checkViolations = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("testRule1");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        List<Rule> rules = new ArrayList<>();
        rules.add(rule1);
        SqlCheckRuleFactory ruleFactory1 = new SqlCheckRuleFactory() {
            @Override
            public SqlCheckRule createRuleInstance(@NonNull ConnectionSession session,
                    @NonNull DialectType dialectType, @NonNull JdbcOperations jdbc) {
                return new SqlCheckRule() {
                    @Override
                    public List<CheckViolation> check(String sql) {
                        CheckViolation violation = new CheckViolation();
                        violation.setType(new RuleViolation.Type("TestViolation"));
                        violation.setMessage("Test violation message");
                        checkViolations.add(violation);
                        return Collections.singletonList(violation);
                    }
                };
            }
        };
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        sqlCheckRules.add(SqlCheckRules.createByRule(Collections.singletonList(ruleFactory1), DialectType.MYSQL, jdbcTemplate));
        Environment env = new Environment();
        env.setRulesetId(1L);

        List<CheckViolation> expectedViolations = checkViolations;

        assertEquals(expectedViolations.size(), sqlCheckService.check(environmentId, databaseName, sqls, config).size());
        assertEquals(expectedViolations, sqlCheckService.check(environmentId, databaseName, sqls, config));
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRuleFactory> candidates = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();

        // Test with empty rules
        assertEquals(Collections.emptyList(), sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate));

        // Test with valid rules
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("testRule1");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        rules.add(rule1);
        SqlCheckRuleFactory ruleFactory1 = new SqlCheckRuleFactory() {
            @Override
            public SqlCheckRule createRuleInstance(@NonNull ConnectionSession session,
                    @NonNull DialectType dialectType, @NonNull JdbcOperations jdbc) {
                return new SqlCheckRule() {};
            }
        };
        candidates.add(ruleFactory1);
        SqlCheckRule sqlCheckRule1 = new SqlCheckRule();
        sqlCheckRule1.setId(1);
        sqlCheckRules.add(sqlCheckRule1);
        List<SqlCheckRule> expectedRules = sqlCheckRules;
        assertEquals(expectedRules, sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate));

        // Test with empty candidates
        assertEquals(Collections.emptyList(), sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate));

        // Test with invalid dialectType
        assertThrows(IllegalArgumentException.class, () -> sqlCheckService.getRules(rules, null, jdbcTemplate));

        // Test with null jdbcOperations
        assertThrows(NullPointerException.class, () -> sqlCheckService.getRules(rules, DialectType.MYSQL, null));
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();

        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("testRule1");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        rules.add(rule1);
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(new RuleViolation.Type("TestViolation"));
        violation1.setMessage("Test violation message");
        violation1.setRuleId(1);
        violations.add(violation1);

        assertEquals(Collections.emptyList(), sqlCheckService.fullFillRiskLevel(rules, Collections.emptyList()));
        assertEquals(Collections.singletonList(rule1), sqlCheckService.fullFillRiskLevel(rules, Collections.singletonList(violation1)));
        assertEquals(rules, sqlCheckService.fullFillRiskLevel(rules, violations));
    }
}