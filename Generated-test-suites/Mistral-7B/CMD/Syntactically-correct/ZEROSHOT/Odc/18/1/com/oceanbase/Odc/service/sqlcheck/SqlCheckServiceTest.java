package com.oceanbase.odc.service.sqlcheck.test;

import static org.junit.Assert.assertEquals;
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
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.DefaultSqlChecker;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.test.util.TestHelper;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;

/**
 * {@link SqlCheckService} Test
 *
 * @author yh263208
 * @since ODC_release_4.1.0
 */
@Slf4j
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SingleConnectionDataSource dataSource;

    @Mock
    private ConnectionSession session;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        when(jdbcTemplate.queryForList(StringUtils.EMPTY, Objects.array())).thenReturn(Collections.emptyList());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(TestHelper.createRules());
        when(environmentService.detail(anyLong())).thenReturn(TestHelper.createEnvironment());
    }

    @Test
    public void testCheck() {
        SqlCheckReq req = TestHelper.createCheckReq();
        ConnectionSession session = TestHelper.createSession();
        ConnectionConfig config = TestHelper.createConnectionConfig();
        List<Rule> rules = TestHelper.createRules();

        when(environmentService.detail(anyLong())).thenReturn(TestHelper.createEnvironment());
        when(session.getRuleSetId()).thenReturn(1L);

        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(2, result.size());

        CheckResult result0 = result.get(0);
        assertEquals("rule1", result0.getRuleName());
        assertEquals(RuleType.SQL_CHECK, result0.getRuleType());
        assertEquals("query1", result0.getQueryId());
        assertEquals("query1", result0.getContent());
        assertEquals(1, result0.getViolations().size());

        CheckViolation violation0 = result0.getViolations().get(0);
        assertEquals(RuleViolation.INJECT_SQL, violation0.getType());
        assertEquals("query1", violation0.getQueryId());
        assertEquals("query1", violation0.getContent());
        assertEquals("1.0", violation0.getRiskLevel());

        CheckResult result1 = result.get(1);
        assertEquals("rule2", result1.getRuleName());
        assertEquals(RuleType.SQL_CHECK, result1.getRuleType());
        assertEquals("query2", result1.getQueryId());
        assertEquals("query2", result1.getContent());
        assertEquals(0, result1.getViolations().size());

        verify(ruleService).list(1L, any(QueryRuleMetadataParams.class));
        verify(environmentService).detail(anyLong());
        verify(jdbcTemplate, never()).query(anyString(), anyArray());
    }

    @Test
    public void testCheckWithDataSource() {
        SqlCheckReq req = TestHelper.createCheckReq();
        ConnectionConfig config = TestHelper.createConnectionConfig();
        List<OffsetString> sqls = Collections.singletonList(TestHelper.createOffsetString("query1"));

        Environment env = TestHelper.createEnvironment();
        List<Rule> rules = TestHelper.createRules();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, null, true);
        when(environmentService.detail(anyLong())).thenReturn(env);

        SqlChecker sqlChecker = mock(SqlChecker.class);
        DefaultSqlChecker defaultSqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, Collections.emptyList());
        when(sqlChecker).thenReturn(defaultSqlChecker);

        when(factory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);

        List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcTemplate);
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        for (Rule rule : rules) {
            sqlCheckRules.add(SqlCheckRules.createByRule(candidates, DialectType.MYSQL, rule));
        }

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation mockViolation = mock(CheckViolation.class);
        checkViolations.add(mockViolation);
        when(defaultSqlChecker.check(anyList(), any(SqlCheckContext.class))).thenReturn(checkViolations);

        List<CheckViolation> result = sqlCheckService.check(env.getId(), env.getName(), sqls, config);
        assertEquals(1, result.size());

        CheckViolation violation = result.get(0);
        assertEquals(RuleViolation.INJECT_SQL, violation.getType());
        assertEquals("query1", violation.getQueryId());
        assertEquals("query1", violation.getContent());
        assertEquals("1.0", violation.getRiskLevel());

        verify(environmentService).detail(anyLong());
        verify(factory).getDataSource();
        verify(jdbcTemplate).query(anyString(), anyArray());
        verify(sqlChecker).check(anyList(), any(SqlCheckContext.class));
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = TestHelper.createRules();
        ConnectionSession session = TestHelper.createSession();
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbcOperations = jdbcTemplate;

        List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(dialectType, jdbcOperations);
        List<SqlCheckRule> sqlCheckRules = getRules(rules, dialectType, jdbcOperations);

        assertEquals(2, sqlCheckRules.size());

        SqlCheckRuleFactory factory1 = SqlCheckRules.getFactoryByName(candidates, RuleType.SQL_CHECK, "rule1");
        SqlCheckRuleFactory factory2 = SqlCheckRules.getFactoryByName(candidates, RuleType.SQL_CHECK, "rule2");
        assertEquals(factory1, sqlCheckRules.get(0).getFactory());
        assertEquals(factory2, sqlCheckRules.get(1).getFactory());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = TestHelper.createRules();
        List<CheckViolation> violations = new ArrayList<>();

        Rule rule1 = new Rule();
        rule1.setMetadata(TestHelper.createRuleMetadata("rule1"));
        violations.add(new CheckViolation(RuleViolation.INJECT_SQL, "query1"));

        Rule rule2 = new Rule();
        rule2.setMetadata(TestHelper.createRuleMetadata("rule2"));
        violations.add(new CheckViolation(RuleViolation.INJECT_SQL, "query2"));

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(2, result.size());

        Rule violatedRule1 = result.get(0);
        assertEquals("rule1", violatedRule1.getMetadata().getName());
        assertEquals("1.0", violatedRule1.getLevel());

        Rule violatedRule2 = result.get(1);
        assertEquals("rule2", violatedRule2.getMetadata().getName());
        assertEquals("1.0", violatedRule2.getLevel());
    }
}