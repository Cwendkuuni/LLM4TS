
package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;
    
    @Mock
    private ConnectionSession connectionSession;
    
    @Mock
    private SqlAsyncExecuteReq sqlAsyncExecuteReq;

    @Mock
    private SqlAsyncExecuteResp sqlAsyncExecuteResp;

    @Mock
    private AuthenticationFacade authenticationFacade;

    private Map<String, Object> context;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        boolean actualResult = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context);
        assertEquals(true, actualResult);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);
        assertNull(sqlExecuteResult.getResultSetMetaData());
        assertEquals(false, sqlExecuteResult.getAllowExport());
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }

    // Write additional test methods as needed
}
