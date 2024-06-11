package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcOperations jdbcOperations;
    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession mockSession;
    private ConnectionConfig mockConfig;
    private Environment mockEnvironment;
    private List<Rule> mockRules;
    private List<SqlCheckRuleFactory> mockRuleFactories;
    private List<SqlCheckRule> mockCheckRules;

    @Before
    public void setup() {
        mockSession = mock(ConnectionSession.class);
        mockConfig = mock(ConnectionConfig.class);
        mockEnvironment = mock(Environment.class);
        mockRules = Collections.singletonList(mockRule());
        mockRuleFactories = SqlCheckRules.getAllFactories(DialectType.MySQL, jdbcOperations);

        when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class))).thenReturn(mockRules);
        when(environmentService.detail(any(Long.class))).thenReturn(mockEnvironment);
        when(jdbcOperations.queryForObject(any(String.class), any(Object[].class), any(Class.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

        SqlCheckRuleFactory mockFactory = mock(SqlCheckRuleFactory.class);
        when(mockFactory.createCheckRule(any(Rule.class), any(JdbcOperations.class)))
                .thenAnswer(invocation -> mockCheckRule());
        when(SqlCheckRules.getAllFactories(DialectType.MySQL, jdbcOperations)).thenReturn(new ArrayList<SqlCheckRuleFactory>() {{
            add(mockFactory);
        }});
    }

    @Test
    public void testCheck() {
        SqlCheckReq req = SqlCheckReq.builder().build();
        when(mockSession.getDialectType()).thenReturn(DialectType.MySQL);
        CheckResult result = sqlCheckService.check(mockSession, req);
        assertEquals(Collections.emptyList(), result.getCheckViolations());
        verifyCheckRulesInvocation(req.getScriptContent(), mockCheckRules);
    }

    @Test
    public void testCheckWithEnvironment() {
        List<OffsetString> sqls = Collections.singletonList(OffsetString.of("SELECT * FROM test;"));
        String databaseName = "test";
        when(mockEnvironment.getRulesetId()).thenReturn(1L);
        ConnectionConfig config = ConnectionConfig.builder().build();
        CheckResult result = sqlCheckService.check(1L, databaseName, sqls, config);
        assertEquals(Collections.emptyList(), result.getCheckViolations());
        verifyCheckRulesInvocation(sqls.get(0).getScriptContent(), mockCheckRules);
    }

    private void verifyCheckRulesInvocation(String scriptContent, List<SqlCheckRule> checkRules) {
        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MySQL, null, checkRules);
        sqlChecker.check(Collections.singletonList(OffsetString.of(scriptContent)), new SqlCheckContext(0));
        for (SqlCheckRule rule : checkRules) {
            assertEquals(1, rule.getCheckCallCount());
        }
    }

    private Rule mockRule() {
        Rule rule = Rule.builder().metadata(RuleMetadata.builder().name("TestRule")
                .type(RuleType.SQL_CHECK).build()).enabled(true).build();
        return rule;
    }

    private SqlCheckRule mockCheckRule() {
        return SqlCheckRule.builder().checkRuleFactory(mockRuleFactories.get(0)).build();
    }
}