import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle() {
        // Typical Use Case
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);

        // Edge Case
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);

        // Error Scenario
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, false);
        result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testDoAfterCompletion() {
        // Typical Use Case
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        response.setStatus(SqlExecuteStatus.SUCCESS);

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        assertTrue(response.isAllowExport());

        // Edge Case
        session.setOrganizationType(OrganizationType.INDIVIDUAL);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        assertTrue(response.isAllowExport());

        // Error Scenario
        session.setOrganizationType(OrganizationType.TEAM);
        session.setDialectType(DialectType.MYSQL);
        response.getResultSetMetaData().setEditable(true);
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        assertFalse(response.getResultSetMetaData().isEditable());
    }

}