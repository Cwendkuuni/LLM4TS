package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ExecutionFilterTest {
    private ExecutionFilter filter1;
    private ExecutionFilter filter2;
    
    @Before
    public void setUp() {
        filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        filter2 = new ExecutionFilter(2, "ACCT2", "2022-01-02", "TSLA", "STOCK", "NYSE", "SELL");
    }
    
    @Test
    public void testConstructor() {
        assertEquals(1, filter1.m_clientId);
        assertEquals("ACCT1", filter1.m_acctCode);
        assertEquals("2022-01-01", filter1.m_time);
        assertEquals("AAPL", filter1.m_symbol);
        assertEquals("STOCK", filter1.m_secType);
        assertEquals("NASDAQ", filter1.m_exchange);
        assertEquals("BUY", filter1.m_side);
    }
    
    @Test
    public void testEquals() {
        assertFalse(filter1.equals(null));
        assertTrue(filter1.equals(filter1));
        assertFalse(filter1.equals(filter2));
        
        ExecutionFilter filterCopy = new ExecutionFilter(1, "acct1", "2022-01-01", "aapl", "stock", "nasdaq", "buy");
        assertTrue(filter1.equals(filterCopy));
        
        ExecutionFilter filterDiffClientId = new ExecutionFilter(2, "ACCT1", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filterDiffClientId));
        
        ExecutionFilter filterDiffAcctCode = new ExecutionFilter(1, "ACCT2", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filterDiffAcctCode));
        
        ExecutionFilter filterDiffTime = new ExecutionFilter(1, "ACCT1", "2022-01-02", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filterDiffTime));
        
        ExecutionFilter filterDiffSymbol = new ExecutionFilter(1, "ACCT1", "2022-01-01", "TSLA", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filterDiffSymbol));
        
        ExecutionFilter filterDiffSecType = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "OPTION", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filterDiffSecType));
        
        ExecutionFilter filterDiffExchange = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(filterDiffExchange));
        
        ExecutionFilter filterDiffSide = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "SELL");
        assertFalse(filter1.equals(filterDiffSide));
    }
}