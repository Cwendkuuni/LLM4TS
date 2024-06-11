import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckWithConnectionSession() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM TABLE");
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setType(RuleType.SQL_CHECK);
        rule.setEnabled(true);
        rules.add(rule);
        when(ruleService.list(any(Long.class), any())).thenReturn(rules);
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertEquals(Collections.emptyList(), checkResults);
        verify(session).getRuleSetId(session);
    }

    @Test
    public void testCheckWithEnvironmentId() {
        Long environmentId = 123L;
        String databaseName = "testDB";
        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql = new OffsetString(0, "SELECT * FROM TABLE");
        sqls.add(sql);
        ConnectionConfig config = new ConnectionConfig();
        Environment env = new Environment();
        env.setRulesetId(456L);
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setType(RuleType.SQL_CHECK);
        rule.setEnabled(true);
        rules.add(rule);
        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(any(Long.class), any())).thenReturn(rules);
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        when(jdbc.queryForObject(any(), any(), any())).thenReturn(1);
        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(Collections.emptyList(), violations);
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setType(RuleType.SQL_CHECK);
        rule.setEnabled(true);
        rules.add(rule);
        JdbcOperations jdbc = mock(JdbcTemplate.class);
        when(jdbc.queryForObject(any(), any(), any())).thenReturn(1);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, jdbc);
        assertEquals(Collections.emptyList(), sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setName("${TestName}");
        rule.getMetadata().setType(RuleType.SQL_CHECK);
        rule.setEnabled(true);
        rules.add(rule);
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(SqlCheckRules.MISSING_SEMICOLON);
        violations.add(violation);
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(1, violatedRules.size());
    }
}