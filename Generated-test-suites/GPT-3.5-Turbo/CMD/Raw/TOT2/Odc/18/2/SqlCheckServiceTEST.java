import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlCheckServiceTest {

    @Mock
    RuleService ruleService;

    @Mock
    EnvironmentService environmentService;

    @Mock
    ConnectionSession session;

    @Mock
    SqlCheckReq req;

    @Mock
    Long environmentId;

    @Mock
    String databaseName;

    @Mock
    ConnectionConfig config;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        assertNotNull(sqlCheckService);

        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), checkResults);
    }

    @Test
    public void testCheckWithEnvironmentId() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        assertNotNull(sqlCheckService);

        List<OffsetString> sqls = new ArrayList<>();
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(Collections.emptyList(), checkViolations);
    }

    @Test
    public void testGetRulesWithSession() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        assertNotNull(sqlCheckService);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), session);
        assertEquals(Collections.emptyList(), sqlCheckRules);
    }

    @Test
    public void testGetRulesWithJdbc() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        assertNotNull(sqlCheckService);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), DialectType.MYSQL, jdbc);
        assertEquals(Collections.emptyList(), sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        assertNotNull(sqlCheckService);

        List<Rule> rules = new ArrayList<>();
        CheckViolation checkViolation = new CheckViolation();
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(checkViolation);
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(0, violatedRules.size());
    }

}