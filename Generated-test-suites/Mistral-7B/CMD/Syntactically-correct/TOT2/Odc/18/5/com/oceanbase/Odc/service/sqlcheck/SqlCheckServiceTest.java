package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

@Slf4j
@Service
@Validated
@SkipAuthorize("inside connect session")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(jdbcTemplate.queryForList(anyString(), anyObject[].class)).thenReturn(Collections.emptyList());
    }

    @Test
    public void testCheck() {
        // Given
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        Long ruleSetId = 1L;
        List<Rule> rules = Collections.emptyList();
        when(ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(rules);

        List<SqlCheckRuleFactory> sqlCheckRuleFactories = SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcTemplate);
        List<Rule> violatedRules = new ArrayList<>();

        // When
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Then
        assertEquals(0, checkResults.size());

        // Test with empty rules
        sqlCheckService = new SqlCheckService();
        CheckResult result = sqlCheckService.check(session, req).get(0);
        assertEquals(RuleViolation.NONE, result.getViolations().get(0).getViolationType());

        // Test with valid rules
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setLevel(1);
        rule.setEnabled(true);

        SqlCheckRuleFactory sqlCheckRuleFactory = SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcTemplate)
                .get(0);
        SqlCheckRule sqlCheckRule = sqlCheckRuleFactory.create(rule);
        when(ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.singletonList(rule));

        List<CheckViolation> checkViolations = new ArrayList<>();
        RuleViolation violation = new RuleViolation();
        violation.setMessage("Test violation message");
        violation.setType(new RuleViolation.Type("TestRuleViolationType"));
        violation.setLineNumber(1L);
        violation.setColumnName("TestColumnName");
        violation.setRiskLevel(1);
        checkViolations.add(violation);

        when(jdbcTemplate.queryForList(anyString(), anyObject[].class)).thenReturn(checkViolations);

        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(1, results.size());
        CheckResult result1 = results.get(0);
        assertEquals(1, result1.getViolations().size());
        assertEquals("Test violation message", result1.getViolations().get(0).getMessage());

        RuleViolation violation1 = result1.getViolations().get(0);
        assertEquals("TestRuleViolationType", violation1.getViolationType().getName());
        assertEquals(1, violation1.getLineNumber());
        assertEquals("TestColumnName", violation1.getColumnName());
        assertEquals(1, violation1.getRiskLevel());

        // Test with multiple violations
        List<CheckViolation> multipleViolations = new ArrayList<>();
        violation = new RuleViolation();
        violation.setMessage("Test violation message 1");
        violation.setType(new RuleViolation.Type("TestRuleViolationType1"));
        violation.setLineNumber(1L);
        violation.setColumnName("TestColumnName1");
        violation.setRiskLevel(1);
        multipleViolations.add(violation);

        violation = new RuleViolation();
        violation.setMessage("Test violation message 2");
        violation.setType(new RuleViolation.Type("TestRuleViolationType2"));
        violation.setLineNumber(2L);
        violation.setColumnName("TestColumnName2");
        violation.setRiskLevel(2);
        multipleViolations.add(violation);

        when(jdbcTemplate.queryForList(anyString(), anyObject[].class)).thenReturn(multipleViolations);

        results = sqlCheckService.check(session, req);
        assertEquals(1, results.size());
        result1 = results.get(0);
        assertEquals(2, result1.getViolations().size());

        violation1 = result1.getViolations().get(0);
        assertEquals("TestRuleViolationType1", violation1.getViolationType().getName());
        assertEquals(1, violation1.getLineNumber());
        assertEquals("TestColumnName1", violation1.getColumnName());
        assertEquals(1, violation1.getRiskLevel());

        violation = result1.getViolations().get(1);
        assertEquals("TestRuleViolationType2", violation.getViolationType().getName());
        assertEquals(2, violation.getLineNumber());
        assertEquals("TestColumnName2", violation.getColumnName());
        assertEquals(2, violation.getRiskLevel());

        // Test with no violations
        when(jdbcTemplate.queryForList(anyString(), anyObject[].class)).thenReturn(Collections.emptyList());

        results = sqlCheckService.check(session, req);
        assertEquals(1, results.size());
        result1 = results.get(0);
        assertEquals(0, result1.getViolations().size());

        // Test check with environment
        ConnectionConfig config = new ConnectionConfig();
        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT 1 FROM test;"));

        List<CheckViolation> environmentCheckViolations = new ArrayList<>();
        violation = new RuleViolation();
        violation.setMessage("Test violation message in environment");
        violation.setType(new RuleViolation.Type("TestRuleViolationType"));
        violation.setLineNumber(1L);
        violation.setColumnName("TestColumnName");
        violation.setRiskLevel(1);
        environmentCheckViolations.add(violation);

        when(jdbcTemplate.queryForList(anyString(), anyObject[].class)).thenReturn(environmentCheckViolations);

        List<CheckResult> environmentCheckResults = sqlCheckService.check(1L, "test", sqls, config);
        assertEquals(1, environmentCheckResults.size());
        result1 = environmentCheckResults.get(0);
        assertEquals(1, result1.getViolations().size());
        assertEquals("Test violation message in environment", result1.getViolations().get(0).getMessage());
        assertEquals("TestRuleViolationType", result1.getViolations().get(0).getViolationType().getName());
        assertEquals(1, result1.getViolations().get(0).getLineNumber());
        assertEquals("TestColumnName", result1.getViolations().get(0).getColumnName());
        assertEquals(1, result1.getViolations().get(0).getRiskLevel());

        // Test check with empty sqls
        sqls = Collections.emptyList();

        List<CheckResult> emptySqlCheckResults = sqlCheckService.check(1L, "test", sqls, config);
        assertEquals(0, emptySqlCheckResults.size());

        // Test getRules
        List<Rule> rules1 = sqlCheckService.getRules(rules, session);
        assertEquals(0, rules1.size());

        Rule rule1 = new Rule();
        rule1.setMetadata(metadata);
        rule1.setLevel(1);
        rule1.setEnabled(true);

        List<Rule> rules2 = sqlCheckService.getRules(Collections.singletonList(rule1), session);
        assertEquals(1, rules2.size());
        assertEquals(rule1, rules2.get(0));

        // Test getRules with dialectType and jdbc
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);
        assertEquals(0, sqlCheckRules.size());

        Rule rule2 = new Rule();
        rule2.setMetadata(metadata);
        rule2.setLevel(1);
        rule2.setEnabled(true);

        List<SqlCheckRule> sqlCheckRules1 = sqlCheckService.getRules(Collections.singletonList(rule2), DialectType.MYSQL, jdbcTemplate);
        assertEquals(1, sqlCheckRules1.size());
        assertEquals(SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcTemplate).get(0).create(rule2), sqlCheckRules1.get(0));

        // Test fullFillRiskLevel
        List<CheckViolation> checkViolations1 = new ArrayList<>();
        violation = new RuleViolation();
        violation.setMessage("Test violation message 1");
        violation.setType(new RuleViolation.Type("TestRuleViolationType1"));
        violation.setLineNumber(1L);
        violation.setColumnName("TestColumnName1");
        violation.setRiskLevel(1);
        checkViolations1.add(violation);

        List<CheckViolation> checkViolations2 = new ArrayList<>();
        violation = new RuleViolation();
        violation.setMessage("Test violation message 2");
        violation.setType(new RuleViolation.Type("TestRuleViolationType2"));
        violation.setLineNumber(2L);
        violation.setColumnName("TestColumnName2");
        violation.setRiskLevel(2);
        checkViolations2.add(violation);

        List<Rule> rules3 = new ArrayList<>();
        rule1 = new Rule();
        rule1.setMetadata(metadata);
        rule1.setLevel(1);
        rule1.setEnabled(true);
        rule1.setViolation(RuleViolation.fromCheckViolation(violation));
        rules3.add(rule1);

        rule2 = new Rule();
        rule2.setMetadata(metadata);
        rule2.setLevel(1);
        rule2.setEnabled(true);
        rule2.setViolation(RuleViolation.fromCheckViolation(violation));
        rules3.add(rule2);

        List<Rule> violatedRules1 = sqlCheckService.fullFillRiskLevel(rules3, checkViolations1);
        assertEquals(2, violatedRules1.size());
        assertEquals(rule1, violatedRules1.get(0));
        assertEquals(rule2, violatedRules1.get(1));

        List<Rule> violatedRules2 = sqlCheckService.fullFillRiskLevel(rules3, checkViolations2);
        assertEquals(2, violatedRules2.size());
        assertEquals(rule1, violatedRules2.get(0));
        assertEquals(rule2, violatedRules2.get(1));

        // Test fullFillRiskLevel with empty checkViolations
        List<Rule> violatedRules3 = sqlCheckService.fullFillRiskLevel(rules3, Collections.emptyList());
        assertEquals(2, violatedRules3.size());
        assertEquals(rule1, violatedRules3.get(0));
        assertEquals(rule2, violatedRules3.get(1));

        // Test buildCheckResults
        List<CheckViolation> checkViolations3 = new ArrayList<>();
        violation = new RuleViolation();
        violation.setMessage("Test violation message 3");
        violation.setType(new RuleViolation.Type("TestRuleViolationType3"));
        violation.setLineNumber(1L);
        violation.setColumnName("TestColumnName3");
        violation.setRiskLevel(1);
        violation.setRuleId(1L);
        checkViolations3.add(violation);

        CheckResult checkResult = new CheckResult();
        checkResult.setViolations(checkViolations3);

        List<CheckResult> checkResults1 = SqlCheckUtil.buildCheckResults(checkViolations3);
        assertEquals(1, checkResults1.size());
        assertEquals(checkResult, checkResults1.get(0));

        List<CheckResult> checkResults2 = SqlCheckUtil.buildCheckResults(Collections.emptyList());
        assertEquals(0, checkResults2.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullSession() {
        sqlCheckService.check(null, req);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullReq() {
        sqlCheckService.check(session, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullRuleSetId() {
        sqlCheckService.check(session, req);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenThrow(new IllegalArgumentException("Null ruleSetId"));
        sqlCheckService.check(session, req);
    }
}