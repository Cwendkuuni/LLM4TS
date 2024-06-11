package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckContext;

@Slf4j
@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcOperations jdbcTemplate;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenCallRealMethod();
    }

    @Test
    public void testCheckWithValidInput() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata().setLevel(1).setName("Rule1"));
        rule1.setViolation(null);
        rules.add(rule1);

        Rule rule2 = new Rule();
        rule2.setMetadata(new RuleMetadata().setLevel(2).setName("Rule2"));
        rule2.setViolation(null);
        rules.add(rule2);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRule rule1Checker = mock(SqlCheckRule.class);
        sqlCheckRules.add(rule1Checker);

        when(SqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class))).thenReturn(sqlCheckRules);

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(Mockito.mock(RuleType.class));
        violation1.setMessage("Mock violation 1");
        violation1.setLevel(1);
        checkViolations.add(violation1);

        List<CheckResult> expectedResult = Collections.singletonList(new CheckResult().setCheckViolations(checkViolations));

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertEquals(expectedResult, result);
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class), any(SqlCheckContext.class));
        verify(environmentService, never()).detail(anyLong());
        verify(sqlCheckRules, times(1)).check(req.getScriptContent());
        verify(jdbcTemplate, never()).query(anyString(), anyArray(), any(ResultSetExtractor.class));
    }

    @Test
    public void testCheckWithEmptyRuleSet() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertTrue(result.isEmpty());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService, never()).detail(anyLong());
        verify(sqlCheckRules, never()).check(anyString());
        verify(jdbcTemplate, never()).query(anyString(), anyArray(), any(ResultSetExtractor.class));
    }

    @Test
    public void testCheckWithEnvironment() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql = new OffsetString();
        sql.setValue("SELECT 1");
        sqls.add(sql);

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata().setLevel(1).setName("Rule1"));
        rule1.setViolation(null);
        rules.add(rule1);

        Rule rule2 = new Rule();
        rule2.setMetadata(new RuleMetadata().setLevel(2).setName("Rule2"));
        rule2.setViolation(null);
        rules.add(rule2);

        Environment env = new Environment();
        env.setRulesetId(1L);

        when(environmentService.detail(anyLong())).thenReturn(env);
        when(ruleService.list(env.getRulesetId(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        when(factory.getDataSource()).thenReturn(dataSource);

        JdbcTemplate jdbc = new JdbcTemplate(dataSource);

        List<SqlCheckRule> checkRules = new ArrayList<>();
        SqlCheckRule rule1Checker = mock(SqlCheckRule.class);
        checkRules.add(rule1Checker);

        when(SqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class))).thenReturn(checkRules);

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(Mockito.mock(RuleType.class));
        violation1.setMessage("Mock violation 1");
        violation1.setLevel(1);
        checkViolations.add(violation1);

        when(SqlCheckRules.createByRule(anyList(), any(DialectType.class), any(Rule.class))).thenReturn(rule1Checker);

        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(config.getDialectType(), null, checkRules);

        List<CheckResult> expectedResult = Collections.singletonList(new CheckResult().setCheckViolations(checkViolations));

        List<CheckResult> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertEquals(expectedResult, result);
        verify(ruleService, times(1)).list(env.getRulesetId(), any(QueryRuleMetadataParams.class));
        verify(environmentService, times(1)).detail(environmentId);
        verify(factory, times(1)).getDataSource();
        verify(dataSource, times(1)).getConnection();
        verify(jdbc, times(sqls.size())).query(anyString(), anyArray(), any(ResultSetExtractor.class));
        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class), any(SqlCheckContext.class));
        verify(sqlChecker, times(sqls.size())).check(anyList(), any(SqlCheckContext.class));
    }

    @Test
    public void testCheckWithError() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata().setLevel(1).setName("Rule1"));
        rule1.setViolation(null);
        rules.add(rule1);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenThrow(new RuntimeException());

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertTrue(result.isEmpty());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService, never()).detail(anyLong());
        verify(sqlCheckRules, never()).check(anyString());
        verify(jdbcTemplate, never()).query(anyString(), anyArray(), any(ResultSetExtractor.class));
    }
}