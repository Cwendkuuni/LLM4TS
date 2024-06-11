import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

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
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Mock
    private SqlCheckContext sqlCheckContext;

    @Captor
    private ArgumentCaptor<List<Rule>> rulesCaptor;

    @Captor
    private ArgumentCaptor<OffsetString> sqlCaptor;

    @Captor
    private ArgumentCaptor<ConnectionConfig> configCaptor;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(environmentService.detail(anyLong())).thenReturn(new Environment());
        when(obConsoleDataSourceFactory.getDataSource()).thenReturn(singleConnectionDataSource);
    }

    @Test
    public void testCheck() {
        // Given
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        ConnectionConfig config = new ConnectionConfig();
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("test"));
        when(jdbcTemplate.query(anyString(), anyArray())).thenReturn(Collections.emptyList());
        when(obConsoleDataSourceFactory.resetSchema(anyFunction())).thenAnswer(invocation -> {
            Object dbName = invocation.getArgument(0);
            return dbName;
        });

        // When
        List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq());

        // Then
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(obConsoleDataSourceFactory, times(1)).getDataSource();
        verify(obConsoleDataSourceFactory, times(1)).resetSchema(anyFunction());
        verify(obConsoleDataSourceFactory, times(1)).getDataSource();
        verify(jdbcTemplate, times(sqls.size())).query(eq("SELECT 1 FROM DUAL"), anyArray());
        verifyNoMoreInteractions(ruleService);
        verifyNoMoreInteractions(obConsoleDataSourceFactory);
        verifyNoMoreInteractions(jdbcTemplate);
        assertEquals(Collections.singletonList(results), sqlCheckService.check(connectionSession, new SqlCheckReq()));
    }

    @Test
    public void testCheckWithEnvironment() {
        // Given
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        ConnectionConfig config = new ConnectionConfig();
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("test"));
        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        // When
        List<CheckViolation> violations = sqlCheckService.check(1L, "testDB", sqls, config);

        // Then
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService, times(1)).detail(anyLong());
        verify(obConsoleDataSourceFactory, times(1)).getDataSource();
        verify(jdbcTemplate, times(sqls.size())).query(eq("SELECT 1 FROM DUAL"), anyArray());
        verifyNoMoreInteractions(ruleService);
        verifyNoMoreInteractions(environmentService);
        verifyNoMoreInteractions(obConsoleDataSourceFactory);
        verifyNoMoreInteractions(jdbcTemplate);
        assertEquals(Collections.emptyList(), violations);
    }

    @Test
    public void testGetRules() {
        // Given
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);
        List<SqlCheckRuleFactory> candidates = new ArrayList<>();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(jdbcTemplate.queryForList(anyString(), anyArray())).thenReturn(Collections.emptyList());

        // When
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        // Then
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(jdbcTemplate, times(1)).queryForList(eq("SELECT * FROM sqlcheck_rules where rule_set_id = ?"), anyArray());
        verifyNoMoreInteractions(ruleService);
        verifyNoMoreInteractions(jdbcTemplate);
        assertSame(Collections.emptyList(), sqlCheckRules);
    }

    @Test
    public void testGetRulesWithJdbc() {
        // Given
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);
        List<SqlCheckRuleFactory> candidates = new ArrayList<>();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        JdbcOperations jdbc = mock(JdbcOperations.class);
        when(jdbc.queryForList(anyString(), anyArray())).thenReturn(Collections.singletonList(new Object[]{"rule1"}));

        // When
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession, jdbc);

        // Then
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(jdbc, times(1)).queryForList(eq("SELECT * FROM sqlcheck_rules where rule_set_id = ?"), anyArray());
        verifyNoMoreInteractions(ruleService);
        verifyNoMoreInteractions(jdbc);
        List<SqlCheckRule> expected = new ArrayList<>();
        expected.add(new DefaultSqlCheckRule("rule1"));
        assertSame(expected, sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        // Given
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(new RuleType("TestType"));
        RuleViolation ruleViolation = new RuleViolation();
        ruleViolation.setMessage("TestMessage");
        violation.setViolation(ruleViolation);
        violations.add(violation);

        // When
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);
        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        // Then
        verifyNoInteractions(ruleService);
        verifyNoInteractions(environmentService);
        verifyNoInteractions(obConsoleDataSourceFactory);
        verifyNoInteractions(jdbcTemplate);
        assertEquals(Collections.singletonList(rule), result);
    }
}