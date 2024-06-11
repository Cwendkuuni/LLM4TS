import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private ConnectionSession session;

    @Test
    public void testDoPreHandle_Success() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        
        assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion_Success() {
        SqlExecuteResult response = new SqlExecuteResult(SqlExecuteStatus.SUCCESS);
        Map<String, Object> context = new HashMap<>();
        
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        
        assertTrue(response.isAllowExport());
    }

    // Add more test cases for other methods in SqlConsoleInterceptor class 

}