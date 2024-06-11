import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.interceptor.*;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext.xml" })
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private ConnectionSession session;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();

        // Typical Use Case
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        boolean result1 = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result1);

        // Edge Case
        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, false);
        boolean result2 = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result2);

        // Error Scenario
        request = null;
        boolean result3 = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertFalse(result3);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();

        // Typical Use Case
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());
        assertTrue(response.isAllowExport());
        
        // Error Scenario
        when(session.getDialectType()).thenReturn(null);
        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());
        assertFalse(response.isAllowExport());
    }

}