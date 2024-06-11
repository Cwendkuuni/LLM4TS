package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    
    @Mock
    private EnvironmentService environmentService;

    @Test
    public void testCheckWithSessionAndReq() {
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertNotNull(results);
    }

    @Test
    public void testCheckWithEnvironmentDetails() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(violations);
    }

    @Test
    public void testGetRulesFromSession() {
        ConnectionSession session = new ConnectionSession();
        List<Rule> rules = new ArrayList<>();
        JdbcOperations jdbc = Mockito.mock(JdbcTemplate.class);
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testGetRulesFromDialectTypeAndJdbc() {
        List<Rule> rules = new ArrayList<>();
        DialectType dialectType = DialectType.ORACLE;
        JdbcOperations jdbc = Mockito.mock(JdbcTemplate.class);
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);
        assertNotNull(sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(violatedRules);
    }

}