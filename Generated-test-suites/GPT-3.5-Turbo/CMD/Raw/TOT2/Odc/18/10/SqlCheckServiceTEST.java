import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    
    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithoutRuleSetId() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithRuleSetId() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckViolations() {
        List<CheckViolation> violations = sqlCheckService.check(1L, "testDB", Collections.singletonList(new OffsetString(0, "SELECT * FROM table")), new ConnectionConfig());
        assertEquals(0, violations.size());
    }

    @Test
    public void testGetRulesWithSession() {
        ConnectionSession session = mock(ConnectionSession.class);
        List<SqlCheckRule> rules = sqlCheckService.getRules(new ArrayList<>(), session);
        assertEquals(0, rules.size());
    }

    @Test
    public void testGetRulesWithJdbc() {
        JdbcOperations jdbc = mock(JdbcOperations.class);
        List<SqlCheckRule> rules = sqlCheckService.getRules(new ArrayList<>(), DialectType.MYSQL, jdbc);
        assertEquals(0, rules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType("test");
        violations.add(violation);
        assertEquals(0, sqlCheckService.fullFillRiskLevel(new ArrayList<>(), violations).size());
    }
    
}