
package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;

import java.util.HashMap;
import java.util.Map;

public class SqlConsoleInterceptorTest {

    @Mock
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
        SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String result = sqlConsoleInterceptor.getExecuteStageName();
        assertTrue(result.equals(SqlExecuteStages.SQL_CONSOLE_RULE));
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = mock(SqlExecuteResult.class);
        ConnectionSession session = mock(ConnectionSession.class);
        Map<String, Object> context = new HashMap<>();

        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
    }

    @Test
    public void testIsIndividualTeam() {
        boolean result = sqlConsoleInterceptor.isIndividualTeam();
        assertTrue(result);
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = mock(SqlTuple.class);
        DialectType dialectType = DialectType.MYSQL;

        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);
        assertTrue(result.getSqlType().equals(SqlType.UNKNOWN));
    }

    @Test
    public void testGetOrder() {
        int result = sqlConsoleInterceptor.getOrder();
        assertTrue(result == 2);
    }
}
