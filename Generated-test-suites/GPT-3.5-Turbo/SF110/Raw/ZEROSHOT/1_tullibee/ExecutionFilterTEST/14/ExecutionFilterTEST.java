import org.junit.*;
import static org.junit.Assert.*;

public class ExecutionFilterTest {
    
    @Test
    public void testConstructorWithArguments() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT-001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT-001", filter.m_acctCode);
        assertEquals("2021-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NASDAQ", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testConstructorWithDefault() {
        ExecutionFilter filter = new ExecutionFilter();
        
        assertEquals(0, filter.m_clientId);
    }
    
    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT-001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT-001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertTrue(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithNull() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT-001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter.equals(null));
    }
    
    @Test
    public void testEqualsWithSameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT-001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertTrue(filter.equals(filter));
    }
    
    @Test
    public void testEqualsWithDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT-001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT-001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT-001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT-002", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT-001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT-001", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT-001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT-001", "2021-01-01", "MSFT", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT-001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT-001", "2021-01-01", "AAPL", "FUTURE", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT-001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT-001", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT-001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT-001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "SELL");
        
        assertFalse(filter1.equals(filter2));
    }
}