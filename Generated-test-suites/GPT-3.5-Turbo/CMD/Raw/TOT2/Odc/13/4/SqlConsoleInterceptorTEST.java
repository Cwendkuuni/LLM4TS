import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    private ConnectionSession mockConnectionSession;

    @Mock
    private SqlAsyncExecuteReq mockSqlAsyncExecuteReq;

    @Mock
    private SqlAsyncExecuteResp mockSqlAsyncExecuteResp;

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setup() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle() {
        // Test if doPreHandle returns true for valid input
        boolean result = sqlConsoleInterceptor.doPreHandle(mockSqlAsyncExecuteReq, mockSqlAsyncExecuteResp, mockConnectionSession);
        assertTrue(result);
    }

    @Test
    public void testHandle() {
        // Test if handle method correctly handles the request and response
        boolean result = sqlConsoleInterceptor.handle(mockSqlAsyncExecuteReq, mockSqlAsyncExecuteResp, mockConnectionSession);
        assertTrue(result);
    }

    @Test
    public void testGetExecuteStageName() {
        // Test if getExecuteStageName returns the correct execute stage name
        String expectedStageName = "SQL_CONSOLE_RULE";
        String result = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals(expectedStageName, result);
    }

    @Test
    public void testDoAfterCompletion() {
        // Test if doAfterCompletion method behaves as expected
        sqlConsoleInterceptor.doAfterCompletion(mockSqlAsyncExecuteResp, mockConnectionSession);
        // Add assertions as needed
    }

    @Test
    public void testIsIndividualTeam() {
        // Test if isIndividualTeam method returns the correct result
        boolean result = sqlConsoleInterceptor.isIndividualTeam();
        // Add assertions as needed
    }

    @Test
    public void testDetermineSqlType() {
        // Test if determineSqlType method correctly determines the SQL type
        // Add assertions as needed
    }

    @Test
    public void testGetOrder() {
        // Test if getOrder method returns the correct order
        int expectedOrder = 2;
        int result = sqlConsoleInterceptor.getOrder();
        assertEquals(expectedOrder, result);
    }
}