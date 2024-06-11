package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithSessionAndReq() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql = new OffsetString("SELECT * FROM table", 0);
        sqls.add(sql);
        ConnectionConfig config = new ConnectionConfig();
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(violations);
        assertEquals(0, violations.size());
    }

    @Test
    public void testGetRulesWithSession() {
        ConnectionSession session = new ConnectionSession();
        List<SqlCheckRule> rules = sqlCheckService.getRules(new ArrayList<>(), session);
        assertNotNull(rules);
        assertEquals(0, rules.size());
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbc() {
        DialectType dialectType = DialectType.MYSQL;
        JdbcTemplate jdbc = new JdbcTemplate();
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);
        assertNotNull(sqlCheckRules);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType("ViolationType");
        violations.add(violation);
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(violatedRules);
        assertEquals(0, violatedRules.size());
    }
}