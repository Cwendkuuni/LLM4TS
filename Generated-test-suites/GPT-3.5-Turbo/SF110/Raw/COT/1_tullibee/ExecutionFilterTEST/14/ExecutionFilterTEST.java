import static org.junit.Assert.*;
import org.junit.Test;

public class ExecutionFilterTest {

   @Test
   public void testDefaultConstructor() {
      ExecutionFilter filter = new ExecutionFilter();
      assertEquals(0, filter.m_clientId);
      assertNull(filter.m_acctCode);
      assertNull(filter.m_time);
      assertNull(filter.m_symbol);
      assertNull(filter.m_secType);
      assertNull(filter.m_exchange);
      assertNull(filter.m_side);
   }

   @Test
   public void testParameterizedConstructor() {
      ExecutionFilter filter = new ExecutionFilter(1, "ACCT001", "2021-01-01", "AAPL", "STK", "NYSE", "BUY");
      assertEquals(1, filter.m_clientId);
      assertEquals("ACCT001", filter.m_acctCode);
      assertEquals("2021-01-01", filter.m_time);
      assertEquals("AAPL", filter.m_symbol);
      assertEquals("STK", filter.m_secType);
      assertEquals("NYSE", filter.m_exchange);
      assertEquals("BUY", filter.m_side);
   }

   @Test
   public void testEquals_null() {
      ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT001", "2021-01-01", "AAPL", "STK", "NYSE", "BUY");
      assertFalse(filter1.equals(null));
   }

   @Test
   public void testEquals_sameObject() {
      ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT001", "2021-01-01", "AAPL", "STK", "NYSE", "BUY");
      assertTrue(filter1.equals(filter1));
   }

   @Test
   public void testEquals_differentObject() {
      ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT001", "2021-01-01", "AAPL", "STK", "NYSE", "BUY");
      ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT001", "2021-01-01", "AAPL", "STK", "NYSE", "SELL");
      assertFalse(filter1.equals(filter2));
   }
  
   @Test
   public void testEquals_equalObject() {
      ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT001", "2021-01-01", "AAPL", "STK", "NYSE", "BUY");
      ExecutionFilter filter2 = new ExecutionFilter(1, "acct001", "2021-01-01", "aapl", "stk", "nyse", "buy");
      assertTrue(filter1.equals(filter2));
   }

}