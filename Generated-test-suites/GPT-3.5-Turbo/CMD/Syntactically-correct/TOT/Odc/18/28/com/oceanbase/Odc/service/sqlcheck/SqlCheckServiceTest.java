import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        // Mocking dependencies
        sqlCheckService.ruleService = mock(RuleService.class);
        sqlCheckService.environmentService = mock(EnvironmentService.class);
    }

    @Test
    public void testCheck_WithValidInput_ReturnCheckResults() {
        // Prepare test data
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        
        // Test the method
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Verify the results
        assertNotNull(checkResults);
        // Add more assertions based on the actual implementation
    }

    @Test
    public void testCheck_WithNoRuleSetId_ReturnEmptyList() {
        // Prepare test data
        ConnectionSession session = new ConnectionSession();
        session.setRuleSetId(null);
        SqlCheckReq req = new SqlCheckReq();
        
        // Test the method
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Verify the results
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheck_WithInvalidSqlContent_ReturnCheckViolations() {
        // Prepare test data
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM Users WHERE id = ");
        
        // Test the method
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Verify the results
        // Assert that checkViolations exist in the checkResults
        // Add more specific assertions based on the actual implementation
    }

    @Test
    public void testCheck_WithValidEnvironmentAndSql_ReturnCheckViolations() {
        // Prepare test data
        Long environmentId = 123L;
        String databaseName = "mydb";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM Users", 0));
        ConnectionConfig config = new ConnectionConfig();
        
        // Test the method
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // Verify the results
        // Assert that checkViolations exist based on the provided test data
        // Add more specific assertions based on the actual implementation
    }

}