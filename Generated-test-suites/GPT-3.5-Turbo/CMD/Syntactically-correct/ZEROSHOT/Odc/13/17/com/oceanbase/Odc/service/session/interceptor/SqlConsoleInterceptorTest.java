import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;
  
    @Mock
    private ConnectionSession session;
  
    @Mock
    private SqlAsyncExecuteReq request;
  
    @Mock
    private SqlAsyncExecuteResp response;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle() {
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle() {
        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String executeStageName = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals("SQL_CONSOLE_RULE", executeStageName);
    }

    @Test
    public void testDoAfterCompletion() {
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        sqlConsoleInterceptor.doAfterCompletion(new SqlExecuteResult(SqlExecuteStatus.SUCCESS), session, context);
        // Add your assertions here
    }

    @Test
    public void testIsIndividualTeam() {
        boolean result = sqlConsoleInterceptor.isIndividualTeam();
        // Add your assertions here
    }

    @Test
    public void testDetermineSqlType() {
        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
        // Add your assertions here
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }

}