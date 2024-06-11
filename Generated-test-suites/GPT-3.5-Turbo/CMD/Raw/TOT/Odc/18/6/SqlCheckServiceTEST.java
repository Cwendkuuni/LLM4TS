import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private ConnectionSession session;

    @Mock
    private SqlCheckReq req;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;
    
    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }

    @Test
    public void testCheckMethodWithSessionAndReq() {
        // Setup
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(req.getDelimiter()).thenReturn(";");
        
        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        
        // Execute
        List<CheckResult> result = sqlCheckService.check(session, req);
        
        // Verify
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckMethodWithEnvironmentIdAndSqlsAndConfig() {
        // Setup
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        
        Environment env = new Environment();
        env.setRulesetId(1L);

        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(1L, new QueryRuleMetadataParams())).thenReturn(new ArrayList<>());
        
        // Execute
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        // Verify
        assertEquals(0, violations.size());
    }

    @Test
    public void testGetRulesMethod() {
        // Setup
        List<Rule> rules = new ArrayList<>();
        
        // Execute
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, DialectType.MYSQL, mock(JdbcTemplate.class));
        
        // Verify
        assertEquals(0, result.size());
    }

    @Test
    public void testFullFillRiskLevelMethod() {
        // Setup
        List<Rule> rules = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        
        rules.add(new Rule());
        violation.setType(new RuleMetadata());
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(violation);

        // Execute
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        
        // Verify
        assertEquals(1, result.size());
    }

}