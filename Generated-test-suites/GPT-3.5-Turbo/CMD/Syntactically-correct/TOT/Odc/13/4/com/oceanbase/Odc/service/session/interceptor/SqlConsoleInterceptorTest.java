import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private SqlAsyncExecuteReq request;
    @Mock
    private SqlAsyncExecuteResp response;
    @Mock
    private ConnectionSession session;
    private Map<String, Object> context;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        request = new SqlAsyncExecuteReq();
        response = new SqlAsyncExecuteResp();
        session = new ConnectionSession();
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        // Typical use case: Set NEED_SQL_CONSOLE_CHECK to true
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));

        // Edge case: Set NEED_SQL_CONSOLE_CHECK to false
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
        assertTrue(sqlConsoleInterceptor.doPreHandle(request, response, session, context));

        // Error scenario: No ruleSetId available
        ConnectionSessionUtil.setRuleSetId(session, null);
        assertFalse(sqlConsoleInterceptor.doPreHandle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletion() {
        // Typical use case: ResultSetMetaData is editable
        SqlExecuteResult successResponse = new SqlExecuteResult(SqlExecuteStatus.SUCCESS);
        successResponse.setResultSetMetaData(new ResultSetMetaData());
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET), eq(session))).thenReturn(false);
        sqlConsoleInterceptor.doAfterCompletion(successResponse, session, context);
        assertTrue(successResponse.isAllowExport());
        assertTrue(successResponse.getResultSetMetaData().isEditable());

        // Edge case: ResultSetMetaData is not editable
        SqlExecuteResult errorResponse = new SqlExecuteResult(SqlExecuteStatus.ERROR);
        errorResponse.setResultSetMetaData(new ResultSetMetaData());
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET), eq(session))).thenReturn(true);
        sqlConsoleInterceptor.doAfterCompletion(errorResponse, session, context);
        assertFalse(errorResponse.getResultSetMetaData().isEditable());

        // Error scenario: NOT_ALLOWED_EXPORT_RESULTSET rule is forbidden
        when(sqlConsoleRuleService.isForbidden(eq(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET), eq(session))).thenReturn(true);
        sqlConsoleInterceptor.doAfterCompletion(successResponse, session, context);
        assertFalse(successResponse.isAllowExport());
    }

}