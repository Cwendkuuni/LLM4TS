package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.*;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {
    
    @InjectMocks
    MySqlCheckService sqlCheckService;
    
    @Mock
    RuleService ruleService;
    
    @Mock
    EnvironmentService environmentService;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testCheckWithConnectionSession() {
        // Create a mock session and SqlCheckReq
        ConnectionSession mockSession = mock(ConnectionSession.class);
        SqlCheckReq mockReq = mock(SqlCheckReq.class);

        // Define behavior for methods on the mocks
        when(mockSession.getSyncJdbcExecutor(anyString())).thenReturn(mock(JdbcOperations.class));
        when(mockSession.getDialectType()).thenReturn(DialectType.OCEANBASE);
        when(mockReq.getDelimiter()).thenReturn(";");
        when(mockReq.getScriptContent()).thenReturn("SELECT * FROM Users;");

        sqlCheckService.check(mockSession, mockReq);
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }
    
    @Test
    public void testCheckWithEnvironmentId() {
        // Create mocks
        Long mockEnvironmentId = 1L;
        ConnectionConfig mockConfig = new ConnectionConfig();
        List<String> mockSqls = Collections.singletonList("SELECT * FROM Users");
        
        Environment mockEnvironment = new Environment(); 
        mockEnvironment.setRulesetId(1L);
        when(environmentService.detail(mockEnvironmentId)).thenReturn(mockEnvironment);

        sqlCheckService.check(mockEnvironmentId, "testDB", mockSqls, mockConfig);
        verify(environmentService, times(1)).detail(mockEnvironmentId);
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }
    
    @Test
    public void testGetRules() {
        // Create mocks
        List<Rule> rules = new ArrayList<>();
        Rule mockRule = new Rule();
        mockRule.setEnabled(true);
        
        RuleMetadata mockMetadata = new RuleMetadata();
        mockMetadata.setType(RuleType.SQL_CHECK);
        mockRule.setMetadata(mockMetadata);
        
        rules.add(mockRule);
        DialectType mockDialectType = DialectType.OCEANBASE;

        // Test the method and verify the result
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, mockDialectType, mock(JdbcOperations.class));
        assertEquals(1, result.size());
        assertEquals(SqlCheckRules.createByRule(SqlCheckRules.getAllFactories(mockDialectType, mock(JdbcOperations.class)), mockDialectType, mockRule), 
                     result.get(0));
    }

    @Test
    public void testFullFillRiskLevel() {
        // Create mocks
        List<CheckViolation> mockViolations = mock(List.class);
        Rule mockRule = mock(Rule.class);
        RuleMetadata mockRuleMetadata = mock(RuleMetadata.class);
        when(mockRule.getMetadata()).thenReturn(mockRuleMetadata);
        
        // Test the method
        sqlCheckService.fullFillRiskLevel(Collections.singletonList(mockRule), mockViolations);
        
        // Verify interactions
        verify(mockViolations, times(1)).forEach(any());
    }
}