package com.oceanbase.odc.service.sqlcheck.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private Environment environment;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckEmpty() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.emptyList());
        List<CheckResult> result = sqlCheckService.check(connectionSession, new SqlCheckReq());
        assertEquals(Collections.emptyList(), result);
        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheckWithViolations() {
        Rule rule = mock(Rule.class);
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRule");
        metadata.setType(RuleType.SQL_CHECK);
        when(rule.getMetadata()).thenReturn(metadata);
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);

        SqlCheckRuleFactory factory = mock(SqlCheckRuleFactory.class);
        when(SqlCheckRules.getAllFactories(eq(DialectType.ORACLE), any(JdbcOperations.class)))
                .thenReturn(Collections.singletonList(factory));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        assertEquals(1, sqlCheckRules.size());

        CheckViolation violation = new CheckViolation();
        violation.setType(RuleType.SQL_CHECK);
        violation.setMessage("Test violation");

        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(violation);

        when(jdbcTemplate.query(anyString(), anyArray(), any(ResultSetHandler.class)))
                .thenReturn(new Object[0]);

        when(sqlCheckService.fullFillRiskLevel(rules, checkViolations))
                .thenReturn(rules);

        List<CheckResult> result = sqlCheckService.check(connectionSession, new SqlCheckReq());
        assertEquals(1, result.size());
        assertEquals(RuleType.SQL_CHECK, result.get(0).getViolations().get(0).getType());
        assertEquals("Test violation", result.get(0).getViolations().get(0).getMessage());

        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(ruleService, never()).saveRuleViolation(any(CheckViolation.class));
        verify(jdbcTemplate, never()).update(anyString(), anyArray());
    }

    @Test
    public void testCheckWithSaveViolations() {
        Rule rule = mock(Rule.class);
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRule");
        metadata.setType(RuleType.SQL_CHECK);
        when(rule.getMetadata()).thenReturn(metadata);
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);

        SqlCheckRuleFactory factory = mock(SqlCheckRuleFactory.class);
        when(SqlCheckRules.getAllFactories(eq(DialectType.ORACLE), any(JdbcOperations.class)))
                .thenReturn(Collections.singletonList(factory));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        assertEquals(1, sqlCheckRules.size());

        CheckViolation violation = new CheckViolation();
        violation.setType(RuleType.SQL_CHECK);
        violation.setMessage("Test violation");

        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(violation);

        doReturn(1L).when(rule).getId();
        doReturn(metadata).when(rule).getMetadata();

        when(jdbcTemplate.query(anyString(), anyArray(), any(ResultSetHandler.class)))
                .thenReturn(new Object[0]);

        when(sqlCheckService.fullFillRiskLevel(rules, checkViolations))
                .thenReturn(rules);

        when(ruleService.saveRuleViolation(any(CheckViolation.class)))
                .thenReturn(true);

        when(jdbcTemplate.update(anyString(), anyArray()))
                .thenReturn(1);

        List<CheckResult> result = sqlCheckService.check(connectionSession, new SqlCheckReq());
        assertEquals(1, result.size());
        assertEquals(RuleType.SQL_CHECK, result.get(0).getViolations().get(0).getType());
        assertEquals("Test violation", result.get(0).getViolations().get(0).getMessage());

        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(ruleService).saveRuleViolation(any(CheckViolation.class));
        verify(jdbcTemplate).update(anyString(), anyArray());
    }

    @Test
    public void testCheckWithDataSource() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenThrow(new RuntimeException());

        SqlCheckRuleFactory factory = mock(SqlCheckRuleFactory.class);
        when(SqlCheckRules.getAllFactories(eq(DialectType.ORACLE), any(JdbcOperations.class)))
                .thenReturn(Collections.singletonList(factory));

        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(environment.getRulesetId()).thenReturn(1L);

        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT 1 FROM DUAL;"));

        List<CheckViolation> checkViolations = new ArrayList<>();

        when(dataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);
        when(jdbcTemplate.query(anyString(), anyArray(), any(ResultSetHandler.class)))
                .thenReturn(new Object[0]);

        when(sqlCheckService.getRules(rules, dialectType, jdbcTemplate))
                .thenReturn(Collections.singletonList(factory));
        when(sqlCheckService.fullFillRiskLevel(rules, checkViolations))
                .thenReturn(rules);

        when(ruleService.saveRuleViolation(any(CheckViolation.class)))
                .thenReturn(true);

        when(jdbcTemplate.update(anyString(), anyArray()))
                .thenReturn(1);

        List<CheckResult> result = sqlCheckService.check(1L, "testDB", sqls, connectionConfig);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getViolations());
        assertEquals(0, result.get(0).getViolations().size());

        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(ruleService, never()).saveRuleViolation(any(CheckViolation.class));
        verify(jdbcTemplate, never()).update(anyString(), anyArray());
    }

    @Test
    public void testCheckWithViolationsFromDataSource() {
        Rule rule = mock(Rule.class);
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRule");
        metadata.setType(RuleType.SQL_CHECK);
        when(rule.getMetadata()).thenReturn(metadata);
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);

        SqlCheckRuleFactory factory = mock(SqlCheckRuleFactory.class);
        when(SqlCheckRules.getAllFactories(eq(DialectType.ORACLE), any(JdbcOperations.class)))
                .thenReturn(Collections.singletonList(factory));

        Environment environment = mock(Environment.class);
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(environment.getRulesetId()).thenReturn(1L);

        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT 1 FROM DUAL;"));

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(RuleType.SQL_CHECK);
        violation.setMessage("Test violation");
        violation.setLineNumber(1);
        violation.setOffset(0);
        checkViolations.add(violation);

        when(dataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);
        when(jdbcTemplate.query(anyString(), anyArray(), any(ResultSetHandler.class)))
                .thenReturn(new Object[0]);

        when(sqlCheckService.getRules(rules, dialectType, jdbcTemplate))
                .thenReturn(Collections.singletonList(factory));
        when(sqlCheckService.fullFillRiskLevel(rules, checkViolations))
                .thenReturn(rules);

        when(ruleService.saveRuleViolation(any(CheckViolation.class)))
                .thenReturn(true);

        when(jdbcTemplate.update(anyString(), anyArray()))
                .thenReturn(1);

        List<CheckResult> result = sqlCheckService.check(1L, "testDB", sqls, connectionConfig);
        assertEquals(1, result.size());
        assertEquals(RuleType.SQL_CHECK, result.get(0).getViolations().get(0).getType());
        assertEquals("Test violation", result.get(0).getViolations().get(0).getMessage());
        assertEquals(1, result.get(0).getViolations().get(0).getLineNumber());
        assertEquals(0, result.get(0).getViolations().get(0).getOffset());

        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(ruleService).saveRuleViolation(any(CheckViolation.class));
        verify(jdbcTemplate).update(anyString(), anyArray());
    }

}