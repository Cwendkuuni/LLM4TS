
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class SqlCheckServiceTest {

    @Test
    public void testCheckWithValidSessionAndRequest() {
        // Test case for valid session and request
        // Create a mock ConnectionSession and SqlCheckReq
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM TABLE");

        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckResult> results = sqlCheckService.check(session, req);

        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    public void testCheckWithInvalidSessionAndRequest() {
        // Test case for invalid session and request
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();

        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckResult> results = sqlCheckService.check(session, req);

        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithValidEnvironmentAndConfig() {
        // Test case for valid environment ID, database name, SQLs, and config
        Long environmentId = 123L;
        String databaseName = "testDB";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM TABLE"));
        ConnectionConfig config = new ConnectionConfig();

        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(violations);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testCheckWithInvalidSQLs() {
        // Test case for invalid SQLs
        Long environmentId = 123L;
        String databaseName = "testDB";
        List<OffsetString> sqls = Arrays.asList();
        ConnectionConfig config = new ConnectionConfig();

        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(violations);
        assertEquals(0, violations.size());
    }

    @Test
    public void testGetRulesWithValidSession() {
        // Test case for getting rules with a valid session
        ConnectionSession session = new ConnectionSession();
        List<Rule> rules = Arrays.asList(new Rule());
        
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        
        assertNotNull(sqlCheckRules);
        assertFalse(sqlCheckRules.isEmpty());
    }
    
}
