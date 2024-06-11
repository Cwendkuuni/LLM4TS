package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.ModelMapper;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation.ViolationType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.service.SqlChecker;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = SqlCheckService.class)
@ContextConfiguration(classes = {SqlCheckService.class, SqlCheckRules.class, SqlCheckRuleFactory.class})
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    @InjectMocks
    private SqlCheckService sqlCheckService;

    @BeforeAll
    public static void init() {
        MockitoAnnotations.initMocks(SqlCheckServiceTest.class);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcTemplate);

        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);

        List<Rule> rules = Arrays.asList(rule);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);
        List<CheckViolation> checkViolations = Arrays.asList(
                new CheckViolation("TestViolation", ViolationType.SYNTAX_ERROR, 1, 10, 15));

        SqlChecker sqlChecker = spy(new DefaultSqlChecker(DialectType.MYSQL, null, sqlCheckRules));
        doReturn(checkViolations).when(sqlChecker).check(Collections.singletonList("SELECT 1 FROM test;"));

        List<CheckResult> result = sqlCheckService.check(session, req);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("TestViolation", result.get(0).getViolation().getType().getName());
    }

    @Test
    public void testCheckWithEnvironment() {
        Long environmentId = 1L;
        String databaseName = "test_database";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT 1 FROM test;"));
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);

        Environment env = new Environment();
        env.setRulesetId(1L);

        when(environmentService.detail(environmentId)).thenReturn(env);
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        OBConsoleDataSourceFactory factory = spy(new OBConsoleDataSourceFactory(config, true, false));
        when(factory.getDataSource()).thenReturn(dataSource);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(new ArrayList<>(), DialectType.MYSQL, jdbcTemplate);
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, sqlCheckRules);
        List<CheckViolation> checkViolations = new ArrayList<>();

        try (SingleConnectionDataSource dbDataSource = (SingleConnectionDataSource) dataSource) {
            JdbcTemplate jdbc = new JdbcTemplate(dbDataSource);
            doReturn(checkViolations).when(sqlChecker).check(Collections.singletonList("SELECT 1 FROM test;"), new SqlCheckContext(1));
            List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);
            Assertions.assertEquals(1, result.size());
            verify(sqlChecker, times(1)).check(Collections.singletonList("SELECT 1 FROM test;"), any(SqlCheckContext.class));
        }
    }

    @Test
    public void testGetRules() {
        DialectType dialectType = DialectType.MYSQL;
        List<Rule> rules = Arrays.asList(new Rule());
        JdbcOperations jdbc = mock(JdbcOperations.class);

        List<SqlCheckRuleFactory> candidates = Arrays.asList(mock(SqlCheckRuleFactory.class));
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbc);
        verify(jdbc, times(1)).query(anyString(), anyArray(), anyMap(), anyMap(), anyMap(), anyInt(), anyInt(), any());
        Assertions.assertEquals(1, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = Arrays.asList(new Rule());
        List<CheckViolation> violations = Arrays.asList(new CheckViolation("TestViolation", ViolationType.SYNTAX_ERROR, 1, 10, 15));
        Map<String, Rule> name2RuleMap = new HashMap<>();
        name2RuleMap.put("TestRule", new Rule().setMetadata(new RuleMetadata().setName("TestRule")));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("TestRule", result.get(0).getMetadata().getName());
    }
}