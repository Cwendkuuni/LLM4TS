package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

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
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@SuppressWarnings("unchecked")
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
    public void testCheckWithValidConnectionSession() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MySQL);
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MySQL);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(new SimpleJdbcTemplate(mock(JdbcTemplate.class)));

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("Rule1");
        metadata1.setLevel(1);
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        rule1.setEnabled(true);
        Rule rule2 = new Rule();
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("Rule2");
        metadata2.setLevel(2);
        metadata2.setType(RuleType.SQL_CHECK);
        rule2.setMetadata(metadata2);
        rule2.setEnabled(false);
        rules.add(rule1);
        rules.add(rule2);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> expectedResults = new ArrayList<>();
        CheckResult result1 = new CheckResult();
        result1.setRuleName("Rule1");
        result1.setViolations(Collections.singletonList(new CheckViolation()));
        result1.setRiskLevel(1);
        expectedResults.add(result1);

        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        List<CheckResult> results = sqlCheckService.check(connectionSession, req);
        assertEquals(expectedResults, results);
        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService, never()).detail(anyLong());
    }

    @Test
    public void testCheckWithInvalidConnectionSession() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MySQL);
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        when(connectionSession.getDialectType()).thenThrow(new RuntimeException());

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setLevel(1);
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        SqlCheckReq req = new SqlCheckReq();

        List<CheckResult> results = sqlCheckService.check(connectionSession, req);
        assertTrue(results.isEmpty());
        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(connectionSession).getDialectType();
    }

    @Test
    public void testCheckWithEmptySqlScript() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MySQL);
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MySQL);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setLevel(1);
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        SqlCheckReq req = new SqlCheckReq();

        List<CheckResult> results = sqlCheckService.check(connectionSession, req);
        assertTrue(results.isEmpty());
        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(connectionSession).getDialectType();
    }

    @Test
    public void testCheckWithValidSqlScript() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MySQL);
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MySQL);

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("Rule1");
        metadata1.setLevel(1);
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        rule1.setEnabled(true);

        Rule rule2 = new Rule();
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("Rule2");
        metadata2.setLevel(2);
        metadata2.setType(RuleType.SQL_CHECK);
        rule2.setMetadata(metadata2);
        rule2.setEnabled(true);

        rules.add(rule1);
        rules.add(rule2);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        String sqlScript = "select * from test where id > 1";
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(sqlScript);

        List<CheckResult> expectedResults = new ArrayList<>();
        CheckResult result1 = new CheckResult();
        result1.setRuleName("Rule1");
        result1.setViolations(Collections.singletonList(new CheckViolation()));
        result1.setRiskLevel(1);
        expectedResults.add(result1);

        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        List<CheckResult> results = sqlCheckService.check(connectionSession, req);
        assertEquals(expectedResults, results);
        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService, never()).detail(anyLong());
    }

    @Test
    public void testCheckWithInvalidSqlScript() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MySQL);
        ConnectionSession connectionSession = mock(ConnectionSession.class);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MySQL);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setLevel(1);
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        String sqlScript = "select * from test where id > 1 + 1";
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(sqlScript);

        List<CheckResult> results = sqlCheckService.check(connectionSession, req);
        assertTrue(results.isEmpty());
        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(connectionSession).getDialectType();
    }

    @Test
    public void testCheckWithEnvironment() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MySQL);

        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/testdb");
        ds.setUsername("testuser");
        ds.setPassword("testpassword");

        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(ds);

        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        SqlCheckContext checkContext = new SqlCheckContext(1);

        List<SqlCheckRule> checkRules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Rule1");
        metadata.setLevel(1);
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        checkRules.add(rule);

        List<Rule> rules = new ArrayList<>();
        rules.add(rule);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        String sqlScript = "select * from test where id > 1";

        SqlCheckService sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
        sqlCheckService.setDataSourceFactory(new OBConsoleDataSourceFactory(connectionConfig, true, false));

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "test", Collections.singletonList(new OffsetString(sqlScript)), connectionConfig).get(0).getViolations();

        List<CheckResult> results = sqlCheckService.checkWithEnvironment(dataSource, sqlScript).get(0).getCheckResults();

        assertEquals(checkViolations, results.get(0).getViolations());
    }
}