import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import com.oceanbase.odc.service.sqlcheck.model.*;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.sqlcheck.rule.*;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.core.sql.split.OffsetString;

public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcOperations jdbcOperations;
    @Mock
    private ConnectionSession connectionSession;
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testCheck() {
        SqlCheckReq req = new SqlCheckReq("scriptContent", "delimiter");
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());
        List<CheckResult> resultList = sqlCheckService.check(connectionSession, req);
        assertEquals(Collections.emptyList(), resultList);
    }

    @Test
    public void testCheckEnvironmentId() {
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("sql1", 1));
        ConnectionConfig config = new ConnectionConfig();
        Environment env = new Environment("env");
        when(environmentService.detail(1L)).thenReturn(env);
        when(ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());
        List<CheckViolation> violations = sqlCheckService.check(1L, "databaseName", sqls, config);
        assertEquals(Collections.emptyList(), violations);
    }

    @Test
    public void testGetRulesByConnectionSession() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);
        assertEquals(Collections.emptyList(), sqlCheckService.getRules(Collections.emptyList(), connectionSession));
    }

    @Test
    public void testGetRulesByJdbcOperations() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());
        when(jdbcOperations.queryForObject("SELECT 1", Integer.class)).thenReturn(1);
        assertEquals(Collections.emptyList(), sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcOperations));
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(rules.size(), violatedRules.size());
    }

}