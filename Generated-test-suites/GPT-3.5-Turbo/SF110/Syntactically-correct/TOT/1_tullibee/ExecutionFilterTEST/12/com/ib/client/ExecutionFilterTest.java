import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCTCODE", "TIME", "SYMBOL", "SECTYPE", "EXCHANGE", "SIDE");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCTCODE", filter.m_acctCode);
        assertEquals("TIME", filter.m_time);
        assertEquals("SYMBOL", filter.m_symbol);
        assertEquals("SECTYPE", filter.m_secType);
        assertEquals("EXCHANGE", filter.m_exchange);
        assertEquals("SIDE", filter.m_side);
    }

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
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCTCODE", "TIME", "SYMBOL", "SECTYPE", "EXCHANGE", "SIDE");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCTCODE", "TIME", "SYMBOL", "SECTYPE", "EXCHANGE", "SIDE");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCTCODE", "TIME", "SYMBOL", "SECTYPE", "EXCHANGE", "SIDE");
        
        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
        assertFalse(filter1.equals(null));
        assertFalse(filter1.equals("some object"));
    }
}