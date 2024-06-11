//Import statements
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

//Test Class
public class SqlConsoleInterceptorTest {

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();

        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        boolean result = interceptor.doPreHandle(request, response, session, context);

        assertFalse(result);
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();

        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        boolean result = interceptor.handle(request, response, session, context);

        assertFalse(result);
    }

    @Test
    public void testGetExecuteStageName() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        String stageName = interceptor.getExecuteStageName();

        assertEquals("SQL_CONSOLE_RULE", stageName);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();

        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        interceptor.doAfterCompletion(response, session, context);

        assertTrue(response.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam() {
        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        boolean result = interceptor.isIndividualTeam();

        assertFalse(result);
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = new SqlTuple();
        DialectType dialectType = DialectType.POSTGRESQL;

        SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
        BasicResult result = interceptor.determineSqlType(sqlTuple, dialectType);

        assertEquals(SqlType.OTHERS, result.getSqlType());
    }

}