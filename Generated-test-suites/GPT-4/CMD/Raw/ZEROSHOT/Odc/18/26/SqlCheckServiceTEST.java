
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private ConnectionSession session;

    @Mock
    private SqlCheckReq req;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckWithSessionAndReq() {
        List<Rule> rulesList = Arrays.asList(mock(Rule.class));
        when(ruleService.list(1L, null)).thenReturn(rulesList);

        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        List<Rule> rulesList = Arrays.asList(mock(Rule.class));
        Environment environment = mock(Environment.class);
        when(environment.getRulesetId()).thenReturn(1L);
        when(environmentService.detail(1L)).thenReturn(environment);
        when(ruleService.list(1L, null)).thenReturn(rulesList);
        
        List<CheckViolation> violations = sqlCheckService.check(1L, "databaseName", Collections.singletonList("sql"), null);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testGetRulesWithSession() {
        List<Rule> rulesList = Arrays.asList(mock(Rule.class));
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rulesList, session);
        assertTrue(sqlCheckRules.isEmpty());
    }

    // Do similar test cases for other methods
}
