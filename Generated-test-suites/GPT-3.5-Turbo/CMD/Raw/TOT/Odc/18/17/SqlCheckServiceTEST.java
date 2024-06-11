import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }

    @Test
    public void testCheckMethodWithValidInput() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.singletonList(new Rule()));
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckMethodWithNullRuleSetId() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(null);
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckMethodWithEmptySqls() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = Collections.emptyList();
        ConnectionConfig config = new ConnectionConfig();
        when(environmentService.detail(1L)).thenReturn(new Environment());
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.singletonList(new Rule()));
      
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
      
        assertEquals(0, checkViolations.size());
    }

    // Add more test cases as needed 

}