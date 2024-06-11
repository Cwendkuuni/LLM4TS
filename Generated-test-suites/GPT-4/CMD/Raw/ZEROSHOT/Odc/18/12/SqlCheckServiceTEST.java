
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Test
    public void testCheck() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        when(ruleService.list(any(), any())).thenReturn(new ArrayList<>());
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(ruleService.list(any(), any())).thenReturn(new ArrayList<>());
        when(environmentService.detail(any())).thenReturn(new Environment());
        List<CheckViolation> violations = sqlCheckService.check(1L, "databaseName", new ArrayList<>(), connectionConfig);
        assertEquals(0, violations.size());
    }

    @Test
    public void testGetRulesWithSession() {
        ConnectionSession session = new ConnectionSession();
        when(ruleService.list(any(), any())).thenReturn(new ArrayList<>());
        List<SqlCheckRule> rules = sqlCheckService.getRules(new ArrayList<>(), session);
        assertEquals(0, rules.size());
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbcOperations() {
        JdbcOperations jdbcOperations = new JdbcTemplate();
        when(ruleService.list(any(), any())).thenReturn(new ArrayList<>());
        List<SqlCheckRule> rules = sqlCheckService.getRules(new ArrayList<>(), DialectType.MYSQL, jdbcOperations);
        assertEquals(0, rules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        when(ruleService.list(any(), any())).thenReturn(new ArrayList<>());
        List<Rule> rules = sqlCheckService.fullFillRiskLevel(new ArrayList<>(), new ArrayList<>());
        assertEquals(0, rules.size());
    }
}
