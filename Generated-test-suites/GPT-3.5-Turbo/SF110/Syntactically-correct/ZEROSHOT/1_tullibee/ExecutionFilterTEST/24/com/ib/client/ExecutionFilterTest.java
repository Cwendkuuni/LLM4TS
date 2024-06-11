package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExecutionFilterTest {

   @Test
   public void testEquals() {
      ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "2022-01-01", "AAPL", "STK", "NYSE", "BUY");
      ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "2022-01-01", "AAPL", "STK", "NYSE", "BUY");
      ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT", "2022-01-01", "AAPL", "STK", "NYSE", "BUY");
      
      assertEquals(filter1.equals(filter2), true);
      assertEquals(filter1.equals(filter3), false);
   }
}