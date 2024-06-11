import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "10:00", "IBM", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "10:00", "IBM", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT2", "15:00", "AAPL", "STOCK", "NASDAQ", "SELL");
        
        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
        assertFalse(filter2.equals(filter3));
    }
    
    // Add more test methods for other methods in ExecutionFilter class
    
}