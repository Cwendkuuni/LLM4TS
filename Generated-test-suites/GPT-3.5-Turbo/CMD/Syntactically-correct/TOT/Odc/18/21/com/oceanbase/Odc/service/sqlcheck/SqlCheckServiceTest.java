import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private ConnectionSession session;
    private SqlCheckReq req;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        session = mock(ConnectionSession.class);
        req = new SqlCheckReq();
    }

    @Test
    public void testCheck_validSessionAndReq() {
        // Prepare test data
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getRuleSetId()).thenReturn(123L);
        req.setDelimiter(";");
        req.setScriptContent("SELECT * FROM table");

        // Execute the method
        List<CheckResult> results = sqlCheckService.check(session, req);

        // Verify the results
        assertEquals(1, results.size());
        // Add more verification steps based on the expected behavior
    }

    @Test
    public void testCheck_emptyRuleSetId() {
        // Prepare test data
        when(session.getRuleSetId()).thenReturn(null);

        // Execute the method
        List<CheckResult> results = sqlCheckService.check(session, req);

        // Verify the results
        assertEquals(0, results.size());
        // Add more verification steps based on the expected behavior
    }

    @Test
    public void testCheck_emptyRules() {
        // Prepare test data
        when(session.getRuleSetId()).thenReturn(123L);

        // Execute the method
        List<CheckResult> results = sqlCheckService.check(session, req);

        // Verify the results
        assertEquals(0, results.size());
        // Add more verification steps based on the expected behavior
    }

    // Add more test cases for other methods in the SqlCheckService class

}