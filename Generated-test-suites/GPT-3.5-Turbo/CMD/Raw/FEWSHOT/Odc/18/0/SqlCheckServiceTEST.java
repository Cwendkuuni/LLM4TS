package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private ConnectionSession session;

    @Mock
    private SqlCheckReq req;

    @Mock
    private ConnectionConfig config;

    @Mock
    private SingleConnectionDataSource dataSource;

    @Mock
    private JdbcTemplate jdbc;

    @Test
    public void testCheck_SessionAndReqNotNull() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        
        Mockito.when(session.getDialectType()).thenReturn(DialectType.ORACLE);
        Mockito.when(req.getDelimiter()).thenReturn(";");
        
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any())).thenReturn(rules);
        Mockito.when(sqlCheckService.getRules(Mockito.eq(rules), Mockito.any(ConnectionSession.class))).thenReturn(sqlCheckRules);
        
        List<CheckViolation> checkViolations = sqlCheckService.check(session, req);
        
        assertNotNull(checkViolations);
        assertFalse(checkViolations.isEmpty());
    }

    @Test
    public void testCheck_EnvironmentIdAndSqlsNotNull() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        
        Long environmentId = 123L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        
        Environment env = new Environment();
        List<Rule> rules = new ArrayList<>();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true, false);
        SqlCheckContext checkContext = new SqlCheckContext((long) sqls.size());
        
        Mockito.when(environmentService.detail(Mockito.eq(environmentId))).thenReturn(env);
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any())).thenReturn(rules);
        Mockito.when(factory.getDataSource()).thenReturn(dataSource);
        Mockito.when(new JdbcTemplate(dataSource)).thenReturn(jdbc);
        
        List<SqlCheckRule> checkRules = new ArrayList<>();
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.ORACLE, null, checkRules);
        List<CheckViolation> checkViolations = new ArrayList<>();
        
        Mockito.when(sqlChecker.check(Mockito.eq(sqls), Mockito.eq(checkContext))).thenReturn(checkViolations);
        
        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetRules_ConnectionSessionNotNull() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        
        Mockito.when(session.getDialectType()).thenReturn(DialectType.ORACLE);
        Mockito.when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbc);
        
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetRules_DialectTypeAndJdbcNotNull() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        
        Mockito.when(jdbc).thenReturn(jdbc);
        
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, DialectType.ORACLE, jdbc);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        
        List<Rule> rules = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        List<CheckViolation> violations = Collections.singletonList(violation);
        
        Rules violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        
        assertNotNull(violatedRules);
        assertFalse(violatedRules.isEmpty());
    }
}