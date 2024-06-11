package com.ib.client;

import static org.junit.Assert.*;
import org.junit.Test;

public class ExecutionFilterTest {

  @Test
  public void testEquals() {
    ExecutionFilter filter1 = new ExecutionFilter(0, "ACCT1", "12:00 PM", "AAPL", "STOCK", "NASDAQ", "BUY");
    ExecutionFilter filter2 = new ExecutionFilter(0, "acct1", "12:00 PM", "AAPL", "STOCK", "NASDAQ", "BUY");
    ExecutionFilter filter3 = new ExecutionFilter(1, "ACCT1", "12:00 PM", "AAPL", "STOCK", "NASDAQ", "BUY");
    ExecutionFilter filter4 = new ExecutionFilter(0, "ACCT1", "1:00 PM", "AAPL", "STOCK", "NASDAQ", "BUY");
    ExecutionFilter filter5 = new ExecutionFilter(0, "ACCT1", "12:00 PM", "AAPL", "OPTION", "NASDAQ", "BUY");
    ExecutionFilter filter6 = new ExecutionFilter(0, "ACCT1", "12:00 PM", "AAPL", "STOCK", "NYSE", "BUY");
    ExecutionFilter filter7 = new ExecutionFilter(0, "ACCT1", "12:00 PM", "AAPL", "STOCK", "NASDAQ", "SELL");

    assertTrue(filter1.equals(filter2));
    assertFalse(filter1.equals(filter3));
    assertFalse(filter1.equals(filter4));
    assertFalse(filter1.equals(filter5));
    assertFalse(filter1.equals(filter6));
    assertFalse(filter1.equals(filter7));
  }
}