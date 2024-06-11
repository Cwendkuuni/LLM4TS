package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private Validator validator;

    @Before
    public void setup() {
        sqlCheckService = new SqlCheckService();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void testCheckMethod() {
        SqlCheckReq req = new SqlCheckReq();
        // Set up req data
        req.setScriptContent("SELECT * FROM table");

        List<CheckResult> results = sqlCheckService.check(mock(ConnectionSession.class), req);

        assertEquals(0, results.size());
    }

    @Test
    public void testCheckMethodWithEmptySession() {
        SqlCheckReq req = new SqlCheckReq();
        // Set up req data
        req.setScriptContent("SELECT * FROM table");

        List<CheckResult> results = sqlCheckService.check(null, req);

        assertEquals(0, results.size());
    }

    @Test
    public void testCheckMethodWithEmptyRules() {
        SqlCheckReq req = new SqlCheckReq();
        // Set up req data
        req.setScriptContent("SELECT * FROM table");

        List<CheckResult> results = sqlCheckService.check(mock(ConnectionSession.class), req);

        assertEquals(0, results.size());
    }

    @Test
    public void testCheckMethodWithEnvironment() {
        List<CheckViolation> violations = sqlCheckService.check(123L, "database", Collections.emptyList(), mock(ConnectionConfig.class));

        assertEquals(0, violations.size());
    }

    @Test
    public void testGetRulesMethod() {
        List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.emptyList(), mock(ConnectionSession.class));

        assertEquals(0, rules.size());
    }

    @Test
    public void testFullFillRiskLevelMethod() {
        List<CheckViolation> violations = Collections.singletonList(new CheckViolation());
        List<CheckViolation> result = sqlCheckService.fullFillRiskLevel(Collections.emptyList(), violations);

        assertEquals(1, result.size());
    }
}