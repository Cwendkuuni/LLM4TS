package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.regulation.ruleset.model.*;

public class SqlCheckServiceTest {
    
    @Mock
    EnvironmentService environmentService;

    @Mock
    RuleService ruleService;

    @InjectMocks
    SqlCheckService sqlCheckService;
    
    @Test
    public void checkTest() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);
        Rule rule = mock(Rule.class);
        List<Rule> rules = Stream.of(rule).collect(Collectors.toList());
        when(ruleService.list(anyLong(), anyObject())).thenReturn(rules);
 
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(result, Collections.emptyList());
    }
}