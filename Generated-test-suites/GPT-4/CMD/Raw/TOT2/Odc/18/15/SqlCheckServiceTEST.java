import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class SqlCheckServiceTest {

    @InjectMocks
    SqlCheckService sqlCheckService;
    
    @Mock
    RuleService ruleService;
    
    @Mock
    EnvironmentService environmentService;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
   
    @Test
    public void testCheckWithSessionAndReq() {
        //setup your session, req objects and expected result
        //when methods were called
        //then check with expected results
    }
    
    @Test
    public void testCheckWithEnvironmentIdDatabaseNameSqlsConfig() {
        //setup your environmentId, databaseName, sqls, config objects and expected result
        //when methods were called
        //then check with expected results
    }
    
    @Test
    public void testGetRulesWithRulesAndSession() {
        //setup your rules, session objects and expected result
        //when methods were called
        //then check with expected results
    }
    
    @Test
    public void testGetRulesWithRulesDialectTypeJdbcOperations() {
        //setup your rules, DialectType, JdbcOperations objects and expected result
        //when methods were called
        //then check with expected results
    }
    
    @Test
    public void testFullFillRiskLevel() {
        //setup your rules, violations objects and expected result
        //when methods were called
        //then check with expected results
    }
    
    @Test(expected = Exception.class) // or any specific exception
    public void testCheckWithSessionAndReqException() {
        //setup conditions to fail
        //when methods were called
    }
    
    @Test(expected = Exception.class) // or any specific exception
    public void testCheckWithEnvironmentIdDatabaseNameSqlsConfigException() {
        //setup conditions to fail
        //when methods were called
    }
}