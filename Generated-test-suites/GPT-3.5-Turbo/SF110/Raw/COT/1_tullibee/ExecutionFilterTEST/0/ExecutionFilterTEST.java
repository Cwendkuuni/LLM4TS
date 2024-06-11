import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT123", "2022-01-01", "AAPL", "STK", "NYSE", "BUY");

        assert(filter1.equals(filter1));
        assert(filter1.equals(filter2));
        assert(!filter1.equals(filter3));
    }
}