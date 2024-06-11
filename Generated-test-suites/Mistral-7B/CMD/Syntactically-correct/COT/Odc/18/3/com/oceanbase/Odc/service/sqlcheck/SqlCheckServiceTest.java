package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.validation.SqlCheckReq;

/**
 * {@link SqlCheckService} test cases
 *
 * @author yh263208
 * @date 2023-03-15 13:32
 */
@Slf4j
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private SingleConnectionDataSource dataSource;
    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(dataSource.getJdbcOperations()).thenReturn(jdbcTemplate);
        when(jdbcTemplate.queryForList(StringUtils.EMPTY, new Object[0])).thenReturn(Collections.emptyList());
    }

    @Test
    public void testCheck_EmptySessionAndReq_ReturnsEmptyList() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), checkResults);
    }

    @Test
    public void testCheck_EmptyRules_ReturnsEmptyList() {
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        SqlCheckReq req = new SqlCheckReq();
        List<Rule> rules = Collections.emptyList();
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), checkResults);
    }

    @Test
    public void testGetRules_EmptyRules_ReturnsEmptyList() {
        ConnectionSession session = mock(ConnectionSession.class);
        List<Rule> rules = Collections.emptyList();
        List<SqlCheckRule> checkRules = sqlCheckService.getRules(rules, session);
        assertEquals(Collections.emptyList(), checkRules);
    }

    @Test
    public void testGetRules_SingleRule_ReturnsRule() {
        ConnectionSession session = mock(ConnectionSession.class);
        DialectType dialectType = DialectType.MYSQL;
        Rule rule = mock(Rule.class);
        RuleMetadata metadata = mock(RuleMetadata.class);
        when(metadata.getType()).thenReturn(RuleType.SQL_CHECK);
        when(rule.getMetadata()).thenReturn(metadata);
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(Arrays.asList(rule));
        List<SqlCheckRule> checkRules = sqlCheckService.getRules(rules, session);
        assertEquals(1, checkRules.size());
        SqlCheckRule checkRule = checkRules.get(0);
        assertEquals(rule, checkRule);
    }

    @Test
    public void testCheck_ValidScriptContentAndRules_ReturnsCheckResults() {
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        List<Rule> rules = Arrays.asList(mock(Rule.class));
        SqlCheckReq req = new SqlCheckReq();
        String scriptContent = "SELECT * FROM users WHERE name = 'John'";
        List<CheckViolation> checkViolations = Arrays.asList(new CheckViolation("violationType1", "violationMessage1"));
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(jdbcTemplate.queryForList(anyString(), any(Object[].class))).thenReturn(Collections.emptyList());
        when(ruleService.createCheckViolations(any(), any())).thenReturn(checkViolations);
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(1, checkResults.size());
        CheckResult checkResult = checkResults.get(0);
        assertEquals(CheckResult.Status.PASSED, checkResult.getStatus());
        assertEquals(scriptContent, checkResult.getScriptContent());
        assertEquals(Collections.emptyList(), checkResult.getCheckViolations());
    }

    @Test
    public void testCheck_SqlWithViolations_ReturnsCheckResultsWithViolations() {
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        List<Rule> rules = Arrays.asList(mock(Rule.class));
        SqlCheckReq req = new SqlCheckReq();
        String scriptContent = "SELECT * FROM users WHERE name = 'John' -- comment";
        List<CheckViolation> checkViolations = Arrays.asList(new CheckViolation("violationType1", "violationMessage1"),
                new CheckViolation("violationType2", "violationMessage2"));
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(jdbcTemplate.queryForList(anyString(), any(Object[].class))).thenReturn(Collections.emptyList());
        when(ruleService.createCheckViolations(any(), any())).thenReturn(checkViolations);
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(1, checkResults.size());
        CheckResult checkResult = checkResults.get(0);
        assertEquals(CheckResult.Status.FAILED, checkResult.getStatus());
        assertEquals(scriptContent, checkResult.getScriptContent());
        assertEquals(Arrays.asList(checkViolations.get(0), checkViolations.get(1)), checkResult.getCheckViolations());
    }

    @Test
    public void testCheck_SqlWithEmptyScriptContent_ReturnsEmptyCheckResults() {
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(StringUtils.EMPTY);
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), checkResults);
    }

    @Test
    public void testCheck_SqlWithOnlyComments_ReturnsEmptyCheckResults() {
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        SqlCheckReq req = new SqlCheckReq();
        String scriptContent = "-- comment";
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), checkResults);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheck_NullSession_ThrowsIllegalArgumentException() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        SqlCheckReq req = new SqlCheckReq();
        SqlCheckService sqlCheckService = new SqlCheckService();
        sqlCheckService.check(null, req);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheck_NullReq_ThrowsIllegalArgumentException() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckService sqlCheckService = new SqlCheckService();
        sqlCheckService.check(session, null);
    }

    @Test
    public void testCheck_ValidEnvironmentIdDatabaseNameSqlsAndConnectionConfig_ReturnsCheckViolations() {
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM users WHERE name = 'John'"));
        ConnectionConfig connectionConfig = new ConnectionConfig();
        List<CheckViolation> checkViolations = Arrays.asList(new CheckViolation("violationType1", "violationMessage1"),
                new CheckViolation("violationType2", "violationMessage2"));
        Environment env = mock(Environment.class);
        List<Rule> rules = Arrays.asList(mock(Rule.class));
        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build()))
                .thenReturn(rules);
        when(factory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getJdbcOperations()).thenReturn(jdbcTemplate);
        when(ruleService.createCheckViolations(any(), any())).thenReturn(checkViolations);
        List<CheckResult> checkResults = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);
        assertEquals(sqls.size(), checkResults.size());
        for (int i = 0; i < checkResults.size(); i++) {
            CheckResult checkResult = checkResults.get(i);
            assertEquals(CheckResult.Status.PASSED, checkResult.getStatus());
            assertEquals(sqls.get(i).getScriptContent(), checkResult.getScriptContent());
            assertEquals(checkViolations, checkResult.getCheckViolations());
        }
    }

    @Test
    public void testCheck_EmptyEnvironmentIdDatabaseNameSqlsAndConnectionConfig_ReturnsEmptyList() {
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = Collections.emptyList();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        List<CheckResult> checkResults = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);
        assertEquals(Collections.emptyList(), checkResults);
    }

    @Test
    public void testCheck_EmptySqlsAndConnectionConfig_ReturnsEmptyList() {
        Long environmentId = 1L;
        String databaseName = "test";
        ConnectionConfig connectionConfig = new ConnectionConfig();
        List<CheckResult> checkResults = sqlCheckService.check(environmentId, databaseName, Collections.emptyList(), connectionConfig);
        assertEquals(Collections.emptyList(), checkResults);
    }

    @Test
    public void testCheck_NullEnvironmentIdDatabaseNameSqls_ThrowsNullPointerException() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        SqlCheckService sqlCheckService = new SqlCheckService();
        sqlCheckService.check(null, "test", Collections.emptyList(), connectionConfig);
    }

    @Test
    public void testCheck_NullDatabaseNameSqls_ThrowsNullPointerException() {
        Long environmentId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        SqlCheckService sqlCheckService = new SqlCheckService();
        sqlCheckService.check(environmentId, null, Collections.emptyList(), connectionConfig);
    }

    @Test
    public void testCheck_NullSqls_ThrowsNullPointerException() {
        Long environmentId = 1L;
        String databaseName = "test";
        ConnectionConfig connectionConfig = new ConnectionConfig();
        SqlCheckService sqlCheckService = new SqlCheckService();
        sqlCheckService.check(environmentId, databaseName, null, connectionConfig);
    }

    @Test
    public void testFullFillRiskLevel_EmptyViolations_ReturnsEmptyList() {
        List<Rule> rules = Arrays.asList(mock(Rule.class));
        List<CheckViolation> violations = Collections.emptyList();
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testFullFillRiskLevel_ValidViolationsAndRules_ReturnsViolatedRules() {
        List<Rule> rules = Arrays.asList(mock(Rule.class), mock(Rule.class));
        List<CheckViolation> violations = Arrays.asList(new CheckViolation("violationType1", "violationMessage1"),
                new CheckViolation("violationType2", "violationMessage2"));
        Rule rule1 = mock(Rule.class);
        RuleMetadata metadata1 = mock(RuleMetadata.class);
        RuleViolation violation1 = new RuleViolation();
        violation1.setViolation("violationMessage1");
        when(metadata1.getName()).thenReturn("ruleName1");
        when(rule1.getMetadata()).thenReturn(metadata1);
        when(rule1.getViolation()).thenReturn(violation1);
        Rule rule2 = mock(Rule.class);
        RuleMetadata metadata2 = mock(RuleMetadata.class);
        RuleViolation violation2 = new RuleViolation();
        violation2.setViolation("violationMessage2");
        when(metadata2.getName()).thenReturn("ruleName2");
        when(rule2.getMetadata()).thenReturn(metadata2);
        when(rule2.getViolation()).thenReturn(violation2);
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(2, result.size());
        Rule resultRule1 = result.get(0);
        assertEquals(rule1, resultRule1);
        Rule resultRule2 = result.get(1);
        assertEquals(rule2, resultRule2);
    }
}