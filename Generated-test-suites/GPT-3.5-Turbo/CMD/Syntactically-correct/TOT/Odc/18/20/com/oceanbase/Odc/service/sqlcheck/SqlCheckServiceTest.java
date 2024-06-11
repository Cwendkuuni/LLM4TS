import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private JdbcOperations jdbcOperations;
    @Mock
    private ConnectionSession session;
    @Mock
    private SqlCheckReq req;

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }

    @Test
    public void testCheckWithSession() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCheckWithEnvironment() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString());
        ConnectionConfig config = new ConnectionConfig();

        Environment env = new Environment();
        env.setRulesetId(1L);
        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(Collections.emptyList());
        when(jdbcOperations.getDataSource()).thenReturn(new SingleConnectionDataSource(config, true, false));
        when(jdbcOperations.execute(any())).thenReturn(null);

        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertEquals(Collections.emptyList(), violations);
    }

}