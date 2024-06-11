import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
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
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);
        
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        
        assertNotNull(checkResults);
    }

    @Test
    public void testCheckWithEnvironmentIdAndDatabaseName() {
        Long environmentId = 1L;
        String databaseName = "testDatabase";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = mock(ConnectionConfig.class);
        
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertNotNull(checkViolations);
    }

    @Test
    public void testGetRulesWithSession() {
        List<Rule> rules = new ArrayList<>();
        ConnectionSession session = mock(ConnectionSession.class);
        
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbcOperations() {
        List<Rule> rules = new ArrayList<>();
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = mock(JdbcOperations.class);
        
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);
        
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        
        assertNotNull(violatedRules);
    }
}