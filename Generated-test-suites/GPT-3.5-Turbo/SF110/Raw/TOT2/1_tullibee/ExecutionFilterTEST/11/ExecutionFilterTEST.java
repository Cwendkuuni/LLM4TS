import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {
    private ExecutionFilter filter;
    
    @Before
    public void setUp() {
        filter = new ExecutionFilter(123, "ACCT123", "12:00", "AAPL", "STOCK", "NASDAQ", "BUY");
    }
    
    @Test
    public void testConstructor() {
        assertEquals(123, filter.m_clientId);
        assertEquals("ACCT123", filter.m_acctCode);
        assertEquals("12:00", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NASDAQ", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testEquals_SameObject() {
        assertTrue(filter.equals(filter));
    }
    
    @Test
    public void testEquals_NullObject() {
        assertFalse(filter.equals(null));
    }
    
    @Test
    public void testEquals_DifferentObject() {
        ExecutionFilter otherFilter = new ExecutionFilter(123, "ACCT123", "12:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertTrue(filter.equals(otherFilter));
    }
    
    @Test
    public void testEquals_DifferentValues() {
        ExecutionFilter otherFilter = new ExecutionFilter(456, "ACCT456", "13:00", "GOOGL", "STOCK", "NYSE", "SELL");
        assertFalse(filter.equals(otherFilter));
    }
}