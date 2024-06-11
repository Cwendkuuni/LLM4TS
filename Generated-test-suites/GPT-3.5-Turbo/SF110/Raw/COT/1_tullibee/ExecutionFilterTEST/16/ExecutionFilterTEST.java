import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "10:00", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "10:00", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT2", "10:00", "AAPL", "STK", "NASDAQ", "BUY");

        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
    }

    // Additional test cases for potential edge cases and exception handling can be added here

}