
import com.oceanbase.odc.service.sqlcheck.*;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckForEnvironment() {
        Environment env = new Environment();
        List<OffsetString> sqls = Arrays.asList(mock(OffsetString.class));
        ConnectionConfig config = new ConnectionConfig();

        when(environmentService.detail(any(Long.class))).thenReturn(env);
        when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckViolation> result = sqlCheckService.check(1L, "testDatabase", sqls, config);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRules() {
        Rule rule = mock(Rule.class);
        ConnectionSession session = mock(ConnectionSession.class);
        JdbcOperations jdbc = mock(JdbcOperations.class);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbc);
        List<SqlCheckRule> result = sqlCheckService.getRules(Collections.singletonList(rule), session);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        Rule rule = mock(Rule.class);
        CheckViolation violation = new CheckViolation();
        List<CheckViolation> violations = Collections.singletonList(violation);
        List<Rule> result = sqlCheckService.fullFillRiskLevel(Collections.singletonList(rule), violations);
        assertFalse(result.isEmpty());
    }
}
