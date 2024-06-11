import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithSessionAndReq() {
        // Typical use case
        // Test when session and request are valid
        // Expecting the list of CheckResult to be returned
        assertNotNull(sqlCheckService.check(new ConnectionSession(), new SqlCheckReq()));

        // Edge case
        // Test when session is null and request is valid
        // Expecting an empty list of CheckResult to be returned
        List<CheckResult> result = sqlCheckService.check(null, new SqlCheckReq());
        assertEquals(0, result.size());

        // Error scenario
        // Test when request is null
        // Expecting an IllegalArgumentException to be thrown
        assertThrows(IllegalArgumentException.class, () -> sqlCheckService.check(new ConnectionSession(), null));
    }

    @Test
    public void testCheckWithEnvironmentIdAndSqls() {
        // Typical use case
        // Test when environmentId, databaseName, sqls, and config are valid
        // Expecting the list of CheckViolation to be returned
        assertNotNull(sqlCheckService.check(1L, "dbname", new ArrayList<>(), new ConnectionConfig()));

        // Edge case
        // Test when sqls is empty
        // Expecting an empty list of CheckViolation to be returned
        List<CheckViolation> violations = sqlCheckService.check(1L, "dbname", new ArrayList<>(), new ConnectionConfig());
        assertEquals(0, violations.size());

        // Error scenario
        // Test when environmentId is null
        // Expecting an IllegalArgumentException to be thrown
        assertThrows(IllegalArgumentException.class, () -> sqlCheckService.check(null, "dbname", new ArrayList<>(), new ConnectionConfig()));
    }

    @Test
    public void testGetRules() {
        // Edge case
        // Test when rules list is empty
        // Expecting an empty list of SqlCheckRule to be returned
        List<SqlCheckRule> rules = sqlCheckService.getRules(new ArrayList<>(), DialectType.MYSQL, null);
        assertEquals(0, rules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        // Edge case
        // Test when rules and violations list are empty
        // Expecting an empty list of Rule to be returned
        List<Rule> rules = sqlCheckService.fullFillRiskLevel(new ArrayList<>(), new ArrayList<>());
        assertEquals(0, rules.size());
    }
}