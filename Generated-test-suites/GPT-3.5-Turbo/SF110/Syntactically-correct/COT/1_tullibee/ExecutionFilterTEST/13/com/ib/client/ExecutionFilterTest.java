import static org.junit.Assert.*;
import org.junit.Test;

public class ExecutionFilterTest {
    
    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = new ExecutionFilter();
        ExecutionFilter filter3 = new ExecutionFilter(1, "ACCT1", "10:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        // Test equal objects
        assertTrue(filter1.equals(filter1));
        assertTrue(filter1.equals(filter2));
        
        // Test non-equal objects
        assertFalse(filter1.equals(null));
        assertFalse(filter1.equals(filter3));
        assertFalse(filter3.equals(filter1));
    }
}