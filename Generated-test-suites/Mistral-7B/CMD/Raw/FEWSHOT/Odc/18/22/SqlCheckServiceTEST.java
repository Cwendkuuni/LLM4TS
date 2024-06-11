package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.DefaultSqlChecker;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.service.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckUtil;

@Slf4j
public class SqlCheckServiceTest {
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private SqlCheckService sqlCheckService;
    private SqlCheckRules sqlCheckRules;
    private DefaultSqlChecker sqlChecker;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
        sqlCheckRules = SqlCheckRules.getInstance();
        sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, Collections.emptyList());
        jdbcTemplate = new JdbcTemplate(new BasicDataSource());
    }

    @After
    public void tearDown() {
        sqlCheckService = null;
        sqlCheckRules = null;
        sqlChecker = null;
        jdbcTemplate = null;
    }

    @Test
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        List<Rule> rules = Arrays.asList(
                new Rule(),
                new Rule()
        );
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(session.getRuleSetId()).thenReturn(1L);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);
        assertEquals(2, sqlCheckRules.size());
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithViolation() {
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        List<Rule> rules = Arrays.asList(
                new Rule()
                        .setMetadata(new RuleMetadata()
                                .setType(RuleType.SQL_CHECK)
                                .setName("TestRule")
                                .setLevel(1)
                        ),
                new Rule()
        );
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(session.getRuleSetId()).thenReturn(1L);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);
        assertEquals(1, sqlCheckRules.size());
        String sql = "SELECT * FROM users WHERE id = 123";
        List<CheckViolation> violations = Arrays.asList(
                new CheckViolation()
                        .setId(1)
                        .setType(new RuleType("TestRule"))
                        .setMessage("Test message")
                        .setLevel(1)
        );
        when(sqlChecker.check(Collections.singletonList(sql), eq(new SqlCheckContext(1)))).thenReturn(violations);
        List<CheckResult> checkResults = sqlCheckService.check(session, new SqlCheckReq().setScriptContent(sql));
        assertEquals(1, checkResults.size());
        CheckResult checkResult = checkResults.get(0);
        assertEquals("Test message", checkResult.getViolation().getMessage());
        assertEquals("TestRule", checkResult.getViolation().getType().getName());
        assertEquals(RuleType.SQL_CHECK, checkResult.getViolation().getType());
        assertEquals(1, checkResult.getViolation().getLevel());
    }

    @Test
    public void testCheckEmptySqls() {
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        List<Rule> rules = Collections.emptyList();
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        List<CheckResult> checkResults = sqlCheckService.check(session, new SqlCheckReq());
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckEnvironment() {
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM users"));
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        List<Rule> rules = Arrays.asList(
                new Rule()
                        .setMetadata(new RuleMetadata()
                                .setType(RuleType.SQL_CHECK)
                                .setName("TestRule")
                                .setLevel(1)
                        ),
                new Rule()
        );
        Environment env = new Environment();
        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build()))
                .thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, config.getDialectType(), jdbcTemplate);
        assertEquals(1, sqlCheckRules.size());
        List<CheckViolation> violations = Collections.emptyList();
        when(sqlChecker.check(Collections.singletonList(sqls.get(0)), eq(new SqlCheckContext(1)))).thenReturn(violations);
        List<CheckResult> checkResults = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckEnvironmentWithViolation() {
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM users WHERE id = 123"));
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        List<Rule> rules = Arrays.asList(
                new Rule()
                        .setMetadata(new RuleMetadata()
                                .setType(RuleType.SQL_CHECK)
                                .setName("TestRule")
                                .setLevel(1)
                        ),
                new Rule()
        );
        Environment env = new Environment();
        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build()))
                .thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, config.getDialectType(), jdbcTemplate);
        assertEquals(1, sqlCheckRules.size());
        String sql = "SELECT * FROM users WHERE id = 123";
        List<CheckViolation> violations = Arrays.asList(
                new CheckViolation()
                        .setId(1)
                        .setType(new RuleType("TestRule"))
                        .setMessage("Test message")
                        .setLevel(1)
        );
        when(sqlChecker.check(Collections.singletonList(sql), eq(new SqlCheckContext(1)))).thenReturn(violations);
        List<CheckResult> checkResults = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(1, checkResults.size());
        CheckResult checkResult = checkResults.get(0);
        assertEquals("Test message", checkResult.getViolation().getMessage());
        assertEquals("TestRule", checkResult.getViolation().getType().getName());
        assertEquals(RuleType.SQL_CHECK, checkResult.getViolation().getType());
        assertEquals(1, checkResult.getViolation().getLevel());
    }
}