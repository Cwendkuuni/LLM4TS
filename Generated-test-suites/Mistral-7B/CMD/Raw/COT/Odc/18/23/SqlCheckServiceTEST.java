import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;
    private SqlCheckReq req;
    private RuleService ruleServiceMock;
    private EnvironmentService environmentServiceMock;
    private JdbcOperations jdbcOperationsMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        req = new SqlCheckReq();
        ruleServiceMock = mock(RuleService.class);
        environmentServiceMock = mock(EnvironmentService.class);
        jdbcOperationsMock = mock(JdbcOperations.class);
        when(ruleServiceMock.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        when(environmentServiceMock.detail(anyLong())).thenReturn(mock(Environment.class));
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperationsMock);
    }

    @Test
    public void testCheckWithEmptyRuleSet() {
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCheckWithEmptyScript() {
        req.setScriptContent(Collections.emptyList());
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullSession() {
        sqlCheckService.check(null, req);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullReq() {
        sqlCheckService.check(session, null);
    }

    @Test
    public void testCheckWithEmptySqls() {
        List<OffsetString> sqls = Collections.emptyList();
        req.setScriptContent(sqls);
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullEnvironmentId() {
        sqlCheckService.check(null, "databaseName", Arrays.asList(mock(OffsetString.class)), mock(ConnectionConfig.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullDatabaseName() {
        sqlCheckService.check(1L, null, Arrays.asList(mock(OffsetString.class)), mock(ConnectionConfig.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullConfig() {
        sqlCheckService.check(1L, "databaseName", Arrays.asList(mock(OffsetString.class)), null);
    }

    @Test
    public void testCheckWithEmptySqlsAndValidRuleSet() {
        List<Rule> rules = Arrays.asList(mock(Rule.class), mock(Rule.class));
        when(ruleServiceMock.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertFalse(sqlCheckRules.isEmpty());
        List<CheckViolation> checkViolations = new ArrayList<>();
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), result);
        verify(jdbcOperationsMock, never()).query(anyString(), anyArray(), anyMap(), anyMap(), anyInt(), anyInt(), any(ResultSetExtractor.class));
    }

    @Test
    public void testCheckWithValidScriptAndValidRuleSet() {
        List<Rule> rules = Arrays.asList(mock(Rule.class), mock(Rule.class));
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertFalse(sqlCheckRules.isEmpty());
        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation = mock(CheckViolation.class);
        checkViolations.add(violation);
        when(jdbcOperationsMock.query(anyString(), anyArray(), anyMap(), anyMap(), anyInt(), anyInt(), any(ResultSetExtractor.class)))
                .thenReturn(new SqlCheckResultExtractor(checkViolations));
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(result.size() > 0);
        verify(jdbcOperationsMock).query(anyString(), anyArray(), anyMap(), anyMap(), anyInt(), anyInt(), any(ResultSetExtractor.class));
    }

    @Test
    public void testCheckWithValidScriptAndEmptyRuleSet() {
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), result);
        verify(jdbcOperationsMock, never()).query(anyString(), anyArray(), anyMap(), anyMap(), anyInt(), anyInt(), any(ResultSetExtractor.class));
    }

    @Test
    public void testCheckWithEmptyRuleSetAndEmptySqls() {
        when(ruleServiceMock.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), result);
        verify(jdbcOperationsMock, never()).query(anyString(), anyArray(), anyMap(), anyMap(), anyInt(), anyInt(), any(ResultSetExtractor.class));
    }

    @Test
    public void testCheckWithValidEnvironmentIdAndDatabaseNameAndValidRuleSetAndSqls() {
        List<Rule> rules = Arrays.asList(mock(Rule.class), mock(Rule.class));
        Environment env = mock(Environment.class);
        when(environmentServiceMock.detail(anyLong())).thenReturn(env);
        when(env.getRulesetId()).thenReturn(1L);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertTrue(sqlCheckRules.size() > 0);
        String dbName = "databaseName";
        List<OffsetString> sqls = Arrays.asList(mock(OffsetString.class), mock(OffsetString.class));
        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation = mock(CheckViolation.class);
        checkViolations.add(violation);
        when(jdbcOperationsMock.query(anyString(), anyArray(), anyMap(), anyMap(), anyInt(), anyInt(), any(ResultSetExtractor.class)))
                .thenReturn(new SqlCheckResultExtractor(checkViolations));
        List<CheckResult> result = sqlCheckService.check(1L, dbName, sqls, mock(ConnectionConfig.class));
        assertTrue(result.size() > 0);
        verify(jdbcOperationsMock).query(anyString(), anyArray(), anyMap(), anyMap(), anyInt(), anyInt(), any(ResultSetExtractor.class));
    }

    @Test
    public void testCheckWithValidEnvironmentIdAndDatabaseNameAndEmptyRuleSetAndSqls() {
        when(environmentServiceMock.detail(anyLong())).thenReturn(mock(Environment.class));
        when(env.getRulesetId()).thenReturn(1L);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), session);
        assertTrue(sqlCheckRules.isEmpty());
        List<CheckResult> result = sqlCheckService.check(1L, "databaseName", Arrays.asList(mock(OffsetString.class)), mock(ConnectionConfig.class));
        assertEquals(Collections.emptyList(), result);
        verify(jdbcOperationsMock, never()).query(anyString(), anyArray(), anyMap(), anyMap(), anyInt(), anyInt(), any(ResultSetExtractor.class));
    }

    @Test
    public void testCheckWithValidEnvironmentIdAndDatabaseNameAndValidRuleSetAndEmptySqls() {
        List<Rule> rules = Arrays.asList(mock(Rule.class), mock(Rule.class));
        Environment env = mock(Environment.class);
        when(environmentServiceMock.detail(anyLong())).thenReturn(env);
        when(env.getRulesetId()).thenReturn(1L);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertTrue(sqlCheckRules.size() > 0);
        List<CheckResult> result = sqlCheckService.check(1L, "databaseName", Collections.emptyList(), mock(ConnectionConfig.class));
        assertEquals(Collections.emptyList(), result);
        verify(jdbcOperationsMock, never()).query(anyString(), anyArray(), anyMap(), anyMap(), anyInt(), anyInt(), any(ResultSetExtractor.class));
    }

    @Test
    public void testCheckWithValidEnvironmentIdAndDatabaseNameAndEmptyRuleSetAndEmptySqls() {
        when(environmentServiceMock.detail(anyLong())).thenReturn(mock(Environment.class));
        when(env.getRulesetId()).thenReturn(1L);
        List<CheckResult> result = sqlCheckService.check(1L, "databaseName", Collections.emptyList(), mock(ConnectionConfig.class));
        assertEquals(Collections.emptyList(), result);
        verify(jdbcOperationsMock, never()).query(anyString(), anyArray(), anyMap(), anyMap(), anyInt(), anyInt(), any(ResultSetExtractor.class));
    }

    @Test
    public void testFullFillRiskLevelWithEmptyViolations() {
        List<CheckViolation> violations = Collections.emptyList();
        List<Rule> rules = Arrays.asList(mock(Rule.class), mock(Rule.class));
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testFullFillRiskLevelWithValidViolations() {
        List<CheckViolation> violations = Arrays.asList(mock(CheckViolation.class), mock(CheckViolation.class));
        List<Rule> rules = Arrays.asList(mock(Rule.class), mock(Rule.class));
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertTrue(result.size() > 0);
        for (Rule rule : result) {
            assertEquals(rule.getLevel(), rule.getViolation().getLevel());
        }
    }
}

class SqlCheckResultExtractor implements ResultSetExtractor<List<CheckViolation>> {

    private List<CheckViolation> checkViolations;

    public SqlCheckResultExtractor(List<CheckViolation> checkViolations) {
        this.checkViolations = checkViolations;
    }

    @Override
    public List<CheckViolation> extractData(ResultSet rs) throws SQLException, DataAccessException {
        while (rs.next()) {
            CheckViolation violation = new CheckViolation();
            violation.setType(RuleType.valueOf(rs.getString("rule_type")));
            violation.setMessage(rs.getString("message"));
            violation.setLevel(rs.getInt("level"));
            violation.setLineNumber(rs.getInt("line_number"));
            violation.setPosition(rs.getInt("position"));
            checkViolations.add(violation);
        }
        return checkViolations;
    }
}