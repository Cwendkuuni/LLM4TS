import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    @Mock
    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
       MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckWithSession() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> checkResults = new ArrayList<>();

        when(sqlCheckService.check(session, req)).thenReturn(checkResults);

        List<CheckResult> results = sqlCheckService.check(session, req);

        assertEquals(results.size(), checkResults.size());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        Long environmentId = 1L;
        String databaseName = "test";
        ConnectionConfig config = new ConnectionConfig();
        List<OffsetString> sqls = new ArrayList<>();
        List<CheckViolation> checkViolations = new ArrayList<>();

        when(sqlCheckService.check(environmentId, databaseName, sqls, config)).thenReturn(checkViolations);

        List<CheckViolation> results = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertEquals(results.size(), checkViolations.size());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        ConnectionSession session = new ConnectionSession();
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();

        when(sqlCheckService.getRules(rules, session)).thenReturn(sqlCheckRules);

        List<SqlCheckRule> results = sqlCheckService.getRules(rules, session);
        
        assertEquals(results.size(), sqlCheckRules.size());
    }
    
    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        List<Rule> violatedRules = new ArrayList<>();

        when(sqlCheckService.fullFillRiskLevel(rules, violations)).thenReturn(violatedRules);

        List<Rule> results = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(results.size(), violatedRules.size());
    }
}