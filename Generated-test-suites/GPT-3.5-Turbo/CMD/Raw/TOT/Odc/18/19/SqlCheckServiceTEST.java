import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.junit.Test;

public class SqlCheckServiceTest {

    private RuleService ruleService = mock(RuleService.class);
    private EnvironmentService environmentService = mock(EnvironmentService.class);

    private SqlCheckService sqlCheckService = new SqlCheckService(ruleService, environmentService);

    @Test
    public void testCheck_ValidSession_ValidRequest_ReturnsCheckResults() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertNotNull(checkResults);
        assertEquals(1, checkResults.size());
        // Add more assertions here based on the expected behavior
    }

    @Test
    public void testCheck_InvalidSession_ValidRequest_ReturnsEmptyList() {
        ConnectionSession session = null;
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertNotNull(checkResults);
        assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheck_ValidEnvironmentId_ValidDatabaseName_ValidSqls_ValidConnectionConfig_ReturnsCheckViolations() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString(0, "SELECT * FROM table"));
        ConnectionConfig config = new ConnectionConfig();

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(checkViolations);
        assertFalse(checkViolations.isEmpty());
        // Add more assertions here based on the expected behavior
    }

    // Add more test cases for edge cases and error scenarios

}