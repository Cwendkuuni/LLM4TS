
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {
    
    @Test
    public void testDefaultConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
    }
    
    @Test
    public void testParameterizedConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT", filter.m_acctCode);
        assertEquals("2020-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STK", filter.m_secType);
        assertEquals("NASDAQ", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        
        // Test equal filter
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertTrue(filter1.equals(filter2));
        
        // Test unequal filters
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter3));
        ExecutionFilter filter4 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "OPT", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter4));
        ExecutionFilter filter5 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "IBIS", "BUY");
        assertFalse(filter1.equals(filter5));
        ExecutionFilter filter6 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "NASDAQ", "SELL");
        assertFalse(filter1.equals(filter6));
    }
    
}
