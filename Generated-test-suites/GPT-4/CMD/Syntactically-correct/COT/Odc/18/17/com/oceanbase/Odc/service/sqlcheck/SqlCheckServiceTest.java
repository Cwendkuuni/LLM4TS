package com.oceanbase.odc.service.sqlcheck;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {
    private SqlCheckService sqlCheckService;
    private ConnectionSession mockSession;
    private SqlCheckReq mockReq;
    private ConnectionConfig mockConfig;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        mockSession = mock(ConnectionSession.class);
        mockReq = mock(SqlCheckReq.class);
        mockConfig = mock(ConnectionConfig.class);
    }

    @Test
    public void testCheckWithSessionAndReq() {
        List<CheckResult> checkResults = sqlCheckService.check(mockSession, mockReq);
        assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "myDatabase", new ArrayList<>(), mockConfig);
        assertTrue(checkViolations.isEmpty());
    }

    @Test
    public void testGetRulesWithSession() {
        List<SqlCheckRule> checkRules = sqlCheckService.getRules(new ArrayList<>(), mockSession);
        assertTrue(checkRules.isEmpty());
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbc() {
        JdbcOperations mockJdbc = mock(JdbcOperations.class);
        List<SqlCheckRule> checkRules = sqlCheckService.getRules(new ArrayList<>(), DialectType.MYSQL, mockJdbc);
        assertTrue(checkRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = sqlCheckService.fullFillRiskLevel(new ArrayList<>(), new ArrayList<>());
        assertTrue(rules.isEmpty());
    }
}