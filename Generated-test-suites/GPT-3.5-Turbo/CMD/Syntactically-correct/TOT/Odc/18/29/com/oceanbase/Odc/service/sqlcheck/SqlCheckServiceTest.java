import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleServiceMock;
    
    @Mock
    private JdbcTemplate jdbcTemplateMock;

    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleServiceMock;
    }

    @Test
    public void testCheckWithValidRequest() {
        // Mock data
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(anyString())).thenReturn(jdbcTemplateMock);
        
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        Rule rule = new Rule();
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);
        when(ruleServiceMock.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        // Test method
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Verify the result
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void testCheckWithInvalidRequest() {
        // Mock data
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.POSTGRESQL);
        when(session.getSyncJdbcExecutor(anyString())).thenReturn(jdbcTemplateMock);
        
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(null);

        Rule rule = new Rule();
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);
        when(ruleServiceMock.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        // Test method
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Verify the result
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRules() {
        // Mock data
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rules.add(rule);

        // Test method
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplateMock);

        // Verify the result
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        // Mock data
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.getMetadata().setName("TestRule");
        rules.add(rule);

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType("Error");
        violations.add(violation);

        // Test method
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        // Verify the result
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

}