
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcOperations;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {
    private SqlCheckService sqlCheckService;
    private ConnectionSession mockSession;
    private RuleService mockRuleService;
    private EnvironmentService mockEnvironmentService;

    @Before
    public void setup() {
        mockSession = Mockito.mock(ConnectionSession.class);
        mockRuleService = Mockito.mock(RuleService.class);
        mockEnvironmentService = Mockito.mock(EnvironmentService.class);

        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(mockRuleService);
        sqlCheckService.setEnvironmentService(mockEnvironmentService);
    }

    @Test
    public void checkTest() {
        SqlCheckReq mockReq = new SqlCheckReq();

        // Test when ruleSetId is null
        Mockito.when(ConnectionSessionUtil.getRuleSetId(mockSession)).thenReturn(null);
        List<CheckResult> result = sqlCheckService.check(mockSession, mockReq);
        assertTrue(result.isEmpty());

        // Test when ruleSetId is not null
        Mockito.when(ConnectionSessionUtil.getRuleSetId(mockSession)).thenReturn(1L);
        Mockito.when(mockRuleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class)))
                .thenReturn(Arrays.asList(new Rule()));
        result = sqlCheckService.check(mockSession, mockReq);
        assertFalse(result.isEmpty());
    }

    @Test
    public void checkTest_withEnvironmentId() {
        Long environmentId = 1L;
        String databaseName = "testDB";
        ConnectionConfig connectionConfig = new ConnectionConfig();
        List<OffsetString> mockSqls = Arrays.asList(new OffsetString("SELECT * FROM testTable", 0));

        //Test when sqls is empty
        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, new ArrayList<>(), connectionConfig);
        assertTrue(result.isEmpty());

        //Test when sqls is not empty
        Environment mockEnvironment = Mockito.mock(Environment.class);
        Mockito.when(mockEnvironmentService.detail(environmentId)).thenReturn(mockEnvironment);
        Mockito.when(mockEnvironment.getRulesetId()).thenReturn(1L);
        Mockito.when(mockRuleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class)))
                .thenReturn(Arrays.asList(new Rule()));

        result = sqlCheckService.check(environmentId, databaseName, mockSqls, connectionConfig);
        assertFalse(result.isEmpty());
    }

    @Test
    public void getRulesTest() {
        List<SqlCheckRule> rules = sqlCheckService.getRules(Arrays.asList(new Rule()), mockSession);
        assertFalse(rules.isEmpty());

        DialectType dialectType = DialectType.MySQL;
        JdbcOperations operations = Mockito.mock(JdbcOperations.class);
        rules = sqlCheckService.getRules(Arrays.asList(new Rule()), dialectType, operations);
        assertFalse(rules.isEmpty());
    }
}
