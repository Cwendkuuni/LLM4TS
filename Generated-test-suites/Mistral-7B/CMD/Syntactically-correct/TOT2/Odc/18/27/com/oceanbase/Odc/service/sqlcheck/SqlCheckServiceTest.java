package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.DelegatingResultSet;
import org.apache.commons.dbcp2.DelegatingStatement;
import org.apache.commons.dbcp2.Handle;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.pool.PooledConnection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

/**
 * {@link SqlCheckService} tests
 *
 * @author yh263208
 * @since ODC_release_4.1.0
 */
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private OBConsoleDataSourceFactory factory;
    @Mock
    private SingleConnectionDataSource dataSource;
    @Mock
    private Environment environment;
    @Mock
    private ConnectionSession session;
    @Mock
    private SqlCheckContext checkContext;

    private SqlCheckService sqlCheckService;
    private PooledConnectionFactory connectionFactory;
    private BasicDataSource dataSourceMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        connectionFactory = new PoolableConnectionFactory();
        dataSourceMock = new BasicDataSource();
        dataSourceMock.setConnectionFactory(connectionFactory);
        when(factory.getDataSource()).thenReturn(dataSourceMock);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
        sqlCheckService.jdbcTemplate = jdbcTemplate;
        sqlCheckService.factory = factory;
    }

    @Test
    public void testCheck() {
        // Given
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setType(RuleType.SQL_CHECK);
        metadata1.setName("Rule1");
        metadata1.setLevel(1);
        rule1.setMetadata(metadata1);
        rule1.setEnabled(true);
        rules.add(rule1);

        Rule rule2 = new Rule();
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setType(RuleType.SQL_CHECK);
        metadata2.setName("Rule2");
        metadata2.setLevel(2);
        rule2.setMetadata(metadata2);
        rule2.setEnabled(true);
        rules.add(rule2);

        when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, config.getDialectType(), jdbcTemplate);

        when(environmentService.detail(ArgumentMatchers.anyLong())).thenReturn(environment);
        when(environment.getRulesetId()).thenReturn(1L);

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(SqlCheckRules.RuleType.IDENTIFIER_SECURITY);
        violation1.setMessage("Identifier security violation");
        violation1.setLevel(1);
        checkViolations.add(violation1);

        CheckViolation violation2 = new CheckViolation();
        violation2.setType(SqlCheckRules.RuleType.IDENTIFIER_SECURITY);
        violation2.setMessage("Another identifier security violation");
        violation2.setLevel(2);
        checkViolations.add(violation2);

        when(jdbcTemplate.query(ArgumentMatchers.anyString(), ArgumentMatchers.any(Map.class))).thenReturn(checkViolations.stream()
                .map(c -> new DelegatingResultSet() {
                    @Override
                    public int getRowNum() {
                        return 1;
                    }
                }).collect(Collectors.toList()));

        when(factory.getDataSource()).thenReturn(dataSourceMock);

        when(checkContext.getCheckViolations()).thenReturn(new ArrayList<>());

        // When
        List<CheckResult> checkResults = sqlCheckService.check(session, new SqlCheckReq());

        // Then
        verify(ruleService, times(1)).list(ArgumentMatchers.anyLong(), ArgumentMatchers.any(QueryRuleMetadataParams.class));
        verify(jdbcTemplate, times(1)).query(ArgumentMatchers.anyString(), ArgumentMatchers.any(Map.class));
        assertEquals(2, checkResults.size());
        assertTrue(checkResults.get(0).getCheckViolations().size() > 0);
        assertTrue(checkResults.get(1).getCheckViolations().size() > 0);
    }

    @Test
    public void testCheckWithEnvironment() {
        // Given
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setType(RuleType.SQL_CHECK);
        metadata1.setName("Rule1");
        metadata1.setLevel(1);
        rule1.setMetadata(metadata1);
        rule1.setEnabled(true);
        rules.add(rule1);

        when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.any(QueryRuleMetadataParams.class))).thenReturn(rules);

        String databaseName = "databaseName";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users"));

        when(environmentService.detail(ArgumentMatchers.anyLong())).thenReturn(environment);
        when(environment.getRulesetId()).thenReturn(1L);

        List<SqlCheckRule> checkRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);

        DefaultSqlChecker sqlChecker = mock(DefaultSqlChecker.class);
        when(sqlChecker.check(ArgumentMatchers.anyList(), ArgumentMatchers.any(SqlCheckContext.class))).thenReturn(Collections.emptyList());

        when(factory.getDataSource()).thenReturn(dataSourceMock);

        // When
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, databaseName, sqls, connectionConfig);

        // Then
        verify(ruleService, times(1)).list(ArgumentMatchers.anyLong(), ArgumentMatchers.any(QueryRuleMetadataParams.class));
        verify(jdbcTemplate, never()).query(ArgumentMatchers.anyString(), ArgumentMatchers.any(Map.class));
        verify(sqlChecker, times(1)).check(ArgumentMatchers.anyList(), ArgumentMatchers.any(SqlCheckContext.class));
        assertTrue(checkViolations.size() > 0);
    }

    @Test
    public void testGetRules() {
        // Given
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setType(RuleType.SQL_CHECK);
        metadata1.setName("Rule1");
        metadata1.setLevel(1);
        rule1.setMetadata(metadata1);
        rule1.setEnabled(true);
        rules.add(rule1);

        when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRuleFactory> candidates = new ArrayList<>();
        SqlCheckRuleFactory ruleFactory1 = mock(SqlCheckRuleFactory.class);

        when(SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcTemplate)).thenReturn(candidates);
        when(SqlCheckRules.createByRule(candidates, DialectType.MYSQL, ArgumentMatchers.any(Rule.class))).thenReturn(new SqlCheckRule());

        // When
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);

        // Then
        verify(ruleService, times(1)).list(ArgumentMatchers.anyLong(), ArgumentMatchers.any(QueryRuleMetadataParams.class));
        verify(SqlCheckRules, times(1)).getAllFactories(DialectType.MYSQL, jdbcTemplate);
        verify(SqlCheckRules, times(rules.size())).createByRule(candidates, DialectType.MYSQL, ArgumentMatchers.any(Rule.class));
        assertEquals(rules.size(), sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        // Given
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setType(RuleType.SQL_CHECK);
        metadata1.setName("Rule1");
        metadata1.setLevel(1);
        rule1.setMetadata(metadata1);
        rule1.setEnabled(true);
        rules.add(rule1);

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(RuleViolation.Type.QUERY_LENGTH);
        violation1.setLevel(1);
        violation1.setMessage("Query length violation");
        violation1.setViolationMessage("Query length exceeds limit");
        violations.add(violation1);

        // When
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        // Then
        assertEquals(rules.size(), violatedRules.size());
        Rule rule = violatedRules.get(0);
        assertEquals(metadata1.getName(), rule.getMetadata().getName());
        assertEquals(metadata1.getLevel(), rule.getLevel());
        assertTrue(rule.getViolation() instanceof RuleViolation);
        assertEquals(RuleViolation.Type.QUERY_LENGTH, ((RuleViolation) rule.getViolation()).getType());
        assertTrue(((RuleViolation) rule.getViolation()).getViolationMessages().contains("Query length violation"));
        assertTrue(((RuleViolation) rule.getViolation()).getViolationMessages().contains("Query length exceeds limit"));
    }
}