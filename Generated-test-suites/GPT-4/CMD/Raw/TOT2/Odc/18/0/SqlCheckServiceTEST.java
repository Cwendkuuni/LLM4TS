import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    @Mock
    private SqlCheckService sqlChecker;
    
    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @BeforeEach
    public void setUp() {
        this.sqlChecker = new SqlCheckService();
        this.ruleService = mock(RuleService.class);
        this.environmentService = mock(EnvironmentService.class);
    }

    @Test
    public void testCheckWithSessionAndRequest() {
        // Init some variables
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();

        // Define the behavior
        when(this.ruleService.list(any(), any())).thenReturn(new ArrayList<>());
        
        // Test
        List<CheckResult> results = this.sqlChecker.check(session, req);

        // Verify the results
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testGetRulesWithRuleList() {
        // Init some variables
        List<Rule> rules = new ArrayList<>();

        // Define the behavior
        when(this.ruleService.list(any(), any())).thenReturn(new ArrayList<>());

        // Test
        List<SqlCheckRule> ruleResults = this.sqlChecker.getRules(rules, session);

        // Verify the results
        assertNotNull(ruleResults);
        assertEquals(0, ruleResults.size());
    }

    // TODO: Continue this way for the other methods
}