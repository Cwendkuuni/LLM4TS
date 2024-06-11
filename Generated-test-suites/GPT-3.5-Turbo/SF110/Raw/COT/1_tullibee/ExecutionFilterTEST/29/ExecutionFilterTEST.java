import static org.junit.Assert.*;
import org.junit.Test;

public class ExecutionFilterTest {

    // Test case for default constructor
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

    // Test case for parameterized constructor
    @Test
    public void testParameterizedConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT", "12:00", "AAPL", "STK", "NYSE", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT", filter.m_acctCode);
        assertEquals("12:00", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    // Test case for equals method when the objects are equal
    @Test
    public void testEqualsEqualObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "12:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "12:00", "AAPL", "STK", "NYSE", "BUY");
        assertTrue(filter1.equals(filter2));
    }

    // Test case for equals method when the objects are not equal
    @Test
    public void testEqualsNotEqualObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "12:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT2", "12:00", "AAPL", "STK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    // Test case for equals method when the object is null
    @Test
    public void testEqualsNullObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT", "12:00", "AAPL", "STK", "NYSE", "BUY");
        assertFalse(filter.equals(null));
    }

    // Test case for equals method when comparing with itself
    @Test
    public void testEqualsSameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT", "12:00", "AAPL", "STK", "NYSE", "BUY");
        assertTrue(filter.equals(filter));
    }

}