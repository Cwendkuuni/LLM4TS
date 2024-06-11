package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;

/**
 * {@link SqlCheckService} test cases
 *
 * @author yh263208
 * @since ODC_release_4.1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcOperations jdbcOperations;

    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private OBConsoleDataSourceFactory factory;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Test
    public void testCheck() {
        // Given
        Rule rule1 = Mockito.mock(Rule.class);
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("rule1");
        metadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(metadata1);
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.eq(QueryRuleMetadataParams.builder().build())))
                .thenReturn(Arrays.asList(rule1));

        List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcOperations);
        SqlCheckRuleFactory mockFactory = Mockito.mock(SqlCheckRuleFactory.class);
        Mockito.when(candidates.get(0)).thenReturn(mockFactory);
        SqlCheckRule rule = Mockito.mock(SqlCheckRule.class);
        Mockito.when(mockFactory.create(rule1)).thenReturn(rule);

        ConnectionConfig config = new ConnectionConfig();
        SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);
        Mockito.when(factory.getDataSource()).thenReturn(dataSource);

        Mockito.when(jdbcOperations.queryForList("SELECT 1", new Object[0])).thenReturn(Collections.emptyList());
        Mockito.when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        Mockito.when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(jdbcOperations);

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, Arrays.asList(rule));

        String scriptContent = "SELECT * FROM users WHERE age < 18";
        CheckViolation violation = new CheckViolation();
        violation.setType(new RuleType("RuleType1"));
        violation.setMessage("Message1");
        violation.setLevel(1);

        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(violation);

        Mockito.when(sqlChecker.check(Collections.singletonList("SELECT * FROM users WHERE age < 18")))
                .thenReturn(checkViolations);

        Mockito.when(environmentService.detail(Mockito.anyLong())).thenReturn(new Environment());

        CheckResult result = sqlCheckService.check(connectionSession, new SqlCheckReq());

        // Then
        assertNotNull(result);
        assertTrue(result.getCheckViolations().size() > 0);
        assertEquals(violation.getMessage(), result.getCheckViolations().get(0).getMessage());
        assertEquals(violation.getLevel(), result.getCheckViolations().get(0).getLevel());

        // Verify
        Mockito.verify(ruleService, Mockito.times(1)).list(Mockito.anyLong(), Mockito.eq(QueryRuleMetadataParams.builder().build()));
        Mockito.verify(ruleService, Mockito.times(1)).list(Mockito.anyLong(), Mockito.eq(QueryRuleMetadataParams.builder().build()));
        Mockito.verify(jdbcOperations, Mockito.times(1)).queryForList("SELECT 1", new Object[0]);
        Mockito.verify(factory, Mockito.times(1)).getDataSource();
        Mockito.verify(sqlChecker, Mockito.times(1)).check(Collections.singletonList("SELECT * FROM users WHERE age < 18"));
        Mockito.verifyNoMoreInteractions(factory);
        Mockito.verifyNoMoreInteractions(connectionSession);
        Mockito.verifyNoMoreInteractions(environmentService);
        Mockito.verifyNoMoreInteractions(ruleService);
    }

    @Test
    public void testCheckWithEmptyRules() {
        // Given
        Rule rule = Mockito.mock(Rule.class);
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("rule1");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);

        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.eq(QueryRuleMetadataParams.builder().build())))
                .thenReturn(Collections.emptyList());

        ConnectionConfig config = new ConnectionConfig();
        SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);
        Mockito.when(factory.getDataSource()).thenReturn(dataSource);

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, Collections.emptyList());

        String scriptContent = "SELECT * FROM users WHERE age < 18";
        List<CheckViolation> checkViolations = sqlChecker.check(Collections.singletonList(scriptContent));

        // Then
        assertNotNull(sqlCheckService);
        assertTrue(checkViolations.isEmpty());

        // Verify
        Mockito.verifyNoMoreInteractions(ruleService);
        Mockito.verifyNoMoreInteractions(jdbcOperations);
        Mockito.verifyNoMoreInteractions(factory);
        Mockito.verifyNoMoreInteractions(connectionSession);
        Mockito.verifyNoMoreInteractions(environmentService);
        Mockito.verifyNoMoreInteractions(rule);
        Mockito.verify(sqlChecker, Mockito.times(1)).check(Collections.singletonList(scriptContent));
    }

    @Test
    public void testCheckWithEmptySqls() {
        // Given
        Rule rule = Mockito.mock(Rule.class);
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("rule1");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);

        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.eq(QueryRuleMetadataParams.builder().build())))
                .thenReturn(Arrays.asList(rule));

        List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcOperations);
        SqlCheckRuleFactory mockFactory = Mockito.mock(SqlCheckRuleFactory.class);
        Mockito.when(candidates.get(0)).thenReturn(mockFactory);
        SqlCheckRule ruleInstance = Mockito.mock(SqlCheckRule.class);
        Mockito.when(mockFactory.create(rule)).thenReturn(ruleInstance);

        ConnectionConfig config = new ConnectionConfig();
        SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);
        Mockito.when(factory.getDataSource()).thenReturn(dataSource);

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, Arrays.asList(ruleInstance));

        List<OffsetString> sqls = Collections.emptyList();

        // Then
        assertNotNull(sqlCheckService);
        assertTrue(sqlCheckService.check(connectionSession, new SqlCheckReq(), sqls).isEmpty());

        // Verify
        Mockito.verifyNoMoreInteractions(ruleService);
        Mockito.verifyNoMoreInteractions(jdbcOperations);
        Mockito.verifyNoMoreInteractions(factory);
        Mockito.verifyNoMoreInteractions(connectionSession);
        Mockito.verifyNoMoreInteractions(environmentService);
        Mockito.verify(sqlChecker, Mockito.times(1)).check(sqls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullSession() {
        // Given
        Rule rule = Mockito.mock(Rule.class);
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("rule1");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);

        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.eq(QueryRuleMetadataParams.builder().build())))
                .thenReturn(Arrays.asList(rule));

        List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcOperations);
        SqlCheckRuleFactory mockFactory = Mockito.mock(SqlCheckRuleFactory.class);
        Mockito.when(candidates.get(0)).thenReturn(mockFactory);
        SqlCheckRule ruleInstance = Mockito.mock(SqlCheckRule.class);
        Mockito.when(mockFactory.create(rule)).thenReturn(ruleInstance);

        ConnectionConfig config = new ConnectionConfig();
        SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);
        Mockito.when(factory.getDataSource()).thenReturn(dataSource);

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, Arrays.asList(ruleInstance));

        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM users WHERE age < 18"));

        // When
        sqlCheckService.check(null, new SqlCheckReq(), sqls);

        // Then
        // AssertionException is expected

        // Verify
        Mockito.verifyNoMoreInteractions(ruleService);
        Mockito.verifyNoMoreInteractions(jdbcOperations);
        Mockito.verifyNoMoreInteractions(factory);
        Mockito.verifyNoMoreInteractions(connectionSession);
        Mockito.verifyNoMoreInteractions(environmentService);
        Mockito.verifyNoMoreInteractions(rule);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullReq() {
        // Given
        Rule rule = Mockito.mock(Rule.class);
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("rule1");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);

        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.eq(QueryRuleMetadataParams.builder().build())))
                .thenReturn(Arrays.asList(rule));

        List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcOperations);
        SqlCheckRuleFactory mockFactory = Mockito.mock(SqlCheckRuleFactory.class);
        Mockito.when(candidates.get(0)).thenReturn(mockFactory);
        SqlCheckRule ruleInstance = Mockito.mock(SqlCheckRule.class);
        Mockito.when(mockFactory.create(rule)).thenReturn(ruleInstance);

        ConnectionConfig config = new ConnectionConfig();
        SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);
        Mockito.when(factory.getDataSource()).thenReturn(dataSource);

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, Arrays.asList(ruleInstance));

        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM users WHERE age < 18"));

        // When
        sqlCheckService.check(connectionSession, null, sqls);

        // Then
        // AssertionException is expected

        // Verify
        Mockito.verifyNoMoreInteractions(ruleService);
        Mockito.verifyNoMoreInteractions(jdbcOperations);
        Mockito.verifyNoMoreInteractions(factory);
        Mockito.verifyNoMoreInteractions(connectionSession);
        Mockito.verifyNoMoreInteractions(environmentService);
        Mockito.verifyNoMoreInteractions(rule);
    }

    @Test
    public void testCheckWithEmptySqlsAndEmptyRules() {
        // Given
        Rule rule = Mockito.mock(Rule.class);
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("rule1");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);

        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.eq(QueryRuleMetadataParams.builder().build())))
                .thenReturn(Collections.emptyList());

        ConnectionConfig config = new ConnectionConfig();
        SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);
        Mockito.when(factory.getDataSource()).thenReturn(dataSource);

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, Collections.emptyList());

        List<OffsetString> sqls = Collections.emptyList();

        // Then
        assertTrue(sqlCheckService.check(connectionSession, new SqlCheckReq(), sqls).isEmpty());

        // Verify
        Mockito.verifyNoMoreInteractions(ruleService);
        Mockito.verifyNoMoreInteractions(jdbcOperations);
        Mockito.verifyNoMoreInteractions(factory);
        Mockito.verifyNoMoreInteractions(connectionSession);
        Mockito.verifyNoMoreInteractions(environmentService);
        Mockito.verify(sqlChecker, Mockito.times(1)).check(sqls);
    }
}