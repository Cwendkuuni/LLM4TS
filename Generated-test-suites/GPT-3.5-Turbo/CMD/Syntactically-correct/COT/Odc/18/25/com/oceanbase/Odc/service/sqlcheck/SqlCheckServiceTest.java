
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SqlCheckServiceTest {

    @Test
    public void testCheckWithConnectionSessionAndSqlCheckReq() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertNotNull(result);
    }

    @Test
    public void testCheckWithEnvironmentIdAndDatabaseNameAndSqlsAndConnectionConfig() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        Long environmentId = 1L;
        String databaseName = "testDb";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(result);
    }

    @Test
    public void testGetRulesWithConnectionSession() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<Rule> rules = new ArrayList<>();
        ConnectionSession session = new ConnectionSession();
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);
        assertNotNull(result);
    }

    @Test
    public void testGetRulesWithRulesAndDialectTypeAndJdbcOperations() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<Rule> rules = new ArrayList<>();
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = new JdbcTemplate();
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, dialectType, jdbc);
        assertNotNull(result);
    }

    @Test
    public void testFullFillRiskLevelWithRulesAndViolations() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(result);
    }
}
