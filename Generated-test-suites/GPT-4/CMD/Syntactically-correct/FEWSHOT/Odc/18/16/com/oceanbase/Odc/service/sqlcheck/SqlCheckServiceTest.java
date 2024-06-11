
package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;
        
    @Mock
    private EnvironmentService environmentService;
    
    @Test
    public void checkTest() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);
        List<Rule> rules = new ArrayList<>();
        Rule rule = mock(Rule.class);
        rules.add(rule);
        
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        assertEquals(Collections.emptyList(), sqlCheckService.check(session, req));
        
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(anyLong());
        assertEquals(Collections.emptyList(), sqlCheckService.check(session, req));
    }

    @Test
    public void checkWithEnvironmentTest() {
        Long environmentId = anyLong();
        String databaseName = "test";
        ConnectionConfig config = mock(ConnectionConfig.class);
        List<OffsetString>sqls = new ArrayList<>();
        
        assertEquals(Collections.emptyList(), sqlCheckService.check(environmentId, databaseName, sqls, config));
        
        List<Rule> rules = new ArrayList<>();
        Rule rule = mock(Rule.class);
        rules.add(rule);
        
        Environment env = mock(Environment.class);
        when(environmentService.detail(environmentId)).thenReturn(env);
        when(env.getRulesetId()).thenReturn(anyLong());
        
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        
        verify(environmentService, times(1)).detail(environmentId);
        verify(ruleService, times(1)).list(env.getRulesetId(), QueryRuleMetadataParams.builder().build());
        assertNotNull(violations);
    }

    @Test
    public void getRulesTest() {
        ConnectionSession session = mock(ConnectionSession.class);
        List<Rule> rules = new ArrayList<>();
        Rule rule = mock(Rule.class);
        rules.add(rule);

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(mock(JdbcOperations.class));
        assertNotNull(this.sqlCheckService.getRules(rules, session));
    }

    @Test
    public void fullFillRiskLevelTest() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = mock(Rule.class);
        rules.add(rule);
        
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = mock(CheckViolation.class);
        violations.add(violation);
        
        assertNotNull(this.sqlCheckService.fullFillRiskLevel(rules, violations));
    }
}
