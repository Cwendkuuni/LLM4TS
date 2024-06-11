import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "10:00", "ABC", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "10:00", "ABC", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT2", "11:30", "DEF", "STOCK", "NYSE", "SELL");
        
        // Test equal filters
        assertEquals(true, filter1.equals(filter2));
        
        // Test different filters
        assertEquals(false, filter1.equals(filter3));
    }
}