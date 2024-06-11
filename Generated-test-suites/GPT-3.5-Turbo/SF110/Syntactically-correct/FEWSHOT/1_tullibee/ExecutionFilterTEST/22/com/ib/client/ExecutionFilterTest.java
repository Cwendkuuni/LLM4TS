import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
        assertEquals(null, filter.m_acctCode);
        assertEquals(null, filter.m_time);
        assertEquals(null, filter.m_symbol);
        assertEquals(null, filter.m_secType);
        assertEquals(null, filter.m_exchange);
        assertEquals(null, filter.m_side);

        ExecutionFilter filter2 = new ExecutionFilter(1, "ACC123", "2021-01-01", "IBM", "STOCK", "NYSE", "BUY");
        assertEquals(1, filter2.m_clientId);
        assertEquals("ACC123", filter2.m_acctCode);
        assertEquals("2021-01-01", filter2.m_time);
        assertEquals("IBM", filter2.m_symbol);
        assertEquals("STOCK", filter2.m_secType);
        assertEquals("NYSE", filter2.m_exchange);
        assertEquals("BUY", filter2.m_side);
    }

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACC123", "2021-01-01", "IBM", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACC123", "2021-01-01", "IBM", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACC123", "2021-01-01", "IBM", "STOCK", "NYSE", "BUY");

        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
    }
}