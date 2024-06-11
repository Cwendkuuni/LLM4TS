package com.oceanbase.odc.service.sqlcheck.test;

import static org.junit.Assert.*;

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
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
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
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;
    private ConnectionConfig config;
    private Environment env;
    private List<Rule> rules;
    private JdbcOperations jdbc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.session = Mockito.mock(ConnectionSession.class);
        this.config = Mockito.spy(new ConnectionConfig());
        this.env = Mockito.mock(Environment.class);
        this.rules = new ArrayList<>();
        this.jdbc = new JdbcTemplate(Mockito.mock(SingleConnectionDataSource.class));
    }

    @Test
    public void testCheck() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRule");
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        this.rules.add(rule);

        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> results = sqlCheckService.check(this.session, req);
        assertTrue(results.isEmpty());

        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("testRule2");
        Rule rule2 = new Rule();
        rule2.setMetadata(metadata2);
        rule2.setEnabled(true);
        rule2.setLevel(2);
        this.rules.add(rule2);

        CheckViolation violation = new CheckViolation();
        violation.setType(RuleType.SQL_INJECTION);
        violation.setMessage("sql injection");

        SqlCheckRule sqlCheckRule = Mockito.mock(SqlCheckRule.class);
        when(sqlCheckRule.check(Mockito.anyString())).thenReturn(Collections.singletonList(violation));

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        sqlCheckRules.add(sqlCheckRule);

        when(this.ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(this.rules);
        when(this.ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(this.rules);
        when(this.ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(this.rules);
        when(this.ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(this.rules);
        when(this.ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(this.rules);

        when(this.ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenAnswer(i -> {
            List<Rule> list = new ArrayList<>();
            list.add(rule);
            list.add(rule2);
            return list;
        });

        when(this.jdbc.queryForList(Mockito.anyString(), Mockito.anyObject[].class)).thenReturn(Collections.emptyList());

        when(SqlCheckRules.getAllFactories(Mockito.any(DialectType.class), Mockito.any(JdbcOperations.class)))
                .thenReturn(Collections.singletonList(Mockito.mock(SqlCheckRuleFactory.class)));

        when(SqlCheckRules.createByRule(Mockito.anyList(), Mockito.any(DialectType.class), Mockito.any(Rule.class)))
                .thenReturn(sqlCheckRule);

        List<CheckResult> results2 = sqlCheckService.check(this.session, req);
        List<CheckViolation> violations = results2.get(0).getViolations();
        assertEquals(1, violations.size());
        assertEquals(RuleViolation.SQL_INJECTION, violations.get(0).getType());
        assertEquals(2, violations.get(0).getLevel());

        List<CheckResult> results3 = sqlCheckService.check(this.session, req);
        List<CheckViolation> violations3 = results3.get(0).getViolations();
        assertTrue(violations3.isEmpty());

        List<CheckResult> results4 = sqlCheckService.check(this.session, req);
        List<CheckViolation> violations4 = results4.get(0).getViolations();
        assertTrue(violations4.isEmpty());
    }

    @Test
    public void testCheckWithEnvironment() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRule");
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rule.setLevel(2);
        this.rules.add(rule);

        CheckViolation violation = new CheckViolation();
        violation.setType(RuleType.SQL_INJECTION);
        violation.setMessage("sql injection");

        SqlCheckRule sqlCheckRule = Mockito.mock(SqlCheckRule.class);
        when(sqlCheckRule.check(Mockito.anyString())).thenReturn(Collections.singletonList(violation));

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        sqlCheckRules.add(sqlCheckRule);

        when(this.environmentService.detail(Mockito.anyLong())).thenReturn(this.env);
        when(this.env.getRulesetId()).thenReturn(1L);

        when(SqlCheckRules.getAllFactories(Mockito.any(DialectType.class), Mockito.any(JdbcOperations.class)))
                .thenReturn(Collections.singletonList(Mockito.mock(SqlCheckRuleFactory.class)));

        when(SqlCheckRules.createByRule(Mockito.anyList(), Mockito.any(DialectType.class), Mockito.any(Rule.class)))
                .thenReturn(sqlCheckRule);

        when(this.jdbc.queryForList(Mockito.anyString(), Mockito.anyObject[].class)).thenReturn(Collections.emptyList());

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "test", Arrays.asList(new OffsetString("test")),
                this.config);
        assertEquals(1, checkViolations.size());
        assertEquals(RuleViolation.SQL_INJECTION, checkViolations.get(0).getType());
        assertEquals(2, checkViolations.get(0).getLevel());
    }

    @Test
    public void testGetRules() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRule");
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rule.setLevel(2);
        this.rules.add(rule);

        DialectType dialectType = DialectType.MYSQL;

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        when(this.ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(this.rules);
        when(this.ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(this.rules);
        when(this.ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(this.rules);
        when(this.ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(this.rules);

        List<SqlCheckRule> rules1 = sqlCheckService.getRules(this.rules, dialectType, this.jdbc);
        assertEquals(1, rules1.size());

        List<SqlCheckRule> rules2 = sqlCheckService.getRules(this.rules, dialectType, this.jdbc);
        assertEquals(1, rules2.size());

        List<SqlCheckRule> rules3 = sqlCheckService.getRules(this.rules, dialectType, this.jdbc);
        assertEquals(1, rules3.size());

        List<SqlCheckRule> rules4 = sqlCheckService.getRules(Collections.emptyList(), dialectType, this.jdbc);
        assertTrue(rules4.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRule");
        metadata.setLevel(1);
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setViolation(new RuleViolation());

        CheckViolation violation = new CheckViolation();
        violation.setType(RuleType.SQL_INJECTION);
        violation.setMessage("sql injection");
        violation.setLevel(2);

        when(this.ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(this.rules);

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(this.rules, Collections.singletonList(violation));
        assertEquals(1, violatedRules.size());
        assertEquals(2, violatedRules.get(0).getLevel());
    }
}