package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private ConnectionSession session;
    private SqlCheckReq req;
    private ConnectionConfig config;
    private List<CheckViolation> checkViolations;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        session = new ConnectionSession();
        req = new SqlCheckReq();
        config = new ConnectionConfig();
        checkViolations = new ArrayList<>();
    }

    @Test
    public void testCheckWithSessionAndReq() {
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertNotNull(checkResults);
    }

    @Test
    public void testCheckWithEnvironmentIdAndDatabaseName() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, new ArrayList<>(), config);
        assertNotNull(violations);
    }

    @Test
    public void testGetRulesWithSession() {
        List<CheckViolation> violations = new ArrayList<>();
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertNotNull(checkResults);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = new ArrayList<>();
        List<CheckViolation> filledViolations = sqlCheckService.fullFillRiskLevel(new ArrayList<>(), violations);
        assertNotNull(filledViolations);
    }
}