package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;

import java.util.*;


public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;
    
    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckWithSession() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();

        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Check returned result
        // To be implemented based on the expected behavior of your system
    }

    @Test
    public void testCheckWithEnvironmentIdDatabaseNameSqlsConfig() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        ConnectionConfig config = new ConnectionConfig();

        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString());

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // Check returned result
        // To be implemented based on the expected behavior of your system
    }

    @Test
    public void testGetRulesWithSession() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());
        ConnectionSession session = new ConnectionSession();

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        // Check returned result
        // To be implemented based on the expected behavior of your system
    }
    
    @Test
    public void testGetRulesWithDialectTypeJdbc() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());
        DialectType dialectType = new DialectType();
        JdbcOperations jdbc = new JdbcTemplate();

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);

        // Check returned result
        // To be implemented based on the expected behavior of your system
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        // Check returned result
        // To be implemented based on the expected behavior of your system
    }
}