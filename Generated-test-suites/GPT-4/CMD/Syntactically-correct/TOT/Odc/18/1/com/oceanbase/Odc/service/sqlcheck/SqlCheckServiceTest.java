package com.oceanbase.odc.service.sqlcheck;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * SqlCheckServiceTest
 *
 */
public class SqlCheckServiceTest {
    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = Mockito.mock(SqlCheckService.class);
    }

    @After
    public void tearDown() {
        sqlCheckService = null;
    }

    @Test
    public void testCheckWithSession() {
        // Arrangement
        Mockito.when(ruleService.list(Mockito.anyInt(), Mockito.any())).thenReturn(Collections.emptyList());

        // Action
        List<CheckResult> result = sqlCheckService.check(Mockito.mock(ConnectionSession.class), Mockito.mock(SqlCheckReq.class));

        // Assertion
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithEnvironment() {
        // Arrangement
        Mockito.when(ruleService.list(Mockito.anyInt(), Mockito.any())).thenReturn(Collections.emptyList());

        // Action
        List<CheckViolation> result = sqlCheckService.check(Mockito.anyLong(), Mockito.anyString(), Collections.emptyList(), Mockito.mock(ConnectionConfig.class));

        // Assertion
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size()); 
    }

    @Test
    public void testGetRulesWithSession() {
        // Arrangement
        ConnectionSession connSession = Mockito.mock(ConnectionSession.class);
        Mockito.when(connSession.getDialectType()).thenReturn(Mockito.mock(DialectType.class));

        // Action
        List<SqlCheckRule> result = sqlCheckService.getRules(Collections.emptyList(), connSession);

        // Assertion
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size()); 
    }

    @Test
    public void testGetRulesWithSpecificParameters() {
        // Arrangement
        Mockito.when(ruleService.list(Mockito.anyInt(), Mockito.any())).thenReturn(Collections.emptyList());

        // Action
        List<SqlCheckRule> result = sqlCheckService.getRules(Collections.emptyList(), Mockito.mock(DialectType.class), Mockito.mock(JdbcOperations.class));

        // Assertion
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());  
    }

    @Test
    public void testFullFillRiskLevel() {
        // Arrangement
        List<Rule> rules = Collections.emptyList();
        List<CheckViolation> violations =  Collections.emptyList();
        
        // Action
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

       // Assertion
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());   
    }
}
