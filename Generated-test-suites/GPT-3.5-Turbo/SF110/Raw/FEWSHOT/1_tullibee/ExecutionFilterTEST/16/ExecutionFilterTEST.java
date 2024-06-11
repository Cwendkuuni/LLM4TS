
package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {
  
  @Test
  public void testConstructorAndGetters() {
    int clientId = 1;
    String acctCode = "ACCT-1";
    String time = "2021-01-01";
    String symbol = "AAPL";
    String secType = "STK";
    String exchange = "NASDAQ";
    String side = "BUY";
    
    ExecutionFilter executionFilter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
    
    assertEquals(clientId, executionFilter.m_clientId);
    assertEquals(acctCode, executionFilter.m_acctCode);
    assertEquals(time, executionFilter.m_time);
    assertEquals(symbol, executionFilter.m_symbol);
    assertEquals(secType, executionFilter.m_secType);
    assertEquals(exchange, executionFilter.m_exchange);
    assertEquals(side, executionFilter.m_side);
  }
  
  @Test
  public void testDefaultConstructor() {
    ExecutionFilter executionFilter = new ExecutionFilter();
    
    assertEquals(0, executionFilter.m_clientId);
    assertEquals(null, executionFilter.m_acctCode);
    assertEquals(null, executionFilter.m_time);
    assertEquals(null, executionFilter.m_symbol);
    assertEquals(null, executionFilter.m_secType);
    assertEquals(null, executionFilter.m_exchange);
    assertEquals(null, executionFilter.m_side);
  }
  
  @Test
  public void testEquals() {
    int clientId = 1;
    String acctCode = "ACCT-1";
    String time = "2021-01-01";
    String symbol = "AAPL";
    String secType = "STK";
    String exchange = "NASDAQ";
    String side = "BUY";
    
    ExecutionFilter executionFilter1 = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
    ExecutionFilter executionFilter2 = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
    
    assertTrue(executionFilter1.equals(executionFilter2));
  }
}
