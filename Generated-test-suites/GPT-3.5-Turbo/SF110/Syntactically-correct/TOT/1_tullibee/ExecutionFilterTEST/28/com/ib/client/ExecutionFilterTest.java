import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testConstructorWithNoParameters() {
        ExecutionFilter filter = new ExecutionFilter();
        
        assertEquals(0, filter.m_clientId);
    }
    
    @Test
    public void testConstructorWithParameters() {
        ExecutionFilter filter = new ExecutionFilter(1, "acct123", "12:00", "AAPL", "STK", "NYSE", "BUY");
        
        assertEquals(1, filter.m_clientId);
        assertEquals("acct123", filter.m_acctCode);
        assertEquals("12:00", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testEqualsWithEqualExecutionFilters() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct123", "12:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct123", "12:00", "AAPL", "STK", "NYSE", "BUY");
        
        assertTrue(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct123", "12:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "acct123", "12:00", "AAPL", "STK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithNullObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "acct123", "12:00", "AAPL", "STK", "NYSE", "BUY");
        
        assertFalse(filter.equals(null));
    }
    
    @Test
    public void testEqualsWithSameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "acct123", "12:00", "AAPL", "STK", "NYSE", "BUY");
        
        assertTrue(filter.equals(filter));
    }
    
    @Test
    public void testEqualsWithDifferentClassObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "acct123", "12:00", "AAPL", "STK", "NYSE", "BUY");
        
        assertFalse(filter.equals(new String("testing")));
    }
    
    @Test
    public void testEqualsWithDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct123", "12:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct456", "12:00", "AAPL", "STK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct123", "12:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct123", "13:00", "AAPL", "STK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct123", "12:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct123", "12:00", "MSFT", "STK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct123", "12:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct123", "12:00", "AAPL", "OPT", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct123", "12:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct123", "12:00", "AAPL", "STK", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct123", "12:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct123", "12:00", "AAPL", "STK", "NYSE", "SELL");
        
        assertFalse(filter1.equals(filter2));
    }
}