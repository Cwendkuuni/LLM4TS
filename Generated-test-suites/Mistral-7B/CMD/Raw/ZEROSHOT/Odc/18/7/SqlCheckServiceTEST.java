package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.Model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.Model.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;

/**
 * Test for {@link SqlCheckService}
 *
 * @author yh263208
 * @date 2023-03-20 11:01
 */
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private OBConsoleDataSourceFactory factory;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        Long ruleSetId = 1L;
        List<Rule> rules = Arrays.asList(createRule("rule1"), createRule("rule2"));
        when(ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        assertEquals(2, sqlCheckRules.size());

        String scriptContent = "select * from table1 where id = 1";
        List<CheckViolation> checkViolations = Arrays.asList(createCheckViolation("rule1"), createCheckViolation("rule2"));
        when(jdbcTemplate.queryForList(eq("sql"), anyList())).thenReturn(checkViolations);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, new SqlCheckReq());
        assertEquals(2, checkResults.size());
        for (CheckResult checkResult : checkResults) {
            assertTrue(checkResult.getViolations().size() > 0);
        }

        verify(ruleService).list(ruleSetId, QueryRuleMetadataParams.builder().build());
        verify(jdbcTemplate, times(2)).queryForList(eq("sql"), anyList());
    }

    @Test
    public void testCheckWithEnvironment() {
        Long ruleSetId = 1L;
        Long environmentId = 1L;
        String databaseName = "test_database";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("select * from table1 where id = 1"),
                new OffsetString("select * from table2 where id = 2"));
        List<Rule> rules = Arrays.asList(createRule("rule1"), createRule("rule2"));
        Environment environment = new Environment();
        environment.setRulesetId(ruleSetId);

        when(environmentService.detail(environmentId)).thenReturn(environment);
        when(ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        when(factory.getDataSource()).thenReturn(singleConnectionDataSource);
        when(singleConnectionDataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);

        List<CheckViolation> checkViolations = Arrays.asList(createCheckViolation("rule1"), createCheckViolation("rule2"));
        when(jdbcTemplate.queryForList(eq("sql"), anyList())).thenReturn(checkViolations);

        List<CheckViolation> results = sqlCheckService.check(environmentId, databaseName, sqls, new ConnectionConfig());
        assertEquals(2, results.size());
        for (CheckViolation checkViolation : results) {
            assertTrue(checkViolation.getViolations().size() > 0);
        }

        verify(environmentService).detail(environmentId);
        verify(ruleService).list(ruleSetId, QueryRuleMetadataParams.builder().build());
        verify(factory).getDataSource();
        verify(singleConnectionDataSource).getJdbcTemplate();
        verify(jdbcTemplate, times(2)).queryForList(eq("sql"), anyList());
    }

    private Rule createRule(String name) {
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName(name);
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        return rule;
    }

    private CheckViolation createCheckViolation(String ruleName) {
        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setType(RuleType.fromString(ruleName));
        return checkViolation;
    }
}