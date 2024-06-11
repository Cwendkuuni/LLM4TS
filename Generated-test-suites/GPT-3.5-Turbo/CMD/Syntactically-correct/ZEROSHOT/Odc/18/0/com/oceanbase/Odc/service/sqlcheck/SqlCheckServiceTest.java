import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    
    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }
    
    @Test
    public void testCheckWithSession() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        Long environmentId = 1L;
        String databaseName = "test_database";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testGetRulesWithSession() {
        List<Rule> rules = new ArrayList<>();
        ConnectionSession session = mock(ConnectionSession.class);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithJdbcOperations() {
        List<Rule> rules = new ArrayList<>();
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = mock(JdbcOperations.class);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(0, violatedRules.size());
    }
}