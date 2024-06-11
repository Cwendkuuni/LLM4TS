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
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT123", filter.m_acctCode);
        assertEquals("2022-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testEqualsMethod_sameObject() {
        ExecutionFilter filter = new ExecutionFilter();
        assertTrue(filter.equals(filter));
    }
    
    @Test
    public void testEqualsMethod_equalObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsMethod_notEqualObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test(expected = ClassCastException.class)
    public void testEqualsMethod_classCastException() {
        ExecutionFilter filter = new ExecutionFilter();
        filter.equals(new Object());
    }
}