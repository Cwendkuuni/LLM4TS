package com.ib.client;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    private ExecutionFilter executionFilter;

    @Before
    public void setUp() {
        executionFilter = new ExecutionFilter();
    }

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACC123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertEquals(1, filter.m_clientId);
        assertEquals("ACC123", filter.m_acctCode);
        assertEquals("2021-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NASDAQ", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACC123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACC123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACC123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
    }

    @Test
    public void testExecutionFilterEqualsWithNull() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACC123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter.equals(null));
    }

    @Test
    public void testExecutionFilterEqualsWithSameInstance() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACC123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertTrue(filter.equals(filter));
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentClass() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACC123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        Object obj = new Object();
        
        assertFalse(filter.equals(obj));
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACC123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACC123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACC123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "DIFF123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACC123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACC123", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACC123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACC123", "2021-01-01", "GOOG", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACC123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACC123", "2021-01-01", "AAPL", "OPTION", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACC123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACC123", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterEqualsWithDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACC123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACC123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "SELL");
        
        assertFalse(filter1.equals(filter2));
    }
}