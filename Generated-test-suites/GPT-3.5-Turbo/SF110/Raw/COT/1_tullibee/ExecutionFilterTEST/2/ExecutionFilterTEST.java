import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {
    private ExecutionFilter filter;
    
    @Before
    public void setUp() {
        filter = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
    }
    
    @Test
    public void testConstructor() {
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT", filter.m_acctCode);
        assertEquals("2020-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NASDAQ", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertEquals(filter, filter1);
        assertNotEquals(filter, filter2);
        assertNotEquals(filter, filter3);
    }
}