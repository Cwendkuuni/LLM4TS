package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcOperations;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private ConnectionSession session;
    private SqlCheckReq req;
    private List<Rule> rules;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        session = mock(ConnectionSession.class);
        req = mock(SqlCheckReq.class);
        rules = new ArrayList<>();
    }

    @Test
    public void testCheckWithSessionAndReq() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(req.getDelimiter()).thenReturn(";");
        
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        
        assertNotNull(checkResults);
        assertEquals(Collections.emptyList(), checkResults);
    }

    @Test
    public void testCheckWithEnvironmentIdAndDatabaseName() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertNotNull(checkViolations);
        assertEquals(Collections.emptyList(), checkViolations);
    }

    @Test
    public void testGetRulesWithSession() {
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        
        assertNotNull(sqlCheckRules);
        assertEquals(Collections.emptyList(), sqlCheckRules);
    }

    @Test
    public void testGetRulesWithJdbc() {
        JdbcOperations jdbc = mock(JdbcOperations.class);
        
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbc);
        
        assertNotNull(sqlCheckRules);
        assertEquals(Collections.emptyList(), sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = new ArrayList<>();
        
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        
        assertNotNull(violatedRules);
        assertEquals(Collections.emptyList(), violatedRules);
    }

}