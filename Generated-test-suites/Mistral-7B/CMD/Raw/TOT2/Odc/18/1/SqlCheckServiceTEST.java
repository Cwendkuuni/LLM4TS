package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private ConnectionSession session;
    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;
    @Mock
    private JdbcOperations jdbcOperations;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private static ConnectionConfig connectionConfig;
    private static BasicDataSource dataSource;
    private static Map<String, Rule> rules;

    @BeforeAll
    public static void setup() {
        connectionConfig = new ConnectionConfig();
        dataSource = new BasicDataSource();
        rules = new HashMap<>();
    }

    @Test
    public void testCheckWithValidSessionAndRequest() {
        when(environmentService.detail(1L)).thenReturn(mock(Environment.class));
        when(ruleService.list(anyLong(), any())).thenReturn(Arrays.asList(new Rule()));
        when(jdbcOperations.queryForList(any(), any())).thenReturn(Collections.emptyList());

        CheckResult result = sqlCheckService.check(session, new SqlCheckReq());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(Collections.emptyList(), result.getViolations());

        verify(ruleService, times(1)).list(anyLong(), any());
        verify(environmentService, times(1)).detail(anyLong());
        verify(jdbcOperations, never()).update(any(), any());
    }

    @Test
    public void testCheckWithEmptyRules() {
        when(environmentService.detail(1L)).thenReturn(mock(Environment.class));
        when(ruleService.list(anyLong(), any())).thenReturn(Collections.emptyList());

        List<CheckViolation> violations = sqlCheckService.check(session, new SqlCheckReq()).getViolations();
        Assertions.assertTrue(violations.isEmpty());

        verify(ruleService, times(1)).list(anyLong(), any());
        verify(environmentService, times(1)).detail(anyLong());
        verify(jdbcOperations, never()).update(any(), any());
    }

    @Test
    public void testCheckWithInvalidSession() {
        when(environmentService.detail(1L)).thenThrow(new RuntimeException());

        Assertions.assertThrows(RuntimeException.class, () -> sqlCheckService.check(session, new SqlCheckReq()));

        verify(ruleService, never()).list(anyLong(), any());
        verify(environmentService, times(1)).detail(anyLong());
        verify(jdbcOperations, never()).update(any(), any());
    }

    @Test
    public void testCheckWithEmptyScriptContent() {
        when(environmentService.detail(1L)).thenReturn(mock(Environment.class));
        when(ruleService.list(anyLong(), any())).thenReturn(Arrays.asList(new Rule()));
        when(jdbcOperations.queryForList(any(), any())).thenReturn(Collections.emptyList());

        sqlCheckService.check(session, new SqlCheckReq(""));

        verify(ruleService, times(1)).list(anyLong(), any());
        verify(environmentService, times(1)).detail(anyLong());
        verify(jdbcOperations, never()).update(any(), any());
    }

    @Test
    public void testCheckWithScriptContainingViolation() {
        when(environmentService.detail(1L)).thenReturn(mock(Environment.class));
        when(ruleService.list(anyLong(), any())).thenReturn(Arrays.asList(new Rule()));
        when(jdbcOperations.queryForList(eq("SQL"), any()))
                .thenReturn(Arrays.asList(new Object[]{"test", "test_schema", "test_user"}));

        List<CheckViolation> violations = sqlCheckService.check(session, new SqlCheckReq("SELECT * FROM test.test_schema.test_user;")).getViolations();
        Assertions.assertFalse(violations.isEmpty());

        verify(ruleService, times(1)).list(anyLong(), any());
        verify(environmentService, times(1)).detail(anyLong());
        verify(jdbcOperations, times(1)).queryForList(eq("SQL"), any());
        verify(jdbcOperations, never()).update(any(), any());
    }

    @Test
    public void testCheckWithDatabaseNotExist() {
        when(environmentService.detail(1L)).thenReturn(mock(Environment.class));
        when(ruleService.list(anyLong(), any())).thenReturn(Arrays.asList(new Rule()));
        when(jdbcOperations.queryForList(any(), any()))
                .thenThrow(new RuntimeException("Database not found exception"));

        Assertions.assertThrows(RuntimeException.class, () -> sqlCheckService.check(session, new SqlCheckReq("SELECT * FROM test.test_schema.test_user;")));

        verify(ruleService, times(1)).list(anyLong(), any());
        verify(environmentService, times(1)).detail(anyLong());
        verify(jdbcOperations, times(1)).queryForList(any(), any());
        verify(jdbcOperations, never()).update(any(), any());
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        when(ruleService.list(anyLong(), any())).thenReturn(Collections.emptyList());

        List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.emptyList(), DialectType.MySQL, jdbcOperations);
        Assertions.assertTrue(rules.isEmpty());

        verify(ruleService, times(1)).list(anyLong(), any());
        verifyNoInteractions(jdbcOperations);
    }

    @Test
    public void testGetRulesWithRules() {
        Rule rule = mock(Rule.class);
        when(ruleService.list(anyLong(), any())).thenReturn(Arrays.asList(rule));

        List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.emptyList(), DialectType.MySQL, jdbcOperations);
        Assertions.assertFalse(rules.isEmpty());
        Assertions.assertEquals(1, rules.size());
        Assertions.assertEquals(rule, rules.get(0));

        verify(ruleService, times(1)).list(anyLong(), any());
        verifyNoInteractions(jdbcOperations);
    }

    @Test
    public void testGetRulesWithEmptyDataSource() {
        when(ruleService.list(anyLong(), any())).thenReturn(Collections.emptyList());

        List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.emptyList(), DialectType.MySQL, jdbcOperations);
        Assertions.assertTrue(rules.isEmpty());

        verify(ruleService, times(1)).list(anyLong(), any());
        verifyNoInteractions(jdbcOperations);
    }

    @Test
    public void testGetRulesWithRulesAndDataSource() {
        Rule rule = mock(Rule.class);
        when(ruleService.list(anyLong(), any())).thenReturn(Arrays.asList(rule));

        List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.singletonList(singleConnectionDataSource), DialectType.MySQL, jdbcOperations);
        Assertions.assertFalse(rules.isEmpty());
        Assertions.assertEquals(1, rules.size());
        Assertions.assertEquals(rule, rules.get(0));

        verify(ruleService, times(1)).list(anyLong(), any());
        verify(jdbcOperations, times(1)).queryForList(any(), any());
    }

    @Test
    public void testFullFillRiskLevelWithEmptyRules() {
        List<CheckViolation> checkViolations = new ArrayList<>();
        List<Rule> rules = Collections.emptyList();

        sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        verifyNoInteractions(rules);
        verifyNoInteractions(checkViolations);
    }

    @Test
    public void testFullFillRiskLevelWithRules() {
        Rule rule = mock(Rule.class);
        List<CheckViolation> checkViolations = Arrays.asList(mock(CheckViolation.class));
        List<Rule> rules = Collections.singletonList(rule);

        sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        verify(rule, times(1)).getLevel();
        verify(checkViolations, times(1)).setLevel(anyInt());
    }
}