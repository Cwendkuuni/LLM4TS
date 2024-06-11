package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.service.sqlcheck.*;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private EnvironmentService environmentService;
    @Mock
    private RuleService ruleService;
    
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkTest() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();

        when(session.getRuleSetId()).thenReturn(1L);
        when(ruleService.list(Mockito.anyLong(), Mockito.any())).thenReturn(Arrays.asList(new Rule()));

        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(1, result.size());
    }

    @Test
    public void checkWithEnvironmentTest() {
        ConnectionConfig config = new ConnectionConfig();
        Environment env = new Environment();
        env.setRulesetId(1L);

        when(environmentService.detail(Mockito.anyLong())).thenReturn(env);
        when(ruleService.list(Mockito.anyLong(), Mockito.any())).thenReturn(Arrays.asList(new Rule()));

        List<CheckViolation> result = sqlCheckService.check(1L, "db", Arrays.asList(new OffsetString()), config);
        assertEquals(1, result.size());
    }
    
     @Test
    public void checkGetRulesTest() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        
        List<SqlCheckRule> rules = sqlCheckService.getRules(Arrays.asList(new Rule()), session);
        assertEquals(1, rules.size());
    }

    @Test
    public void checkFullFillRiskLevelTest() {
        List<Rule> rules = Arrays.asList(new Rule());
        List<CheckViolation> violations = Arrays.asList(new CheckViolation());

        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(1, filledRules.size());
    }
}
