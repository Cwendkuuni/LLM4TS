import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {
    
    @Mock
    private RuleService ruleService;
    
    @Mock
    private EnvironmentService environmentService;
    
    private SqlCheckService sqlCheckService;
    
    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }
    
    @Test
    public void testCheckSuccess() {
        // Prepare data
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        
        // Mock behavior
        when(ruleService.list(anyLong(), any())).thenReturn(Collections.singletonList(new Rule()));
        
        // Call the method
        List<CheckResult> result = sqlCheckService.check(session, req);
        
        // Verify the result
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testCheckFailure() {
        // Prepare data
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        
        // Mock behavior
        when(ruleService.list(anyLong(), any())).thenReturn(Collections.emptyList());
        
        // Call the method
        List<CheckResult> result = sqlCheckService.check(session, req);
        
        // Verify the result
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testCheckViolations() {
        // Prepare data
        Long environmentId = 1L;
        String databaseName = "testDatabase";
        OffsetString offsetString = new OffsetString("SELECT * FROM TABLE", 5, 10);
        List<OffsetString> sqls = Collections.singletonList(offsetString);
        ConnectionConfig config = new ConnectionConfig();
        
        // Mock behavior
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(ruleService.list(anyLong(), any())).thenReturn(Collections.singletonList(new Rule()));
        
        // Call the method
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        // Verify the result
        assertNotNull(violations);
        assertEquals(1, violations.size());
    }
  
}