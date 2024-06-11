package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckContext;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    @Validated
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck_Valid() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        when(session.getDialectType()).thenReturn(DialectType.MySQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(mock(JdbcOperations.class));
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(Collections.singletonList(new Rule()));

        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), results);

        // Test with valid rules
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        List<Rule> rules = Collections.singletonList(rule);
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(SqlCheckRules.getAllFactories(eq(DialectType.MySQL), isA(JdbcOperations.class))).thenReturn(Collections.singletonList(mock(SqlCheckRuleFactory.class)));
        when(rule.getMetadata()).thenReturn(new RuleMetadata());
        when(rule.getMetadata().getType()).thenReturn(RuleType.SQL_CHECK);

        CheckResult result = sqlCheckService.check(session, req).get(0);
        assertEquals("Test result", result.getMessage());

        verify(ruleService, times(1)).list(anyLong(), eq(QueryRuleMetadataParams.builder().build()));
        verify(rule, times(1)).getMetadata();
        verify(rule.getMetadata(), times(1)).getType();
        verify(SqlCheckRules, times(1)).getAllFactories(eq(DialectType.MySQL), isA(JdbcOperations.class));
        verify(SqlCheckRules, times(1)).createByRule(eq(Collections.singletonList(mock(SqlCheckRuleFactory.class))), eq(DialectType.MySQL), eq(rule));
    }

    @Test
    public void testCheck_EmptyRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        when(session.getDialectType()).thenReturn(DialectType.MySQL);

        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), results);

        verify(ruleService, times(1)).list(anyLong(), eq(QueryRuleMetadataParams.builder().build()));
    }

    @Test
    public void testCheck_InvalidRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        when(session.getDialectType()).thenReturn(DialectType.MySQL);
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(Collections.singletonList(new Rule()));

        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), results);

        verify(ruleService, times(1)).list(anyLong(), eq(QueryRuleMetadataParams.builder().build()));
        verify(rule, times(0)).getMetadata();
        verify(rule.getMetadata(), times(0)).getType();
        verify(SqlCheckRules, times(0)).getAllFactories(eq(DialectType.MySQL), isA(JdbcOperations.class));
        verify(SqlCheckRules, times(0)).createByRule(anyList(), eq(DialectType.MySQL), eq(rule));
    }

    @Test
    public void testCheck_MultipleRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        when(session.getDialectType()).thenReturn(DialectType.MySQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(mock(JdbcOperations.class));
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(Collections.singletonList(new Rule()));

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("Rule1");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        Rule rule2 = new Rule();
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("Rule2");
        metadata2.setType(RuleType.SQL_CHECK);
        rule2.setMetadata(metadata2);
        rules.add(rule1);
        rules.add(rule2);
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(SqlCheckRules.getAllFactories(eq(DialectType.MySQL), isA(JdbcOperations.class))).thenReturn(Collections.singletonList(mock(SqlCheckRuleFactory.class)));

        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(2, results.size());
        assertEquals("Test result 1", results.get(0).getMessage());
        assertEquals("Test result 2", results.get(1).getMessage());

        verify(ruleService, times(1)).list(anyLong(), eq(QueryRuleMetadataParams.builder().build()));
        verify(rule1, times(1)).getMetadata();
        verify(rule1.getMetadata(), times(1)).getType();
        verify(rule2, times(1)).getMetadata();
        verify(rule2.getMetadata(), times(1)).getType();
        verify(SqlCheckRules, times(1)).getAllFactories(eq(DialectType.MySQL), isA(JdbcOperations.class));
        verify(SqlCheckRules, times(2)).createByRule(anyList(), eq(DialectType.MySQL), eq(rule1));
        verify(SqlCheckRules, times(2)).createByRule(anyList(), eq(DialectType.MySQL), eq(rule2));
    }

    @Test
    public void testCheck_Environment() {
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users"));
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MySQL);

        Environment env = new Environment();
        env.setRulesetId(1L);
        when(environmentService.detail(environmentId)).thenReturn(env);

        List<CheckResult> results = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(1, results.size());
        assertEquals("Test result", results.get(0).getMessage());

        verify(environmentService, times(1)).detail(environmentId);
        verify(ruleService, times(1)).list(env.getRulesetId(), QueryRuleMetadataParams.builder().build());
        verify(SqlCheckRules, times(1)).getAllFactories(eq(DialectType.MySQL), isA(JdbcOperations.class));
        verify(SqlCheckRules, times(1)).createByRule(anyList(), eq(DialectType.MySQL), any(SqlCheckRule.class));
    }

    @Test
    public void testCheck_EmptySqls() {
        Long environmentId = 1L;
        String databaseName = "test";
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MySQL);

        List<CheckResult> results = sqlCheckService.check(environmentId, databaseName, Collections.emptyList(), config);
        assertEquals(0, results.size());

        verify(environmentService, times(1)).detail(environmentId);
        verify(ruleService, times(1)).list(eq(1L), QueryRuleMetadataParams.builder().build());
        verify(SqlCheckRules, times(0)).getAllFactories(eq(DialectType.MySQL), isA(JdbcOperations.class));
        verify(SqlCheckRules, times(0)).createByRule(anyList(), eq(DialectType.MySQL), any(SqlCheckRule.class));
    }

    @Test
    public void testCheck_CheckViolations() {
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users WHERE age < 18"));
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MySQL);

        Environment env = new Environment();
        env.setRulesetId(1L);
        List<Rule> rules = new ArrayList<>();

        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("Rule1");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        rules.add(rule1);

        Rule rule2 = new Rule();
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("Rule2");
        metadata2.setType(RuleType.SQL_CHECK);
        rule2.setMetadata(metadata2);
        rules.add(rule2);

        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        when(SqlCheckRules.getAllFactories(eq(DialectType.MySQL), isA(JdbcOperations.class))).thenReturn(Collections.singletonList(mock(SqlCheckRuleFactory.class)));

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(RuleType.SQL_CHECK);
        violation1.setMessage("Test violation 1");
        violation1.setLevel("HIGH");
        violations.add(violation1);

        CheckViolation violation2 = new CheckViolation();
        violation2.setType(RuleType.SQL_CHECK);
        violation2.setMessage("Test violation 2");
        violation2.setLevel("LOW");
        violations.add(violation2);

        when(SqlCheckRules.createByRule(anyList(), eq(DialectType.MySQL), eq(rule1))).thenReturn(mock(SqlCheckRule.class));
        when(SqlCheckRules.createByRule(anyList(), eq(DialectType.MySQL), eq(rule2))).thenReturn(mock(SqlCheckRule.class));
        when(SqlCheckRules.createByRule(anyList(), eq(DialectType.MySQL), any(SqlCheckRule.class)).check(anyList(), any(SqlCheckContext.class))).thenReturn(violations);

        List<CheckResult> results = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(1, results.size());
        assertEquals(2, results.get(0).getViolations().size());

        verify(environmentService, times(1)).detail(environmentId);
        verify(ruleService, times(1)).list(env.getRulesetId(), QueryRuleMetadataParams.builder().build());
        verify(SqlCheckRules, times(1)).getAllFactories(eq(DialectType.MySQL), isA(JdbcOperations.class));
        verify(SqlCheckRules, times(2)).createByRule(anyList(), eq(DialectType.MySQL), eq(rule1));
        verify(SqlCheckRules, times(2)).createByRule(anyList(), eq(DialectType.MySQL), eq(rule2));
        verify(SqlCheckRules, times(1)).createByRule(anyList(), eq(DialectType.MySQL), any(SqlCheckRule.class)).check(anyList(), any(SqlCheckContext.class));
    }
}