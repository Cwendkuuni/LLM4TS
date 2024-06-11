
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ConnectionSession connectionSession;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        Mockito.when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(123L);
    }

    @Test
    public void testCheck() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        SqlCheckReq req = new SqlCheckReq();
        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, req);
        assertNotNull(checkResults);
        assertFalse(checkResults.isEmpty());
    }

    @Test
    public void testCheckWithEnvironment() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        Long environmentId = 456L;
        String databaseName = "test";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM table", 0));
        ConnectionConfig config = new ConnectionConfig();
        Environment environment = new Environment();
        environment.setRulesetId(789L);

        Mockito.when(environmentService.detail(environmentId)).thenReturn(environment);
        Mockito.when(ruleService.list(environment.getRulesetId(), QueryRuleMetadataParams.builder().build()))
                .thenReturn(Collections.singletonList(new Rule()));

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(checkViolations);
        assertFalse(checkViolations.isEmpty());
    }

    @Test
    public void testGetRules() {
        SqlCheckService sqlCheckService = new SqlCheckService();
        List<Rule> rules = Collections.singletonList(new Rule());
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        assertNotNull(sqlCheckRules);
        assertFalse(sqlCheckRules.isEmpty());
    }

}
