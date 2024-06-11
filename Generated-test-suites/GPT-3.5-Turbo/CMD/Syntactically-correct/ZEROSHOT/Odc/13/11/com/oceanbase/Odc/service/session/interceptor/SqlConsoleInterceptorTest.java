import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private ConnectionSession session;
    private Map<String, Object> context;

    @Before
    public void setup() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        request = mock(SqlAsyncExecuteReq.class);
        response = mock(SqlAsyncExecuteResp.class);
        session = mock(ConnectionSession.class);
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle_Success() {
        // Setup
        when(request.getQueryLimit()).thenReturn(null);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        context.put("SQL_CHECK_INTERCEPTED", true);

        // Test
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        // Verify
        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_Failure() {
        // Setup
        when(request.getQueryLimit()).thenReturn(null);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        context.put("NEED_SQL_CONSOLE_CHECK", true);

        // Test
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        // Verify
        assertFalse(result);
    }

    @Test
    public void testDoAfterCompletion_AllowExportTrue() {
        // Setup
        SqlExecuteResult executeResult = new SqlExecuteResult();
        executeResult.setStatus(SqlExecuteStatus.SUCCESS);

        // Test
        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, context);

        // Verify
        assertTrue(executeResult.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_AllowExportFalse() {
        // Setup
        SqlExecuteResult executeResult = new SqlExecuteResult();
        executeResult.setStatus(SqlExecuteStatus.SUCCESS);
        context.put("NOT_ALLOWED_EXPORT_RESULTSET", true);

        // Test
        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, context);

        // Verify
        assertFalse(executeResult.isAllowExport());
    }
}