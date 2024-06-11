import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

public class SqlCheckServiceTest {

    @Test
    public void testCheckWithValidData() {
        // Test check method with valid data
        SqlCheckService sqlCheckService = new SqlCheckService();
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithEmptyScriptContent() {
        // Test check method with empty script content
        SqlCheckService sqlCheckService = new SqlCheckService();
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("");
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithNullSession() {
        // Test check method with null session
        SqlCheckService sqlCheckService = new SqlCheckService();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        List<CheckResult> results = sqlCheckService.check(null, req);
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithNullRequest() {
        // Test check method with null request
        SqlCheckService sqlCheckService = new SqlCheckService();
        ConnectionSession session = new ConnectionSession();
        List<CheckResult> results = sqlCheckService.check(session, null);
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        // Test getRules method with empty rules list
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.emptyList(), DialectType.MYSQL, new JdbcTemplate());
        assertNotNull(rules);
        assertEquals(0, rules.size());
    }

    @Test
    public void testFullFillRiskLevelWithEmptyViolations() {
        // Test fullFillRiskLevel method with empty violations list
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckViolation> violations = Collections.emptyList();
        List<Rule> rules = new ArrayList<>();
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(violatedRules);
        assertEquals(0, violatedRules.size());
    }

}