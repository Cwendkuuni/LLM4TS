package com.oceanbase.odc.service.sqlcheck;
import org.junit.Before;
import org.junit.Test;

public class SqlCheckServiceTest {
    private SqlCheckService sqlCheckService;
    // let's assume that testSession, testReq, .. etc. are instances of their respective types
    private ConnectionSession testSession;
    private SqlCheckReq testReq;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void checkTest() {
        List<CheckResult> resultList = sqlCheckService.check(testSession, testReq);
        assertNotNull(resultList);
    }

    @Test
    public void checkTest2() {
        List<CheckViolation> violationList = sqlCheckService.check(testEnvironmentId, testDatabaseName, testSqls, testConfig);
        assertNotNull(violationList);
    }

    @Test
    public void getRulesTest1() {
        List<SqlCheckRule> rulesList1 = sqlCheckService.getRules(testRulesList, testSession);
        assertNotNull(rulesList1);
    }

    @Test
    public void getRulesTest2() {
        List<SqlCheckRule> rulesList2 = sqlCheckService.getRules(testRulesList, testDialectType, testJdbc);
        assertNotNull(rulesList2);
    }

    @Test
    public void fullFillRiskLevelTest() {
        List<Rule> filledRuleList = sqlCheckService.fullFillRiskLevel(testRulesList, testViolationList);
        assertNotNull(filledRuleList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenInputIsNull() {
        sqlCheckService.check(null, null);
        sqlCheckService.check(testEnvironmentId, null, null, null);
        sqlCheckService.getRules(null, null);
        sqlCheckService.getRules(null, null, null);
        sqlCheckService.fullFillRiskLevel(null, null);
    }

@Test
public void shouldReturnEmptyListWhenGivenEmptyList() {
        assertTrue(sqlCheckService.check(testEnvironmentId, testDatabaseName, new ArrayList<>(), testConfig).isEmpty());
        assertTrue(sqlCheckService.getRules(new ArrayList<>(), testSession).isEmpty());
        assertTrue(sqlCheckService.getRules(new ArrayList<>(), testDialectType, testJdbc).isEmpty());
}
}