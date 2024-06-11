package com.oceanbase.odc.service.sqlcheck.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
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
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcOperations jdbcOperations;
    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;
    @Mock
    private ConnectionSession session;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private Environment env;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        when(singleConnectionDataSource.getDataSource()).thenReturn(jdbcOperations);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);
        when(environmentService.detail(1L)).thenReturn(env);
        when(env.getRulesetId()).thenReturn(1L);
        when(ruleService.list(1L, any(QueryRuleMetadataParams.class))).thenReturn( Arrays.asList(new Rule()) );
    }

    @Test
    public void testCheck() {
        OffsetString sql1 = new OffsetString("SELECT * FROM table1 WHERE id = 1");
        OffsetString sql2 = new OffsetString("SELECT * FROM table1 WHERE id = 2");

        doNothing().when(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));

        List<CheckResult> results = sqlCheckService.check(session, new SqlCheckReq("test", Arrays.asList(sql1, sql2)));

        assertEquals(2, results.size());
        for (CheckResult result : results) {
            assertNotNull(result.getViolations());
            assertEquals(0, result.getViolations().size());
        }
    }

    @Test
    public void testCheckWithViolations() {
        OffsetString sql = new OffsetString("SELECT * FROM table1 WHERE id = 1 -- delete");

        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setLevel(1);
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setViolation(RuleViolation.fromCheckViolation(new CheckViolation("TestRule", 1, "TestViolation")));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Arrays.asList(rule));

        List<CheckResult> results = sqlCheckService.check(session, new SqlCheckReq("test", Arrays.asList(sql)));

        assertEquals(1, results.size());
        CheckResult result = results.get(0);
        assertNotNull(result.getViolations());
        assertEquals(1, result.getViolations().size());
        assertEquals("TestViolation", result.getViolations().get(0).getMessage());
    }

    @Test
    public void testCheckInEnvironment() {
        OffsetString sql = new OffsetString("SELECT * FROM table1 WHERE id = 1 -- delete");

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckViolation> violations = sqlCheckService.check(1L, "testDB", Arrays.asList(sql), connectionConfig);

        assertEquals(0, violations.size());
    }

    @Test
    public void testCheckInEnvironmentWithViolations() {
        OffsetString sql = new OffsetString("SELECT * FROM table1 WHERE id = 1 -- delete");

        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setLevel(1);
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setViolation(RuleViolation.fromCheckViolation(new CheckViolation("TestRule", 1, "TestViolation")));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.singletonList(rule));

        List<CheckViolation> violations = sqlCheckService.check(1L, "testDB", Arrays.asList(sql), connectionConfig);

        assertEquals(1, violations.size());
        CheckViolation violation = violations.get(0);
        assertNotNull(violation.getMetadata());
        assertEquals("TestRule", violation.getMetadata().getName());
        assertEquals("TestViolation", violation.getMessage());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = Arrays.asList(new Rule(), new Rule());

        List<SqlCheckRule> checkRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcOperations);

        assertEquals(2, checkRules.size());
    }

    @Test
    public void testGetRulesEmpty() {
        List<Rule> rules = Collections.emptyList();

        List<SqlCheckRule> checkRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcOperations);

        assertEquals(0, checkRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("Rule1");
        metadata1.setLevel(1);

        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("Rule2");
        metadata2.setLevel(2);

        List<Rule> rules = Arrays.asList(new Rule().setMetadata(metadata1), new Rule().setMetadata(metadata2));

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation("Rule1", 1, "TestViolation1");
        violation1.setLevel(3);
        CheckViolation violation2 = new CheckViolation("Rule2", 1, "TestViolation2");
        violation2.setLevel(4);
        violations.add(violation1);
        violations.add(violation2);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(2, result.size());
        Rule rule1 = result.get(0);
        Rule rule2 = result.get(1);
        assertEquals(3, rule1.getMetadata().getLevel());
        assertEquals(4, rule2.getMetadata().getLevel());
    }
}