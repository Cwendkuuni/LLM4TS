package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule.RuleViolationType;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(createRuleList());
        when(environmentService.detail(anyLong())).thenReturn(createEnvironment());
        when(jdbcTemplate.query(anyString(), anyArray())).thenReturn(createCheckViolationList());
    }

    @Test
    public void testCheck() {
        ConnectionSession session = createConnectionSession();
        SqlCheckReq req = createSqlCheckReq();
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(CollectionUtils.isNotEmpty(result));
    }

    @Test
    public void testCheckWithEmptyRuleSet() {
        ConnectionSession session = createConnectionSession();
        SqlCheckReq req = createSqlCheckReq();
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(CollectionUtils.isEmpty(result));
    }

    @Test
    public void testCheckWithEmptySqls() {
        ConnectionSession session = createConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(CollectionUtils.isEmpty(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullSession() {
        SqlCheckReq req = new SqlCheckReq();
        sqlCheckService.check(null, req);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullSqlCheckReq() {
        ConnectionSession session = createConnectionSession();
        sqlCheckService.check(session, null);
    }

    @Test
    public void testCheckWithEmptyRules() {
        ConnectionSession session = createConnectionSession();
        SqlCheckReq req = createSqlCheckReq();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(CollectionUtils.isEmpty(result));
    }

    @Test
    public void testCheckWithEmptyCheckRules() {
        ConnectionSession session = createConnectionSession();
        SqlCheckReq req = createSqlCheckReq();
        List<Rule> rules = createRuleList();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
        assertTrue(CollectionUtils.isEmpty(sqlCheckRules));
    }

    @Test
    public void testCheckWithValidSql() {
        ConnectionSession session = createConnectionSession();
        SqlCheckReq req = createSqlCheckReq();
        List<Rule> rules = createRuleList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        String sql = "SELECT * FROM test WHERE id = ?";
        List<CheckViolation> checkViolations = createCheckViolationList();
        when(jdbcTemplate.query(anyString(), anyArray())).thenReturn(checkViolations);
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(0, result.get(0).getViolations().size());
    }

    @Test
    public void testCheckWithInvalidSql() {
        ConnectionSession session = createConnectionSession();
        SqlCheckReq req = createSqlCheckReq();
        List<Rule> rules = createRuleList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        String sql = "INVALID SQL";
        List<CheckViolation> checkViolations = createCheckViolationList();
        when(jdbcTemplate.query(anyString(), anyArray())).thenReturn(checkViolations);
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(1, result.get(0).getViolations().size());
        CheckViolation violation = result.get(0).getViolations().get(0);
        assertEquals(RuleViolationType.SQL_FORMAT.name(), violation.getType().getName());
    }

    @Test
    public void testCheckWithSqlContainingError() {
        ConnectionSession session = createConnectionSession();
        SqlCheckReq req = createSqlCheckReq();
        List<Rule> rules = createRuleList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        String sql = "SELECT * FROM test WHERE id = ?";
        List<CheckViolation> checkViolations = createCheckViolationList();
        when(jdbcTemplate.query(sql.replace("test", "nonexistent_table"), new Object[] { 1L }))
                .thenThrow(new RuntimeException("Mock Exception"));
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(1, result.get(0).getViolations().size());
        CheckViolation violation = result.get(0).getViolations().get(0);
        assertEquals(RuleViolationType.TABLE_NOT_FOUND.name(), violation.getType().getName());
    }

    @Test
    public void testCheckWithSqlContainingInvalidParameter() {
        ConnectionSession session = createConnectionSession();
        SqlCheckReq req = createSqlCheckReq();
        List<Rule> rules = createRuleList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        String sql = "SELECT * FROM test WHERE id = ?";
        List<CheckViolation> checkViolations = createCheckViolationList();
        when(jdbcTemplate.query(sql.replace("test", "test"), new Object[] { 10L }))
                .thenThrow(new IllegalArgumentException("Mock Exception"));
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(1, result.get(0).getViolations().size());
        CheckViolation violation = result.get(0).getViolations().get(0);
        assertEquals(RuleViolationType.INVALID_SQL_PARAMETER.name(), violation.getType().getName());
    }

    @Test
    public void testCheckWithSqlContainingInvalidKeyword() {
        ConnectionSession session = createConnectionSession();
        SqlCheckReq req = createSqlCheckReq();
        List<Rule> rules = createRuleList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        String sql = "SELECT * FROM test WITH ID = ?";
        List<CheckViolation> checkViolations = createCheckViolationList();
        when(jdbcTemplate.query(sql.replace("test", "test"), new Object[] { 1L }))
                .thenReturn(checkViolations);
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(1, result.get(0).getViolations().size());
        CheckViolation violation = result.get(0).getViolations().get(0);
        assertEquals(RuleViolationType.INVALID_SQL_KEYWORD.name(), violation.getType().getName());
    }

    @Test
    public void testCheckWithSqlContainingInvalidFunction() {
        ConnectionSession session = createConnectionSession();
        SqlCheckReq req = createSqlCheckReq();
        List<Rule> rules = createRuleList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        String sql = "SELECT * FROM test WHERE id IN (SELECT id FROM nonexistent_table)";
        List<CheckViolation> checkViolations = createCheckViolationList();
        when(jdbcTemplate.query(sql.replace("test", "test"), anyArray()))
                .thenThrow(new RuntimeException("Mock Exception"));
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(1, result.get(0).getViolations().size());
        CheckViolation violation = result.get(0).getViolations().get(0);
        assertEquals(RuleViolationType.INVALID_SQL_FUNCTION.name(), violation.getType().getName());
    }

    @Test
    public void testCheckWithSqlContainingInvalidComment() {
        ConnectionSession session = createConnectionSession();
        SqlCheckReq req = createSqlCheckReq();
        List<Rule> rules = createRuleList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        String sql = "SELECT * FROM test--comment";
        List<CheckViolation> checkViolations = createCheckViolationList();
        when(jdbcTemplate.query(sql.replace("test", "test"), anyArray()))
                .thenReturn(checkViolations);
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(1, result.get(0).getViolations().size());
        CheckViolation violation = result.get(0).getViolations().get(0);
        assertEquals(RuleViolationType.INVALID_SQL_COMMENT.name(), violation.getType().getName());
    }

    @Test
    public void testCheckWithSqlContainingInvalidString() {
        ConnectionSession session = createConnectionSession();
        SqlCheckReq req = createSqlCheckReq();
        List<Rule> rules = createRuleList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        String sql = "SELECT * FROM test WHERE id = '1' OR 1 = 'a'";
        List<CheckViolation> checkViolations = createCheckViolationList();
        when(jdbcTemplate.query(sql.replace("test", "test"), anyArray()))
                .thenReturn(checkViolations);
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(1, result.get(0).getViolations().size());
        CheckViolation violation = result.get(0).getViolations().get(0);
        assertEquals(RuleViolationType.INVALID_SQL_STRING.name(), violation.getType().getName());
    }

    @Test
    public void testCheckWithSqlContainingInvalidRegex() {
        ConnectionSession session = createConnectionSession();
        SqlCheckReq req = createSqlCheckReq();
        List<Rule> rules = createRuleList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        String sql = "SELECT * FROM test WHERE id REGEXP '[a-z]'";
        List<CheckViolation> checkViolations = createCheckViolationList();
        when(jdbcTemplate.query(sql.replace("test", "test"), anyArray()))
                .thenReturn(checkViolations);
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(1, result.get(0).getViolations().size());
        CheckViolation violation = result.get(0).getViolations().get(0);
        assertEquals(RuleViolationType.INVALID_SQL_REGEX.name(), violation.getType().getName());
    }

    @Test
    public void testCheckWithSqlContainingInvalidDataType() {
        ConnectionSession session = createConnectionSession();
        SqlCheckReq req = createSqlCheckReq();
        List<Rule> rules = createRuleList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        String sql = "SELECT * FROM test WHERE id = CAST('1' AS INT) + CAST('a' AS INT)";
        List<CheckViolation> checkViolations = createCheckViolationList();
        when(jdbcTemplate.query(sql.replace("test", "test"), anyArray()))
                .thenThrow(new RuntimeException("Mock Exception"));
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(1, result.get(0).getViolations().size());
        CheckViolation violation = result.get(0).getViolations().get(0);
        assertEquals(RuleViolationType.INVALID_SQL_DATA_TYPE.name(), violation.getType().getName());
    }

    private ConnectionSession createConnectionSession() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        return session;
    }

    private SqlCheckReq createSqlCheckReq() {
        return new SqlCheckReq();
    }

    private List<Rule> createRuleList() {
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("test_rule_name");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        rule1.setEnabled(true);
        Rule rule2 = new Rule();
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("another_rule_name");
        metadata2.setType(RuleType.SQL_CHECK);
        rule2.setMetadata(metadata2);
        rule2.setEnabled(false);
        return new ArrayList<Rule>() {{
            add(rule1);
            add(rule2);
        }};
    }

    private Environment createEnvironment() {
        Environment env = mock(Environment.class);
        when(environmentService.detail(anyLong())).thenReturn(env);
        return env;
    }

    private List<CheckViolation> createCheckViolationList() {
        return Collections.emptyList();
    }
}