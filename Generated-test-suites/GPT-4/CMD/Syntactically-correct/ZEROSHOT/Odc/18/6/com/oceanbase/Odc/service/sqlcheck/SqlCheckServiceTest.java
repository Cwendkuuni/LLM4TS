
package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.core.sql.split.OffsetString;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    ConnectionSession connectionSession;

    @Mock
    SqlCheckReq sqlCheckReq;

    @Mock 
    ConnectionConfig connectionConfig;

    List<OffsetString> sqls = new ArrayList<>();

    @Before
    public void setUp(){
       sqls.add(new OffsetString("select * from test", 1));
    }

    @Test
    public void check_test(){
        when(connectionSession.getProperty("RuleSetId")).thenReturn(1L);
        when(ruleService.list(eq(1L), any(QueryRuleMetadataParams.class))).thenReturn(Collections.EMPTY_LIST);
        sqlCheckService.check(connectionSession, sqlCheckReq);
        verify(ruleService).list(eq(1L), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void check_environment_test(){
        when(environmentService.detail(1L)).thenReturn(mock(Environment.class));
        sqlCheckService.check(1L, "test", sqls, connectionConfig);
        verify(environmentService).detail(1L);
    }

    @Test
    public void fullFillRiskLevel_test() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = mock(Rule.class);
        rules.add(rule);

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = mock(CheckViolation.class);
        violations.add(violation);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(result.size(), 1);
    }
}
