import static org.junit.Assert.*;

import org.junit.Test;

public class ExecutionFilterTest {
    
    @Test
    public void testDefaultConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        
        assertEquals(0, filter.m_clientId);
        assertNull(filter.m_acctCode);
        assertNull(filter.m_time);
        assertNull(filter.m_symbol);
        assertNull(filter.m_secType);
        assertNull(filter.m_exchange);
        assertNull(filter.m_side);
    }
    
    @Test
    public void testParameterizedConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT_001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT_001", filter.m_acctCode);
        assertEquals("10:00", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testEqualsSameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT_001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertTrue(filter.equals(filter));
    }
    
    @Test
    public void testEqualsNullObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT_001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter.equals(null));
    }
    
    @Test
    public void testEqualsDifferentObject() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT_001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT_002", "11:00", "GOOG", "STOCK", "NASDAQ", "SELL");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsEqualObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT_001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT_001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertTrue(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT_001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT_001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT_001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT_002", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT_001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT_001", "11:00", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT_001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT_001", "10:00", "GOOG", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT_001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT_001", "10:00", "AAPL", "OPTION", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT_001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT_001", "10:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT_001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT_001", "10:00", "AAPL", "STOCK", "NYSE", "SELL");
        
        assertFalse(filter1.equals(filter2));
    }
}