import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Before
    public void setup() {
        ruleService = Mockito.mock(RuleService.class);
        environmentService = Mockito.mock(EnvironmentService.class);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }

    @Test
    public void testCheck() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setDelimiter(";");
        req.setScriptContent("SELECT * FROM table");

        Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithEmptyRules() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setDelimiter(";");
        req.setScriptContent("SELECT * FROM table");

        Mockito.when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckViolation() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();

        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckViolationWithEmptySqls() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = Collections.emptyList();
        ConnectionConfig config = new ConnectionConfig();

        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

}