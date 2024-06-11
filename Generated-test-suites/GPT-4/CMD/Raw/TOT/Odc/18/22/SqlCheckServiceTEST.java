import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    @Mock
    private ConnectionSession session;

    @Mock
    private SqlCheckReq req;

    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(mock(RuleService.class));
        sqlCheckService.setEnvironmentService(mock(EnvironmentService.class));
    }

    @Test
    public void testCheckEmptyRules() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithRules() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(12L);
        when(ruleService.list(anyLong(), any())).thenReturn(new ArrayList<Rule>());
        List<CheckResult> results = sqlCheckService.check(session, req);
        //Additional checks needed
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<Rule>();
        // Fill it with example data

        List<CheckViolation> violations = new ArrayList<CheckViolation>();
        // Fill it with example data

        List<Rule> results = sqlCheckService.fullFillRiskLevel(rules, violations);
        //Additional checks needed
    }

    // Similar steps should be followed for the remaining exposed public method: check(Long environmentId, String databaseName,List<OffsetString> sqls, ConnectionConfig config)
}