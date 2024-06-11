package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    
    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = mock(RuleService.class);
        sqlCheckService.environmentService = mock(EnvironmentService.class);
    }

    @Test
    public void testCheckWithConnectionSessionAndSqlCheckReq() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(123L);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertNotNull(checkResults);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEnvironmentIdAndDatabaseName() {
        long environmentId = 456L;
        String databaseName = "test_db";
        OffsetString sql = new OffsetString("SELECT * FROM table", 0, 20);
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(sql);
        ConnectionConfig config = new ConnectionConfig();
        when(sqlCheckService.environmentService.detail(environmentId)).thenReturn(new Environment());
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(checkViolations);
        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testGetRulesWithConnectionSession() {
        ConnectionSession session = mock(ConnectionSession.class);
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbcOperations() {
        DialectType dialectType = DialectType.ORACLE;
        JdbcOperations jdbc = mock(JdbcOperations.class);
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);
        assertNotNull(sqlCheckRules);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setType("TestViolation");
        checkViolation.setMessage("Test violation message");
        checkViolations.add(checkViolation);
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);
        assertNotNull(violatedRules);
        assertEquals(1, violatedRules.size());
        assertEquals("TestViolation", violatedRules.get(0).getViolation().getType());
    }
}