package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRule;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        this.sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithSessionAndReq() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        List<CheckResult> results = sqlCheckService.check(session, req);

        assertNotNull(results);
    }

    @Test
    public void testCheckWithEnvironmentIdAndSqls() {
        Long environmentId = 1L;
        String databaseName = "testDatabase";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM table", 0, 22));
        ConnectionConfig config = new ConnectionConfig();

        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(violations);
    }

    @Test
    public void testGetRulesWithSession() {
        ConnectionSession session = new ConnectionSession();
        List<Rule> rules = Collections.singletonList(new Rule(new RuleMetadata("rule1", "description1")));
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testGetRulesWithDialectAndJdbc() {
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = new JdbcTemplate();
        List<Rule> rules = Collections.singletonList(new Rule(new RuleMetadata("rule1", "description1")));
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);

        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = Collections.singletonList(new Rule(new RuleMetadata("rule1", "description1")));
        List<CheckViolation> violations = Collections.singletonList(new CheckViolation("violation1"));

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertNotNull(violatedRules);
    }
}