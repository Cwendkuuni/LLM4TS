package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

import java.util.ArrayList;
import java.util.List;

public class SqlCheckServiceTest {
    
    @Mock
    private RuleService ruleService;
    
    @Mock
    private EnvironmentService environmentService;
    
    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
    }

    @Test
    public void testCheckWithSessionAndReq() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        session.setDialectType(DialectType.MYSQL);
        session.setDelimiter(";");
        
        List<CheckResult> results = sqlCheckService.check(session, req);
        
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.ORACLE);
        
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertNotNull(violations);
        assertEquals(0, violations.size());
    }

    @Test
    public void testGetRulesWithSession() {
        List<Rule> rules = new ArrayList<>();
        ConnectionSession session = new ConnectionSession();
        session.setDialectType(DialectType.POSTGRESQL);
        
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        
        assertNotNull(sqlCheckRules);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbc() {
        List<Rule> rules = new ArrayList<>();
        DialectType dialectType = DialectType.SQLITE;
        JdbcOperations jdbc = mock(JdbcTemplate.class);
        
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);
        
        assertNotNull(sqlCheckRules);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        
        assertNotNull(violatedRules);
        assertEquals(0, violatedRules.size());
    }
}