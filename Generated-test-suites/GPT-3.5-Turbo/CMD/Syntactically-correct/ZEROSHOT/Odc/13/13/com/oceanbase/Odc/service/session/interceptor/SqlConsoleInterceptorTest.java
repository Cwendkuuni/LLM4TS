import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private ConnectionSession session;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;
    private Map<String, Object> context;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        session = mock(ConnectionSession.class);
        request = mock(SqlAsyncExecuteReq.class);
        response = mock(SqlAsyncExecuteResp.class);
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandleWithNullRuleSetId() {
        when(session.getDialectType()).thenReturn(DialectType.ORACLE);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDoPreHandleWithIndividualTeam() {
        when(session.getDialectType()).thenReturn(DialectType.ORACLE);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(123L);
        when(sqlConsoleInterceptor.isIndividualTeam()).thenReturn(true);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testDoPreHandleWithFalseNeedCheck() {
        when(session.getDialectType()).thenReturn(DialectType.ORACLE);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(123L);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testHandle() {
        // To be implemented
    }

    @Test
    public void testGetExecuteStageName() {
        String stageName = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals("SQL_CONSOLE_RULE", stageName);
    }

    @Test
    public void testDoAfterCompletionWithSuccessStatus() {
        SqlExecuteResult executeResult = mock(SqlExecuteResult.class);
        when(executeResult.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);
        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, context);
        // Assert expected behavior
    }

    @Test
    public void testDoAfterCompletionWithIndividualTeam() {
        when(sqlConsoleInterceptor.isIndividualTeam()).thenReturn(true);
        SqlExecuteResult executeResult = mock(SqlExecuteResult.class);
        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, context);
        // Assert expected behavior
    }

    @Test
    public void testDoAfterCompletionWithNullRuleSetId() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        SqlExecuteResult executeResult = mock(SqlExecuteResult.class);
        sqlConsoleInterceptor.doAfterCompletion(executeResult, session, context);
        // Assert expected behavior
    }

    @Test
    public void testIsIndividualTeam() {
        // To be implemented
    }

    @Test
    public void testDetermineSqlType() {
        // To be implemented
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }
}