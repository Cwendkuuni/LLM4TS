package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

public class SqlCheckServiceTest {

    @Test
    public void testCheckWithSessionAndReq() {
        // Setup
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();

        // Test
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Verify
        assertNotNull(checkResults);
    }

    @Test
    public void testCheckWithEnvironmentAndDatabase() {
        // Setup
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString(0, "SELECT * FROM table"));
        ConnectionConfig config = new ConnectionConfig();

        // Test
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // Verify
        assertNotNull(checkViolations);
    }

    @Test
    public void testGetRulesWithSession() {
        // Setup
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        List<Rule> rules = Collections.singletonList(new Rule());
        
        // Test
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        // Verify
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbc() {
        // Setup
        DialectType dialectType = DialectType.ORACLE;
        JdbcOperations jdbc = Mockito.mock(JdbcOperations.class);
        List<Rule> rules = Collections.singletonList(new Rule());
        
        // Test
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);

        // Verify
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        // Setup
        List<Rule> rules = Collections.singletonList(new Rule());
        List<CheckViolation> violations = Collections.singletonList(new CheckViolation());
        
        // Test
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        // Verify
        assertNotNull(violatedRules);
    }

}