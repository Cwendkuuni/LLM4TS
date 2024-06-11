package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private ConnectionSession session;

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
    }

    @Test
    public void testCheckWithSessionAndReq() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        req.setDelimiter(";");

        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertNotNull(checkResults);
        assertEquals(1, checkResults.size());
        CheckResult checkResult = checkResults.get(0);
        assertNotNull(checkResult.getCheckViolations());
    }

    @Test
    public void testCheckWithEnvironmentAndSqls() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString(0, "SELECT * FROM table"));
        ConnectionConfig config = new ConnectionConfig();

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(checkViolations);
        assertFalse(checkViolations.isEmpty());
    }

    @Test
    public void testGetRulesWithSession() {
        List<SqlCheckRule> rules = sqlCheckService.getRules(new ArrayList<>(), session);

        assertNotNull(rules);
        assertTrue(rules.isEmpty());
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbc() {
        List<SqlCheckRule> rules = sqlCheckService.getRules(new ArrayList<>(), DialectType.MYSQL, jdbc);

        assertNotNull(rules);
        assertTrue(rules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation("Violation 1", "Description 1"));
        violations.add(new CheckViolation("Violation 2", "Description 2"));
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule("Rule 1", "Description 1", 1));
        rules.add(new Rule("Rule 2", "Description 2", 2));

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertNotNull(violatedRules);
        assertFalse(violatedRules.isEmpty());
        assertEquals(2, violatedRules.size());
    }
}