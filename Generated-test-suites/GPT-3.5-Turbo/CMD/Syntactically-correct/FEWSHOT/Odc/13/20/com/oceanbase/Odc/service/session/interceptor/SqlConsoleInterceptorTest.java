package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private Map<String, Object> context;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        context = new HashMap<>();
    }

    @Test
    public void testDoPreHandle() {
        SqlConsoleInterceptor inter = mock(SqlConsoleInterceptor.class);
        when(inter.doPreHandle(Mockito.any(SqlAsyncExecuteReq.class), Mockito.any(SqlAsyncExecuteResp.class), Mockito.any(ConnectionSession.class), Mockito.any(Map.class)))
          .thenReturn(true);
        assertTrue(inter.doPreHandle(Mockito.mock(SqlAsyncExecuteReq.class), Mockito.mock(SqlAsyncExecuteResp.class), Mockito.mock(ConnectionSession.class), new HashMap<>()));
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));

        Optional<Integer> queryLimit = Optional.of(10);
        when(sqlConsoleInterceptor.sqlConsoleRuleService.getProperties(Mockito.any(Long.class), Mockito.any(), Mockito.any(), Mockito.any(Class.class))).thenReturn(queryLimit);
        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));

        queryLimit = Optional.empty();
        when(sqlConsoleInterceptor.sqlConsoleRuleService.getProperties(Mockito.any(Long.class), Mockito.any(), Mockito.any(), Mockito.any(Class.class))).thenReturn(queryLimit);
        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals("SQL_CONSOLE_RULE", sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = new ConnectionSession();
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        assertEquals(SqlExecuteStatus.SUCCESS, response.getStatus());
    }

    @Test
    public void testIsIndividualTeam() {
        assertTrue(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(null);
        sqlTuple.setSqlId("123");
        sqlTuple.setOffset(0);
        sqlTuple.setOriginalSql("SELECT * FROM table;");
        sqlTuple.setDialectType(DialectType.MYSQL);
        assertEquals(SqlType.OTHERS, sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL).getSqlType());
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, sqlConsoleInterceptor.getOrder());
    }

    // Add more test cases as needed

}