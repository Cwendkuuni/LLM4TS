import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheck_ValidSessionAndRequest_ReturnCheckResults() {
        // Prepare data
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        // Execute method
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Verify result
        assertEquals(0, result.size());
    }

    @Test
    public void testCheck_InvalidSession_ReturnEmptyList() {
        // Prepare data
        ConnectionSession session = null;
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        // Execute method
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Verify result
        assertEquals(0, result.size());
    }

    @Test
    public void testCheck_InvalidScriptContent_ReturnEmptyList() {
        // Prepare data
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(null);

        // Execute method
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Verify result
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckEnvironment_ValidInput_ReturnCheckViolations() {
        // Prepare data
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();

        // Execute method
        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // Verify result
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckEnvironment_EmptySqls_ReturnEmptyList() {
        // Prepare data
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();

        // Execute method
        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // Verify result
        assertEquals(0, result.size());
    }

    @Test
    public void testGetRules_ValidRules_ReturnSqlCheckRules() {
        // Prepare data
        List<Rule> rules = new ArrayList<>();

        // Execute method
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, DialectType.MYSQL, mock(JdbcTemplate.class));

        // Verify result
        assertEquals(0, result.size());
    }

}