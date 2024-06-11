import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.Collections;
import java.util.List;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;

    @Before
    public void setUp() {
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
    }

    @Test
    public void testCheckWithValidParams() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table;");
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.singletonList(new Rule()));
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithNullRuleSetId() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table;");
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(null);
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithEmptySqls() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Collections.emptyList();
        ConnectionConfig config = new ConnectionConfig();
        when(environmentService.detail(environmentId)).thenReturn(new Environment());
        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(0, result.size());
    }
}