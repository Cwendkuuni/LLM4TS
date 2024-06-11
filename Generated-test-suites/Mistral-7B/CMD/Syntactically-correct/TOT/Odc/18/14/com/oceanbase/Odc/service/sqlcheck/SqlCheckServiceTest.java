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
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private ConnectionSession connectionSession;
    @Mock
    private SingleConnectionDataSource dataSource;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.singletonList(getRule()));
        when(environmentService.detail(anyLong())).thenReturn(getEnvironment());
        when(dataSource.getJdbcOperations()).thenReturn(jdbcTemplate);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(0, results.size());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService, never()).detail(anyLong());
        verifyNoMoreInteractions(connectionSession);
        verifyNoMoreInteractions(dataSource);
    }

    @Test
    public void testCheckWithEnvironmentAndConfig() {
        String databaseName = "test_db";
        ConnectionConfig config = new ConnectionConfig();
        Long environmentId = 1L;
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("select * from test_table"));

        Environment env = getEnvironment();
        List<Rule> rules = getRules();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true, false);
        factory.resetSchema(origin -> databaseName);
        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) factory.getDataSource();

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, config.getDialectType(), jdbcTemplate);

        List<CheckViolation> violations = new ArrayList<>();
        for (OffsetString sql : sqls) {
            violations.add(new CheckViolation("TEST", sql.getValue(), "Test Violation", RuleType.SQL_CHECK));
        }

        when(environmentService.detail(anyLong())).thenReturn(env);
        when(ruleService.list(env.getRulesetId(), QueryRuleMetadataParams.builder().build())).thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(sqls.size(), results.size());

        for (int i = 0; i < sqls.size(); i++) {
            assertEquals(violations.get(i).getMessage(), results.get(i).getViolations().get(0).getMessage());
            assertEquals(violations.get(i).getRiskLevel(), results.get(i).getViolations().get(0).getRiskLevel());
        }

        verify(environmentService, times(1)).detail(environmentId);
        verify(ruleService, times(1)).list(env.getRulesetId(), QueryRuleMetadataParams.builder().build());
        verify(connectionSession, never()).getDialectType();
        verify(dataSource, never()).getJdbcOperations();
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = getRules();
        ConnectionSession session = mock(ConnectionSession.class);
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbcOperations = jdbcTemplate;

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session, dialectType, jdbcOperations);
        assertEquals(1, sqlCheckRules.size());

        verify(session, times(1)).getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY);
        verify(jdbcOperations, times(1)).query(anyString(), anyArray(), anyMap());
        verifyNoMoreInteractions(session);
        verifyNoMoreInteractions(jdbcOperations);
    }

    private Rule getRule() {
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setEnabled(true);
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        return rule;
    }

    private Environment getEnvironment() {
        Environment env = new Environment();
        env.setRulesetId(1L);
        return env;
    }

    private List<Rule> getRules() {
        Rule rule1 = getRule();
        Rule rule2 = getRule();
        List<Rule> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        return rules;
    }
}