package com.oceanbase.odc.service.sqlcheck.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
import com.oceanbase.odc.service.regulation.ruleset.Model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.sqlcheck.util.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckContext;
import com.oceanbase.odc.test.util.TestUtils;

/**
 * Test class for {@link SqlCheckService}
 *
 * @author yh263208
 * @date 2023-03-28 11:01
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;

    @Mock
    private OBConsoleDataSourceFactory factory;

    @Mock
    private Environment environment;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private SqlCheckContext checkContext;

    @Test
    public void testCheckWithValidConnectionSessionAndValidReq() {
        // Given
        List<Rule> rules = TestUtils.generateRandomRules(5);
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(singleConnectionDataSource.getJdbcOperations()).thenReturn(jdbcTemplate);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        String scriptContent = "SELECT * FROM testTable";
        SqlCheckReq req = new SqlCheckReq();
        req.setDelimiter(";");
        req.setScriptContent(scriptContent);

        // When
        List<CheckResult> result = sqlCheckService.check(connectionSession, req);

        // Then
        verify(ruleService, times(1)).list(anyLong(), eq(QueryRuleMetadataParams.builder().build()));
        verify(jdbcTemplate, times(1)).check(eq(scriptContent), eq(checkContext));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckWithEmptySqls() {
        // Given
        List<Rule> rules = TestUtils.generateRandomRules(5);
        Long environmentId = 1L;
        String databaseName = "testDatabase";
        ConnectionConfig config = new ConnectionConfig();
        when(environmentService.detail(environmentId)).thenReturn(environment);
        when(environment.getRulesetId()).thenReturn(1L);
        when(factory.getDataSource(anyLong(), anyString(), any(), anyBoolean(), anyBoolean()))
                .thenReturn(singleConnectionDataSource);
        when(singleConnectionDataSource.getJdbcOperations()).thenReturn(jdbcTemplate);
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);

        // When
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, Collections.emptyList(), config);

        // Then
        verify(environmentService, times(1)).detail(environmentId);
        verify(factory, times(1)).getDataSource(eq(environmentId), eq(databaseName), any(), eq(true), eq(false));
        verify(jdbcTemplate, times(0)).check(anyList(), any());
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testCheckWithValidSqls() {
        // Given
        List<Rule> rules = TestUtils.generateRandomRules(5);
        Long environmentId = 1L;
        String databaseName = "testDatabase";
        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql1 = new OffsetString("SELECT * FROM testTable WHERE id = 1;");
        OffsetString sql2 = new OffsetString("DELETE FROM testTable WHERE id = 2;");
        sqls.add(sql1);
        sqls.add(sql2);
        Environment env = new Environment();
        env.setRulesetId(1L);
        when(environmentService.detail(environmentId)).thenReturn(env);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        factory.resetSchema(origin -> databaseName);
        SqlCheckContext checkContext = new SqlCheckContext((long) sqls.size());
        when(factory.getDataSource(anyLong(), anyString(), any(), anyBoolean(), anyBoolean()))
                .thenReturn(singleConnectionDataSource);
        when(singleConnectionDataSource.getJdbcOperations()).thenReturn(jdbcTemplate);
        when(ruleService.list(anyLong(), eq(QueryRuleMetadataParams.builder().build()))).thenReturn(rules);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);

        // When
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);

        // Then
        verify(environmentService, times(1)).detail(environmentId);
        verify(factory, times(1)).getDataSource(eq(environmentId), eq(databaseName), any(), eq(true), eq(false));
        verify(jdbcTemplate, times(sqls.size())).check(anyList(), checkContext);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        // Given
        List<Rule> rules = Collections.emptyList();

        // When
        List<SqlCheckRule> checkRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);

        // Then
        verify(jdbcTemplate, times(0)).query(anyString(), anyArray(), any());
        assertTrue(checkRules.isEmpty());
    }

    @Test
    public void testGetRulesWithValidRules() {
        // Given
        List<Rule> rules = TestUtils.generateRandomRules(5);
        List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcTemplate);

        // When
        List<SqlCheckRule> checkRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);

        // Then
        verify(jdbcTemplate, times(rules.size())).query(anyString(), anyArray(), any());
        assertFalse(checkRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevelWithEmptyCheckViolations() {
        // Given
        List<Rule> rules = TestUtils.generateRandomRules(5);
        List<CheckViolation> violations = Collections.emptyList();

        // When
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        // Then
        verify(jdbcTemplate, times(0)).query(anyString(), anyArray(), any());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFullFillRiskLevelWithValidCheckViolations() {
        // Given
        List<Rule> rules = TestUtils.generateRandomRules(5);
        List<CheckViolation> violations = TestUtils.generateRandomCheckViolations(5);

        // When
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        // Then
        verify(jdbcTemplate, times(violations.size())).query(anyString(), anyArray(), any());
        assertFalse(result.isEmpty());
    }
}