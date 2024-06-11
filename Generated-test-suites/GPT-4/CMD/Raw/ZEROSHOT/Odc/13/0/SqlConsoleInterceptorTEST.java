import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.core.shared.constant.OrganizationType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private SqlAsyncExecuteReq sqlAsyncExecuteReq;

    @Mock
    private SqlAsyncExecuteResp sqlAsyncExecuteResp;

    @Mock
    private SqlExecuteResult sqlExecuteResult;

    @InjectMocks
    private SqlConsoleInterceptor interceptor;

    private Map<String, Object> context;

    @Before
    public void setUp() {
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        when(interceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context)).thenReturn(true);
        boolean result = interceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context);
        assertTrue(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String result = interceptor.getExecuteStageName();
        assertEquals("SQL_CONSOLE_RULE", result);
    }

    @Test
    public void testDoAfterCompletion() {
        setupOrganizationType();
        interceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);
        verify(connectionSession, times(1)).getOrganizationType();
    }

    @Test
    public void testGetOrder() {
        int result = interceptor.getOrder();
        assertEquals(2, result);
    }

    private void setupOrganizationType() {
        when(connectionSession.getOrganizationType())
                .thenReturn(OrganizationType.INDIVIDUAL);
    }
}