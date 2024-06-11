import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private ConnectionSession session;

    @Mock
    private SqlCheckReq req;

    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }

    @Test
    public void testCheckValidSessionAndReq() {
        // Setup
        List<CheckResult> expectedResults = Arrays.asList(new CheckResult("Violation1"), new CheckResult("Violation2"));
        Mockito.when(session.getDialectType()).thenReturn(DialectType.ORACLE);
        Mockito.when(req.getDelimiter()).thenReturn(";");
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any())).thenReturn(Arrays.asList(new Rule("Rule1", "Level1"),
                new Rule("Rule2", "Level2")));
        Mockito.when(sqlCheckService.getRules(Mockito.anyList(), Mockito.any(ConnectionSession.class))).thenReturn(
                Arrays.asList(new SqlCheckRule(1), new SqlCheckRule(2)));
        Mockito.when(session.getRuleSetId()).thenReturn(123L);

        // Test
        List<CheckResult> actualResults = sqlCheckService.check(session, req);

        // Verify
        assertNotNull(actualResults);
        assertFalse(actualResults.isEmpty());
        assertEquals(2, actualResults.size());
        assertEquals("Violation1", actualResults.get(0).getMessage());
        assertEquals("Violation2", actualResults.get(1).getMessage());
    }

    @Test
    public void testCheckEmptySessionOrReq() {
        // Setup
        Mockito.when(session.getDialectType()).thenReturn(DialectType.ORACLE);
        Mockito.when(req.getDelimiter()).thenReturn(";");
        Mockito.when(session.getRuleSetId()).thenReturn(null);

        // Test
        List<CheckResult> actualResults = sqlCheckService.check(session, req);

        // Verify
        assertNotNull(actualResults);
        assertTrue(actualResults.isEmpty());
    }

    @Test
    public void testCheckDatabaseNameAndSqls() {
        // Setup
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM TABLE", 1, 2));
        CheckViolation violation = new CheckViolation("Error", 1, 2);
        Mockito.when(environmentService.detail(Mockito.anyLong())).thenReturn(new Environment());
        Mockito.when(environmentService.detail(Mockito.anyLong())).thenReturn(new Environment());
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any())).thenReturn(Arrays.asList(new Rule("Rule1", "Level1")));
        Mockito.when(sqlCheckService.getRules(Mockito.anyList(), Mockito.any(DialectType.class), Mockito.any(JdbcTemplate.class)))
                .thenReturn(Arrays.asList(new SqlCheckRule(1)));
        Mockito.when(sqlCheckService.fullFillRiskLevel(Mockito.anyList(), Mockito.anyList())).thenReturn(Arrays.asList(new Rule()));

        // Test
        List<CheckViolation> actualViolations = sqlCheckService.check(123L, "Database", sqls, new ConnectionConfig());

        // Verify
        assertNotNull(actualViolations);
        assertFalse(actualViolations.isEmpty());
        assertEquals(1, actualViolations.size());
        assertEquals("Error", actualViolations.get(0).getMessage());
        assertEquals(1, actualViolations.get(0).getStartOffset());
        assertEquals(2, actualViolations.get(0).getEndOffset());
    }
}