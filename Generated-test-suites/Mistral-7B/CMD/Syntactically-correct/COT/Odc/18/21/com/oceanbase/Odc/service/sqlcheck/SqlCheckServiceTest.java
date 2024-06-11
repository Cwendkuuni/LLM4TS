package com.oceanbase.odc.service.sqlcheck.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.ModelClassUtil;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

@RunWith(MockitoJUnitRunner.class)
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
    private JdbcTemplate jdbcTemplate;

    private SingleConnectionDataSource dataSource;

    @Before
    public void setup() {
        dataSource = mock(SingleConnectionDataSource.class);
        when(jdbcTemplate.getDataSource()).thenReturn(dataSource);
    }

    @Test
    public void testCheckMethodWithValidInput() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(connectionSession.getId()).thenReturn(1L);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        SqlCheckReq req = new SqlCheckReq();
        List<Rule> rules = Arrays.asList(getRuleMock(), getRuleMock());
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);

        List<CheckResult> expectedCheckResults = Arrays.asList(
                new CheckResult(1, "checkViolation1", "violationMessage1", "ruleName1", 1),
                new CheckResult(2, "checkViolation2", "violationMessage2", "ruleName2", 1)
        );

        List<CheckViolation> checkViolations = Arrays.asList(
                new CheckViolation(1, "checkViolation1", RuleType.SQL_CHECK, "violationMessage1"),
                new CheckViolation(2, "checkViolation2", RuleType.SQL_CHECK, "violationMessage2")
        );

        List<SqlCheckRule> sqlCheckRules = Arrays.asList(getSqlCheckRuleMock(), getSqlCheckRuleMock());

        when(sqlCheckService.getRules(anyList(), eq(DialectType.MYSQL), eq(jdbcTemplate))).thenReturn(sqlCheckRules);
        when(sqlCheckService.fullFillRiskLevel(anyList(), anyList())).thenReturn(rules);

        List<CheckResult> actualCheckResults = sqlCheckService.check(session, req);

        assertEquals(expectedCheckResults, actualCheckResults);
    }

    @Test
    public void testCheckMethodWithEmptyRuleList() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(connectionSession.getId()).thenReturn(1L);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        SqlCheckReq req = new SqlCheckReq();

        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(Collections.emptyList());

        List<CheckResult> expectedCheckResults = Collections.emptyList();

        List<CheckResult> actualCheckResults = sqlCheckService.check(session, req);

        assertEquals(expectedCheckResults, actualCheckResults);
    }

    @Test
    public void testCheckMethodWithEmptySqlCheckRuleList() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(connectionSession.getId()).thenReturn(1L);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        SqlCheckReq req = new SqlCheckReq();

        List<Rule> rules = Arrays.asList(getRuleMock(), getRuleMock());
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);

        when(sqlCheckService.getRules(anyList(), eq(DialectType.MYSQL), eq(jdbcTemplate))).thenReturn(Collections.emptyList());

        List<CheckResult> expectedCheckResults = Collections.emptyList();

        List<CheckResult> actualCheckResults = sqlCheckService.check(session, req);

        assertEquals(expectedCheckResults, actualCheckResults);
    }

    @Test
    public void testCheckMethodWithEmptyInputScriptContent() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(connectionSession.getId()).thenReturn(1L);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(StringUtils.EMPTY);

        List<Rule> rules = Arrays.asList(getRuleMock(), getRuleMock());
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = Arrays.asList(getSqlCheckRuleMock(), getSqlCheckRuleMock());

        when(sqlCheckService.getRules(anyList(), eq(DialectType.MYSQL), eq(jdbcTemplate))).thenReturn(sqlCheckRules);

        List<CheckViolation> expectedCheckViolations = Collections.emptyList();
        List<CheckResult> expectedCheckResults = SqlCheckUtil.buildCheckResults(expectedCheckViolations);

        List<CheckResult> actualCheckResults = sqlCheckService.check(session, req);

        assertEquals(expectedCheckResults, actualCheckResults);
    }

    @Test
    public void testCheckMethodWithEnvironmentIdInput() {
        Long environmentId = 1L;
        String databaseName = "testDatabase";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM testTable;"));
        ConnectionConfig config = new ConnectionConfig();

        Environment env = mock(Environment.class);
        when(environmentService.detail(environmentId)).thenReturn(env);
        when(env.getRulesetId()).thenReturn(1L);

        List<Rule> rules = Arrays.asList(getRuleMock(), getRuleMock());
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);

        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        when(factory.getDataSource()).thenReturn(dataSource);

        SqlCheckContext checkContext = mock(SqlCheckContext.class);

        when(sqlCheckService.getRules(anyList(), any(DialectType.class), any(JdbcOperations.class))).thenReturn(Collections.emptyList());

        List<CheckViolation> expectedCheckViolations = Collections.emptyList();
        List<CheckResult> expectedCheckResults = SqlCheckUtil.buildCheckResults(expectedCheckViolations);

        List<CheckResult> actualCheckResults = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertEquals(expectedCheckResults, actualCheckResults);
    }

    private Rule getRuleMock() {
        Rule rule = mock(Rule.class);
        RuleMetadata metadata = mock(RuleMetadata.class);
        when(metadata.getName()).thenReturn("ruleName");
        when(metadata.getType()).thenReturn(RuleType.SQL_CHECK);
        when(rule.getMetadata()).thenReturn(metadata);
        return rule;
    }

    private SqlCheckRule getSqlCheckRuleMock() {
        return mock(SqlCheckRule.class);
    }
}