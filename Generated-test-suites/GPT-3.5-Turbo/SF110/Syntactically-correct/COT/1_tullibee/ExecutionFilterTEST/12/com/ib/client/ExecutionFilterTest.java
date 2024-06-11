
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

  @Test
  public void testEquals() {
    ExecutionFilter filter1 = new ExecutionFilter();
    ExecutionFilter filter2 = new ExecutionFilter();
    ExecutionFilter filter3 = new ExecutionFilter(1, "ACCT", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
    ExecutionFilter filter4 = new ExecutionFilter(1, "ACCT", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");

    assertEquals(true, filter1.equals(filter1)); // reflexive property
    assertEquals(true, filter1.equals(filter2)); // symmetric property
    assertEquals(true, filter2.equals(filter1)); // symmetric property
    assertEquals(true, filter3.equals(filter4)); // transitive property
    assertEquals(true, filter4.equals(filter3)); // transitive property
    assertEquals(false, filter1.equals(null)); // null comparison
    assertEquals(false, filter1.equals("test")); // String comparison

  }
}
