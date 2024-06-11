import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private ConnectionSession session;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        // Prepare data
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        // Mock behavior
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(mock(JdbcOperations.class));
        
        // Test the method
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, new ArrayList<>());

        assertNotNull(violatedRules);
        assertEquals(0, violatedRules.size());
    }

    @Test
    public void testCheckWithEnvironment() {
        // Prepare data
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();

        // Mock behavior
        when(ruleService.list(anyLong(), any())).thenReturn(new ArrayList<>());
        when(ruleService.detail(anyLong())).thenReturn(new Environment());
        when(session.getRuleSetId(session)).thenReturn(null);

        // Test the method
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(checkViolations);
        assertEquals(0, checkViolations.size());
    }
}