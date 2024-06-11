
package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.validation.Valid;

import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

public class SqlCheckServiceTest {

    @Test
    public void testCheckWithConnectionSession() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq("SELECT * FROM table", ";");
        assertNotNull(sqlCheckService.check(session, req));
    }

    @Test
    public void testCheckWithEnvironmentId() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString(0, "SELECT * FROM table"));
        ConnectionConfig config = new ConnectionConfig();
        assertNotNull(sqlCheckService.check(environmentId, databaseName, sqls, config));
    }

    @Test
    public void testGetRulesWithConnectionSession() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        ConnectionSession session = new ConnectionSession();
        assertNotNull(sqlCheckService.getRules(new ArrayList<>(), session));
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbc() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        DialectType dialectType = DialectType.MYSQL;
        JdbcTemplate jdbc = new JdbcTemplate();
        assertNotNull(sqlCheckService.getRules(new ArrayList<>(), dialectType, jdbc));
    }

    @Test
    public void testFullFillRiskLevel() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<Rule> rules = new ArrayList<>();
        CheckViolation violation = new CheckViolation("Type", "Description");
        List<CheckViolation> violations = Collections.singletonList(violation);
        assertNotNull(sqlCheckService.fullFillRiskLevel(rules, violations));
    }
}
