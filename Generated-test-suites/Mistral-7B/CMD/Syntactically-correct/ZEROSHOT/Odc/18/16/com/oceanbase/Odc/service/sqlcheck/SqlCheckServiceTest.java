package com.oceanbase.odc.service.sqlcheck.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

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
import com.oceanbase.odc.core.collaboration.Environment;
import com.oceanbase.odc.core.collaboration.EnvironmentService;
import com.oceanbase.odc.core.connection.ConnectionConfig;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleType;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

/**
 * {@link SqlCheckService} Test
 *
 * @author yh263208
 * @date 2023-03-15 11:01
 * @since ODC_release_4.1.0
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private OBConsoleDataSourceFactory factory;

    @Mock
    private SingleConnectionDataSource dataSource;

    @Mock
    private ConnectionSession session;

    @Mock
    private ConnectionConfig config;

    private List<Rule> rules;
    private List<SqlCheckRule> sqlCheckRules;
    private List<CheckViolation> checkViolations;
    private CheckResult checkResult;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        rules = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("rule1");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        RuleViolation violation1 = new RuleViolation();
        violation1.setType("violation1");
        violation1.setMessage("violation1 message");
        rule1.setViolation(violation1);
        rules.add(rule1);
        Rule rule2 = new Rule();
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("rule2");
        metadata2.setType(RuleType.SQL_CHECK);
        rule2.setMetadata(metadata2);
        RuleViolation violation2 = new RuleViolation();
        violation2.setType("violation2");
        violation2.setMessage("violation2 message");
        rule2.setViolation(violation2);
        rules.add(rule2);
        sqlCheckRules = new ArrayList<>();
        sqlCheckRules.add(mock(SqlCheckRule.class));
        sqlCheckRules.add(mock(SqlCheckRule.class));
        checkViolations = new ArrayList<>();
        checkViolations.add(new CheckViolation("violation1", "violation1 message", RuleType.SQL_CHECK, null));
        checkViolations.add(new CheckViolation("violation2", "violation2 message", RuleType.SQL_CHECK, null));
        checkResult = new CheckResult();
        checkResult.setCheckViolations(checkViolations);
    }

    @Test
    public void testCheck() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        when(getRules(rules, eq(DialectType.MYSQL), eq(jdbcTemplate))).thenReturn(sqlCheckRules);
        when(jdbcTemplate.execute(anyString(), anyArray())).thenReturn(null);
        when(factory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(session.getRuleSetId()).thenReturn(1L);

        List<CheckResult> results = sqlCheckService.check(session, new SqlCheckReq("scriptContent", null));
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getCheckViolations());
        assertEquals(2, results.get(0).getCheckViolations().size());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(session, times(1)).getDialectType();
        verify(session, times(1)).getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY);
        verify(sqlCheckService, times(1)).getRules(rules, eq(DialectType.MYSQL), eq(jdbcTemplate));
        verify(jdbcTemplate, times(1)).execute(anyString(), anyArray());
        verify(factory, times(1)).getDataSource();
        verify(dataSource, times(1)).getJdbcTemplate();
        verify(environmentService, times(1)).detail(anyLong());
    }

    @Test
    public void testCheckWithEnvironment() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(config.getDialectType()).thenReturn(DialectType.MYSQL);
        when(factory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);

        List<CheckViolation> violations = sqlCheckService.check(1L, "databaseName", sqls(), config).getCheckViolations();
        assertEquals(2, violations.size());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(config, times(1)).getDialectType();
        verify(factory, times(1)).getDataSource();
        verify(dataSource, times(1)).getJdbcTemplate();
    }

    private List<OffsetString> sqls() {
        OffsetString sql1 = new OffsetString("SELECT * FROM table1 WHERE 1 = 1;");
        OffsetString sql2 = new OffsetString("SELECT * FROM table2 WHERE 1 = 1;");
        return Collections.singletonList(sql1).stream().concat(Collections.singletonList(sql2).stream()).collect(Collectors.toList());
    }

    @Test
    public void testGetRules() {
        when(ruleService.list(anyList(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(config.getDialectType()).thenReturn(DialectType.MYSQL);
        when(jdbcTemplate.queryForList(anyString(), anyArray())).thenReturn(Collections.emptyList());

        List<SqlCheckRule> rules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);
        assertEquals(2, rules.size());

        verify(ruleService, times(1)).list(anyList(), any(QueryRuleMetadataParams.class));
        verify(config, times(1)).getDialectType();
        verify(jdbcTemplate, times(1)).queryForList(anyString(), anyArray());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = new ArrayList<>();
        String ruleName1 = "${rule1}";
        String ruleName2 = "${rule2}";
        CheckViolation violation1 = new CheckViolation("violation1", "violation1 message", RuleType.SQL_CHECK, null);
        violation1.setType(ruleName1);
        CheckViolation violation2 = new CheckViolation("violation2", "violation2 message", RuleType.SQL_CHECK, null);
        violation2.setType(ruleName2);
        violations.add(violation1);
        violations.add(violation2);

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("rule1");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        rule1.setLevel(2);
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("rule2");
        metadata2.setType(RuleType.SQL_CHECK);
        rule2.setMetadata(metadata2);
        rule2.setLevel(4);
        rules.add(rule1);
        rules.add(rule2);

        sqlCheckService.fullFillRiskLevel(rules, violations);
        Rule rule1 = rules.get(0);
        Rule rule2 = rules.get(1);
        assertEquals(2, rule1.getViolation().getLevel());
        assertEquals(4, rule2.getViolation().getLevel());
    }
}