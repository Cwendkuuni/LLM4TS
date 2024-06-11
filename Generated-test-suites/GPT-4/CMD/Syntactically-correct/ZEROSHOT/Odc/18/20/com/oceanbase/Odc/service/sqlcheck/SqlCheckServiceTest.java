package com.oceanbase.odc.service.sqlcheck;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {
    private SqlCheckService sqlCheckServiceUnderTest;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private ConnectionSession session;
    private SqlCheckReq req;
    private Rule rule;
    private Environment env;

    @Before
    public void setUp() {
        sqlCheckServiceUnderTest = new SqlCheckService();
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        session = mock(ConnectionSession.class);
        req = mock(SqlCheckReq.class);
        rule = mock(Rule.class);
        env = mock(Environment.class);
    }

    @Test
    public void testCheck() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<>(
                Arrays.asList(rule)));
        when(req.getScriptContent()).thenReturn("Select * from test");
        when(req.getDelimiter()).thenReturn(";");
        when(session.getDialectType()).thenReturn(DialectType.ORACLE);
        List<CheckResult> result = sqlCheckServiceUnderTest.check(session, req);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCheckWithEnvironmentId() {
        when(environmentService.detail(anyLong())).thenReturn(env);
        when(env.getRulesetId()).thenReturn(1L);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<>(
                Arrays.asList(rule)));
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.ORACLE);
        List<CheckViolation> result = sqlCheckServiceUnderTest.check(1L, "testDb", sqls, config);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetRulesWithSession() {
        when(session.getDialectType()).thenReturn(DialectType.ORACLE);
        when(session.getSyncJdbcExecutor(anyString())).thenReturn(mock(JdbcOperations.class));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<>(
                Arrays.asList(rule)));
        List<SqlCheckRule> result = sqlCheckServiceUnderTest.getRules(new ArrayList<>(Arrays.asList(rule)), session);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetRulesWithJdbc() {
        List<Rule> rules = new ArrayList<>();
        DialectType dialectType = DialectType.ORACLE;
        JdbcOperations jdbc = mock(JdbcOperations.class);
        List<SqlCheckRule> result = sqlCheckServiceUnderTest.getRules(rules, dialectType, jdbc);
        Assert.assertNotNull(result);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(SqlCheckRuleType.ColumnCaseCheck);
        List<CheckViolation> violations = new ArrayList<>(Arrays.asList(violation));
        when(rule.getMetadata()).thenReturn(new RuleMetadata());
        when(rule.getMetadata().getName()).thenReturn("${ColumnCaseCheck}");
        List<Rule> result = sqlCheckServiceUnderTest.fullFillRiskLevel(rules, violations);
        Assert.assertNotNull(result);
    }
}
