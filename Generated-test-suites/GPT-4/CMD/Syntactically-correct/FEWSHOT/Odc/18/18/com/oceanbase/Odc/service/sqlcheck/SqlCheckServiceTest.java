
package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private ConnectionSession session;
    @Mock
    private SqlCheckReq req;

    @Before
    public void setup() {
        when(ruleService.list(12345L, QueryRuleMetadataParams.builder().build())).thenReturn(new ArrayList<Rule>());
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(req.getDelimiter()).thenReturn(";");
        when(req.getScriptContent()).thenReturn("SELECT * FROM table;");
    }

    @Test
    public void testCheck() {
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), result);
    }
}
