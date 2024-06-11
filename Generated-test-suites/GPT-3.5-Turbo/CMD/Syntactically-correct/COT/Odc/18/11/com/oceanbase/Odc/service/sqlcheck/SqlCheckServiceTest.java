
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

public class SqlCheckServiceTest {
    private SqlCheckService sqlCheckService;
    
    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }
    
    @Test
    public void testCheck_ValidSessionAndReq() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        
        List<CheckResult> result = sqlCheckService.check(session, req);
        
        assertNotNull(result);
    }
    
    @Test
    public void testCheck_InvalidSession() {
        SqlCheckReq req = new SqlCheckReq();
        
        List<CheckResult> result = sqlCheckService.check(null, req);
        
        assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    public void testCheck_InvalidReq() {
        ConnectionSession session = mock(ConnectionSession.class);
        
        List<CheckResult> result = sqlCheckService.check(session, null);
        
        assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    public void testCheckViolation() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM Table"));
        ConnectionConfig config = new ConnectionConfig();
        
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertNotNull(violations);
    }
}
