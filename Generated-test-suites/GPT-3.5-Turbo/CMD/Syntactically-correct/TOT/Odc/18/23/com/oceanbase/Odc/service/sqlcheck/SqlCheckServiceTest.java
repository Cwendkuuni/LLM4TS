import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.service.sqlcheck.CheckResult;
import com.oceanbase.odc.service.sqlcheck.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.session.ConnectionSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    
    @Before
    public void setup() {
        sqlCheckService = new SqlCheckService();
    }
    
    @Test
    public void testCheck_Method1_Typical_UseCase() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        
        List<CheckResult> results = sqlCheckService.check(session, req);
        
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }
    
    @Test
    public void testCheck_Method2_EdgeCase() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertNotNull(violations);
    }
}