package com.oceanbase.odc.service.sqlcheck;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SqlCheckServiceTest {

    @InjectMocks
    SqlCheckService sqlCheckService;

    @Mock
    RuleService ruleService;
    
    @Mock
    EnvironmentService environmentService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkTest(){
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setEnabled(true);
        rules.add(rule);
        
        when(ruleService.list(anyLong(),any())).thenReturn(rules);

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertEquals("CheckResult size is wrong",1,result.size());
    }

    @Test
    public void checkTest_NoRule(){
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();

        List<Rule> rules = new ArrayList<>();
        
        when(ruleService.list(anyLong(),any())).thenReturn(rules);

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertEquals("CheckResult size is wrong",0,result.size());
    }

    @Test
    public void checkTest_RuleDisabled(){
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setEnabled(false);
        rules.add(rule);
        
        when(ruleService.list(anyLong(),any())).thenReturn(rules);

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertEquals("CheckResult size is wrong",0,result.size());
    }

    @Test
    public void getRulesTest_NoRules(){
        ConnectionSession session = new ConnectionSession();
        List<Rule> rules = new ArrayList<>();

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);

        assertEquals("SqlCheckRule size is wrong",0,result.size());
    }
}