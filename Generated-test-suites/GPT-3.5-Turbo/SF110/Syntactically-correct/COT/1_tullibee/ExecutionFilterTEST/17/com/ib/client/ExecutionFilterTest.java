import static org.junit.Assert.*;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterDefaultConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
    }
    
    @Test
    public void testExecutionFilterParameterizedConstructor() {
        int clientId = 1;
        String acctCode = "ACCT";
        String time = "10:00";
        String symbol = "AAPL";
        String secType = "STOCK";
        String exchange = "NASDAQ";
        String side = "BUY";
        
        ExecutionFilter filter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
        
        assertEquals(clientId, filter.m_clientId);
        assertEquals(acctCode, filter.m_acctCode);
        assertEquals(time, filter.m_time);
        assertEquals(symbol, filter.m_symbol);
        assertEquals(secType, filter.m_secType);
        assertEquals(exchange, filter.m_exchange);
        assertEquals(side, filter.m_side);
    }
    
    @Test
    public void testEqualsSameObject() {
        ExecutionFilter filter = new ExecutionFilter();
        assertTrue(filter.equals(filter));
    }
    
    @Test
    public void testEqualsNullObject() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(new Object()));
    }
    
    @Test
    public void testEqualsSameAttributes() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "10:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "10:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertTrue(filter1.equals(filter2));
        assertTrue(filter2.equals(filter1));
    }
    
    @Test
    public void testEqualsDifferentAttributes() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "10:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT", "10:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
        assertFalse(filter2.equals(filter1));
    }
    
    @Test
    public void testEqualsIgnoreCase() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "10:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct", "10:00", "aapl", "stock", "nasdaq", "buy");
        assertTrue(filter1.equals(filter2));
        assertTrue(filter2.equals(filter1));
    }
}