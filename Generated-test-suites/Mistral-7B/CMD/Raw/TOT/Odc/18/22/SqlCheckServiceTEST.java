package com.oceanbase.odc.service.sqlcheck;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@SuppressWarnings("ResultOfMethodCallIgnored")
@Service
@Validated
@SkipAuthorize("inside connect session")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcOperations jdbc;
    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;
    @Autowired
    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(singleConnectionDataSource.getDataSource()).thenReturn(jdbc);
    }

    @Test
    public void testCheckIsCalledWithValidParameters() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        Long ruleSetId = 1L;
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rules.add(rule);
        when(ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRule sqlCheckRule = new DefaultSqlCheckRule(DialectType.MYSQL, jdbc);
        sqlCheckRules.add(sqlCheckRule);
        when(sqlCheckService.getRules(rules, DialectType.MYSQL, jdbc)).thenReturn(sqlCheckRules);
        CheckResult result = sqlCheckService.check(session, req);
        verify(jdbc, times(1)).checkPermission(any(String.class), any(String.class), any(String.class), anyInt(), anyInt());
        verifyNoMoreInteractions(ruleService, environmentService, singleConnectionDataSource);
        assertEquals(Collections.emptyList(), result.getCheckViolations());
    }

    @Test
    public void testCheckIsCalledWithEmptyRuleSet() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        Long ruleSetId = 1L;
        when(ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());
        CheckResult result = sqlCheckService.check(session, req);
        verifyNoInteractions(jdbc);
        verifyNoMoreInteractions(ruleService, environmentService, singleConnectionDataSource);
        assertEquals(Collections.emptyList(), result.getCheckViolations());
    }

    @Test
    public void testCheckIsCalledWithEmptySql() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        Long ruleSetId = 1L;
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rules.add(rule);
        when(ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRule sqlCheckRule = new DefaultSqlCheckRule(DialectType.MYSQL, jdbc);
        sqlCheckRules.add(sqlCheckRule);
        when(sqlCheckService.getRules(rules, DialectType.MYSQL, jdbc)).thenReturn(sqlCheckRules);
        CheckResult result = sqlCheckService.check(session, req);
        verifyNoMoreInteractions(jdbc);
        verifyNoMoreInteractions(ruleService, environmentService, singleConnectionDataSource);
        assertEquals(Collections.emptyList(), result.getCheckViolations());
    }

    @Test
    public void testCheckIsCalledWithValidSqlAndRuleViolations() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        Long ruleSetId = 1L;
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        RuleViolation violation = new RuleViolation();
        violation.setMessage("Test violation message");
        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setType(SqlCheckRules.TestRule.class);
        checkViolation.setViolation(violation);
        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(checkViolation);
        when(ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRule sqlCheckRule = new DefaultSqlCheckRule(DialectType.MYSQL, jdbc);
        sqlCheckRules.add(sqlCheckRule);
        when(sqlCheckService.getRules(rules, DialectType.MYSQL, jdbc)).thenReturn(sqlCheckRules);
        when(sqlCheckRule.check(req.getScriptContent())).thenReturn(checkViolations);
        CheckResult result = sqlCheckService.check(session, req);
        verify(jdbc, times(1)).checkPermission(any(String.class), any(String.class), any(String.class), anyInt(), anyInt());
        verifyNoMoreInteractions(ruleService, environmentService, singleConnectionDataSource);
        assertEquals(checkViolations, result.getCheckViolations());
    }

    @Test
    public void testCheckIsCalledWithEmptySqlAndRuleViolations() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        Long ruleSetId = 1L;
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        RuleViolation violation = new RuleViolation();
        violation.setMessage("Test violation message");
        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setType(SqlCheckRules.TestRule.class);
        checkViolation.setViolation(violation);
        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(checkViolation);
        when(ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRule sqlCheckRule = new DefaultSqlCheckRule(DialectType.MYSQL, jdbc);
        sqlCheckRules.add(sqlCheckRule);
        when(sqlCheckService.getRules(rules, DialectType.MYSQL, jdbc)).thenReturn(sqlCheckRules);
        when(sqlCheckRule.check(Collections.emptyList())).thenReturn(Collections.emptyList());
        CheckResult result = sqlCheckService.check(session, req);
        verifyNoMoreInteractions(jdbc);
        verifyNoMoreInteractions(ruleService, environmentService, singleConnectionDataSource);
        assertEquals(Collections.emptyList(), result.getCheckViolations());
    }

    @Test
    public void testCheckIsCalledWithValidEnvironmentParameters() {
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql = new OffsetString("SELECT * FROM users WHERE age > 18");
        sqls.add(sql);
        ConnectionConfig config = new ConnectionConfig();
        DialectType dialectType = DialectType.MYSQL;
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rules.add(rule);
        when(environmentService.detail(environmentId)).thenReturn(new Environment());
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true, false);
        when(factory.getDataSource()).thenReturn(singleConnectionDataSource);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRule sqlCheckRule = new DefaultSqlCheckRule(DialectType.MYSQL, jdbc);
        sqlCheckRules.add(sqlCheckRule);
        when(sqlCheckService.getRules(rules, dialectType, jdbc)).thenReturn(sqlCheckRules);
        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setType(SqlCheckRules.TestRule.class);
        checkViolation.setViolation(new RuleViolation());
        checkViolations.add(checkViolation);
        List<CheckViolation> checkViolationsResult = sqlCheckService.check(environmentId, databaseName, sqls, config);
        verify(jdbc, times(sqls.size())).checkPermission(any(String.class), any(String.class), any(String.class), anyInt(), anyInt());
        verifyNoMoreInteractions(ruleService, environmentService, singleConnectionDataSource);
        assertEquals(checkViolationsResult, checkViolations);
    }

    @Test
    public void testCheckIsCalledWithEmptySqlsParameters() {
        Long environmentId = 1L;
        String databaseName = "testdb";
        ConnectionConfig config = new ConnectionConfig();
        DialectType dialectType = DialectType.MYSQL;
        List<OffsetString> sqls = new ArrayList<>();
        when(environmentService.detail(environmentId)).thenReturn(new Environment());
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true, false);
        when(factory.getDataSource()).thenReturn(singleConnectionDataSource);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        List<CheckViolation> checkViolationsResult = sqlCheckService.check(environmentId, databaseName, sqls, config);
        verify(jdbc, times(0)).checkPermission(any(String.class), any(String.class), any(String.class), anyInt(), anyInt());
        verifyNoMoreInteractions(ruleService, environmentService, singleConnectionDataSource);
        assertEquals(Collections.emptyList(), checkViolationsResult);
    }
}