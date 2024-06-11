package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext.xml" })
public class SqlCheckServiceTest {

    @Mock
    ConnectionSession session;

    @Mock
    SqlCheckReq req;

    @Mock
    ConnectionConfig config;

    @Test
    public void testCheck() {
        SqlCheckService sqlCheckService = new SqlCheckService();

        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertNotNull(checkResults);
        assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        SqlCheckService sqlCheckService = new SqlCheckService();

        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM table"));
        
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(checkViolations);
        assertTrue(checkViolations.isEmpty());
    }

    @Test
    public void testGetRules() {
        SqlCheckService sqlCheckService = new SqlCheckService();

        List<Rule> rules = Collections.singletonList(new Rule());
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        assertNotNull(sqlCheckRules);
        assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        SqlCheckService sqlCheckService = new SqlCheckService();

        List<Rule> rules = Collections.singletonList(new Rule());
        List<CheckViolation> checkViolations = Collections.singletonList(new CheckViolation());

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        assertNotNull(violatedRules);
        assertEquals(1, violatedRules.size());
    }
}