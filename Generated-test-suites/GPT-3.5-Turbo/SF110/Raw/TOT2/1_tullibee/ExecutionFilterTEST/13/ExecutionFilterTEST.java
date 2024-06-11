import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {
    
    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        assertEquals(0, executionFilter.m_clientId);
    }
    
    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT002", "12:00", "GOOG", "STOCK", "NASDAQ", "SELL");
        
        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
    }
}