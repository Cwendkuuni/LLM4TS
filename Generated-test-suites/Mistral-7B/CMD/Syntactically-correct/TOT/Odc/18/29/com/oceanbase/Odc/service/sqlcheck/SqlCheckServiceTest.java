package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.StoredArgumentsCaptor;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;
    @MockitoAnnotations.Mock
    private RuleService ruleService;
    @MockitoAnnotations.Mock
    private EnvironmentService environmentService;

    private ConnectionSession session;
    private ConnectionConfig config;
    private Environment env;

    @Before
    public void setup() {
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(Collections.singletonList(Mockito.mock(Rule.class)));
        Mockito.when(environmentService.detail(Mockito.anyLong())).thenReturn(env);
        Mockito.when(env.getRulesetId()).thenReturn(1L);
        session = Mockito.mock(ConnectionSession.class);
        config = Mockito.mock(ConnectionConfig.class);
        Mockito.when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        Mockito.when(session.getRuleSetId()).thenReturn(1L);
        Mockito.when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(Mockito.mock(JdbcOperations.class));
    }

    @Test
    public void testCheckWithValidInput() {
        List<Rule> rules = Mockito.mock(List.class);
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertFalse(sqlCheckRules.isEmpty());
        Mockito.verify(session).getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY);
        Mockito.verifyNoMoreInteractions(session);

        SqlCheckReq req = new SqlCheckReq();
        req.setDelimiter(";");
        req.setScriptContent("SELECT * FROM users WHERE id = 1;");
        CheckResult result = sqlCheckService.check(session, req);
        assertNotNull(result);
        assertTrue(result.getCheckViolations().isEmpty());
    }

    @Test
    public void testCheckWithEmptyInput() {
        List<Rule> rules = Mockito.mock(List.class);
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(rules);
        SqlCheckReq req = new SqlCheckReq();
        CheckResult result = sqlCheckService.check(session, req);
        assertTrue(result.getCheckViolations().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullSession() {
        SqlCheckReq req = new SqlCheckReq();
        sqlCheckService.check(null, req);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckWithNullReq() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        sqlCheckService.check(session, null);
    }

    @Test
    public void testCheckWithEnvironmentId() {
        List<Rule> rules = Mockito.mock(List.class);
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(rules);
        OBConsoleDataSourceFactory factory = Mockito.mock(OBConsoleDataSourceFactory.class);
        Mockito.when(factory.getDataSource()).thenReturn(Mockito.mock(SingleConnectionDataSource.class));
        Mockito.when(environmentService.detail(Mockito.anyLong())).thenReturn(env);
        Mockito.when(env.getRulesetId()).thenReturn(1L);
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, factory.getDataSource().getDialectType(), Mockito.mock(JdbcOperations.class));
        assertFalse(sqlCheckRules.isEmpty());
        Mockito.verify(factory).getDataSource();
        Mockito.verifyNoMoreInteractions(factory);

        String databaseName = "test";
        List<OffsetString> sqls = List.of(OffsetString.of("SELECT * FROM users WHERE id = 1;"));
        CheckResult result = sqlCheckService.check(1L, databaseName, sqls, config);
        assertNotNull(result);
        assertTrue(result.getCheckViolations().isEmpty());
    }

    @Test
    public void testCheckWithEmptySqls() {
        List<Rule> rules = Mockito.mock(List.class);
        OBConsoleDataSourceFactory factory = Mockito.mock(OBConsoleDataSourceFactory.class);
        Mockito.when(factory.getDataSource()).thenReturn(Mockito.mock(SingleConnectionDataSource.class));
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, factory.getDataSource().getDialectType(), Mockito.mock(JdbcOperations.class));
        assertFalse(sqlCheckRules.isEmpty());
        Mockito.verify(factory).getDataSource();
        Mockito.verifyNoMoreInteractions(factory);

        List<OffsetString> sqls = Collections.emptyList();
        CheckResult result = sqlCheckService.check(1L, "test", sqls, config);
        assertTrue(result.getCheckViolations().isEmpty());
    }

    @Test
    public void testCheckWithInvalidConnectionConfig() {
        List<Rule> rules = Mockito.mock(List.class);
        OBConsoleDataSourceFactory factory = Mockito.mock(OBConsoleDataSourceFactory.class);
        Mockito.when(factory.getDataSource()).thenReturn(Mockito.mock(SingleConnectionDataSource.class));
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, factory.getDataSource().getDialectType(), Mockito.mock(JdbcOperations.class));
        assertFalse(sqlCheckRules.isEmpty());
        Mockito.verify(factory).getDataSource();
        Mockito.verifyNoMoreInteractions(factory);

        Mockito.when(config.getDialectType()).thenReturn(DialectType.ORACLE);
        CheckResult result = sqlCheckService.check(1L, "test", List.of(), config);
        assertNull(result);
    }

    @Test
    public void testGetRulesWithValidInput() {
        List<Rule> rules = Mockito.mock(List.class);
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(rules);
        DialectType dialectType = DialectType.MYSQL;
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, Mockito.mock(JdbcOperations.class));
        assertFalse(sqlCheckRules.isEmpty());
        Mockito.verify(ruleService).list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class));
        Mockito.verifyNoMoreInteractions(ruleService);

        SqlCheckRules sqlCheckRulesFactory = Mockito.mock(SqlCheckRules.class);
        Mockito.when(sqlCheckRulesFactory.getAllFactories(dialectType, Mockito.any(JdbcOperations.class))).thenReturn(Collections.singletonList(Mockito.mock(SqlCheckRuleFactory.class)));
        OngoingStubbing<SqlCheckRule> sqlCheckRuleStub = Mockito.doReturn(Mockito.mock(SqlCheckRule.class));
        Mockito.when(rules.get(0).getMetadata()).thenReturn(Mockito.mock(RuleMetadata.class));
        RuleMetadata metadata = Mockito.mock(RuleMetadata.class);
        Mockito.when(metadata.getType()).thenReturn(RuleType.SQL_CHECK);
        Mockito.when(rules.get(0).getEnabled()).thenReturn(true);
        Mockito.when(rules.get(0).getMetadata().getName()).thenReturn("test");
        Mockito.when(sqlCheckRulesFactory.createByRule(Collections.singletonList(sqlCheckRuleStub.capture()), dialectType, rules.get(0))).thenReturn(Mockito.mock(SqlCheckRule.class));
        sqlCheckRules = sqlCheckRulesFactory.createByRule(sqlCheckRules, dialectType, rules.get(0));
        assertEquals(1, sqlCheckRules.size());
        SqlCheckRule rule = sqlCheckRuleStub.getValue();
        assertTrue(rule instanceof SqlCheckRule);
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        List<Rule> rules = Collections.emptyList();
        SqlCheckRules sqlCheckRulesFactory = Mockito.mock(SqlCheckRules.class);
        List<SqlCheckRule> sqlCheckRules = sqlCheckRulesFactory.getRules(rules, DialectType.MYSQL, Mockito.mock(JdbcOperations.class));
        assertTrue(sqlCheckRules.isEmpty());
        Mockito.verifyNoInteractions(sqlCheckRulesFactory);
    }
}