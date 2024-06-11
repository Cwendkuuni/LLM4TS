import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterDefaultConstructor() {
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
    public void testExecutionFilterParameterizedConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "11:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT123", filter.m_acctCode);
        assertEquals("11:00", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testEqualsWithEqualObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "11:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "11:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "11:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT456", "12:00", "GOOGL", "STOCK", "NASDAQ", "SELL");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithNullObject() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(null));
    }

    @Test
    public void testEqualsWithSameObject() {
        ExecutionFilter filter = new ExecutionFilter();
        assertTrue(filter.equals(filter));
    }

}