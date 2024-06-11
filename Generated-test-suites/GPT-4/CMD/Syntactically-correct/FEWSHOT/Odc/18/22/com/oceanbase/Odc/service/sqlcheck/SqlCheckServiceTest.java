package com.oceanbase.odc.service.sqlcheck;

import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {
    
    @InjectMocks
    SqlCheckService sqlCheckService;

    @Mock
    RuleService ruleService;

    @Mock
    EnvironmentService environmentService;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckFirstMethod() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        
        assertTrue(sqlCheckService.check(session, req).isEmpty());
        
        //Add further assertions to check specific behaviour
    }

    @Test
    public void testCheckSecondMethod() {
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(config.getDialectType()).thenReturn(DialectType.MYSQL);
        
        List<OffsetString> sqls = new ArrayList<>();
        assertTrue(sqlCheckService.check(1L, "db1", sqls, config).isEmpty());
        
        //Add further assertions to check specific behaviour
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rules.add(rule);

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violations.add(violation);

        assertTrue(sqlCheckService.fullFillRiskLevel(rules, violations).isEmpty());
        
        //Add further assertions to check specific behaviour
    }
}