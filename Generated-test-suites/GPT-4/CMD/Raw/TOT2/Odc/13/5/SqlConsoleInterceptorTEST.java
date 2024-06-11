import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.Before;
import java.util.*;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

public class SqlConsoleInterceptorTest {

    @InjectMocks
    SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    ConnectionSession session;
    @Mock
    SqlAsyncExecuteReq request;
    @Mock
    SqlAsyncExecuteResp response;

    private Map<String, Object> context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = new HashMap<>();
    }

    @Test
    public void doPreHandleTest() {
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void doPreHandleNullTest() {
        sqlConsoleInterceptor.doPreHandle(null, null, null, null);
    }

    @Test
    public void doAfterCompletionTest() {
        SqlExecuteResult result = new SqlExecuteResult();
        sqlConsoleInterceptor.doAfterCompletion(result, session, context);
        assertTrue(result.isAllowExport());
    }

    @Test(expected = NullPointerException.class)
    public void doAfterCompletionNullTest() {
        sqlConsoleInterceptor.doAfterCompletion(null, null, null);
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        DialectType dialectType = mock(DialectType.class);
        BasicResult result = (BasicResult)ReflectionTestUtils.invokeMethod(sqlConsoleInterceptor, "determineSqlType", sqlTuple, dialectType);
        assertEquals(SqlType.OTHERS, result.getSqlType());
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }
}