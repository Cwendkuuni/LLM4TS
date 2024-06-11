import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private SqlAsyncExecuteReq mockRequest;
    @Mock
    private SqlAsyncExecuteResp mockResponse;
    @Mock
    private ConnectionSession mockSession;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testHandle() {
        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.doPreHandle(mockRequest, mockResponse, mockSession, context);
        assertFalse(result);
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals("SQL_CONSOLE_RULE", sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteStatus status = SqlExecuteStatus.SUCCESS;
        when(mockResponse.getStatus()).thenReturn(status);
        when(mockSession.getRuleSetId()).thenReturn(123L);
        
        sqlConsoleInterceptor.doAfterCompletion(mockResponse, mockSession, new HashMap<>());
        
        verify(mockResponse, times(1)).setAllowExport(anyBoolean());
    }

    @Test
    public void testIsIndividualTeam() {
        boolean result = sqlConsoleInterceptor.isIndividualTeam();
        assertFalse(result);
    }

    @Test
    public void testDetermineSqlType() {
        // Test when SQL AST is null
        when(mockSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(mockResponse.getSqls().get(0).getAst()).thenReturn(null);
        when(mockResponse.getSqls().get(0).getSqlId()).thenReturn("sqlId");
        
        sqlConsoleInterceptor.determineSqlType(mockResponse.getSqls().get(0), DialectType.MYSQL);
        
        assertNotNull(mockResponse.getSqls().get(0).getAst());
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }
}