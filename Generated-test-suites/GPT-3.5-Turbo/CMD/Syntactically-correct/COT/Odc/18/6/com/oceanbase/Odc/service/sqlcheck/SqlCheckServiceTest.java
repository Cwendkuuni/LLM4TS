
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckSession() {
        // Test case for check(ConnectionSession session, SqlCheckReq req)
        // Edge case: when ruleSetId is null
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertNotNull(checkResults);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckEnvironment() {
        // Test case for check(Long environmentId, String databaseName, List<OffsetString> sqls, ConnectionConfig config)
        // Edge case: when sqls is empty
        Long environmentId = 1L;
        String databaseName = "testDatabase";
        List<OffsetString> sqls = Collections.emptyList();
        ConnectionConfig config = new ConnectionConfig();
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(checkViolations);
        assertEquals(0, checkViolations.size());
    }
}
