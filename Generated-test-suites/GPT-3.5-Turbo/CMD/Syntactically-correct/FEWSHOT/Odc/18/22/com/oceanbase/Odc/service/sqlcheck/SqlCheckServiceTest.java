import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithSessionAndReq() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> result = sqlCheckService.check(session, req);
        
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithEnvironmentIdAndDatabaseName() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql1 = new OffsetString("SELECT * FROM table1", 0);
        OffsetString sql2 = new OffsetString("SELECT * FROM table2", 0);
        sqls.add(sql1);
        sqls.add(sql2);
        ConnectionConfig config = new ConnectionConfig();
        
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertNotNull(violations);
        assertEquals(0, violations.size());
    }

    @Test
    public void testGetRulesWithSession() {
        ConnectionSession session = new ConnectionSession();
        List<Rule> rules = new ArrayList<>();
        
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        
        assertNotNull(sqlCheckRules);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbc() {
        DialectType dialectType = DialectType.MYSQL;
        JdbcTemplate jdbc = new JdbcTemplate();
        List<Rule> rules = new ArrayList<>();
        
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);
        
        assertNotNull(sqlCheckRules);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation("Violation1", "Description1");
        CheckViolation violation2 = new CheckViolation("Violation2", "Description2");
        violations.add(violation1);
        violations.add(violation2);
        
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        
        assertNotNull(violatedRules);
        assertEquals(2, violatedRules.size());
        assertEquals("Violation1", violatedRules.get(0).getViolation().getName());
        assertEquals("Violation2", violatedRules.get(1).getViolation().getName());
    }
}