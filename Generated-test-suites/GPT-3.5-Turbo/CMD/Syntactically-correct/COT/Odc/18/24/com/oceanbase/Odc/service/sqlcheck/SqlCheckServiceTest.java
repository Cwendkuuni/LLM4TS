
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;

public class SqlCheckServiceTest {

    private static SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheck() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = List.of(new OffsetString());
        ConnectionConfig config = new ConnectionConfig();
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = List.of(new Rule());
        ConnectionSession session = new ConnectionSession();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertFalse(sqlCheckRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = List.of(new Rule());
        List<CheckViolation> violations = List.of(new CheckViolation());
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertFalse(violatedRules.isEmpty());
    }
}
