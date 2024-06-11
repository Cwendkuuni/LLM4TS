import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    private ExecutionFilter executionFilter;

    // Test cases for default constructor
    @Test
    public void testDefaultConstructor() {
        executionFilter = new ExecutionFilter();
        assertEquals(0, executionFilter.m_clientId);
    }

    // Test cases for parameterized constructor
    @Test
    public void testParameterizedConstructor() {
        executionFilter = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NYSE", "BUY");
        assertEquals(1, executionFilter.m_clientId);
        assertEquals("ACCT123", executionFilter.m_acctCode);
        assertEquals("09:00", executionFilter.m_time);
        assertEquals("AAPL", executionFilter.m_symbol);
        assertEquals("STK", executionFilter.m_secType);
        assertEquals("NYSE", executionFilter.m_exchange);
        assertEquals("BUY", executionFilter.m_side);
    }

    // Test cases for equals method
    @Test
    public void testEqualsMethod() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter executionFilter3 = new ExecutionFilter(2, "ACCT456", "10:00", "IBM", "OPT", "NASDAQ", "SELL");

        assertTrue(executionFilter1.equals(executionFilter2));
        assertFalse(executionFilter1.equals(executionFilter3));
    }

    // Test cases for equals method - edge case
    @Test
    public void testEqualsMethodWithNullComparison() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter executionFilter2 = null;

        assertFalse(executionFilter1.equals(executionFilter2));
    }

}