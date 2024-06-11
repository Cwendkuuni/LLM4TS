
package com.oceanbase.odc.service.session.interceptor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;

public class SqlConsoleInterceptorTest {

    @Mock
    private ConnectionSession connectionSession;
    
    @Mock
    private SqlAsyncExecuteReq request;

    @Mock
    private SqlAsyncExecuteResp response;

    @Mock
    private SqlExecuteResult sqlExecuteResult;

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;
    
    private Map<String, Object> context;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        context = new HashMap<>();
    }

    @AfterEach
    public void cleanUp() {
    }

    @Test
    public void testDoPreHandle() {

        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response,
                connectionSession, context);

        Assertions.assertTrue(result);
    }

    @Test
    public void testDoAfterCompletion() {
        when(sqlExecuteResult.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);

        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult,
                connectionSession, context);

        verify(sqlExecuteResult).setStatus(SqlExecuteStatus.SUCCESS);
    }

    @Test
    public void testDoPreHandle_WithFalseNeedSqlConsoleCheck() {

        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response,
                connectionSession, context);

        Assertions.assertTrue(result);
    }

    @Test
    public void testGetOrder() {
        Assertions.assertEquals(2, sqlConsoleInterceptor.getOrder());
    }

}
