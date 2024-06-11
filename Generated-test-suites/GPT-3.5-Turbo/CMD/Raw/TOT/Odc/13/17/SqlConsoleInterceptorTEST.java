import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;

public class SqlConsoleInterceptorTest {

    @Mock
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle() {
        // Typical Use Case: all conditions are met for preHandle to return true
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));

        // Edge Case: context is null
        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, null));

        // Error Scenario: ruleSetId is null
        session.setRuleSetId(null);
        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletion() {
        // Typical Use Case: response status is success, rules are not violated
        sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);
        assertTrue(sqlExecuteResult.isAllowExport());

        // Edge Case: individual team, should set allowExport to true
        session.setOrganizationType(OrganizationType.INDIVIDUAL);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);
        assertTrue(sqlExecuteResult.isAllowExport());

        // Error Scenario: response status is not success
        sqlExecuteResult.setStatus(SqlExecuteStatus.FAILURE);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, context);
        assertFalse(sqlExecuteResult.isAllowExport());
    }

}