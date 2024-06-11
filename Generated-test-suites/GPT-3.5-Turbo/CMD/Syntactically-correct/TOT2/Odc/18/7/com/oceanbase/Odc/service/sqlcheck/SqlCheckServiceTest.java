import static org.junit.Assert.assertEquals;
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

    // Mock dependencies
    private RuleService ruleService;
    private EnvironmentService environmentService;

    @Before
    public void setUp() {
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);

        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }

    @Test
    public void testCheck_ValidSession_ValidSqlCheckReq() {
        // Mock data
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();

        // Mock behavior
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        // Call the method
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Verify the result
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheck_EnvironmentId_DatabaseName_Sqls_ConnectionConfig() {
        // Mock data
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        Environment env = new Environment();
        env.setRulesetId(1L);

        // Mock behavior
        when(environmentService.detail(anyLong())).thenReturn(env);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        // Call the method
        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // Verify the result
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Add more test cases for other public methods here

}