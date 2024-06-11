import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        //mock the dependencies here
        AuthenticationFacade authFacade = mock(AuthenticationFacade.class);
        RuleService ruleService = mock(RuleService.class);
        SqlConsoleRuleService sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        sqlConsoleInterceptor = new SqlConsoleInterceptor(authFacade, ruleService, sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle() {
       SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
       SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
       ConnectionSession session = mock(ConnectionSession.class);
       Map<String, Object> context = new HashMap<>();
       //update hashmap content based on your testing needs
       boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
       //assert statements
    }

    @Test
    public void testGetExecuteStageName() {
       //assert statements
    }

    @Test
    public void testDoAfterCompletion() {
       //mock and pass the data to the method to test
    }

    @Test
    public void testGetOrder() {
       //assert statements
    }

   // Write more tests covering edge cases and exceptions
}