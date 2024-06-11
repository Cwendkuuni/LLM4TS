package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;
    
    @Mock
    private ConnectionSession connectionSession;
    
    @Mock
    private ConnectionConfig connectionConfig;
    
    @Mock
    private JdbcOperations jdbcOperations;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckWithConnectionSession() {
        // Arrange
        SqlCheckReq sq = new SqlCheckReq();
        List<Rule> rules = new ArrayList<>();
        
        // Assume
        when(ruleService.list(any(),any())).thenReturn(rules);
        
        // Act
        List<CheckResult> result = sqlCheckService.check(connectionSession, sq);
        
        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithEnvironmentID() {
        // Arrange
        Long environmentId = 1234L;
        String databaseName = "TestDB";
        List<OffsetString> sqls = new ArrayList<>();
        
        Environment environment = new Environment();
        environment.setId(environmentId);
        environment.setRulesetId(4321L);
        
        // Assume
        when(environmentService.detail(environmentId)).thenReturn(environment);
        
        // Act
        List<CheckViolation> result = sqlCheckService.check(environmentId,databaseName,sqls,connectionConfig);
        
        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testGetRules() {
        // Arrange
        List<Rule> rules = new ArrayList<>();
        
        // Assume
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);
        
        // Act
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, connectionSession);
        
        // Assert
        assertEquals(0, result.size());
    }
}