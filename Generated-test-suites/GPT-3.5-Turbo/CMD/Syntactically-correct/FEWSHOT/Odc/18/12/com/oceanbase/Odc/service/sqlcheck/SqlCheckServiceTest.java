package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SqlCheckServiceTest {

    @Test
    public void testCheckWithConnectionSessionAndSqlCheckReq() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        
        assertNotNull(checkResults);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEnvironmentIdAndDatabaseNameAndSqlsAndConnectionConfig() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertNotNull(checkViolations);
        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testGetRulesWithConnectionSession() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(new ArrayList<>(), session);
        
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbcOperations() {
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = new JdbcTemplate();
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(new ArrayList<>(), dialectType, jdbc);
        
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        
        assertNotNull(violatedRules);
        assertEquals(0, violatedRules.size());
    }
}