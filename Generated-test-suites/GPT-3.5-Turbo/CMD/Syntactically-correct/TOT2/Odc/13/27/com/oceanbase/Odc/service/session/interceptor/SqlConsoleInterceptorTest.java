import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
    }

    @Test
    public void testDoPreHandle() {
        // Test when ruleSetId is null
        // Set up
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        
        // Execution
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        
        // Assertion
        assertTrue(result);
        
        // Test when ruleSetId is not null and context does not contain the key NEED_SQL_CONSOLE_CHECK
        // Set up
        session.setDialectType(DialectType.ORACLE);
        session.setRuleSetId(1L);
        
        // Execution
        result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        
        // Assertion
        assertTrue(result);
        
        // Test when ruleSetId is not null, context contains the key NEED_SQL_CONSOLE_CHECK and it is set to false
        // Set up
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
        
        // Execution
        result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
        
        // Assertion
        assertTrue(result);
        
        // Add more test cases as needed for different scenarios and edge cases
    }

    @Test
    public void testDoAfterCompletion() {
        // Test when response status is not SUCCESS
        // Set up
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.FAILED);
        ConnectionSession session = new ConnectionSession();
        Map<String, Object> context = new HashMap<>();
        
        // Execution
        sqlConsoleInterceptor.doAfterCompletion(response, session, context);
        
        // Assertion
        // Add assertions as needed
        
        // Test for other scenarios and edge cases
    }

    // Add more test cases for other methods in the SqlConsoleInterceptor class

}