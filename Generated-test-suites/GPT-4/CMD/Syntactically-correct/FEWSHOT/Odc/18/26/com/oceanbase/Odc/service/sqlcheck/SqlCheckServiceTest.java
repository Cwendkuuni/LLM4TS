package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SqlCheckServiceTest {
    @Mock
    private ConnectionSession mockSession;
    @Mock
    private SqlCheckReq mockReq;
    @Mock
    private ConnectionConfig mockConfig;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testCheckWithSessionAndReq() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        when(mockSession.getRuleSetId()).thenReturn(null);
        assertTrue(sqlCheckService.check(mockSession, mockReq).isEmpty());
    }
    
    @Test
    public void testCheckWithEnvironmentIdAndDatabaseName() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        assertTrue(sqlCheckService.check(1L, "databaseName", Collections.emptyList(), mockConfig).isEmpty());
    }
    
    @Test
    public void testGetRulesWithRuleListAndSession() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        assertTrue(sqlCheckService.getRules(Collections.emptyList(), mockSession).isEmpty());
    }
    
    @Test
    public void testGetRulesWithRuleListAndDialectTypeAndJdbc() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        assertTrue(sqlCheckService.getRules(Collections.emptyList(), null, null).isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        Rule rule = new Rule();
        CheckViolation violation = new CheckViolation();
        assertEquals(0, sqlCheckService.fullFillRiskLevel(Collections.singletonList(rule), Collections.singletonList(violation)).size());
    }
}