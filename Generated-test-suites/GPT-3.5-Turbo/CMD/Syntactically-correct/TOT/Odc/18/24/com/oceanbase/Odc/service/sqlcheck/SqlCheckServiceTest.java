import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.*;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import lombok.NonNull;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }

    @Test
    public void testCheckWithConnectionSession() {
        ConnectionSession session = mock(ConnectionSession.class);
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        List<Rule> rules = new ArrayList<>();
        SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
        rules.add(new Rule());
        when(ruleService.list(eq(ruleSetId), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table;");
        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertNotNull(checkResults);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM table;", 0));
        ConnectionConfig config = new ConnectionConfig();
        Environment env = new Environment();
        env.setRulesetId(1L);

        List<Rule> rules = new ArrayList<>();
        SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
        rules.add(new Rule());
        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(eq(env.getRulesetId()), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(checkViolations);
        assertEquals(0, checkViolations.size());
    }

}