import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    public void testCheckWithSessionAndRequest() {
        // Test case for check method with session and request
        assertNotNull(sqlCheckService);

        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM TABLE");

        List<CheckResult> result = sqlCheckService.check(session, req);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithEnvironmentIdAndSqls() {
        // Test case for check method with environment id and SQLs
        assertNotNull(sqlCheckService);

        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM TABLE", 0, 0));
        ConnectionConfig config = new ConnectionConfig();

        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(violations);
        assertEquals(0, violations.size());
    }

    @Test
    public void testGetRulesFromSession() {
        // Test case for getRules method with session
        assertNotNull(sqlCheckService);

        ConnectionSession session = mock(ConnectionSession.class);

        List<SqlCheckRule> rules = sqlCheckService.getRules(new ArrayList<>(), session);
        assertNotNull(rules);
        assertEquals(0, rules.size());
    }

    @Test
    public void testGetRulesFromDialectAndJdbc() {
        // Test case for getRules method with dialect and JDBC
        assertNotNull(sqlCheckService);

        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = mock(JdbcTemplate.class);

        List<SqlCheckRule> rules = sqlCheckService.getRules(new ArrayList<>(), dialectType, jdbc);
        assertNotNull(rules);
        assertEquals(0, rules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        // Test case for fullFillRiskLevel method
        assertNotNull(sqlCheckService);

        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(violatedRules);
        assertEquals(0, violatedRules.size());
    }
}