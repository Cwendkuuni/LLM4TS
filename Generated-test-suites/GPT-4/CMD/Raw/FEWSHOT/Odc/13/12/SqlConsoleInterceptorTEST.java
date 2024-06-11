
package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

public class SqlConsoleInterceptorTest {
    private SqlConsoleInterceptor interceptor;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    
    @Before
    public void setup() {
        ruleService = Mockito.mock(RuleService.class);
        sqlConsoleRuleService = Mockito.mock(SqlConsoleRuleService.class);
        interceptor = new SqlConsoleInterceptor();
        interceptor.setRuleService(ruleService);
        interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }
    
    @Test
    public void testDoPreHandle() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
        
        boolean result = interceptor.doPreHandle(request, response, session, context);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testHandle() throws Exception {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        
        List<SqlTuple> tuples = new ArrayList<>();
        tuples.add(new SqlTuple());
        response.setSqls(tuples);
        
        Mockito.when(sqlConsoleRuleService.getProperties(Mockito.anyLong(), Mockito.any(), Mockito.any(), Mockito.any()))
               .thenReturn(Optional.of(10));
               
        
        boolean result = interceptor.handle(request, response, session, context);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testDetermineSqlType() {
        SqlTuple tuple = new SqlTuple();
        tuple.setOriginalSql("SELECT * FROM test;");
        
        BasicResult result = interceptor.determineSqlType(tuple, DialectType.MYSQL);
        
        assertEquals(SqlType.SELECT, result.getSqlType());
    }
    
    @Test(expected = SyntaxErrorException.class)
    public void testDetermineSqlTypeWithException() {
        SqlTuple tuple = new SqlTuple();
        tuple.setOriginalSql("SELECT * FORM test;"); // intentional typo to cause parsing error
        
        interceptor.determineSqlType(tuple, DialectType.MYSQL);
    }
    
    @Test
    public void testGetOrder() {
        int order = interceptor.getOrder();
        assertEquals(2, order);
    }
}
