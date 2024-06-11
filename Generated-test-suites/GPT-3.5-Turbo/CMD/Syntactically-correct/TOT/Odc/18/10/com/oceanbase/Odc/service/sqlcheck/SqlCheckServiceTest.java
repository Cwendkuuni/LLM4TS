import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
    }

    @Test
    public void testCheckWithConnectionSession() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM test_table;");
        
        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        
        // Assert logic here
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM test_table;", 0));
        ConnectionConfig config = new ConnectionConfig();
        
        Environment env = new Environment();
        env.setRulesetId(1L);
        when(environmentService.detail(environmentId)).thenReturn(env);
        
        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        // Assert logic here
        assertEquals(0, checkViolations.size());
    }

}