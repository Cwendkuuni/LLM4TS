package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

/**
 * Test class for {@link SqlCheckService}
 *
 * @author yh263208
 */
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SingleConnectionDataSource dataSource;

    @Mock
    private ConnectionSession session;

    @Mock
    private Environment environment;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        SqlCheckReq req = new SqlCheckReq();
        ConnectionSession sessionMock = mock(ConnectionSession.class);
        when(sessionMock.getDialectType()).thenReturn(DialectType.MySQL);
        when(sessionMock.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);
        when(environmentService.detail(1L)).thenReturn(environment);
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.singletonList(ruleMock()));
        when(dataSource.getConnection()).thenReturn(mockConnection());

        List<CheckResult> result = sqlCheckService.check(sessionMock, req);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ruleName", result.get(0).getViolations().get(0).getCheckRuleName());
    }

    @Test
    public void testCheckWithEmptyRules() {
        ConnectionConfig config = new ConnectionConfig();
        when(environmentService.detail(1L)).thenReturn(environment);
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());

        List<CheckViolation> violations = sqlCheckService.check(1L, "testDbName", Collections.singletonList(new OffsetString("")), config);
        assertNotNull(violations);
        assertEquals(0, violations.size());
    }

    @Test
    public void testCheckWithEmptySqls() {
        ConnectionConfig config = new ConnectionConfig();
        List<OffsetString> sqls = Collections.singletonList(new OffsetString(""));
        when(environmentService.detail(1L)).thenReturn(environment);
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.singletonList(ruleMock()));
        when(dataSource.getConnection()).thenReturn(mockConnection());

        List<CheckViolation> violations = sqlCheckService.check(1L, "testDbName", sqls, config);
        assertNotNull(violations);
        assertEquals(0, violations.size());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = ruleMock();
        Rule rule2 = ruleMock();
        Rule rule3 = ruleMock();
        rules.add(rule1);
        rules.add(rule2);
        rules.add(rule3);
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        DialectType dialectType = DialectType.MySQL;
        when(session.getDialectType()).thenReturn(dialectType);

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = ruleMock();
        Rule rule2 = ruleMock();
        rules.add(rule1);
        rules.add(rule2);
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(rule1.getMetadata().getType());
        CheckViolation violation2 = new CheckViolation();
        violation2.setType(rule2.getMetadata().getType());

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(violation1);
        violations.add(violation2);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(rule1.getMetadata().getName(), result.get(0).getMetadata().getName());
        assertEquals(rule2.getMetadata().getName(), result.get(1).getMetadata().getName());
    }

    @Test
    public void testFullFillRiskLevelWithEmptyRules() {
        List<Rule> rules = new ArrayList<>();
        CheckViolation violation = new CheckViolation();

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(violation);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    private Rule ruleMock() {
        Rule rule = mock(Rule.class);
        RuleMetadata ruleMetadata = mock(RuleMetadata.class);
        when(rule.getMetadata()).thenReturn(ruleMetadata);
        when(ruleMetadata.getName()).thenReturn("ruleName");
        when(ruleMetadata.getType()).thenReturn(RuleType.SQL_CHECK);
        return rule;
    }

    private Connection connectionMock() {
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        return connection;
    }
}