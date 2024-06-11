import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;

public class SqlConsoleInterceptorTest {
  
    private SqlConsoleInterceptor interceptor;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private ConnectionSession session;
    private Map<String, Object> context;
  
    @Before
    public void setUp() {
        interceptor = new SqlConsoleInterceptor();
        request = mock(SqlAsyncExecuteReq.class);
        response = mock(SqlAsyncExecuteResp.class);
        session = mock(ConnectionSession.class);
        context = new HashMap<>();
    }
  
    @Test
    public void testHandle_WithValidInput_ReturnsTrue() {
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        boolean result = interceptor.doPreHandle(request, response, session, context);
        assertEquals(true, result);
    }
  
    @Test
    public void testDoAfterCompletion_WithSuccessStatus_AllowsExport() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        interceptor.doAfterCompletion(result, session, context);
        assertEquals(true, result.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_WithForbiddenExport_DoNotAllowExport() {
        SqlExecuteResult result = new SqlExecuteResult();
        result.setStatus(SqlExecuteStatus.SUCCESS);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
        interceptor.doAfterCompletion(result, session, context);
        assertEquals(false, result.isAllowExport());
    }

    @Test
    public void testDetermineSqlType_WithValidInput_ReturnsBasicResult() {
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        AtomicReference<Rule> rule = new AtomicReference<>();
        BasicResult result = interceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
        assertEquals(SqlType.OTHERS, result.getSqlType());
    }
  
}