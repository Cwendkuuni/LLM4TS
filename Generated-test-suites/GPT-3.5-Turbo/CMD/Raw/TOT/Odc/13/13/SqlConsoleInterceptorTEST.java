import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle() {
        // Typical use case - ruleSetId and context present
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();
        context.put("NEED_SQL_CONSOLE_CHECK", true);

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        
        assertEquals(true, result);
    }

    @Test
    public void testDoAfterCompletion() {
        // Typical use case - resultSetMetaData editable
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        ConnectionSession session = mock(ConnectionSession.class);

        when(response.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);

        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

        assertEquals(true, response.isAllowExport());
    }

    @Test
    public void testDetermineSqlType() {
        // Error scenario - SyntaxErrorException
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(null);
        sqlTuple.setOriginalSql("SELECT * FROM table");

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        assertEquals(SqlType.UNKNOWN, result.getSqlType());
        assertEquals(true, result.isSyntaxError());
    }
}