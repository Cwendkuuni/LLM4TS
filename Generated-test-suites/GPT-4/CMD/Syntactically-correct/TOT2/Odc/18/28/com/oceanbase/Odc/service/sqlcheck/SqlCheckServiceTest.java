package com.oceanbase.odc.service.sqlcheck;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest{

    @InjectMocks
    SqlCheckService sqlCheckService;

    @Mock
    RuleService ruleService;

    @Mock
    EnvironmentService environmentService;

    @Before
    public void setUp(){
        when(ruleService.list(anyLong(),
            QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());
    }

   @Test
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType())
            .thenReturn(DialectType.POSTGRESQL);

        SqlCheckReq req = mock(SqlCheckReq.class);

        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheckEnvironment(){
        Long envId = 1L;
        String db = "testDB";

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.POSTGRESQL);

        Environment env = new Environment();
        env.setRulesetId(1L);

        when(environmentService.detail(anyLong())).thenReturn(env);

        List<OffsetString> sqls = new ArrayList<>();
        List<CheckViolation> checkViolations = sqlCheckService.check(envId, db, sqls, config);

        assertTrue(checkViolations.isEmpty());
    }
}