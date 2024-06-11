import org.junit.Test;

public class ExecutionFilterTest {
    ExecutionFilter executionFilter;
    
    @Test
    public void testExecutionFilterConstructor() {
        executionFilter = new ExecutionFilter(1, "ACCT001", "09:00:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertNotEquals(null, executionFilter);
        assertEquals(1, executionFilter.m_clientId);
        assertEquals("ACCT001", executionFilter.m_acctCode);
        assertEquals("09:00:00", executionFilter.m_time);
        assertEquals("AAPL", executionFilter.m_symbol);
        assertEquals("STOCK", executionFilter.m_secType);
        assertEquals("NASDAQ", executionFilter.m_exchange);
        assertEquals("BUY", executionFilter.m_side);
    }
    
    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT001", "09:00:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(2, "ACCT002", "10:00:00", "MSFT", "STOCK", "NYSE", "SELL");
        ExecutionFilter executionFilter3 = null;
        
        assertTrue(executionFilter1.equals(executionFilter1));
        assertTrue(executionFilter1.equals(new ExecutionFilter(1, "ACCT001", "09:00:00", "AAPL", "STOCK", "NASDAQ", "BUY")));
        assertFalse(executionFilter1.equals(executionFilter2));
        assertFalse(executionFilter1.equals(executionFilter3));
    }
}