
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Test
    public void testCheckForSession() {
        // Setup
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        when(ruleService.list(any(), any())).thenReturn(Arrays.asList(new Rule()));

        // Execute
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Verify
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckForEnvironment() {
        // Setup
        Long environmentId = 1L;
        String databaseName = "Test";
        when(environmentService.detail(any())).thenReturn(new Environment());
        when(ruleService.list(any(), any())).thenReturn(Arrays.asList(new Rule()));

        // Execute
        List result = sqlCheckService.check(environmentId, databaseName, Arrays.asList(), new ConnectionConfig());

        // Verify
        assertEquals(0, result.size());
    }

    @Test
    public void testGetRulesForSession() {
        // Setup
        ConnectionSession session = new ConnectionSession();
        session.setDialectType(DialectType.MYSQL);
        when(ruleService.list(any(), any())).thenReturn(Arrays.asList(new Rule()));

        // Execute
        List result = sqlCheckService.getRules(Arrays.asList(new Rule()), session);

        // Verify
        assertEquals(0, result.size());
    }

    @Test
    public void testGetRules() {
        // Setup
        DialectType dialectType = DialectType.MYSQL;
        when(ruleService.list(any(), any())).thenReturn(Arrays.asList(new Rule()));

        // Execute
        List result = sqlCheckService.getRules(Arrays.asList(new Rule()), dialectType, new JdbcOperations());

        // Verify
        assertEquals(0, result.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        // Setup
        CheckViolation violation = new CheckViolation();
        violation.setType(RuleType.SQL_CHECK);
        Rule rule = new Rule();
        List<Rule> rules = Arrays.asList(rule);

        // Execute
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, Arrays.asList(violation));

        // Verify
        assertEquals(0, result.size());
    }

}
