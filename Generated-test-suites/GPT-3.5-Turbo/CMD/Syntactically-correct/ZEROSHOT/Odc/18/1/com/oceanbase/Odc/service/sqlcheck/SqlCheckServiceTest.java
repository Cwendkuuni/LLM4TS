import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private ConnectionSession session;
    private ConnectionConfig config;
    private DialectType dialectType;
    private SingleConnectionDataSource dataSource;
    private OffsetString offsetString;

    @Before
    public void setUp() {
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        session = mock(ConnectionSession.class);
        config = mock(ConnectionConfig.class);
        dialectType = DialectType.ORACLE; // Set your desired dialect type here
        dataSource = mock(SingleConnectionDataSource.class);
        offsetString = mock(OffsetString.class);

        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
    }

    @Test
    public void testCheckWithSession() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM TABLE");

        when(session.getDialectType()).thenReturn(dialectType);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(dataSource);

        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertNotNull(checkResults);
        assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheckWithEnvironmentId() {
        Long environmentId = 1L; // Set your desired environment ID here
        String databaseName = "test_database";
        List<OffsetString> sqls = Arrays.asList(offsetString);

        when(environmentService.detail(environmentId)).thenReturn(new Environment());
        when(ruleService.list(environmentId, QueryRuleMetadataParams.builder().build())).thenReturn(new ArrayList<>());

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(checkViolations);
        assertTrue(checkViolations.isEmpty());
    }

    @Test
    public void testGetRules() {
        List<com.oceanbase.odc.service.regulation.ruleset.model.Rule> rules = new ArrayList<>();
        List<com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, dataSource);

        assertNotNull(sqlCheckRules);
        assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<com.oceanbase.odc.service.regulation.ruleset.model.Rule> rules = new ArrayList<>();
        List<CheckViolation> checkViolations = new ArrayList<>();

        com.oceanbase.odc.service.regulation.ruleset.model.Rule r = new com.oceanbase.odc.service.regulation.ruleset.model.Rule();
        r.setMetadata(new com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata());
        r.setEnabled(true);
        rules.add(r);

        CheckViolation cv = new CheckViolation();
        cv.setType(new com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule());
        checkViolations.add(cv);

        List<com.oceanbase.odc.service.regulation.ruleset.model.Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        assertNotNull(violatedRules);
        assertTrue(!violatedRules.isEmpty());
        assertEquals(1, violatedRules.size());
    }
}