package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("rule1");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        rule1.setLevel(1);
        RuleViolation violation1 = new RuleViolation();
        violation1.setMessage("violation1");
        CheckViolation checkViolation1 = new CheckViolation();
        checkViolation1.setType(new RuleType("rule1"));
        checkViolation1.setViolation(violation1);
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(checkViolation1);
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getRuleSetId()).thenReturn(1L);
        when(session.getSyncJdbcExecutor(eq(ConnectionSessionConstants.CONSOLE_DS_KEY))).thenReturn((JdbcOperations) mock(JdbcTemplate.class));

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertEquals(1, sqlCheckRules.size());

        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(1, checkResults.size());
        assertEquals(violation1.getMessage(), checkResults.get(0).getViolations().get(0).getMessage());
        assertEquals(1, checkResults.get(0).getViolations().size());
        verify(ruleService, times(1)).list(anyLong(), eq(QueryRuleMetadataParams.builder().build()));
        verify(environmentService, times(1)).detail(anyLong());
        verify(session, times(1)).getDialectType();
        verify(session, times(1)).getRuleSetId();
        verify(session, times(1)).getSyncJdbcExecutor(eq(ConnectionSessionConstants.CONSOLE_DS_KEY));
    }

    @Test
    public void testCheckWithEnvironment() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql1 = new OffsetString();
        sql1.setScriptContent("select * from test where 1=1");
        sqls.add(sql1);
        Environment environment = new Environment();
        environment.setRulesetId(1L);
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(ruleService.list(eq(environment.getRulesetId()), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(Collections.emptyList());
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        when(jdbc.queryForObject(anyString(), anyArray())).thenReturn(null);
        when(dataSource.getJdbcTemplate()).thenReturn(jdbc);
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("rule1");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        rule1.setLevel(1);
        RuleViolation violation1 = new RuleViolation();
        violation1.setMessage("violation1");
        CheckViolation checkViolation1 = new CheckViolation();
        checkViolation1.setType(new RuleType("rule1"));
        checkViolation1.setViolation(violation1);
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(checkViolation1);
        when(ruleService.list(eq(environment.getRulesetId()), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> checkRules = sqlCheckService.getRules(rules, config.getDialectType(), jdbc);
        assertEquals(0, checkRules.size());

        List<CheckViolation> checkViolations = sqlCheckService.check(environment.getId(), "test", sqls, config);
        assertEquals(0, checkViolations.size());

        verify(environmentService, times(1)).detail(anyLong());
        verify(ruleService, times(2)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(dataSource, times(1)).getJdbcTemplate();
        verify(jdbc, times(sqls.size())).queryForObject(anyString(), anyArray());
    }
}