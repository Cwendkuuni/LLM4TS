
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {
    
    @Test
    public void testDefaultConstructor() {
        ExecutionFilter ef = new ExecutionFilter();
        assertEquals(0, ef.m_clientId);
        assertNull(ef.m_acctCode);
        assertNull(ef.m_time);
        assertNull(ef.m_symbol);
        assertNull(ef.m_secType);
        assertNull(ef.m_exchange);
        assertNull(ef.m_side);
    }
    
    @Test
    public void testParameterizedConstructor() {
        ExecutionFilter ef = new ExecutionFilter(1, "ACC001", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(1, ef.m_clientId);
        assertEquals("ACC001", ef.m_acctCode);
        assertEquals("2021-01-01", ef.m_time);
        assertEquals("AAPL", ef.m_symbol);
        assertEquals("STOCK", ef.m_secType);
        assertEquals("NYSE", ef.m_exchange);
        assertEquals("BUY", ef.m_side);
    }
    
    @Test
    public void testEquals() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACC001", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(1, "ACC001", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter ef3 = new ExecutionFilter(2, "ACC002", "2021-01-02", "GOOG", "STOCK", "NASDAQ", "SELL");
        
        assertTrue(ef1.equals(ef1));
        assertTrue(ef1.equals(ef2));
        assertFalse(ef1.equals(null));
        assertFalse(ef1.equals(""));
        assertFalse(ef1.equals(ef3));
    }
}
