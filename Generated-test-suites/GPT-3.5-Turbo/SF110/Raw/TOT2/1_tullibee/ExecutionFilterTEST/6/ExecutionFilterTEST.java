import static org.junit.Assert.*;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testDefaultConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
    }

    @Test
    public void testParameterizedConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT", "9:00 AM", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT", filter.m_acctCode);
        assertEquals("9:00 AM", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testEqualsSameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT", "9:00 AM", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter.equals(filter));
    }

    @Test
    public void testEqualsNullObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT", "9:00 AM", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter.equals(null));
    }

    @Test
    public void testEqualsDifferentObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "9:00 AM", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT", "9:00 AM", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsDifferentTypes() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT", "9:00 AM", "AAPL", "STOCK", "NYSE", "BUY");
        Object obj = new Object();
        assertFalse(filter.equals(obj));
    }

}