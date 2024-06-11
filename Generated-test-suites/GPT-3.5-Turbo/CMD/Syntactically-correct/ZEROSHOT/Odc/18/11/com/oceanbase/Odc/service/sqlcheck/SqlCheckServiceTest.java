package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private Validator validator;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testCheckWithConnectionSessionAndSqlCheckReq() {
        // Create a ConnectionSession and SqlCheckReq object
        // Perform validation on these objects

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

        // Assert the checkResults list is not null
        assertNotNull(checkResults);
        // Assert the size of checkResults list
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEnvironmentIdAndDatabaseNameAndSqlsAndConnectionConfig() {
        // Create environmentId, databaseName, sqls and connectionConfig objects
        // Perform validation on these objects

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);

        // Assert the checkViolations list is not null
        assertNotNull(checkViolations);
        // Assert the size of checkViolations list
        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testGetRulesWithConnectionSession() {
        // Create a ConnectionSession object
        // Perform validation on the object

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        // Assert the sqlCheckRules list is not null
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testGetRulesWithRulesAndDialectTypeAndJdbcOperations() {
        // Create rules, dialectType and JdbcOperations objects
        // Perform validation on these objects

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbcOperations);

        // Assert the sqlCheckRules list is not null
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        // Create rules and CheckViolation objects
        // Perform validation on these objects

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        // Assert the violatedRules list is not null
        assertNotNull(violatedRules);
    }
}