import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testEmptyConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
    }
    
    @Test
    public void testConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT123", filter.m_acctCode);
        assertEquals("10:00", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testEquals_sameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter.equals(filter));
    }
    
    @Test
    public void testEquals_nullObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter.equals(null));
    }
    
    @Test
    public void testEquals_differentClass() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter.equals("ACCT123"));
    }
    
    @Test
    public void testEquals_sameValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_differentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_differentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT456", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_differentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "11:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_differentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "10:00", "IBM", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_differentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "FUTURE", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_differentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_differentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "SELL");
        assertFalse(filter1.equals(filter2));
    }
    
}