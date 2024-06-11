package com.oceanbase.odc.service.sqlcheck.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.sqlcheck.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;

@Slf4j
public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        jdbcTemplate = mock(JdbcTemplate.class);
        sqlCheckService = new SqlCheckService() {
            @Autowired
            public SqlCheckService(RuleService ruleService, EnvironmentService environmentService,
                    JdbcTemplate jdbcTemplate) {
                super.ruleService = ruleService;
                super.environmentService = environmentService;
                super.jdbcOperations = jdbcTemplate;
            }
        };
        when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<>());
        when(environmentService.detail(Mockito.anyLong())).thenReturn(new Environment());
    }

    @Test
    public void testCheck() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getRuleSetId()).thenReturn(1L);
        SqlCheckReq req = new SqlCheckReq();
        CheckResult result = sqlCheckService.check(session, req);
        assertNotNull(result);
    }

    @Test
    public void testCheckWithEmptyRules() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getRuleSetId()).thenReturn(1L);
        SqlCheckReq req = new SqlCheckReq();
        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
        assertNotNull(sqlCheckRules.get(0));
    }

    @Test
    public void testCheckWithEmptySqls() {
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(checkViolations);
        assertNotNull(checkViolations.get(0));
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);
        assertNotNull(sqlCheckRules);
        assertNotNull(sqlCheckRules.get(0));
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);
        assertNotNull(sqlCheckRules);
        assertNotNull(sqlCheckRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        Rule rule = new Rule();
        rule.setLevel(1);
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setName("testRule");
        rules.add(rule);

        CheckViolation violation = new CheckViolation();
        violation.setType(new RuleType());
        violation.getType().setName("testType");
        CheckResult result = new CheckResult();
        result.setCheckViolations(Collections.singletonList(violation));

        sqlCheckService.fullFillRiskLevel(rules, Collections.singletonList(result));
        Rule ruleResult = rules.get(0);
        CheckViolation violationResult = ruleResult.getViolation();
        assertNotNull(violationResult);
        assertNotNull(violationResult.getLevel());
        assertEquals(violationResult.getLevel(), 1);
    }

    @Test
    public void testFullFillRiskLevelWithEmptyRules() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(rules);
        assertNotNull(rules.isEmpty());
    }
}