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
        // Setup
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        
        // Test
        List<CheckResult> result = SqlCheckService.check(session, req);
        
        // Verify
        assertNotNull(result);
    }

    @Test
    public void testCheckWithEnvironmentIdAndDatabaseName() {
        // Setup
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        
        // Test
        List<CheckViolation> violations = SqlCheckService.check(environmentId, databaseName, sqls, config);
        
        // Verify
        assertNotNull(violations);
    }

    @Test
    public void testGetRulesWithConnectionSession() {
        // Setup
        List<Rule> rules = new ArrayList<>();
        ConnectionSession session = new ConnectionSession();
        
        // Test
        List<SqlCheckRule> sqlCheckRules = SqlCheckService.getRules(rules, session);
        
        // Verify
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbcOperations() {
        // Setup
        List<Rule> rules = new ArrayList<>();
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = new JdbcTemplate(new SingleConnectionDataSource());
        
        // Test
        List<SqlCheckRule> sqlCheckRules = SqlCheckService.getRules(rules, dialectType, jdbc);
        
        // Verify
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        // Setup
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        
        // Test
        List<Rule> violatedRules = SqlCheckService.fullFillRiskLevel(rules, violations);
        
        // Verify
        assertNotNull(violatedRules);
    }

}