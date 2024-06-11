import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlCheckServiceTest {
    
    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck_session_request_emptyRuleList() {
        // Test when the ruleSetId is null and returns an empty list of CheckResult
        // This is an edge case scenario
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckResult> result = sqlCheckService.check(session, req);
        
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCheck_environmentId_databaseName_emptySqls() {
        // Test when sqls is empty and returns an empty list of CheckViolation
        // This is an edge case scenario
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testGetRules_emptyRuleList() {
        // Test when the list of rules is empty and returns an empty list of SqlCheckRule
        // This is an edge case scenario
        List<Rule> rules = Collections.emptyList();
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, DialectType.MYSQL, null);
        
        assertEquals(Collections.emptyList(), result);
    }

}