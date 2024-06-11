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
        int clientId = 1;
        String acctCode = "ACCT001";
        String time = "2020-01-01";
        String symbol = "AAPL";
        String secType = "STK";
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
        assertFalse(filter.equals("test"));
    }
    
    @Test
    public void testEqualsEqualObject() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT001", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT001", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertTrue(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsNotEqualObject() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT001", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT002", "2020-01-02", "GOOG", "OPT", "NYSE", "SELL");
        assertFalse(filter1.equals(filter2));
    }
}