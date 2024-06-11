import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import org.apache.commons.collections4.CollectionUtils;
import org.mockito.Mockito;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
    public void testCheckWithConnectionSession() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertNotNull(checkResults);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        Long environmentId = 1L;
        String databaseName = "testDb";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString(0, "SELECT * FROM TABLE"));
        ConnectionConfig config = new ConnectionConfig();
        
        Environment environment = new Environment();
        Rule rule = new Rule();
        rule.setEnabled(true);
        List<Rule> rules = Collections.singletonList(rule);
        
        Mockito.when(environmentService.detail(environmentId)).thenReturn(environment);
        Mockito.when(ruleService.list(environment.getRulesetId(), QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(checkViolations);
        assertTrue(CollectionUtils.isEmpty(checkViolations));
    }

    @Test
    public void testGetRulesWithConnectionSession() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        List<Rule> rules = Collections.emptyList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithJdbcTemplate() {
        JdbcTemplate jdbc = Mockito.mock(JdbcTemplate.class);
        DialectType dialectType = DialectType.MYSQL;
        List<Rule> rules = Collections.emptyList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);
        assertNotNull(sqlCheckRules);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        Rule rule = new Rule();
        rule.setEnabled(true);
        rule.setMetadata(new RuleMetadata());
        
        List<Rule> rules = Collections.singletonList(rule);
        CheckViolation violation = new CheckViolation();
        violation.setType(new RuleViolation());
        
        List<CheckViolation> violations = Collections.singletonList(violation);
        
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(violatedRules);
        assertEquals(1, violatedRules.size());
    }

}