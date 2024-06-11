package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private ConnectionSession session;

    @Mock
    private SqlCheckReq req;

    @Mock
    private ConnectionConfig config;

    @Mock
    private SingleConnectionDataSource dataSource;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckSessionReq() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rules.add(rule);
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckEnvironmentDatabaseSqlsConfig() {
        List<CheckResult> results = sqlCheckService.check(1L, "testDb", new ArrayList<>(), config);
        assertEquals(0, results.size());
    }

    @Test
    public void testGetRulesWithSession() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rules.add(rule);

        Mockito.when(session.getDialectType()).thenReturn(DialectType.OCEANBASE);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbc() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rules.add(rule);

        Mockito.when(session.getDialectType()).thenReturn(DialectType.OCEANBASE);
        Mockito.when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.OCEANBASE, jdbcTemplate);
        assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rules.add(rule);

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violations.add(violation);
        
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(rules.size(), violatedRules.size());
    }
}