import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class SqlCheckServiceTest {

    @Test
    public void testCheck() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEnvironment() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM table", 0));
        ConnectionConfig config = new ConnectionConfig();
        
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertEquals(0, checkViolations.size());
    }
}