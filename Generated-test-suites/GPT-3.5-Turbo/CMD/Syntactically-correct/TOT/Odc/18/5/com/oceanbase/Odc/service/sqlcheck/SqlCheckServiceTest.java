import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckService;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        // Perform any setup if needed
    }

    @Test
    public void testCheck_Success() {
        // Setup
        SqlCheckReq req = new SqlCheckReq();
        
        // Execute
        List<CheckResult> results = sqlCheckService.check(session, req);
        
        // Verify
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheck_Failure() {
        // Setup
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users", 0));
        ConnectionConfig config = new ConnectionConfig();
        
        // Execute
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        // Verify
        assertNotNull(violations);
        assertEquals(0, violations.size());
    }

}