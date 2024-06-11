import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import java.util.List;

public class SqlCheckServiceTest {

    @Mock
    ConnectionSession session;

    @Mock
    SqlCheckReq req;

    @Mock
    EnvironmentService environmentService;

    @Mock
    JdbcTemplate jdbc;

    SqlCheckService sqlCheckService = new SqlCheckService();

    @Test
    public void testCheckValidSessionAndRequest() {
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckEmptySqls() {
        List<OffsetString> sqls = new ArrayList<>();
        String databaseName = "testdb";
        Long environmentId = 1L;
        ConnectionConfig config = new ConnectionConfig();
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(violations);
        assertEquals(0, violations.size());
    }

    @Test
    public void testGetRulesWithValidSession() {
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
        assertEquals(0, sqlCheckRules.size());
    }

}