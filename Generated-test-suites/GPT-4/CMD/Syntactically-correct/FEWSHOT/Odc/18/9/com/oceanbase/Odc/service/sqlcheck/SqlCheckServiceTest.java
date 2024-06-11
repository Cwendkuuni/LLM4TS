package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.core.sql.split.OffsetString;


public class SqlCheckServiceTest {
    
    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;
    
    @Mock
    private EnvironmentService environmentService;
    
    @Mock
    private ConnectionSession session;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckConnectionSession() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("CREATE TABLE Test (ID INT, Name VARCHAR(20))");

        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any())).thenReturn(new ArrayList<>());

        List<CheckResult> result = sqlCheckService.check(session, req);
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCheckLitSqlsConfig() {
        ConnectionConfig connectionConfig=new ConnectionConfig();
        Environment environment=new Environment();
        environment.setRulesetId(1L);
        OffsetString offsetString=new OffsetString(1L, "Check Lit sqls and connection");
        List<OffsetString> sqls=new ArrayList<>();
        sqls.add(offsetString);
        Mockito.when(environmentService.detail(Mockito.anyLong())).thenReturn(environment);
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any())).thenReturn(new ArrayList<>());
        List<CheckViolation> result = sqlCheckService.check(1L, "testDb",sqls, connectionConfig);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testGetRulesListRuleConnectionSession() {
        Rule rule = new Rule();
        
        Mockito.when(session.getSyncJdbcExecutor(Mockito.anyString())).thenReturn(null);
        Mockito.when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        List<SqlCheckRule> result = sqlCheckService.getRules(Arrays.asList(rule), session);
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testFullFillRiskLevel() {
        CheckViolation checkViolation = new CheckViolation();
        Rule rule = new Rule();
        
        List<Rule> result = sqlCheckService.fullFillRiskLevel(Arrays.asList(rule), Arrays.asList(checkViolation));

        assertTrue(result.isEmpty());
    }
}