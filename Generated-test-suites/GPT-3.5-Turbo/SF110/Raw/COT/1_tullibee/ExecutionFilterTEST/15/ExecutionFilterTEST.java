
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = new ExecutionFilter();
        
        assertTrue(filter1.equals(filter2));
        
        filter1 = new ExecutionFilter(1, "ACCT", "TIME1", "SYM", "SEC", "EX", "BUY");
        filter2 = new ExecutionFilter(1, "ACCT", "TIME1", "SYM", "SEC", "EX", "BUY");
        
        assertTrue(filter1.equals(filter2));
        
        filter1 = new ExecutionFilter(1, "ACCT", "TIME1", "SYM", "SEC", "EX", "BUY");
        filter2 = new ExecutionFilter(2, "ACCT", "TIME1", "SYM", "SEC", "EX", "BUY");
        
        assertFalse(filter1.equals(filter2));
        
        filter1 = new ExecutionFilter(1, "ACCT", "TIME1", "SYM", "SEC", "EX", "BUY");
        filter2 = new ExecutionFilter(1, "ACCT2", "TIME1", "SYM", "SEC", "EX", "BUY");
        
        assertFalse(filter1.equals(filter2));
        
        filter1 = new ExecutionFilter(1, "ACCT", "TIME1", "SYM", "SEC", "EX", "BUY");
        filter2 = new ExecutionFilter(1, "ACCT", "TIME2", "SYM", "SEC", "EX", "BUY");
        
        assertFalse(filter1.equals(filter2));
        
        filter1 = new ExecutionFilter(1, "ACCT", "TIME1", "SYM", "SEC", "EX", "BUY");
        filter2 = new ExecutionFilter(1, "ACCT", "TIME1", "SYM2", "SEC", "EX", "BUY");
        
        assertFalse(filter1.equals(filter2));
        
        filter1 = new ExecutionFilter(1, "ACCT", "TIME1", "SYM", "SEC", "EX", "BUY");
        filter2 = new ExecutionFilter(1, "ACCT", "TIME1", "SYM", "SEC2", "EX", "BUY");
        
        assertFalse(filter1.equals(filter2));
        
        filter1 = new ExecutionFilter(1, "ACCT", "TIME1", "SYM", "SEC", "EX", "BUY");
        filter2 = new ExecutionFilter(1, "ACCT", "TIME1", "SYM", "SEC", "EX2", "BUY");
        
        assertFalse(filter1.equals(filter2));
        
        filter1 = new ExecutionFilter(1, "ACCT", "TIME1", "SYM", "SEC", "EX", "BUY");
        filter2 = new ExecutionFilter(1, "ACCT", "TIME1", "SYM", "SEC", "EX", "SELL");
        
        assertFalse(filter1.equals(filter2));
    }
}
