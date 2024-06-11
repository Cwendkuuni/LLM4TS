import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

   @Test
   public void testEquals_whenObjectIsNull_returnFalse() {
      ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
      ExecutionFilter filter2 = null;
      assertFalse(filter1.equals(filter2));
   }

   @Test
   public void testEquals_whenObjectIsSame_returnTrue() {
      ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
      ExecutionFilter filter2 = filter1;
      assertTrue(filter1.equals(filter2));
   }

   @Test
   public void testEquals_whenObjectsAreEqual_returnTrue() {
      ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
      ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
      assertTrue(filter1.equals(filter2));
   }

   @Test
   public void testEquals_whenClientIdIsDifferent_returnFalse() {
      ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
      ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT1", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
      assertFalse(filter1.equals(filter2));
   }

   @Test
   public void testEquals_whenAcctCodeIsDifferent_returnFalse() {
      ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
      ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT2", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
      assertFalse(filter1.equals(filter2));
   }

   @Test
   public void testEquals_whenTimeIsDifferent_returnFalse() {
      ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
      ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
      assertFalse(filter1.equals(filter2));
   }

   @Test
   public void testEquals_whenSymbolIsDifferent_returnFalse() {
      ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
      ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "09:00", "GOOG", "STOCK", "NYSE", "BUY");
      assertFalse(filter1.equals(filter2));
   }

   @Test
   public void testEquals_whenSecTypeIsDifferent_returnFalse() {
      ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
      ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "09:00", "AAPL", "OPTION", "NYSE", "BUY");
      assertFalse(filter1.equals(filter2));
   }

   @Test
   public void testEquals_whenExchangeIsDifferent_returnFalse() {
      ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
      ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "09:00", "AAPL", "STOCK", "NASDAQ", "BUY");
      assertFalse(filter1.equals(filter2));
   }

   @Test
   public void testEquals_whenSideIsDifferent_returnFalse() {
      ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
      ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "09:00", "AAPL", "STOCK", "NYSE", "SELL");
      assertFalse(filter1.equals(filter2));
   }

   // Add more test methods to cover all methods in the ExecutionFilter class

}