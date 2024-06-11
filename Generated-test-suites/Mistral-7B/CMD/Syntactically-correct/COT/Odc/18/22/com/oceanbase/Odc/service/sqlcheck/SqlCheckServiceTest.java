package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.test.SqlCheckServiceTestData;

/**
 * {@link SqlCheckServiceTest}
 *
 * @author yh263208
 * @since ODC_release_4.1.0
 */
@Slf4j
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        session = new ConnectionSession();
        ruleService = Mockito.mock(RuleService.class);
        environmentService = Mockito.mock(EnvironmentService.class);
        jdbcTemplate = new JdbcTemplate(Mockito.mock(JdbcOperations.class));
        Mockito.when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        Mockito.when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
    }

    @Test
    public void testCheckWithValidInputs() {
        SqlCheckReq req = SqlCheckTestData.getCheckReq();
        ConnectionSession session = SqlCheckTestData.getValidSession();
        List<Rule> rules = SqlCheckTestData.getValidRules();
        when(ruleService.list(anyLong(), isA(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(session, req);
        assertTrue(results.size() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullSession() {
        SqlCheckReq req = SqlCheckReq.builder().build();
        sqlCheckService.check(null, req);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullReq() {
        ConnectionSession session = SqlCheckTestData.getValidSession();
        sqlCheckService.check(session, null);
    }

    @Test
    public void testCheckWithEmptyRules() {
        ConnectionSession session = SqlCheckTestData.getValidSession();
        SqlCheckReq req = SqlCheckReq.builder().build();
        when(ruleService.list(anyLong(), isA(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> results = sqlCheckService.check(session, req);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckEnvironmentWithValidInputs() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = SqlCheckTestData.getValidSqls();
        ConnectionConfig config = SqlCheckTestData.getValidConfig();

        Environment env = Environment.builder().build();
        when(environmentService.detail(environmentId)).thenReturn(env);

        List<Rule> rules = SqlCheckTestData.getValidRules();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true, false);
        when(factory.getDataSource()).thenReturn(Mockito.mock(SingleConnectionDataSource.class));

        List<SqlCheckRule> checkRules = sqlCheckService.getRules(rules, config.getDialectType(), jdbcTemplate);

        List<CheckViolation> violations = new ArrayList<>();
        for (OffsetString sql : sqls) {
            List<CheckViolation> tempViolations = checkRules.stream()
                    .map(rule -> rule.check(sql.getScriptContent()))
                    .filter(violation -> violation != null)
                    .collect(Collectors.toList());
            violations.addAll(tempViolations);
        }

        List<CheckResult> results = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(sqls.size(), results.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckEnvironmentWithNullEnvironmentId() {
        String databaseName = "test_db";
        List<OffsetString> sqls = SqlCheckTestData.getValidSqls();
        ConnectionConfig config = SqlCheckTestData.getValidConfig();

        sqlCheckService.check(null, databaseName, sqls, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckEnvironmentWithNullDatabaseName() {
        Long environmentId = 1L;
        List<OffsetString> sqls = SqlCheckTestData.getValidSqls();
        ConnectionConfig config = SqlCheckTestData.getValidConfig();

        sqlCheckService.check(environmentId, null, sqls, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckEnvironmentWithEmptySqls() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        ConnectionConfig config = SqlCheckTestData.getValidConfig();

        sqlCheckService.check(environmentId, databaseName, Collections.emptyList(), config);
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        List<Rule> rules = Collections.emptyList();
        List<SqlCheckRule> checkRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);

        assertTrue(checkRules.isEmpty());
    }

    @Test
    public void testGetRulesWithValidInputs() {
        List<Rule> rules = SqlCheckTestData.getValidRules();
        List<SqlCheckRule> checkRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);

        assertFalse(checkRules.isEmpty());
        assertEquals(rules.size(), checkRules.size());
    }

    @Test
    public void testFullFillRiskLevelWithEmptyViolations() {
        List<CheckViolation> violations = Collections.emptyList();
        List<Rule> rules = SqlCheckTestData.getValidRules();

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFullFillRiskLevelWithValidInputs() {
        List<CheckViolation> violations = new ArrayList<>();
        List<Rule> rules = SqlCheckTestData.getValidRules();

        String name = "${TestRule}";
        CheckViolation violation = new CheckViolation();
        violation.setType(RuleType.TEST);
        violation.setLevel(1);
        violation.setMessage("Test violation message");
        violations.add(violation);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(1, result.size());
        Rule rule = result.get(0);
        assertEquals(RuleType.TEST, rule.getType());
        assertEquals(1, rule.getLevel());
    }
}