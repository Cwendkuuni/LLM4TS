package com.oceanbase.odc.service.sqlcheck.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
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
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckUtil;

@Slf4j
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcOperations jdbcOperations;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SqlChecker sqlChecker;

    @Mock
    private SqlCheckContext sqlCheckContext;

    @Mock
    private Rule rule;

    @Mock
    private RuleMetadata ruleMetadata;

    @Mock
    private CheckResult checkResult;

    @Mock
    private CheckViolation checkViolation;

    @Mock
    private OBConsoleDataSourceFactory factory;

    @Mock
    private Environment env;

    @Mock
    private ConnectionConfig connectionConfig;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(rule.getMetadata()).thenReturn(ruleMetadata);
        when(ruleMetadata.getName()).thenReturn("ruleName");
        when(ruleMetadata.getType()).thenReturn(RuleType.SQL_CHECK);
        when(rule.getEnabled()).thenReturn(true);
        when(ruleMetadata.getLevel()).thenReturn(1);
        when(jdbcOperations.queryForList(anyString(), any(), any())).thenReturn(Collections.emptyList());
        when(jdbcOperations.queryForObject(anyString(), any(), any())).thenReturn(null);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);
        when(singleConnectionDataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);
        when(ruleService.list(anyLong(), any())).thenReturn(Arrays.asList(rule));
        when(environmentService.detail(anyLong())).thenReturn(env);
        when(env.getRulesetId()).thenReturn(1L);
        when(factory.getDataSource()).thenReturn(singleConnectionDataSource);
        when(sqlChecker.check(anyList(), any())).thenReturn(Collections.emptyList());
        when(sqlCheckUtil.buildCheckResults(anyList())).thenReturn(Arrays.asList(checkResult));
        when(ruleService.list(anyLong(), any())).thenReturn(Collections.emptyList());
        when(SqlCheckRules.getAllFactories(any(), any())).thenReturn(Collections.emptyList());
    }

    @Test
    public void testCheck() {
        SqlCheckReq req = new SqlCheckReq("scriptContent");
        ConnectionSession session = mock(ConnectionSession.class);
        when(connectionSession.getId()).thenReturn("sessionId");
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckEnvironment() {
        String databaseName = "testDatabase";
        List<OffsetString> sqls = Arrays.asList(OffsetString.of("select * from test_table"));
        ConnectionConfig config = new ConnectionConfig();
        Long environmentId = 1L;
        List<CheckViolation> violations = Arrays.asList(new CheckViolation("violationMessage", 1));
        List<Rule> rules = Arrays.asList(rule);
        when(env.getRulesetId()).thenReturn(1L);
        when(sqlChecker.check(anyList(), any())).thenReturn(violations);
        when(ruleService.list(anyLong(), any())).thenReturn(rules);
        when(SqlCheckRules.getAllFactories(any(), any())).thenReturn(Collections.singletonList(SqlCheckRules.MySqlRuleFactory.INSTANCE));
        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(result);
        assertEquals(violations.size(), result.size());
        for (int i = 0; i < violations.size(); ++i) {
            assertEquals(violations.get(i).getMessage(), result.get(i).getMessage());
            assertEquals(violations.get(i).getLevel(), result.get(i).getLevel());
        }
    }

    @Test
    public void testGetRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        DialectType dialectType = DialectType.MYSQL;
        List<Rule> rules = Arrays.asList(rule);
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session, dialectType, jdbcOperations);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetRulesEmpty() {
        List<Rule> rules = Collections.emptyList();
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, dialectType, jdbcOperations);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = Arrays.asList(checkViolation);
        List<Rule> rules = Arrays.asList(rule);
        Map<String, Rule> name2RuleMap = new HashMap<>();
        name2RuleMap.put(ruleMetadata.getName(), rule);
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(rule.getLevel(), result.get(0).getLevel());
    }

    @Test
    public void testFullFillRiskLevelEmpty() {
        List<CheckViolation> violations = Collections.emptyList();
        List<Rule> rules = Collections.emptyList();
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}