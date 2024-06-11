import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.core.session.ConnectionSession;
import java.util.ArrayList;
import java.util.List;
import org.mockito.Mockito;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithValidSessionAndRequest() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithInvalidSession() {
        ConnectionSession session = null;
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithEnvironmentIdAndSqls() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        sqls.add(new OffsetString("SELECT * FROM table", 0));
        List<CheckViolation> results = sqlCheckService.check(environmentId, databaseName, sqls, null);
        assertNotNull(results);
        assertEquals(1, results.size());
    }

}