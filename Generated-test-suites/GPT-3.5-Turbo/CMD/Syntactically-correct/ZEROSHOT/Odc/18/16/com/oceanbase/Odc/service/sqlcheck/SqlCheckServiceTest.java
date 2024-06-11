package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private List<CheckResult> checkResults;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithConnectionSession() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        checkResults = sqlCheckService.check(session, req);
        
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM table", 0));
        
        ConnectionConfig config = new ConnectionConfig();
        checkResults = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testGetRulesWithSession() {
        List<Rule> rules = new ArrayList<>();
        ConnectionSession session = mock(ConnectionSession.class);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithJdbc() {
        List<Rule> rules = new ArrayList<>();
        JdbcOperations jdbc = mock(JdbcOperations.class);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbc);

        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation("Violation", "Error"));
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(1, violatedRules.size());
    }
}