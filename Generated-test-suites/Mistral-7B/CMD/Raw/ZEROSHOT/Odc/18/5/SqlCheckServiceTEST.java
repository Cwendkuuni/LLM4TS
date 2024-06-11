package com.oceanbase.odc.service.sqlcheck.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.DelegatingConnection;
import org.apache.commons.dbcp2.DelegatingPreparedStatement;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import com.oceanbase.odc.service.regulation.ruleset.ModelTestUtils;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.service.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.service.DefaultSqlChecker;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

class SqlCheckServiceTest {

    private RuleService ruleService;
    private EnvironmentService environmentService;
    private SqlCheckService sqlCheckService;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        sqlCheckService = new SqlCheckService() {
            @Override
            List<Rule> getRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbc) {
                return SqlCheckServiceTest.this.getRules(rules, dialectType, jdbc);
            }
        };
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
        jdbcTemplate = new JdbcTemplate(new JdbcOperations() {
            @Override
            public int update(String sql, Object... args) {
                return 1;
            }

            @Override
            public <T> T query(String sql, RowMapper<T> rowMapper) {
                return null;
            }

            @Override
            public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
                return Collections.emptyList();
            }

            @Override
            public <T> T queryForObject(String sql, RowMapper<T> rowMapper) {
                return null;
            }
        });
    }

    @Test
    void testCheck() {
        // Given
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        SqlCheckReq req = new SqlCheckReq();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        when(ruleService.list(anyLong(), any())).thenReturn(rules);

        // When
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Then
        verify(ruleService, times(1)).list(anyLong(), any());
        assertTrue(checkResults.isEmpty());
    }

    @Test
    void testCheck_withViolation() {
        // Given
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("select 1 from test where id = 1 -- admin");
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRule");
        metadata.setType(RuleType.SQL_INJECTION);
        metadata.setLevel(1);
        rule.setMetadata(metadata);
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        when(ruleService.list(anyLong(), any())).thenReturn(rules);

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, new ArrayList<>());
        SqlCheckContext checkContext = new SqlCheckContext(1);
        when(sqlChecker.check(anyList(), checkContext)).thenReturn(Collections.singletonList(new CheckViolation()));

        sqlCheckService.setSqlChecker(sqlChecker);

        // When
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Then
        verify(ruleService, times(1)).list(anyLong(), any());
        verify(sqlChecker, times(1)).check(anyList(), any());
        assertNotNull(checkResults);
        assertTrue(checkResults.size() > 0);
        assertEquals(1, checkResults.get(0).getViolations().size());
        assertEquals(RuleViolation.SQL_INJECTION, checkResults.get(0).getViolations().get(0).getViolationType());
        assertEquals(1, checkResults.get(0).getViolations().get(0).getLevel());
        assertEquals("testRule", checkResults.get(0).getViolations().get(0).getRuleName());
    }

    @Test
    void testCheck_withEnvironment() {
        // Given
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("select 1 from test where id = 1 -- admin"));
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRule");
        metadata.setType(RuleType.SQL_INJECTION);
        metadata.setLevel(1);
        rule.setMetadata(metadata);
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);

        Environment env = new Environment();
        env.setRulesetId(1L);

        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(env.getRulesetId(), any())).thenReturn(rules);

        // When
        List<CheckResult> checkResults = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // Then
        verify(ruleService, times(1)).list(env.getRulesetId(), any());
        verifyNoInteractions(sqlCheckService);
        assertNotNull(checkResults);
        assertTrue(checkResults.isEmpty());
    }

    @Test
    void testCheck_withEnvironment_withViolation() {
        // Given
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("select 1 from test where id = 1 -- admin"));
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);

        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRule");
        metadata.setType(RuleType.SQL_INJECTION);
        metadata.setLevel(1);
        rule.setMetadata(metadata);

        List<Rule> rules = new ArrayList<>();
        rules.add(rule);

        Environment env = new Environment();
        env.setRulesetId(1L);

        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(env.getRulesetId(), any())).thenReturn(rules);

        SqlCheckContext checkContext = new SqlCheckContext(sqls.size());
        SqlChecker sqlChecker = new DefaultSqlChecker(config.getDialectType(), null, new ArrayList<>());
        when(sqlChecker.check(anyList(), checkContext)).thenReturn(Collections.singletonList(new CheckViolation()));

        sqlCheckService.setSqlChecker(sqlChecker);

        // When
        List<CheckResult> checkResults = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // Then
        verify(ruleService, times(1)).list(env.getRulesetId(), any());
        verify(sqlChecker, times(sqls.size())).check(anyList(), checkContext);
        assertNotNull(checkResults);
        assertTrue(checkResults.size() > 0);
        assertEquals(1, checkResults.get(0).getViolations().size());
        assertEquals(RuleViolation.SQL_INJECTION, checkResults.get(0).getViolations().get(0).getViolationType());
        assertEquals(1, checkResults.get(0).getViolations().get(0).getLevel());
        assertEquals("testRule", checkResults.get(0).getViolations().get(0).getRuleName());
    }

    @Test
    void testGetRules() {
        // Given
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rules.add(rule);

        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = mock(JdbcOperations.class);

        // When
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);

        // Then
        verify(jdbc, times(0)).query(anyString(), any(RowMapper.class));
        assertEquals(1, sqlCheckRules.size());
        assertInstanceOf(SqlCheckRulesTestFactory.class.getRuleFactoryClass(), sqlCheckRules.get(0).getClass());
    }

    @Test
    void testGetRules_withEmptyRules() {
        // Given
        List<Rule> rules = Collections.emptyList();

        // When
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL,
                mock(JdbcOperations.class));

        // Then
        verify(ruleService, times(0)).list(anyLong(), any());
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    void testGetRules_withEmptyRulesAndJdbc() {
        // Given
        List<Rule> rules = Collections.emptyList();

        // When
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL,
                mock(JdbcOperations.class));

        // Then
        verify(ruleService, times(0)).list(anyLong(), any());
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    void testFullFillRiskLevel() {
        // Given
        List<CheckViolation> checkViolations = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("testRule");
        metadata.setType(RuleType.SQL_CHECK);
        metadata.setLevel(1);
        rule.setMetadata(metadata);

        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setType(RuleType.SQL_INJECTION);

        // When
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        // Then
        assertEquals(1, violatedRules.size());
        assertEquals(rule, violatedRules.get(0));
    }

    private List<SqlCheckRule> getRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbc) {
        if (CollectionUtils.isEmpty(rules)) {
            return Collections.emptyList();
        }
        List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(dialectType, jdbc);
        return rules.stream().filter(rule -> {
            RuleMetadata metadata = rule.getMetadata();
            if (metadata == null || !Boolean.TRUE.equals(rule.getEnabled())) {
                return false;
            }
            return Objects.equals(metadata.getType(), RuleType.SQL_CHECK);
        }).map(rule -> {
            try {
                return SqlCheckRules.createByRule(candidates, dialectType, rule);
            } catch (Exception e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}

class SqlCheckRulesTestFactory {
    public static Class<? extends SqlCheckRuleFactory> getRuleFactoryClass() {
        return SqlCheckRulesTestFactory1.class;
    }

    static class SqlCheckRulesTestFactory1 implements SqlCheckRuleFactory {
        @Override
        public SqlCheckRule createRule(Rule rule) {
            return new SqlCheckRule();
        }
    }
}