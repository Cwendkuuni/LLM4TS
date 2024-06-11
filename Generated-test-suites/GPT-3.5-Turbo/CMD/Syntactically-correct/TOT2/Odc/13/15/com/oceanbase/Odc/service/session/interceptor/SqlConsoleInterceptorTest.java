import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.model.SqlTuple;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setup() {
        sqlConsoleInterceptor = spy(new SqlConsoleInterceptor());
        sqlConsoleInterceptor.authenticationFacade = mock(AuthenticationFacade.class);
        sqlConsoleInterceptor.ruleService = mock(RuleService.class);
        sqlConsoleInterceptor.sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
    }

    @Test
    public void testDoPreHandle_WhenRuleSetIdNull_ReturnTrue() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion_WhenResponseNotSuccess_NoChanges() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        response.setStatus(SqlExecuteStatus.FAILURE);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);

        assertFalse(response.isAllowExport());
    }
}