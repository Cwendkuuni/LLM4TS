
import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testParametrizedConstructor() {
        int clientId = 1;
        String acctCode = "ACCT001";
        String time = "2021-01-01";
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
    public void testEqualsMethod() {
        int clientId = 1;
        String acctCode = "ACCT001";
        String time = "2021-01-01";
        String symbol = "AAPL";
        String secType = "STOCK";
        String exchange = "NASDAQ";
        String side = "BUY";
        
        ExecutionFilter filter1 = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
        ExecutionFilter filter2 = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
        ExecutionFilter filter3 = new ExecutionFilter();
        
        assertTrue(filter1.equals(filter2));
        assertTrue(filter2.equals(filter1));
        assertFalse(filter1.equals(filter3));
    }
}
