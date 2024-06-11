
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

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

    // Test for check(ConnectionSession session, SqlCheckReq req)
    @Test
    public void testCheckConnectionSession() {
        ConnectionSession session = mock(ConnectionSession.class);
        Rule rule = new Rule();
        rule.setEnabled(true);
        RuleMetadata metadata = new RuleMetadata();
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.singletonList(rule));

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        
        List<CheckResult> results = sqlCheckService.check(session, req);

        assertNotNull(results);
        assertTrue(!results.isEmpty());
    }

    // Test for check(Long environmentId, String databaseName, List<OffsetString> sqls, ConnectionConfig config)
    @Test
    public void testCheckEnvironmentId() {
        Environment env = new Environment();
        env.setRulesetId(1L);

        Rule rule = new Rule();
        rule.setEnabled(true);
        RuleMetadata metadata = new RuleMetadata();
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);

        when(environmentService.detail(1L)).thenReturn(env);
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.singletonList(rule));

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);

        List<CheckViolation> violations = sqlCheckService.check(1L, "dbName", new ArrayList<>(), config);

        assertNotNull(violations);
        assertTrue(violations.isEmpty());
    }

}
