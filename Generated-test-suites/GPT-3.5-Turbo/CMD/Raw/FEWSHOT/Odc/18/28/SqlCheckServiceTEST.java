package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;

import java.util.ArrayList;
import java.util.List;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheck_ConnectionSession_ValidSqlCheckReq() {
        // Create a mock ConnectionSession and SqlCheckReq
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        
        // Call the check method with the mock objects
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        
        // Assert that the checkResults list is not null
        assertNotNull(checkResults);
    }

    @Test
    public void testCheck_EnvironmentId_DatabaseName_Sqls_ConnectionConfig() {
        // Create mock parameters for the method
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        
        // Create a mock ConnectionConfig
        ConnectionConfig config = new ConnectionConfig();
        
        // Call the check method with the mock parameters
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        // Assert that the checkViolations list is not null
        assertNotNull(checkViolations);
    }

    @Test
    public void testGetRules_ConnectionSession() {
        // Create a mock ConnectionSession
        ConnectionSession session = new ConnectionSession();
        
        // Call the getRules method with the mock session
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(new ArrayList<Rule>(), session);
        
        // Assert that the sqlCheckRules list is not null
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testGetRules_Rules_DialectType_JdbcOperations() {
        // Create mock parameters for the method
        List<Rule> rules = new ArrayList<>();
        DialectType dialectType = DialectType.MYSQL;
        JdbcTemplate jdbc = new JdbcTemplate();
        
        // Call the getRules method with the mock parameters
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);
        
        // Assert that the sqlCheckRules list is not null
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel_Rules_CheckViolations() {
        // Create mock parameters for the method
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        
        // Call the fullFillRiskLevel method with the mock parameters
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        
        // Assert that the violatedRules list is not null
        assertNotNull(violatedRules);
    }
}