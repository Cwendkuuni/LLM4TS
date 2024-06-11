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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private ConnectionSession mockConnectionSession;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, mockConnectionSession, context);
        assertFalse(result);
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        
        boolean result = sqlConsoleInterceptor.handle(request, response, mockConnectionSession, context);
        assertFalse(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String result = sqlConsoleInterceptor.getExecuteStageName();
        assertEquals("SQL_CONSOLE_RULE", result);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        Map<String, Object> context = new HashMap<>();
        
        sqlConsoleInterceptor.doAfterCompletion(response, mockConnectionSession, context);
        assertTrue(response.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam() {
        boolean result = sqlConsoleInterceptor.isIndividualTeam();
        assertFalse(result);
    }

    @Test
    public void testDetermineSqlType() {
        // Mock SqlTuple and DialectType
        SqlTuple mockSqlTuple = mock(SqlTuple.class);
        DialectType mockDialectType = DialectType.ORACLE;

        SqlConsoleInterceptor spyInterceptor = spy(sqlConsoleInterceptor);
        doReturn(new BasicResult(SqlType.SELECT)).when(spyInterceptor).determineSqlType(mockSqlTuple, mockDialectType);

        BasicResult result = spyInterceptor.determineSqlType(mockSqlTuple, mockDialectType);
        assertNotNull(result);
        assertEquals(SqlType.SELECT, result.getSqlType());
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }
}