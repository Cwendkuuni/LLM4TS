package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SqlCheckServiceTest {

    @Test
    public void testCheck_ConnectionSessionSqlCheckReq() {
        // Test when ruleSetId is null
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertNotNull(result);
        assertEquals(0, result.size());

        // Test when rules exist
        // Add test cases here...

    }

    @Test
    public void testCheck_LongStringListOffsetStringConnectionConfig() {
        // Test with empty sqls
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = Collections.emptyList();
        ConnectionConfig config = new ConnectionConfig();
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(violations);
        assertEquals(0, violations.size());

        // Test with valid sqls
        // Add test cases here...

    }

    @Test
    public void testGetRules_ConnectionSession() {
        // Test with empty rules
        ConnectionSession session = new ConnectionSession();
        List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.emptyList(), session);
        assertNotNull(rules);
        assertEquals(0, rules.size());

        // Test with valid rules
        // Add test cases here...

    }

    @Test
    public void testGetRules_Rule() {
        // Test with null rules
        List<Rule> rules = null;
        List<SqlCheckRule> sqlRules = sqlCheckService.getRules(rules, DialectType.MYSQL, new JdbcTemplate());
        assertNotNull(sqlRules);
        assertEquals(0, sqlRules.size());

        // Test with valid rules
        // Add test cases here...

    }

    @Test
    public void testFullFillRiskLevel() {
        // Test with empty rules and violations
        List<Rule> rules = Collections.emptyList();
        List<CheckViolation> violations = Collections.emptyList();
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(violatedRules);
        assertEquals(0, violatedRules.size());

        // Test with valid rules and violations
        // Add test cases here...

    }

    // Add more test cases for other methods in SqlCheckService if needed

}