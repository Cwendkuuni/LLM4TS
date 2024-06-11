import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
    }

    @Test
    public void testCheck() {
        //TODO: Implement test for check method with ConnectionSession and SqlCheckReq parameters
    }

    @Test
    public void testCheckWithEnvironment() {
        //TODO: Implement test for check method with environmentId, databaseName, sqls, and config parameters
    }

    @Test
    public void testGetRulesWithSession() {
        //TODO: Implement test for getRules method with rules and ConnectionSession parameters
    }

    @Test
    public void testGetRulesWithJdbc() {
        //TODO: Implement test for getRules method with rules, DialectType, and JdbcOperations parameters
    }

    @Test
    public void testFullFillRiskLevel() {
        //TODO: Implement test for fullFillRiskLevel method with rules and violations parameters
    }
}