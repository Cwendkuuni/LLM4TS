import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private ConnectionSession mockSession;
    private RuleService mockRuleService;
    private JdbcTemplate mockJdbcTemplate;

    @Before
    public void setup() {
        sqlCheckService = new SqlCheckService();
        mockSession = mock(ConnectionSession.class);
        mockRuleService = mock(RuleService.class);
        mockJdbcTemplate = mock(JdbcTemplate.class);

        sqlCheckService.ruleService = mockRuleService;
    }

    @Test
    public void testCheckWithSession() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setName("Rule1");
        rules.add(rule1);

        when(mockSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(mockSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(mockJdbcTemplate);
        when(mockRuleService.list(any(Long.class), any())).thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(mockSession, req);
        assertEquals(1, results.size());
        assertEquals("Rule1", results.get(0).getName());
    }

    @Test
    public void testCheckWithEnvironmentAndConfig() {
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setSeverity("HIGH");
        violations.add(violation1);

        when(mockRuleService.list(any(Long.class), any())).thenReturn(new ArrayList<>());
        when(mockJdbcTemplate.getDataSource()).thenReturn(new SingleConnectionDataSource(mock(ConnectionConfig.class), true));

        List<CheckViolation> results = sqlCheckService.check(1L, "database", Collections.emptyList(), new ConnectionConfig());
        assertEquals(1, results.size());
        assertEquals("HIGH", results.get(0).getSeverity());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rules.add(rule);

        when(mockRuleService.list(any(Long.class), any())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, mockJdbcTemplate);
        assertEquals(1, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType("Type");
        violations.add(violation);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.getMetadata().setName("Type");
        rules.add(rule);

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(1, violatedRules.size());
        assertEquals("Type", violatedRules.get(0).getMetadata().getName());
        assertEquals("Type", violatedRules.get(0).getViolation().getType());
    }
}