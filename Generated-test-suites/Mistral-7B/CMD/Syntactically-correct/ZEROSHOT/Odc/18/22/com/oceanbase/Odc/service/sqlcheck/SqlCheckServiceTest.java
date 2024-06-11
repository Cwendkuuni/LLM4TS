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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.rule.DefaultSqlChecker;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

/**
 * Test for {@link SqlCheckService}
 *
 * @author yh263208
 */
@Slf4j
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;
    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcOperations jdbcOperations;
    @Mock
    private ConnectionSession connectionSession;
    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;
    @Mock
    private Environment environment;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    private List<Rule> rules;
    private List<SqlCheckRuleFactory> sqlCheckRuleFactories;
    private List<SqlCheckRule> sqlCheckRules;

    @Before
    public void setUp() {
        rules = new ArrayList<>();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("rule1");
        metadata1.setType(RuleType.SQL_CHECK);
        Rule rule1 = new Rule();
        rule1.setMetadata(metadata1);
        rule1.setEnabled(true);
        rules.add(rule1);

        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("rule2");
        metadata2.setType(RuleType.SQL_CHECK);
        Rule rule2 = new Rule();
        rule2.setMetadata(metadata2);
        rule2.setEnabled(false);
        rules.add(rule2);

        sqlCheckRuleFactories = SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcOperations);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(obConsoleDataSourceFactory.getDataSource()).thenReturn(singleConnectionDataSource);
        when(singleConnectionDataSource.getJdbcTemplate()).thenReturn(jdbcOperations);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(jdbcOperations);
    }

    @Test
    public void testCheck() {
        CheckResult expectedResult = new CheckResult();
        expectedResult.setRuleName("rule1");
        expectedResult.setRuleViolation(CheckViolation.fromRuleViolation(RuleViolation.SQL_SYNTAX_ERROR));
        expectedResult.setRiskLevel(3);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        when(jdbcOperations.query(anyString(), anyArray()))
                .thenReturn(Collections.emptyList().toArray(new Object[0]));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        CheckResult result = sqlCheckService.check(connectionSession, new SqlCheckReq());

        assertEquals(expectedResult, result);
    }

    @Test
    public void testCheckWithEnvironmentIds() {
        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setRuleName("rule1");
        violation1.setType(RuleViolation.SQL_SYNTAX_ERROR);
        violation1.setPosition(new int[2]);
        violation1.setMessage("message1");

        CheckViolation violation2 = new CheckViolation();
        violation2.setRuleName("rule2");
        violation2.setType(RuleViolation.SQL_SYNTAX_ERROR);
        violation2.setPosition(new int[2]);
        violation2.setMessage("message2");

        checkViolations.add(violation1);
        checkViolations.add(violation2);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(environment.getRulesetId()).thenReturn(1L);

        when(obConsoleDataSourceFactory.getDataSource()).thenReturn(singleConnectionDataSource);
        when(singleConnectionDataSource.getJdbcTemplate()).thenReturn(jdbcOperations);

        when(jdbcOperations.query(anyString(), anyArray()))
                .thenReturn(Collections.emptyList().toArray(new Object[0]));

        when(jdbcOperations.query(anyString(), anyArray())).thenReturn(new Object[0]);

        when(jdbcOperations.query(anyString(), anyArray()))
                .thenReturn(new Object[] { "expectedValue" });

        List<CheckViolation> result = sqlCheckService.check(1L, "databaseName", Collections.singletonList(new OffsetString("select * from test")), connectionConfig);

        assertEquals(checkViolations, result);
    }

    @Test
    public void testGetRules() {
        List<SqlCheckRuleFactory> sqlCheckRuleFactories = new ArrayList<>();
        SqlCheckRuleFactory factory1 = mock(SqlCheckRuleFactory.class);
        sqlCheckRuleFactories.add(factory1);
        SqlCheckRuleFactory factory2 = mock(SqlCheckRuleFactory.class);
        sqlCheckRuleFactories.add(factory2);

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(sqlCheckRules, sqlCheckRules);
    }

    @Test
    public void testGetRulesWithJdbcOperations() {
        List<SqlCheckRuleFactory> sqlCheckRuleFactories = new ArrayList<>();
        SqlCheckRuleFactory factory1 = mock(SqlCheckRuleFactory.class);
        sqlCheckRuleFactories.add(factory1);
        SqlCheckRuleFactory factory2 = mock(SqlCheckRuleFactory.class);
        sqlCheckRuleFactories.add(factory2);

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        when(jdbcOperations.query(anyString(), anyArray()))
                .thenReturn(Collections.emptyList().toArray(new Object[0]));

        sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbcOperations);

        assertEquals(sqlCheckRules, sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setRuleName("rule1");
        violation1.setType(RuleViolation.SQL_SYNTAX_ERROR);
        violation1.setPosition(new int[2]);
        violation1.setMessage("message1");

        CheckViolation violation2 = new CheckViolation();
        violation2.setRuleName("rule1");
        violation2.setType(RuleViolation.SQL_SYNTAX_ERROR);
        violation2.setPosition(new int[2]);
        violation2.setMessage("message2");

        checkViolations.add(violation1);
        checkViolations.add(violation2);

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setLevel(3);
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("rule1");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        rule1.setViolation(RuleViolation.fromCheckViolation(CheckViolation.fromRuleViolation(violation1)));
        rules.add(rule1);

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        assertEquals(rules.size(), violatedRules.size());
    }
}