package com.oceanbase.odc.service.sqlcheck;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    
    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        
        Rule rule = new Rule();
        rule.setEnabled(true);
        
        doReturn(Collections.singletonList(rule)).when(ruleService).list(any(), any());
        
        List result = sqlCheckService.check(session, req);
        
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        List result = sqlCheckService.check(1L, "default", Collections.emptyList(), new ConnectionConfig());
        
        assertEquals(0, result.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        Rule rule = new Rule();
        rule.setEnabled(true);
        
        List<Rule> rules = Collections.singletonList(rule);
        List<CheckViolation> violations = Collections.emptyList();

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertTrue(result.isEmpty());
    }
}