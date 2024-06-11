package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.CheckResult;
import com.oceanbase.odc.service.sqlcheck.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.ModelTestUtils;
import com.oceanbase.odc.service.sqlcheck.RuleMetadata;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Service
@Validated
@SkipAuthorize("inside connect session")
class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcOperations jdbcOperations;
    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;
    @Autowired
    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Test
    void checkTest() {
        MockitoAnnotations.initMocks(this);

        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        Mockito.when(session.getRuleSetId()).thenReturn(1L);
        Mockito.when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        Mockito.when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);

        RuleMetadata ruleMetadata = ModelTestUtils.getRuleMetadata();
        List<Rule> rules = Arrays.asList(new Rule(), new Rule());
        List<SqlCheckRule> sqlCheckRules = getRules(rules, session);
        when(ruleService.list(anyLong(), any())).thenReturn(rules);

        CheckResult result = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), result.getViolations());

        verify(jdbcOperations, times(1)).query(anyString(), anyArray(), anyMap());
    }

    @Test
    void testCheckWithViolation() {
        MockitoAnnotations.initMocks(this);

        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        Mockito.when(session.getRuleSetId()).thenReturn(1L);
        Mockito.when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        Mockito.when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);

        RuleMetadata ruleMetadata = ModelTestUtils.getRuleMetadata();
        List<Rule> rules = Arrays.asList(new Rule(), new Rule());
        Rule rule = new Rule();
        rule.setMetadata(ruleMetadata);
        rule.setEnabled(Boolean.TRUE);
        rules.add(rule);
        String ruleName = ruleMetadata.getName();

        List<SqlCheckRule> sqlCheckRules = getRules(rules, session);
        when(ruleService.list(anyLong(), any())).thenReturn(rules);

        SqlCheckRule sqlCheckRule = Mockito.mock(SqlCheckRule.class);
        Mockito.when(sqlCheckRule.check(anyString())).thenReturn(new CheckViolation("testViolation", 1, 1, RuleType.SQL_INJECTION, "test"));
        when(sqlCheckRules.get(0)).thenReturn(sqlCheckRule);

        CheckResult result = sqlCheckService.check(session, req);
        List<CheckViolation> violations = result.getViolations();
        assertEquals(1, violations.size());
        assertEquals("testViolation", violations.get(0).getName());
        assertEquals(1, violations.get(0).getLineNumber());
        assertEquals(1, violations.get(0).getColNumber());
        assertEquals(RuleType.SQL_INJECTION, violations.get(0).getType());

        verify(jdbcOperations, times(1)).query(anyString(), anyArray(), anyMap());
    }

    @Test
    void testCheckWithEnvironment() {
        MockitoAnnotations.initMocks(this);

        Environment environment = new Environment();
        Mockito.when(environmentService.detail(anyLong())).thenReturn(environment);
        Long environmentId = 1L;
        String databaseName = "testDatabase";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("testSql1"), new OffsetString("testSql2"));
        ConnectionConfig config = new ConnectionConfig();

        SqlCheckContext checkContext = new SqlCheckContext(sqls.size());

        List<Rule> rules = Arrays.asList(new Rule(), new Rule());
        RuleMetadata ruleMetadata = ModelTestUtils.getRuleMetadata();
        Rule rule = new Rule();
        rule.setMetadata(ruleMetadata);
        rule.setEnabled(Boolean.TRUE);
        rules.add(rule);

        List<SqlCheckRule> sqlCheckRules = getRules(rules, DialectType.MYSQL, jdbcOperations);
        Mockito.when(ruleService.list(environment.getRulesetId(), any())).thenReturn(rules);

        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, sqlCheckRules);
        List<CheckViolation> checkViolations = new ArrayList<>();

        for (OffsetString sql : sqls) {
            List<CheckViolation> violations = sqlChecker.check(Collections.singletonList(sql), checkContext);
            checkViolations.addAll(violations);
        }

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);
        assertEquals(2, violatedRules.size());
        assertEquals(ruleMetadata.getName(), violatedRules.get(0).getMetadata().getName());
        assertEquals(ruleMetadata.getLevel(), violatedRules.get(0).getLevel());

        verify(jdbcOperations, never()).query(anyString(), anyArray(), anyMap());
    }

    private List<SqlCheckRule> getRules(List<Rule> rules, ConnectionSession session) {
        return getRules(rules, session.getDialectType(), session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY));
    }

    private List<SqlCheckRule> getRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbc) {
        return sqlCheckRules.getAllFactories(dialectType, jdbc)
                .stream()
                .filter(factory -> {
                    RuleMetadata metadata = rules.get(0).getMetadata();
                    return Objects.equals(metadata.getType(), RuleType.SQL_CHECK)
                            && Boolean.TRUE.equals(metadata.isEnabled())
                            && factory.supports(dialectType);
                })
                .map(factory -> {
                    try {
                        return sqlCheckRules.createByRule(factory, dialectType, rules.get(0));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}