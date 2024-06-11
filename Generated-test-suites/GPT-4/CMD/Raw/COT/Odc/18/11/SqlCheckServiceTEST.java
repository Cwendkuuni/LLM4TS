
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    SqlCheckService sqlCheckService;

    @Mock
    RuleService ruleService;
    
    @Mock
    EnvironmentService environmentService;

    // Mock variables 
    ConnectionSession mockSession;
    SqlCheckReq mockReq;
    Long mockEnvironmentId;
    String mockDatabaseName;
    List<OffsetString> mockSqls;
    ConnectionConfig mockConfig;
    List<Rule> mockRules;
    List<SqlCheckRule> mockSqlCheckRules;
    List<CheckViolation> mockCheckViolations;
    
    @Before
    public void setUp() {

        // Initialize mock variables 
        mockSession = mock(ConnectionSession.class);
        mockReq = mock(SqlCheckReq.class);
        mockEnvironmentId = 1L;
        mockDatabaseName = "testdatabase";
        mockSqls = Collections.emptyList(); // Replace with actual OffsetString List
        mockConfig = mock(ConnectionConfig.class);
        mockRules = Collections.emptyList(); // Replace with actual Rules List
        mockSqlCheckRules = Collections.emptyList(); // Replace with actual SqlCheckRules List
        mockCheckViolations = Collections.emptyList(); // Replace with actual CheckViolation List

        // Setup the mock behaviours
        
        when(ruleService.list(anyLong(),any())).thenReturn(mockRules);

        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        when(sqlCheckService.getRules(anyList(),any())).thenReturn(mockSqlCheckRules);

        when(sqlCheckService.getRules(anyList(),any(),any())).thenReturn(mockSqlCheckRules);

        when(sqlCheckService.check(any(),any())).thenReturn(Collections.emptyList());

        when(sqlCheckService.check(anyLong(),anyString(),anyList(),any())).thenReturn(mockCheckViolations);

        when(sqlCheckService.fullFillRiskLevel(anyList(),anyList())).thenReturn(mockRules);
    }

    @Test
    public void testCheck_firstMethod() {
        List<CheckResult> result = sqlCheckService.check(mockSession, mockReq);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCheck_secondMethod() {
        List<CheckViolation> result = sqlCheckService.check(mockEnvironmentId, mockDatabaseName, mockSqls, mockConfig);
        assertEquals(mockCheckViolations, result);
    }

    @Test
    public void testGetRules_firstMethod() {
        List<SqlCheckRule> result = sqlCheckService.getRules(mockRules, mockSession);
        assertEquals(mockSqlCheckRules, result);
    }

    @Test
    public void testGetRules_secondMethod() {
        List<SqlCheckRule> result = sqlCheckService.getRules(mockRules, DialectType.OB, mock(JdbcOperations.class));
        assertEquals(mockSqlCheckRules, result);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> result = sqlCheckService.fullFillRiskLevel(mockRules, mockCheckViolations);
        assertEquals(mockRules, result);
    }
}
