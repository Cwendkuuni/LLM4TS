package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Unit tests for {@link SqlCheckService}.
 */
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck_ValidScript() {
        // Given
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        // When
        List<CheckResult> results = sqlCheckService.check(session, req);

        // Then
        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService).detail(anyLong());
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheck_EmptyScript() {
        // Given
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        // When
        List<CheckResult> results = sqlCheckService.check(session, req);

        // Then
        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService).detail(anyLong());
        assertEquals(0, results.size());
    }

    @Test
    public void testCheck_InvalidScript() {
        // Given
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        // When
        String invalidScript = "";
        List<CheckResult> results = sqlCheckService.check(session, req);

        // Then
        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService).detail(anyLong());
        assertEquals(0, results.size());
        assertEquals("Invalid script content", exception.getMessage());
    }

    @Test
    public void testCheck_WithViolations() {
        // Given
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        RuleViolation violation = new RuleViolation();
        violation.setMessage("Violation message");
        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setType(new RuleType("TestRuleType"));
        checkViolation.setViolation(violation);
        List<CheckViolation> violations = Collections.singletonList(checkViolation);

        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("Rule2");
        metadata2.setType(RuleType.SQL_CHECK);
        Rule rule2 = new Rule();
        rule2.setMetadata(metadata2);
        rule2.setEnabled(true);
        RuleViolation violation2 = new RuleViolation();
        violation2.setMessage("Another violation message");
        CheckViolation checkViolation2 = new CheckViolation();
        checkViolation2.setType(new RuleType("TestRuleType2"));
        checkViolation2.setViolation(violation2);
        violations.add(checkViolation2);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        // When
        String script = "SELECT * FROM invalid_table";
        List<CheckResult> results = sqlCheckService.check(session, req);

        // Then
        verify(ruleService, times(2)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService).detail(anyLong());
        assertEquals(2, results.size());
        for (CheckResult result : results) {
            assertNotNull(result.getRuleName());
            assertNotNull(result.getViolation());
        }
    }

    @Test
    public void testCheck_WithEnvironment() {
        // Given
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);
        long envId = 1L;
        Environment env = new Environment();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(environmentService.detail(envId)).thenReturn(env);

        // When
        String script = "SELECT * FROM valid_table";
        List<CheckResult> results = sqlCheckService.check(session, req);

        // Then
        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService).detail(envId);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheck_WithConnectionConfig() {
        // Given
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);
        ConnectionConfig config = new ConnectionConfig();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        // When
        String script = "SELECT * FROM valid_table";
        List<CheckViolation> checkViolations = sqlCheckService.check(config, "database_name", Arrays.asList(new OffsetString("SELECT * FROM valid_table")), config);

        // Then
        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testGetRules() {
        // Given
        ConnectionSession session = new ConnectionSession();
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);
        Rule rule2 = new Rule();
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("Rule2");
        metadata2.setType(RuleType.SQL_CHECK);
        rule2.setMetadata(metadata2);
        rule2.setEnabled(true);
        rules.add(rule2);
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = mock(JdbcOperations.class);
        List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(dialectType, jdbc);

        // When
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);

        // Then
        verify(jdbc).getDataSource();
        assertEquals(2, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        // Given
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setLevel(1);
        RuleViolation violation = new RuleViolation();
        violation.setMessage("Violation message");
        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setType(new RuleType("TestRuleType"));
        checkViolation.setViolation(violation);
        List<CheckViolation> violations = Collections.singletonList(checkViolation);

        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("Rule2");
        metadata2.setType(RuleType.SQL_CHECK);
        Rule rule2 = new Rule();
        rule2.setMetadata(metadata2);
        rule2.setLevel(2);
        RuleViolation violation2 = new RuleViolation();
        violation2.setMessage("Another violation message");
        CheckViolation checkViolation2 = new CheckViolation();
        checkViolation2.setType(new RuleType("TestRuleType2"));
        checkViolation2.setViolation(violation2);
        violations.add(checkViolation2);

        // When
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        // Then
        assertEquals(2, violatedRules.size());
        Rule rule1 = violatedRules.get(0);
        assertEquals(1, rule1.getLevel());
        Rule rule2 = violatedRules.get(1);
        assertEquals(2, rule2.getLevel());
    }
}