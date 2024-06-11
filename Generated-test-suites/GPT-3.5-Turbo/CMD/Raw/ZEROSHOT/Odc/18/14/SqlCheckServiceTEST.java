package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private ConnectionSession session;
    private SqlCheckReq req;
    private List<CheckViolation> checkViolations;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        session = mock(ConnectionSession.class);
        req = new SqlCheckReq("script content", "delimiter");
        
        // Mocking the behavior of session object
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
    }

    @Test
    public void testCheckWithSession() {
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertNotNull(checkResults);
        assertEquals(Collections.emptyList(), checkResults);
    }

    @Test
    public void testGetRules() {
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(new ArrayList<>(), session);
        assertNotNull(sqlCheckRules);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation("violation1", "type1");
        CheckViolation violation2 = new CheckViolation("violation2", "type2");
        violations.add(violation1);
        violations.add(violation2);

        List<CheckResult> checkResults = new ArrayList<>();
        CheckResult checkResult1 = new CheckResult(violation1);
        CheckResult checkResult2 = new CheckResult(violation2);
        checkResults.add(checkResult1);
        checkResults.add(checkResult2);

        List<CheckResult> violatedRules = sqlCheckService.fullFillRiskLevel(new ArrayList<>(), violations);
        assertNotNull(violatedRules);
        assertEquals(2, violatedRules.size());
        assertEquals(violation1.getType(), violatedRules.get(0).getViolation().getType());
        assertEquals(violation2.getType(), violatedRules.get(1).getViolation().getType());
    }

}