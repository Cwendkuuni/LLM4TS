import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Test
    public void testDoPreHandle() {
        //initialize your mock objects and context map

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        //you can perform assertions based on what your expectations of the method input will produce
        assertTrue(result); //or assertFalse(result) depending on your context variables
    }

    @Test
    public void testDoAfterCompletion(){
        //initialize your mock objects and context map

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        //assertions to validate your post process
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertTrue(order == 2);
    }
}