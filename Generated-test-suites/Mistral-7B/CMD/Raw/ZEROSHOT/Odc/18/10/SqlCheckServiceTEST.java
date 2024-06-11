package com.oceanbase.odc.service.sqlcheck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
import com.oceanbase.odc.service.regulation.ruleset.model.CheckResult;
import com.oceanbase.odc.service.regulation.ruleset.model.CheckViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckUtil;

@Slf4j
public class SqlCheckServiceTest {

    @Test
    public void testCheckMethod() {
        RuleService ruleService = mock(RuleService.class);
        EnvironmentService environmentService = mock(EnvironmentService.class);
        ConnectionSession session = mock(ConnectionSession.class);
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);

        // Set up mock objects
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(new JdbcTemplate(dataSource));
        when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(getRules());
        when(environmentService.detail(Mockito.anyLong())).thenReturn(getEnvironment());

        SqlCheckService sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM emp;");

        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), checkResults);

        // Test with given violations
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation(" violation1", RuleType.SQL_CHECK, "level1");
        violation1.setType("violationType1");
        CheckViolation violation2 = new CheckViolation(" violation2", RuleType.SQL_CHECK, "level2");
        violation2.setType("violationType2");
        violations.add(violation1);
        violations.add(violation2);

        when(dataSource.query(Mockito.anyString(), Mockito.anyArray(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyObject()))
                .thenReturn(violations);

        List<CheckResult> checkResultsWithViolations = sqlCheckService.check(session, req);
        assertEquals(2, checkResultsWithViolations.size());
        assertEquals("level1", checkResultsWithViolations.get(0).getCheckViolations().get(0).getLevel());
        assertEquals("level2", checkResultsWithViolations.get(1).getCheckViolations().get(0).getLevel());
    }

    @Test
    public void testCheckMethodWithEnvironment() {
        RuleService ruleService = mock(RuleService.class);
        EnvironmentService environmentService = mock(EnvironmentService.class);
        ConnectionConfig config = new ConnectionConfig();

        // Set up mock objects
        when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(getRules());

        SqlCheckService sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);

        Environment env = new Environment();
        env.setRulesetId(1L);

        when(environmentService.detail(Mockito.anyLong())).thenReturn(env);

        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM emp;"));

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation(" violation1", RuleType.SQL_CHECK, "level1");
        violation1.setType("violationType1");
        CheckViolation violation2 = new CheckViolation(" violation2", RuleType.SQL_CHECK, "level2");
        violation2.setType("violationType2");
        checkViolations.add(violation1);
        checkViolations.add(violation2);

        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        when(factory.getDataSource()).thenReturn(new SingleConnectionDataSource());

        SqlCheckContext checkContext = new SqlCheckContext((long) sqls.size());
        JdbcOperations jdbc = mock(JdbcOperations.class);

        when(factory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getJdbcTemplate()).thenReturn(new JdbcTemplate(jdbc));

        List<SqlCheckRule> checkRules = getRules(getRules(), DialectType.MYSQL, jdbc);
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, checkRules);

        when(jdbc.query(Mockito.anyString(), Mockito.anyArray(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyObject()))
                .thenReturn(checkViolations);

        List<CheckResult> checkResults = sqlCheckService.check(env.getId(), "test_db", sqls, config);
        assertEquals(2, checkResults.size());
        assertEquals("level1", checkResults.get(0).getCheckViolations().get(0).getLevel());
        assertEquals("level2", checkResults.get(1).getCheckViolations().get(0).getLevel());
    }

    private List<Rule> getRules() {
        Rule rule1 = new Rule();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("ruleName1");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        rule1.setEnabled(true);

        Rule rule2 = new Rule();
        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("ruleName2");
        metadata2.setType(RuleType.SQL_CHECK);
        rule2.setMetadata(metadata2);
        rule2.setEnabled(true);

        return new ArrayList<Rule>() {{
            add(rule1);
            add(rule2);
        }};
    }

    private Environment getEnvironment() {
        Environment env = new Environment();
        env.setId(1L);
        env.setRulesetId(1L);
        return env;
    }

    private List<SqlCheckRule> getRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbc) {
        return getRules(rules, dialectType).stream()
                .map(rule -> getSqlCheckRuleFactory(rule, dialectType, jdbc))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Rule> getRules(List<Rule> rules) {
        return rules;
    }

    private SqlCheckRuleFactory getSqlCheckRuleFactory(Rule rule, DialectType dialectType, JdbcOperations jdbc) {
        List<SqlCheckRuleFactory> candidates = Collections.singletonList(SqlCheckRules.getFactoryFor(dialectType));
        return SqlCheckRules.createByRule(candidates, dialectType, rule);
    }
}