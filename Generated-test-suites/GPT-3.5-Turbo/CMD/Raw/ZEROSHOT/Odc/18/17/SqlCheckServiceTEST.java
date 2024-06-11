import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        List<CheckViolation> violations = new ArrayList<>();
        // Add your expected violations here

        List<CheckResult> expectedResults = SqlCheckUtil.buildCheckResults(violations);

        when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> actualResults = sqlCheckService.check(session, req);

        assertEquals(expectedResults.size(), actualResults.size());
        // Add additional assertions for CheckResults if needed
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = Arrays.asList(new Rule(), new Rule());
        ConnectionSession session = mock(ConnectionSession.class);
        JdbcOperations jdbc = mock(JdbcOperations.class);

        List<SqlCheckRule> expectedRules = Collections.emptyList();

        List<SqlCheckRule> actualRules = sqlCheckService.getRules(rules, session);

        assertEquals(expectedRules.size(), actualRules.size());
        // Add additional assertions for SqlCheckRules if needed
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = Arrays.asList(new Rule(), new Rule());
        List<CheckViolation> violations = new ArrayList<>();
        // Add your CheckViolations and expected ViolatedRules here

        List<Rule> expectedViolatedRules = Collections.emptyList();

        List<Rule> actualViolatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(expectedViolatedRules.size(), actualViolatedRules.size());
        // Add additional assertions for ViolatedRules if needed
    }
}