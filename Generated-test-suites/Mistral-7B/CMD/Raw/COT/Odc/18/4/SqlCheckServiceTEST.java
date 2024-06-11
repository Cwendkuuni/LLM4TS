package com.oceanbase.odc.service.sqlcheck.test;

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
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.test.util.TestData;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

/**
 * {@link SqlCheckServiceTest}
 *
 * @author yh263208
 * @date 2023-02-28 11:01
 */
@Slf4j
public class SqlCheckServiceTest {

    private ConnectionSession session;
    private ConnectionConfig config;
    private JdbcTemplate jdbcTemplate;
    private SqlCheckService service;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private List<Rule> rules;

    @Before
    public void setUp() {
        this.session = mock(ConnectionSession.class);
        this.config = TestData.getConnectionConfig();
        this.jdbcTemplate = mock(JdbcTemplate.class);
        this.service = new SqlCheckService();
        this.ruleService = mock(RuleService.class);
        this.environmentService = mock(EnvironmentService.class);
        this.rules = Arrays.asList(TestData.getRule1(), TestData.getRule2());

        ConnectionSessionUtil.setSession(this.session);
        when(this.session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(this.session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(this.jdbcTemplate);
        when(this.ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(this.rules);

        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(this.config, true, false);
        when(factory.getDataSource()).thenReturn(dataSource);

        this.service.setRuleService(this.ruleService);
        this.service.setEnvironmentService(this.environmentService);
    }

    @Test
    public void testCheckWithSession() {
        String scriptContent = "select * from test where id = 1";
        SqlCheckReq req = TestData.getSqlCheckReq();
        req.setScriptContent(scriptContent);

        List<CheckViolation> checkViolations = Arrays.asList(TestData.getCheckViolation1(), TestData.getCheckViolation2());
        when(this.jdbcTemplate.query(TestData.getCheckSql1(), new Object[0]))
                .thenReturn(TestData.getCheckSqlResult1());
        when(this.jdbcTemplate.query(TestData.getCheckSql2(), new Object[0]))
                .thenReturn(TestData.getCheckSqlResult2());

        List<Rule> violatedRules = this.service.fullFillRiskLevel(this.rules, checkViolations);
        List<CheckResult> results = this.service.check(this.session, req);

        assertEquals(2, results.size());
        assertEquals(TestData.getCheckResult1(), results.get(0));
        assertEquals(TestData.getCheckResult2(), results.get(1));
        assertEquals(2, violatedRules.size());
        assertEquals(TestData.getRule1(), violatedRules.get(0));
        assertEquals(TestData.getRule2(), violatedRules.get(1));
    }

    @Test
    public void testCheckWithoutRuleSet() {
        when(ConnectionSessionUtil.getRuleSetId(this.session)).thenReturn(null);
        String scriptContent = "select * from test where id = 1";
        SqlCheckReq req = TestData.getSqlCheckReq();
        req.setScriptContent(scriptContent);

        List<CheckViolation> checkViolations = Collections.emptyList();
        List<CheckResult> results = this.service.check(this.session, req);

        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithEmptyRules() {
        List<Rule> emptyRules = Collections.emptyList();
        when(this.ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(emptyRules);
        String scriptContent = "select * from test where id = 1";
        SqlCheckReq req = TestData.getSqlCheckReq();
        req.setScriptContent(scriptContent);

        List<CheckViolation> checkViolations = Collections.emptyList();
        List<CheckResult> results = this.service.check(this.session, req);

        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithSqls() {
        String scriptContent1 = "select * from test where id = 1";
        String scriptContent2 = "select * from test where id = 2";
        List<OffsetString> sqls = Arrays.asList(new OffsetString(0, scriptContent1), new OffsetString(1, scriptContent2));

        List<CheckViolation> checkViolations1 = Arrays.asList(TestData.getCheckViolation1());
        List<CheckViolation> checkViolations2 = Arrays.asList(TestData.getCheckViolation2());

        when(this.jdbcTemplate.query(TestData.getCheckSql1(), new Object[0]))
                .thenReturn(TestData.getCheckSqlResult1());
        when(this.jdbcTemplate.query(TestData.getCheckSql2(), new Object[0]))
                .thenReturn(TestData.getCheckSqlResult2());

        List<Rule> violatedRules1 = this.service.fullFillRiskLevel(this.rules, checkViolations1);
        List<Rule> violatedRules2 = this.service.fullFillRiskLevel(this.rules, checkViolations2);

        List<CheckResult> results = this.service.check(this.config, "test_db", sqls, this.config);

        assertEquals(2, results.size());
        assertEquals(TestData.getCheckResult1(), results.get(0));
        assertEquals(TestData.getCheckResult2(), results.get(1));
        assertEquals(2, violatedRules1.size());
        assertEquals(TestData.getRule1(), violatedRules1.get(0));
        assertEquals(2, violatedRules2.size());
        assertEquals(TestData.getRule2(), violatedRules2.get(0));
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        List<Rule> emptyRules = Collections.emptyList();
        List<SqlCheckRule> checkRules = this.service.getRules(emptyRules, DialectType.MYSQL, this.jdbcTemplate);

        assertEquals(0, checkRules.size());
    }

    @Test
    public void testGetRulesWithRules() {
        List<Rule> rules = Arrays.asList(TestData.getRule1(), TestData.getRule2());
        List<SqlCheckRule> checkRules = this.service.getRules(rules, DialectType.MYSQL, this.jdbcTemplate);

        assertEquals(2, checkRules.size());
        assertEquals(TestData.getCheckRule1(), checkRules.get(0));
        assertEquals(TestData.getCheckRule2(), checkRules.get(1));
    }
}