package com.oceanbase.odc.service.sqlcheck;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRule;

public class SqlCheckServiceTest {

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private RuleService ruleService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private JdbcOperations jdbcOperations;

    @Mock
    private SqlCheckRule sqlCheckRule;

    @Mock
    private Rule rule;

    @Mock
    private RuleMetadata ruleMetadata;

    @Mock
    private Environment environment;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        when(rule.getMetadata()).thenReturn(ruleMetadata);
        when(ruleService.list(any(), any())).thenReturn(Arrays.asList(rule));
        when(sqlCheckService.getRules(Arrays.asList(rule), connectionSession)).thenReturn(Arrays.asList(sqlCheckRule));
        SqlCheckReq request = mock(SqlCheckReq.class);
        when(request.getScriptContent()).thenReturn(Collections.emptyList());
        List<CheckResult> result = sqlCheckService.check(connectionSession, request);
        assertThat(result.size(), is(0));
    }

    @Test
    public void testGetRules() {
        when(rule.getMetadata()).thenReturn(ruleMetadata);
        when(connectionSession.getDialectType()).thenReturn(DialectType.OB);
        when(connectionSession.getSyncJdbcExecutor(anyString())).thenReturn(jdbcOperations);
        List<SqlCheckRule> rules = sqlCheckService.getRules(Arrays.asList(rule), connectionSession);
        assertThat(rules.size(), is(0));
    }

    @Test
    public void testFullFillRiskLevel() {
        CheckViolation checkViolation = mock(CheckViolation.class);
        when(checkViolation.getType()).thenReturn(sqlCheckRule);
        when(rule.getMetadata()).thenReturn(ruleMetadata);
        when(ruleMetadata.getName()).thenReturn("$");
        List<Rule> rules = sqlCheckService.fullFillRiskLevel(Arrays.asList(rule), Arrays.asList(checkViolation));
        assertThat(rules.size(), is(0));
    }
}
