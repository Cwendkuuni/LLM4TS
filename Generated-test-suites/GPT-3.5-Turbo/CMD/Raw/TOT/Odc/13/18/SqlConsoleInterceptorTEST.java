import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setup() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.authenticationFacade = authenticationFacade;
        sqlConsoleInterceptor.ruleService = ruleService;
        sqlConsoleInterceptor.sqlConsoleRuleService = sqlConsoleRuleService;
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        
        when(sqlConsoleRuleService.getProperties(anyLong(), eq(SqlConsoleRules.MAX_RETURN_ROWS), any(), eq(Integer.class)))
            .thenReturn(Optional.of(10));

        when(sqlConsoleRuleService.getProperties(anyLong(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(), eq(Integer.class)))
            .thenReturn(Optional.of(5));
        // Add more mock setups as needed

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        // Add assertions based on the expected behavior
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET), any())).thenReturn(true);
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET), any())).thenReturn(false);
        // Add more mock setups as needed

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        // Add assertions based on the expected behavior
    }

    // Add more test cases for other methods if needed

}