package com.oceanbase.odc.service.session.interceptor;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setup() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        boolean result = sqlConsoleInterceptor.handle(request, response, session, context);
        assertFalse(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String executeStage = sqlConsoleInterceptor.getExecuteStageName();
        assertNotNull(executeStage);
        assertEquals("SQL_CONSOLE_RULE", executeStage);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        assertTrue(response.isAllowExport());
    }

    @Test
    public void testIsIndividualTeam() {
        boolean individual = sqlConsoleInterceptor.isIndividualTeam();
        assertFalse(individual);
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = new SqlTuple();
        DialectType dialectType = DialectType.MYSQL;
        BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);
        assertNotNull(result);
    }

    @Test
    public void testGetOrder() {
        int order = sqlConsoleInterceptor.getOrder();
        assertEquals(2, order);
    }
}