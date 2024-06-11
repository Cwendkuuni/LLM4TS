import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    private ExecutionFilter executionFilter;

    @Before
    public void setUp() {
        executionFilter = new ExecutionFilter();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(0, executionFilter.m_clientId);
    }

    @Test
    public void testParametrizedConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT_CODE", "TIME", "SYMBOL", "SEC_TYPE", "EXCHANGE", "SIDE");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT_CODE", filter.m_acctCode);
        assertEquals("TIME", filter.m_time);
        assertEquals("SYMBOL", filter.m_symbol);
        assertEquals("SEC_TYPE", filter.m_secType);
        assertEquals("EXCHANGE", filter.m_exchange);
        assertEquals("SIDE", filter.m_side);
    }

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT_CODE", "TIME", "SYMBOL", "SEC_TYPE", "EXCHANGE", "SIDE");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT_CODE", "TIME", "SYMBOL", "SEC_TYPE", "EXCHANGE", "SIDE");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT_CODE", "TIME", "SYMBOL", "SEC_TYPE", "EXCHANGE", "SIDE");

        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
    }
}