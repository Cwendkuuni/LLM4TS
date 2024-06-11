package com.oceanbase.odc.service.sqlcheck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest{

    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;

    @BeforeEach
    public void setup() {
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        sqlCheckService = new SqlCheckService(ruleService, environmentService);
    }

    @Test
    public void testCheckMethod() {
        // Arrange
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);
        List<Rule> rules = Collections.emptyList();

        when(ruleService.list(any(), any())).thenReturn(rules);

        // Act
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Assert
        assertEquals(result.size(), 0);
        
        verify(ruleService, times(1)).list(any(), any());
    }

    // All other test methods follow here
}