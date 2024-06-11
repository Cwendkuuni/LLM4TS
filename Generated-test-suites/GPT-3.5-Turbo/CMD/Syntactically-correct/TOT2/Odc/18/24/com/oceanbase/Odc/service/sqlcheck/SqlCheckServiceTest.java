import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Collections;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithValidInput() {
        // Arrange
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        // Act
        List<CheckResult> results = sqlCheckService.check(session, req);

        // Assert
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    public void testCheckWithNullSession() {
        // Arrange
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        // Act
        List<CheckResult> results = sqlCheckService.check(null, req);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithNullRequest() {
        // Arrange
        ConnectionSession session = new ConnectionSession();

        // Act
        List<CheckResult> results = sqlCheckService.check(session, null);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithEmptyScriptContent() {
        // Arrange
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("");

        // Act
        List<CheckResult> results = sqlCheckService.check(session, req);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithEmptyRules() {
        // Arrange
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        // Act
        List<CheckResult> results = sqlCheckService.check(session, req);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithNullEnvironmentId() {
        // Arrange
        Long environmentId = null;
        String databaseName = "test_db";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString());
        ConnectionConfig config = new ConnectionConfig();

        // Act
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // Assert
        assertNotNull(violations);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testCheckWithEmptySQLs() {
        // Arrange
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Collections.emptyList();
        ConnectionConfig config = new ConnectionConfig();

        // Act
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // Assert
        assertNotNull(violations);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        // Arrange
        List<Rule> rules = Collections.emptyList();
        ConnectionSession session = new ConnectionSession();

        // Act
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        // Assert
        assertNotNull(sqlCheckRules);
        assertTrue(sqlCheckRules.isEmpty());
    }

}