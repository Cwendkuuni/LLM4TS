
package com.oceanbase.odc.service.sqlcheck;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import org.springframework.jdbc.core.JdbcTemplate;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import org.springframework.jdbc.core.JdbcOperations;

public class SqlCheckServiceTest {
    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private ConnectionSession session;
    @Mock
    private SqlCheckReq req;
    @Mock
    private JdbcOperations jdbc;
    @Mock
    private JdbcTemplate jdbcTemplate;
    
    private SqlCheckService sqlCheckService;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
    }
    
    @Test
    public void testCheck() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(req.getScriptContent()).thenReturn("SELECT * FROM Dummy");
        when(req.getDelimiter()).thenReturn(";");
        when(ruleService.list(any(), any())).thenReturn(new ArrayList<>());
        
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    public void testCheckWithEnvironmentId() {
        when(environmentService.detail(any())).thenReturn(new Environment());
        when(ruleService.list(any(), any())).thenReturn(new ArrayList<>());
        
        List<CheckViolation> result = sqlCheckService.check(1l, "TestDB", new ArrayList<>(), new ConnectionConfig());
        assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    public void testGetRules() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbc);
        
        List<SqlCheckRule> result = sqlCheckService.getRules(new ArrayList<>(), session);
        assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    public void testGetRulesWithJdbc() {
        List<SqlCheckRule> result = sqlCheckService.getRules(new ArrayList<>(), DialectType.MYSQL, jdbcTemplate);
        assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    public void testFullFillRiskLevel() {
        List<Rule> result = sqlCheckService.fullFillRiskLevel(new ArrayList<Rule>(), new ArrayList<CheckViolation>());
        assertTrue(result.isEmpty());
    }
}
