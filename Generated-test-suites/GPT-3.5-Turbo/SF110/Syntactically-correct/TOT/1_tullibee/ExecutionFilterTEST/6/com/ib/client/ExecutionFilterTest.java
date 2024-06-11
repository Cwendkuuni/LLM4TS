import static org.junit.Assert.*;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT001", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter4 = new ExecutionFilter(1, "ACCT002", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertTrue(filter1.equals(filter2)); // typical use case
        assertFalse(filter1.equals(filter3)); // typical use case
        assertFalse(filter1.equals(filter4)); // typical use case
        
        ExecutionFilter nullFilter = null;
        assertFalse(filter1.equals(nullFilter)); // error scenario
    }

}