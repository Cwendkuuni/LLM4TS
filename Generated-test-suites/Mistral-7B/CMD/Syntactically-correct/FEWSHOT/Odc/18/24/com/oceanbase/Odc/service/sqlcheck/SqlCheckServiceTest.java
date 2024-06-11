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
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

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
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private OBConsoleDataSourceFactory dataSourceFactory;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SingleConnectionDataSource dataSource;

    @Mock
    private ConnectionSession session;

    @Mock
    private ConnectionConfig config;

    @Component
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(dataSourceFactory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);
    }

    @Test
    public void testCheck_emptyRules_returnEmptyResult() {
        assertEquals(Collections.emptyList(), sqlCheckService.check(session, new SqlCheckReq()));
    }

    @Test
    public void testCheck_noMatchingRules_returnEmptyResult() {
        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        assertEquals(Collections.emptyList(), sqlCheckService.check(session, new SqlCheckReq()));
    }

    @Test
    public void testCheck_matchingRules_returnCheckResults() {
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata("testRule", RuleType.SQL_CHECK, null, null, null, null, null, null));
        sqlCheckRules.add(SqlCheckRules.createByRule(sqlCheckRules, DialectType.MYSQL, rule));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.singletonList(rule));
        when(SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcTemplate)).thenReturn(sqlCheckRules);

        CheckViolation violation = new CheckViolation("testViolation", RuleType.SQL_CHECK, null);
        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(violation);
        SqlChecker sqlChecker = mock(SqlChecker.class);
        when(sqlChecker.check(any(), any())).thenReturn(checkViolations);

        List<CheckResult> checkResults = sqlCheckService.check(session, new SqlCheckReq());
        assertEquals(1, checkResults.size());
        assertEquals(violation.getType().getName(), checkResults.get(0).getViolation().getType().getName());
    }

    @Test
    public void testCheckWithEnvironment() {
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata("testRule", RuleType.SQL_CHECK, null, null, null, null, null, null));
        sqlCheckRules.add(SqlCheckRules.createByRule(sqlCheckRules, DialectType.MYSQL, rule));

        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.singletonList(rule));
        when(SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcTemplate)).thenReturn(sqlCheckRules);

        String databaseName = "testDatabase";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("testSql"));
        ConnectionConfig config = new ConnectionConfig();
        List<CheckViolation> checkViolations = new ArrayList<>();

        SqlCheckContext checkContext = new SqlCheckContext(sqls.size());
        JdbcOperations jdbc = mock(JdbcOperations.class);
        when(jdbcTemplate.getJdbcOperations()).thenReturn(jdbc);
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        when(dataSourceFactory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);

        SqlChecker sqlChecker = mock(SqlChecker.class);
        when(sqlChecker.check(any(), any())).thenReturn(checkViolations);

        SqlCheckService sqlCheckService = new SqlCheckService() {{
            this.ruleService = ruleService;
            this.environmentService = environmentService;
            this.dataSourceFactory = dataSourceFactory;
        }};

        List<CheckResult> checkResults = sqlCheckService.check(1L, databaseName, sqls, config);
        assertEquals(1, checkResults.size());
    }
}