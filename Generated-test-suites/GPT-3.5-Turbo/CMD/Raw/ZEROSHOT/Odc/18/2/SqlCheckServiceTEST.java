package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SqlCheckServiceTest {

    @Test
    public void testCheck() {
        // Create test data
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        // Call the method to be tested
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Assert the result
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEnvironment() {
        // Create test data
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();

        // Call the method to be tested
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // Assert the result
        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testGetRules() {
        // Create test data
        List<Rule> rules = new ArrayList<>();
        ConnectionSession session = new ConnectionSession();

        // Call the method to be tested
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        // Assert the result
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        // Create test data
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();

        // Call the method to be tested
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        // Assert the result
        assertEquals(0, violatedRules.size());
    }
}