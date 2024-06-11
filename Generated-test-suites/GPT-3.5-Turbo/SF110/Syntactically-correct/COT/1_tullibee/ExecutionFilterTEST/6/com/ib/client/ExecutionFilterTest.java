import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {
  
  @Test
  public void testEquals() {
    ExecutionFilter filter1 = new ExecutionFilter();
    ExecutionFilter filter2 = new ExecutionFilter();
    ExecutionFilter filter3 = new ExecutionFilter(1, "ACCT", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
    
    // Test equal objects
    assertTrue(filter1.equals(filter2));
    assertTrue(filter2.equals(filter1));
    
    // Test non-equal objects
    assertFalse(filter1.equals(filter3));
    assertFalse(filter3.equals(filter1));
    assertFalse(filter2.equals(filter3));
    assertFalse(filter3.equals(filter2));
    
    // Test null input
    assertFalse(filter1.equals(null));
  }
  
  @Test
  public void testConstructor() {
    ExecutionFilter filter = new ExecutionFilter(1, "ACCT", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
    
    // Test attribute assignments
    assertEquals(1, filter.m_clientId);
    assertEquals("ACCT", filter.m_acctCode);
    assertEquals("10:00", filter.m_time);
    assertEquals("AAPL", filter.m_symbol);
    assertEquals("STOCK", filter.m_secType);
    assertEquals("NYSE", filter.m_exchange);
    assertEquals("BUY", filter.m_side);
  }
  
}