package com.oceanbase.odc.service.sqlcheck;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;

public class SqlCheckServiceTest {

    @InjectMocks
    SqlCheckService service;

    @Mock
    RuleService ruleService;

    @Mock
    EnvironmentService environmentService;

    @Mock
    ConnectionSession session;

    @Mock
    SqlCheckReq req;

    @Mock
    List<Rule> rules;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        Mockito.when(ConnectionSessionUtil.getRuleSetId(any())).thenReturn(Long.valueOf(1));
        Mockito.when(service.check(session,req)).thenReturn(new ArrayList<>());
        
        List<CheckResult> result = service.check(session, req);
        Assert.assertEquals(result, new ArrayList<>());
    }
    
    @Test
    public void testGetRules() {
        Mockito.when(ConnectionSessionUtil.getRuleSetId(any())).thenReturn(Long.valueOf(1));
        List<SqlCheckRule> sqlCheckRules = service.getRules(rules, session);

        Assert.assertEquals(sqlCheckRules, new ArrayList<>());
    }
}