import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(1, executionFilter.m_clientId);
        assertEquals("ACCT123", executionFilter.m_acctCode);
        assertEquals("10:00", executionFilter.m_time);
        assertEquals("AAPL", executionFilter.m_symbol);
        assertEquals("STOCK", executionFilter.m_secType);
        assertEquals("NYSE", executionFilter.m_exchange);
        assertEquals("BUY", executionFilter.m_side);
    }

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testExecutionFilterNotEquals() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(2, "ACCT456", "12:00", "GOOG", "STOCK", "NASDAQ", "SELL");
        assertFalse(executionFilter1.equals(executionFilter2));
    }
}