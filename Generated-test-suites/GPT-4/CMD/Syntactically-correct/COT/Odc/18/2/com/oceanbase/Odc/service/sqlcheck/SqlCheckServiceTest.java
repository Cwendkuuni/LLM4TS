
package com.oceanbase.odc.service.sqlcheck;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckMethodWithConnectionSessionAndSqlCheckReq() {
        ConnectionSession cs = new ConnectionSession();
        SqlCheckReq request = new SqlCheckReq();
        when(this.ruleService.list(any(), any())).thenReturn(Arrays.asList(new Rule()));
        List<CheckResult> results = sqlCheckService.check(cs, request);
        assertTrue(!results.isEmpty());
    }

    @Test
    public void testCheckMethodWithEnvironmentIdDatabaseNameSqlsAndConnectionConfig() {
        List<OffsetString> offsetStrings = Arrays.asList(new OffsetString());
        ConnectionConfig cc = new ConnectionConfig();
        Environment environment = new Environment();
        when(this.environmentService.detail(any())).thenReturn(environment);
        when(this.ruleService.list(any(), any())).thenReturn(Arrays.asList(new Rule()));
        List<CheckResult> results = sqlCheckService.check(1L, "database", offsetStrings, cc);
        assertTrue(!results.isEmpty());
    }

    @Test
    public void testGetRulesMethodWithRulesAndConnectionSession() {
        ConnectionSession cs = new ConnectionSession();
        when(this.ruleService.list(any(), any())).thenReturn(Arrays.asList(new Rule()));
        List<SqlCheckRule> rules = sqlCheckService.getRules(Arrays.asList(new Rule()), cs);
        assertTrue(!rules.isEmpty());
    }

    @Test
    public void testGetRulesMethodWithRulesDialectTypeAndJdbcOperations() {
        JdbcOperations jdbc = Mockito.mock(JdbcOperations.class);
        when(this.ruleService.list(any(), any())).thenReturn(Arrays.asList(new Rule()));
        List<SqlCheckRule> rules = sqlCheckService.getRules(Arrays.asList(new Rule()), DialectType.ORACLE, jdbc);
        assertTrue(!rules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = Arrays.asList(new Rule());
        List<CheckViolation> violations = Arrays.asList(new CheckViolation());
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(rules, result);
    }
}
