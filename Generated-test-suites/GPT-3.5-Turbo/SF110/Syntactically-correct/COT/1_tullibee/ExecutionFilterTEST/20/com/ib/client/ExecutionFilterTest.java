import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {
    
    @Test
    public void testConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
        assertNull(filter.m_acctCode);
        assertNull(filter.m_time);
        assertNull(filter.m_symbol);
        assertNull(filter.m_secType);
        assertNull(filter.m_exchange);
        assertNull(filter.m_side);
        
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertEquals(1, filter2.m_clientId);
        assertEquals("ACCT001", filter2.m_acctCode);
        assertEquals("2021-01-01", filter2.m_time);
        assertEquals("AAPL", filter2.m_symbol);
        assertEquals("STOCK", filter2.m_secType);
        assertEquals("NASDAQ", filter2.m_exchange);
        assertEquals("BUY", filter2.m_side);
    }
    
    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT002", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "SELL");
        
        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
    }
}