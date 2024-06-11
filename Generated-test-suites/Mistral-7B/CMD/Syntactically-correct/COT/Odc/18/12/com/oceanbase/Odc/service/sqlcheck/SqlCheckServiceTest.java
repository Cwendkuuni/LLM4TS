import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.DelegatingPreparedStatementSet;
import org.apache.commons.dbcp2.PreparedStatementHolder;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.ConnectionConfig;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.util.SqlCheckUtil;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @InjectMocks
    private SqlCheckService sqlCheckService;
    private Connection connection;
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private SimpleJdbcTemplate simpleJdbcTemplate;

    @Before
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(connection));
        namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        simpleJdbcTemplate = new SimpleJdbcTemplate(jdbcTemplate);
        when(connection.prepareStatement(anyString())).thenAnswer(invocation ->
                new PreparedStatementHolder(((PreparedStatement) connection.createStatement()), PreparedStatement.RETURN_GENERATED_KEYS));
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testCheckWithValidSessionAndReq() {
        List<Rule> rules = Arrays.asList(getRuleMock());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        ConnectionSession session = getSessionMock();
        SqlCheckReq req = getSqlCheckReqMock();
        List<CheckViolation> expectedCheckViolations = Arrays.asList(getCheckViolationMock());
        List<Rule> expectedViolatedRules = Arrays.asList(getRuleMock(), getRuleMock());
        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        List<CheckResult> result = sqlCheckService.check(session, req);

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verifyNoMoreInteractions(ruleService);
        verify(environmentService, times(1)).detail(anyLong());
        verifyNoMoreInteractions(environmentService);
        assertEquals(1, result.size());
        assertEquals(expectedCheckViolations, result.get(0).getCheckViolations());
        assertEquals(expectedViolatedRules, result.get(0).getViolatedRules());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullSession() {
        SqlCheckReq req = getSqlCheckReqMock();
        sqlCheckService.check(null, req);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullReq() {
        ConnectionSession session = getSessionMock();
        sqlCheckService.check(session, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithEmptyRuleList() {
        ConnectionSession session = getSessionMock();
        SqlCheckReq req = getSqlCheckReqMock();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        sqlCheckService.check(session, req);
    }

    @Test
    public void testCheckWithEmptyScriptContent() {
        ConnectionSession session = getSessionMock();
        SqlCheckReq req = new SqlCheckReq();
        List<CheckViolation> expectedCheckViolations = Collections.emptyList();
        List<Rule> expectedViolatedRules = Collections.emptyList();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Arrays.asList(getRuleMock()));

        List<CheckResult> result = sqlCheckService.check(session, req);

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verifyNoMoreInteractions(ruleService);
        assertEquals(1, result.size());
        assertEquals(expectedCheckViolations, result.get(0).getCheckViolations());
        assertEquals(expectedViolatedRules, result.get(0).getViolatedRules());
    }

    @Test
    public void testCheckWithEnvironmentIdAndDatabaseName() {
        List<Rule> rules = Arrays.asList(getRuleMock());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        String databaseName = "test_db";
        Long environmentId = 1L;
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM test_table"));
        ConnectionConfig config = new ConnectionConfig();

        List<CheckViolation> expectedCheckViolations = Arrays.asList(getCheckViolationMock());
        List<Rule> expectedViolatedRules = Arrays.asList(getRuleMock(), getRuleMock());

        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        List<CheckResult> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verifyNoMoreInteractions(ruleService);
        verify(environmentService, times(1)).detail(anyLong());
        verifyNoMoreInteractions(environmentService);
        assertEquals(1, result.size());
        assertEquals(expectedCheckViolations, result.get(0).getCheckViolations());
        assertEquals(expectedViolatedRules, result.get(0).getViolatedRules());
    }

    @Test
    public void testGetRulesWithEmptyRuleList() {
        List<Rule> rules = Collections.emptyList();
        List<SqlCheckRule> expectedRules = Collections.emptyList();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, mock(ConnectionSession.class));

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verifyNoMoreInteractions(ruleService);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetRulesWithValidSessionAndRules() {
        List<Rule> rules = Arrays.asList(getRuleMock());
        List<SqlCheckRule> expectedRules = Arrays.asList(getSqlCheckRuleMock());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, mock(ConnectionSession.class));

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verifyNoMoreInteractions(ruleService);
        assertEquals(expectedRules, result);
    }

    @Test
    public void testFullFillRiskLevelWithEmptyCheckViolations() {
        List<CheckViolation> checkViolations = Collections.emptyList();
        List<Rule> expectedRules = Arrays.asList(getRuleMock(), getRuleMock());

        sqlCheckService.fullFillRiskLevel(Collections.emptyList(), checkViolations);

        verifyNoInteractions(sqlCheckService);
        assertEquals(expectedRules, sqlCheckService.violatedRules);
    }

    @Test
    public void testFullFillRiskLevelWithValidCheckViolations() {
        List<CheckViolation> checkViolations = Arrays.asList(getCheckViolationMock());
        List<Rule> expectedRules = Arrays.asList(getRuleMock(), getRuleMock());

        sqlCheckService.fullFillRiskLevel(Arrays.asList(getRuleMock()), checkViolations);

        verifyNoInteractions(sqlCheckService);
        assertEquals(expectedRules, sqlCheckService.violatedRules);
    }

    private ConnectionSession getSessionMock() {
        return mock(ConnectionSession.class);
    }

    private Rule getRuleMock() {
        Rule rule = mock(Rule.class);
        RuleMetadata metadata = mock(RuleMetadata.class);
        when(rule.getMetadata()).thenReturn(metadata);
        when(metadata.getType()).thenReturn(RuleType.SQL_CHECK);
        return rule;
    }

    private SqlCheckReq getSqlCheckReqMock() {
        return new SqlCheckReq();
    }

    private CheckViolation getCheckViolationMock() {
        return new CheckViolation();
    }

    private SqlCheckRule getSqlCheckRuleMock() {
        return mock(SqlCheckRule.class);
    }

}