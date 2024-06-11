
package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlCheckServiceTest {

    @InjectMocks
    SqlCheckService sqlCheckService;

    @Mock
    RuleService ruleService;

    @Mock
    EnvironmentService environmentService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);

        // If required, you can set return values for your mock object's methods:
        // when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        List<CheckResult> results = sqlCheckService.check(session, req);
        assertNotNull(results);
    }

    @Test
    public void testCheckWithEnvironmentId() {
        Long environmentId = 1L;
        String databaseName = "databaseTest";
        List<OffsetString> sqls = new ArrayList<>(); // Populate this as required for the test
        ConnectionConfig config = mock(ConnectionConfig.class);

        List<CheckViolation> results = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(results);
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>(); // Populate this as required for the test
        ConnectionSession session = mock(ConnectionSession.class);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbc() {
        List<Rule> rules = new ArrayList<>(); // Populate this as required for the test
        DialectType dialectType = DialectType.MYSQL; // or whatever value is required for the test
        JdbcOperations jdbc = new JdbcTemplate();

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>(); // Populate this as required for the test
        List<CheckViolation> violations = new ArrayList<>(); // Populate this as required for the test

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(result);
    }
}
