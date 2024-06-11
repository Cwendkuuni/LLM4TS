package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
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
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private JdbcOperations jdbcTemplate;
    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        jdbcTemplate = mock(JdbcOperations.class);
        singleConnectionDataSource = mock(SingleConnectionDataSource.class);
        when(singleConnectionDataSource.getDataSource()).thenReturn(new JdbcTemplate(jdbcTemplate));
        sqlCheckService = new SqlCheckService() {
            {
                this.ruleService = ruleService;
                this.environmentService = environmentService;
            }
        };
    }

    @Test
    public void testCheckWithNullSessionAndNullReq() {
        assertNull(sqlCheckService.check(null, null));
    }

    @Test
    public void testCheckWithNullSessionAndValidReq() {
        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        assertNull(sqlCheckService.check(null, sqlCheckReq));
    }

    @Test
    public void testCheckWithValidSessionAndNullReq() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        assertNull(sqlCheckService.check(session, null));
    }

    @Test
    public void testCheckWithValidSessionAndEmptyScriptContent() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(Mockito.anyLong(), Mockito.eq(QueryRuleMetadataParams.builder().build()))).thenReturn(Collections.emptyList());
        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);
        assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheckWithValidSessionAndValidScriptContent() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata("rule1", RuleType.SQL_CHECK, null, null));
        rule1.setEnabled(true);
        Rule rule2 = new Rule();
        rule2.setMetadata(new RuleMetadata("rule2", RuleType.SQL_CHECK, null, null));
        rule2.setEnabled(false);
        rules.add(rule1);
        rules.add(rule2);
        when(ruleService.list(Mockito.anyLong(), Mockito.eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertTrue(sqlCheckRules.isEmpty());
        List<CheckViolation> checkViolations = Collections.singletonList(new CheckViolation("violation1", "message1"));
        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);
        assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheckWithValidSessionAndValidScriptContentAndViolations() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata("rule1", RuleType.SQL_CHECK, null, null));
        rule1.setEnabled(true);
        Rule rule2 = new Rule();
        rule2.setMetadata(new RuleMetadata("rule2", RuleType.SQL_CHECK, null, null));
        rule2.setEnabled(false);
        rules.add(rule1);
        rules.add(rule2);
        when(ruleService.list(Mockito.anyLong(), Mockito.eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        SqlCheckRule sqlCheckRule1 = Mockito.mock(SqlCheckRule.class);
        when(sqlCheckRule1.check("scriptContent1")).thenReturn(Collections.singletonList(new CheckViolation("violation1", "message1")));
        List<SqlCheckRule> sqlCheckRulesList = new ArrayList<>();
        sqlCheckRulesList.add(sqlCheckRule1);
        when(SqlCheckRules.getAllFactories(Mockito.eq(DialectType.MYSQL), Mockito.eq(jdbcTemplate))).thenReturn(sqlCheckRulesList);
        List<CheckViolation> checkViolations = Collections.singletonList(new CheckViolation("violation1", "message1"));
        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);
        List<CheckViolation> expectedCheckViolations = sqlCheckService.getRules(rules, session).get(0).check("scriptContent1");
        assertEquals(expectedCheckViolations, checkViolations);
        assertEquals(1, checkResults.size());
        CheckResult checkResult = checkResults.get(0);
        assertEquals("violation1", checkResult.getViolations().get(0).getType().getName());
        assertEquals("message1", checkResult.getViolations().get(0).getMessage());
    }

    @Test
    public void testCheckWithEnvironmentIdAndEmptySqlsList() {
        Environment environment = new Environment();
        when(environmentService.detail(Mockito.anyLong())).thenReturn(environment);
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata("rule1", RuleType.SQL_CHECK, null, null));
        rule.setEnabled(true);
        rules.add(rule);
        when(ruleService.list(environment.getRulesetId(), Mockito.eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        assertFalse(sqlCheckService.check(Mockito.anyLong(), null, Collections.emptyList(), Mockito.any(ConnectionConfig.class)).isEmpty());
    }

    @Test
    public void testCheckWithEnvironmentIdAndValidSqlsList() {
        Environment environment = new Environment();
        when(environmentService.detail(Mockito.anyLong())).thenReturn(environment);
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata("rule1", RuleType.SQL_CHECK, null, null));
        rule.setEnabled(true);
        rules.add(rule);
        OffsetString sql1 = new OffsetString("sql1");
        OffsetString sql2 = new OffsetString("sql2");
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(sql1);
        sqls.add(sql2);
        ConnectionConfig config = new ConnectionConfig();
        when(ruleService.list(environment.getRulesetId(), Mockito.eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);
        SqlCheckContext checkContext = new SqlCheckContext(2);
        List<CheckViolation> checkViolations = sqlCheckService.check(Mockito.anyLong(), null, sqls, config);
        assertEquals(2, checkViolations.size());
    }

}