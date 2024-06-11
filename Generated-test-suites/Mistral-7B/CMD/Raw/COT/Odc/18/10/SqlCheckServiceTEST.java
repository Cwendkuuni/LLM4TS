package com.oceanbase.odc.service.sqlcheck.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
import com.oceanbase.odc.service.regulation.ruleset.Model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckContext;

@RunWith(MockitoJUnitRunner.class)
@Validated
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private ConnectionSession connectionSession;
    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;
    @Mock
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Test
    public void testCheck() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MySQL);

        Rule rule = new Rule();
        rule.setLevel(1);
        rule.setEnabled(true);
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setType(RuleType.SQL_CHECK);
        rule.getMetadata().setName("testrule");

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Arrays.asList(rule));
        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
        when(SqlCheckRules.createByRule(anyList(), DialectType.MySQL, any(Rule.class))).thenReturn(sqlCheckRule);
        when(jdbcTemplate.query(anyString(), any(Object[].class))).thenReturn(new Object[0]);

        when(obConsoleDataSourceFactory.getDataSource()).thenReturn(singleConnectionDataSource);
        when(singleConnectionDataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(new RuleType("testrule"));
        violation.setMessage("test violation");
        violation.setRiskLevel(1);
        checkViolations.add(violation);

        List<SqlCheckRule> sqlCheckRules = Arrays.asList(sqlCheckRule);
        when(sqlCheckService.getRules(anyList(), DialectType.MySQL, any(JdbcOperations.class))).thenReturn(sqlCheckRules);

        SqlCheckContext checkContext = new SqlCheckContext(1);

        String scriptContent = "select * from test where id = 1";
        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, new SqlCheckReq(scriptContent));
        assertEquals(1, checkResults.size());
        assertEquals(violation.getMessage(), checkResults.get(0).getViolations().get(0).getMessage());
        assertEquals(violation.getRiskLevel(), checkResults.get(0).getViolations().get(0).getRiskLevel());
    }

    @Test
    public void testCheckWithEmptyRules() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MySQL);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, new SqlCheckReq(""));
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEmptySqls() {
        Long environmentId = 1L;
        String databaseName = "test";
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MySQL);

        List<OffsetString> sqls = Collections.emptyList();

        Environment env = new Environment();
        env.setRulesetId(1L);

        when(environmentService.detail(environmentId)).thenReturn(env);

        List<Rule> rules = Arrays.asList(new Rule());
        when(ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build())).thenReturn(rules);

        when(obConsoleDataSourceFactory.getDataSource()).thenReturn(singleConnectionDataSource);
        when(singleConnectionDataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = Arrays.asList(new Rule());
        DialectType dialectType = DialectType.MySQL;
        JdbcOperations jdbc = mock(JdbcOperations.class);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);
        assertEquals(1, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        JdbcOperations jdbc = mock(JdbcOperations.class);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), DialectType.MySQL, jdbc);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFullFillRiskLevelWithNullRules() {
        List<CheckViolation> violations = new ArrayList<>();
        sqlCheckService.fullFillRiskLevel(null, violations);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFullFillRiskLevelWithNullViolations() {
        List<Rule> rules = new ArrayList<>();
        sqlCheckService.fullFillRiskLevel(rules, null);
    }
}