package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
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
import com.oceanbase.odc.service.regulation.ruleset.Model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRulesFactory;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckContext;

@Component
public class SqlCheckServiceTest {

    private RuleService ruleService;
    private EnvironmentService environmentService;
    private SqlCheckService sqlCheckService;
    private JdbcOperations jdbcOperations;
    private ConnectionSession connectionSession;
    private List<Rule> rules;

    @Before
    public void setUp() {
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
        SqlCheckRules sqlCheckRules = new SqlCheckRules();
        SqlCheckRulesFactory sqlCheckRulesFactory = new SqlCheckRulesFactory();
        sqlCheckRulesFactory.registerRules(sqlCheckRules);
        SqlCheckRules sqlCheckRulesInstance = Mockito.spy(sqlCheckRules);
        sqlCheckService.setSqlCheckRulesFactory(sqlCheckRulesFactory);
        sqlCheckService.setSqlCheckRules(sqlCheckRulesInstance);
        jdbcOperations = new JdbcTemplate(new JdbcTemplate().getDataSource());
        connectionSession = Mockito.mock(ConnectionSession.class);

        Rule rule1 = Mockito.mock(Rule.class);
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("rule1");
        metadata1.setType(RuleType.SQL_CHECK);
        when(rule1.getMetadata()).thenReturn(metadata1);
        Rule rule2 = Mockito.mock(Rule.class);
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("rule2");
        metadata2.setType(RuleType.SQL_CHECK);
        when(rule2.getMetadata()).thenReturn(metadata2);
        rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);

        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
    }

    @Test
    public void testCheck() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        CheckResult result = sqlCheckService.check(connectionSession, new SqlCheckReq());
        assertEquals(Collections.emptyList(), result.getCheckViolations());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        assertEquals(Collections.emptyList(), sqlCheckRules);
    }

    @Test
    public void testCheckWithViolations() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(Mockito.mock(RuleType.class));
        violation1.setMessage("Violation 1");
        violation1.setLevel(1);
        checkViolations.add(violation1);

        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(jdbcOperations.queryForList(anyString(), anyArray())).thenReturn(Collections.emptyList());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        assertEquals(2, sqlCheckRules.size());

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, sqlCheckRules);
        List<CheckViolation> violations = sqlChecker.check(Collections.singletonList("test script"));
        sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(1, violation1.getLevel());

        CheckResult result = sqlCheckService.check(connectionSession, new SqlCheckReq());
        assertEquals(checkViolations, result.getCheckViolations());
    }

    @Test
    public void testCheckWithEmptySqls() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(Mockito.mock(RuleType.class));
        violation1.setMessage("Violation 1");
        violation1.setLevel(1);
        checkViolations.add(violation1);

        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(jdbcOperations.queryForList(anyString(), anyArray())).thenReturn(Collections.emptyList());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        assertEquals(2, sqlCheckRules.size());

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, sqlCheckRules);
        List<CheckViolation> violations = sqlChecker.check(Collections.emptyList());

        assertEquals(0, violations.size());

        CheckResult result = sqlCheckService.check(connectionSession, new SqlCheckReq());
        assertEquals(Collections.emptyList(), result.getCheckViolations());
    }

    @Test
    public void testCheckWithEmptyRules() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(Collections.emptyList());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), connectionSession);
        assertEquals(0, sqlCheckRules.size());

        CheckResult result = sqlCheckService.check(connectionSession, new SqlCheckReq());
        assertEquals(Collections.emptyList(), result.getCheckViolations());
    }
}