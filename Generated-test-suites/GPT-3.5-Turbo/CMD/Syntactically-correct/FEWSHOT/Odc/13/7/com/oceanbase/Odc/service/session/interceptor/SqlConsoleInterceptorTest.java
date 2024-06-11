
package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor interceptor;
    private ConnectionSession session;

    @Before
    public void setup() {
        interceptor = new SqlConsoleInterceptor();
        session = new ConnectionSession();
        session.setDialectType(DialectType.ORACLE);
    }

    @Test
    public void testHandle() {
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        sqlTuple.setSqlTuple(new SqlTuple());
        sqlTuple.getSqlTuple().setSqlId("1");
        sqlTuple.getSqlTuple().setOriginalSql("SELECT * FROM table1");
        Map<String, Object> context = new HashMap<>();
        boolean result = interceptor.handle(sqlTuple, session, context);
        assertTrue(result);

        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
        result = interceptor.handle(sqlTuple, session, context);
        assertTrue(result);

        context.put(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED, true);
        result = interceptor.handle(sqlTuple, session, context);
        assertTrue(result);

        context.remove(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED);
        result = interceptor.handle(sqlTuple, session, context);
        assertTrue(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String stageName = interceptor.getExecuteStageName();
        assertEquals("SQL_CONSOLE_RULE", stageName);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        interceptor.doAfterCompletion(response, session, new HashMap<>());
        assertTrue(response.isAllowExport());

        response.setStatus(SqlExecuteStatus.FAILURE);
        interceptor.doAfterCompletion(response, session, new HashMap<>());
        assertFalse(response.isAllowExport());
    }

    @Test
    public void testDoPreHandle() {
        SqlTuplesWithViolation sqlTuple = new SqlTuplesWithViolation();
        sqlTuple.setSqlTuple(new SqlTuple());
        sqlTuple.getSqlTuple().setSqlId("1");
        sqlTuple.getSqlTuple().setOriginalSql("SELECT * FROM table1");
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.getSqls().add(sqlTuple);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        boolean result = interceptor.doPreHandle(request, response, session, context);
        assertTrue(result);
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, interceptor.getOrder());
    }
}
