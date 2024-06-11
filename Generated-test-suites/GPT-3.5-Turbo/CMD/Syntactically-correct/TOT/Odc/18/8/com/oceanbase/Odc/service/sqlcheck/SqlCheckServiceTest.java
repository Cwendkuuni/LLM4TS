import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithValidSessionAndRequest() {
        // Test case for check method with valid ConnectionSession and SqlCheckReq
        // Typical use case
        // Expected: List of CheckResult is returned
    }

    @Test
    public void testCheckWithNullSession() {
        // Test case for check method with null ConnectionSession
        // Error scenario
        // Expected: Empty list is returned
    }

    @Test
    public void testCheckWithEmptyRules() {
        // Test case for check method with empty rules
        // Edge case
        // Expected: Empty list is returned
    }

    @Test
    public void testCheckWithValidEnvironmentIdAndParams() {
        // Test case for check method with valid environmentId, databaseName, sqls, and config
        // Typical use case
        // Expected: List of CheckViolation is returned
    }

    @Test
    public void testCheckWithEmptySqlList() {
        // Test case for check method with empty sqls list
        // Edge case
        // Expected: Empty list is returned
    }

    @Test
    public void testGetRulesWithValidSession() {
        // Test case for getRules method with valid ConnectionSession
        // Typical use case
        // Expected: List of SqlCheckRule is returned
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        // Test case for getRules method with empty rules
        // Edge case
        // Expected: Empty list is returned
    }

    @Test
    public void testFullFillRiskLevelWithValidRulesAndViolations() {
        // Test case for fullFillRiskLevel method with valid rules and violations
        // Typical use case
        // Expected: List of Rule is returned
    }

}