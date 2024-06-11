import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession connectionSession;
    private SqlCheckReq sqlCheckReq;
    private List<Environment> environments;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        connectionSession = mock(ConnectionSession.class);
        sqlCheckReq = new SqlCheckReq();
        environments = new ArrayList<>();
        connectionConfig = new ConnectionConfig();
    }

    @Test
    public void testCheckWithSession() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getRuleSetId()).thenReturn(1L);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertNotNull(checkResults);
        assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheckWithEnvironment() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<CheckViolation> checkViolations = new ArrayList<>();

        when(environmentService.detail(environmentId)).thenReturn(new Environment());
        when(ruleService.list(eq(environmentId), any())).thenReturn(Collections.singletonList(new Rule()));
        when(jdbcTemplate.queryForObject(anyString(), eq(DialectType.MYSQL))).thenReturn(checkViolations);

        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, Collections.emptyList(), connectionConfig);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertNotNull(sqlCheckRules);
        assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertNotNull(violatedRules);
        assertFalse(violatedRules.isEmpty());
    }
}