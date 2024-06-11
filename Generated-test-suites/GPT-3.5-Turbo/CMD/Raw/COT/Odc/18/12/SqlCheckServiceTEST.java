
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.junit.Test;

import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

public class SqlCheckServiceTest {

    @Test
    public void testCheck() {
        SqlCheckService sqlCheckService = new SqlCheckService();

        // Mock ConnectionSession
        ConnectionSession session = mock(ConnectionSession.class);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM test_table;");
        
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        
        assertEquals(Collections.emptyList(), checkResults);
    }

    @Test
    public void testCheckWithEnvironment() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        
        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql = new OffsetString(0, 19, "SELECT * FROM test_table;");
        sqls.add(sql);
        
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "test_db", sqls, new ConnectionConfig());
        
        assertEquals(Collections.emptyList(), checkViolations);
    }

    @Test
    public void testGetRules() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setEnabled(true);
        rules.add(rule);
        
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, new JdbcTemplate());
        
        assertEquals(Collections.emptyList(), sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.getMetadata().setName("TEST_RULE");
        rules.add(rule);
        
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(new RuleType("TEST_RULE"));
        violations.add(violation);
        
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        
        assertEquals(1, violatedRules.size());
    }
}
