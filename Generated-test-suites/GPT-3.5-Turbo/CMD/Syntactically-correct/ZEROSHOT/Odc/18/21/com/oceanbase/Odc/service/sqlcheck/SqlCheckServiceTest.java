package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Before
    public void setUp() {
        ruleService = mock(RuleService.class);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
    }

    @Test
    public void testCheckMethodWithValidRequest() {
        // Arrange
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM TABLE_NAME");
        when(ruleService.list(anyLong(), any())).thenReturn(Collections.singletonList(new Rule()));

        // Act
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Assert
        assertEquals(1, checkResults.size());
        verify(ruleService, times(1)).list(anyLong(), any());
    }

    @Test
    public void testCheckMethodWithEmptyRequest() {
        // Arrange
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        when(ruleService.list(anyLong(), any())).thenReturn(Collections.emptyList());

        // Act
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        // Assert
        assertEquals(0, checkResults.size());
        verify(ruleService, times(1)).list(anyLong(), any());
    }

    @Test
    public void testGetRulesMethodWithValidInput() {
        // Arrange
        List<Rule> rules = Collections.singletonList(new Rule());

        // Act
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules);

        // Assert
        assertEquals(1, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesMethodWithEmptyInput() {
        // Arrange
        List<Rule> rules = Collections.emptyList();

        // Act
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules);

        // Assert
        assertEquals(0, sqlCheckRules.size());
    }

    // Add more test cases to cover all methods in SqlCheckService

}