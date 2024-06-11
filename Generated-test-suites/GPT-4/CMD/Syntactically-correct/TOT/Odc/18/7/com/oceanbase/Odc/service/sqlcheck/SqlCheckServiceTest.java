package com.oceanbase.odc.service.sqlcheck;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    // Mocked objects and parameters for the methods
    @Mock
    private ConnectionSession session;

    @Mock
    private SqlCheckReq req;

    private Long ruleSetId;

    // Set up the environment for each test
    @Before
    public void setup() {
        ruleSetId = 123L;

        // You will need to provide realistic mock objects and responses
    }
 

    // Test case for check() method without ruleSetId, should return empty list.
    @Test
    public void check_withNoRuleSetId() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(result.isEmpty());
    }

    // Test case for check() method with ruleSetId but no defined rules.
    @Test
    public void check_withRuleSetId_noRules() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(result.isEmpty());
    }

    // Add more test cases...
}