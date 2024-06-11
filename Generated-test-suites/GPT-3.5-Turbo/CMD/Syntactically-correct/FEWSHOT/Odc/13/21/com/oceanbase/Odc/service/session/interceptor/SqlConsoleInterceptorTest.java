package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor interceptor;

    @Before
    public void setup() {
        interceptor = new SqlConsoleInterceptor();
        interceptor.authenticationFacade = mock(AuthenticationFacade.class);
        interceptor.ruleService = mock(RuleService.class);
        interceptor.sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
    }

    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        
        boolean result = interceptor.doPreHandle(request, response, session, context);
        
        assertNotNull(result);
    }

    @Test
    public void testHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        
        boolean result = interceptor.handle(request, response, session, context);
        
        assertNotNull(result);
    }

    @Test
    public void testGetExecuteStageName() {
        String stageName = interceptor.getExecuteStageName();
        
        assertEquals("SQL_CONSOLE_RULE", stageName);
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        
        interceptor.doAfterCompletion(response, session, context);
        
        assertNotNull(response);
    }

    @Test
    public void testIsIndividualTeam() {
        boolean isIndividual = interceptor.isIndividualTeam();
        
        assertNotNull(isIndividual);
    }

    @Test
    public void testDetermineSqlType() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setAst(null);
        sqlTuple.setSqlId("1");
        sqlTuple.setOriginalSql("SELECT * FROM table");
        sqlTuple.setOffset(0);
        
        BasicResult result = interceptor.determineSqlType(sqlTuple, DialectType.ORACLE);
        
        assertNotNull(result);
    }

    @Test
    public void testGetOrder() {
        int order = interceptor.getOrder();
        
        assertEquals(2, order);
    }
}