package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private ConnectionSession session;

    @Mock
    private SingleConnectionDataSource dataSource;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private JdbcOperations jdbcOperations;

    @Mock
    private OBConsoleDataSourceFactory factory;

    @Mock
    private Environment env;

    @Mock
    private ConnectionConfig config;

    @Before
    public void setup() {
        when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(Arrays.asList(
                createRule("rule1"), createRule("rule2")));
        when(factory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getJdbcOperations()).thenReturn(jdbcOperations);
        when(environmentService.detail(Mockito.anyLong())).thenReturn(env);
        when(env.getRulesetId()).thenReturn(1L);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);
    }

    @Test
    public void testCheck() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users WHERE age > 18");
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(2, results.size());
        assertEquals("rule1", results.get(0).getRuleName());
        assertEquals("rule2", results.get(1).getRuleName());
        assertNotNull(results.get(0).getLocations());
        assertNotNull(results.get(1).getLocations());
    }

    @Test
    public void testCheckWithEmptyRules() {
        when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        List<CheckResult> results = sqlCheckService.check(session, new SqlCheckReq());
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithEmptyScript() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("");
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithEnvironment() {
        OffsetString sql = new OffsetString("SELECT * FROM users WHERE age > 18");
        List<OffsetString> sqls = Collections.singletonList(sql);
        when(env.getRulesetId()).thenReturn(1L);
        List<Rule> rules = Arrays.asList(createRule("rule1"), createRule("rule2"));
        when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation("rule1", "violation1"));
        when(jdbcOperations.query(Mockito.anyString(), Mockito.anyArray())).thenReturn(Collections.emptyList());
        when(factory.getDataSource()).thenReturn(dataSource);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcOperations);
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, sqlCheckRules);
        List<CheckResult> results = sqlCheckService.check(null, sqls, config);
        assertEquals(2, results.size());
        assertEquals("rule1", results.get(0).getRuleName());
        assertEquals("rule2", results.get(1).getRuleName());
        assertNotNull(results.get(0).getLocations());
        assertNotNull(results.get(1).getLocations());
        assertEquals("violation1", results.get(0).getViolations().get(0).getMessage());
        assertEquals("violation1", results.get(1).getViolations().get(0).getMessage());
    }

    @Test
    public void testCheckWithEmptySql() {
        OffsetString sql = new OffsetString("");
        List<OffsetString> sqls = Collections.singletonList(sql);
        when(env.getRulesetId()).thenReturn(1L);
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcOperations);
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, sqlCheckRules);
        List<CheckResult> results = sqlCheckService.check(null, sqls, config);
        assertEquals(0, results.size());
    }

    private Rule createRule(String name) {
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName(name);
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        return rule;
    }
}