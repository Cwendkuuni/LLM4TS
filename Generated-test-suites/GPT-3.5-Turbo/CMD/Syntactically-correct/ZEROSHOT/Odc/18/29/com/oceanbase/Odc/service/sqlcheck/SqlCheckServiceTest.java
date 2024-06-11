import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

import java.util.Arrays;
import java.util.List;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private RuleService ruleServiceMock;
    private EnvironmentService environmentServiceMock;
    private ConnectionSession sessionMock;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        ruleServiceMock = mock(RuleService.class);
        environmentServiceMock = mock(EnvironmentService.class);
        sessionMock = mock(ConnectionSession.class);
        sqlCheckService.setRuleService(ruleServiceMock);
        sqlCheckService.setEnvironmentService(environmentServiceMock);
    }

    @Test
    public void testCheckWithSession() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        req.setDelimiter(";");
        
        when(sessionMock.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleServiceMock.list(anyLong(), any())).thenReturn(Arrays.asList(new Rule(), new Rule()));
        
        List<CheckResult> checkResults = sqlCheckService.check(sessionMock, req);
        
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEnvironment() {
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM table", 0));
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        
        when(environmentServiceMock.detail(anyLong())).thenReturn(new Environment());
        when(ruleServiceMock.list(anyLong(), any())).thenReturn(Arrays.asList(new Rule(), new Rule()));
        
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "database", sqls, config);
        
        assertEquals(0, checkViolations.size());
    }
}