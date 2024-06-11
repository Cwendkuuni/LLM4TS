package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

public class SqlCheckServiceTest {

    @Test
    public void testCheckWithSessionAndRequest() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertNotNull(checkResults);
    }

    @Test
    public void testCheckWithEnvironmentIdAndDatabaseName() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        Long environmentId = 1L;
        String databaseName = "myDatabase";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString(0, "SELECT * FROM table"));
        ConnectionConfig config = new ConnectionConfig();
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(checkViolations);
    }

    @Test
    public void testGetRulesWithSession() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        ConnectionSession session = new ConnectionSession();
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbcOperations() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = new JdbcTemplate(new SingleConnectionDataSource());
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        // Add some mock violations and rules before testing
        Rule rule1 = new Rule();
        rule1.getMetadata().setName("Rule1");
        rules.add(rule1);
        
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(CheckViolation.Type.SECURITY);
        violations.add(violation1);

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(violatedRules);
        assertEquals(1, violatedRules.size());
    }
}