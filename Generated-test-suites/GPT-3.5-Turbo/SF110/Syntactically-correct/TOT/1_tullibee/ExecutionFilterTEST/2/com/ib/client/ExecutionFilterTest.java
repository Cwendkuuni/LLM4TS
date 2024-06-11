package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "ACC1234", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(1, executionFilter.m_clientId);
        assertEquals("ACC1234", executionFilter.m_acctCode);
        assertEquals("2021-01-01", executionFilter.m_time);
        assertEquals("AAPL", executionFilter.m_symbol);
        assertEquals("STOCK", executionFilter.m_secType);
        assertEquals("NYSE", executionFilter.m_exchange);
        assertEquals("BUY", executionFilter.m_side);
    }

    @Test
    public void testExecutionFilterDefaultConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        assertEquals(0, executionFilter.m_clientId);
        assertNull(executionFilter.m_acctCode);
        assertNull(executionFilter.m_time);
        assertNull(executionFilter.m_symbol);
        assertNull(executionFilter.m_secType);
        assertNull(executionFilter.m_exchange);
        assertNull(executionFilter.m_side);
    }

    @Test
    public void testEquals() {
        // same execution filter object
        ExecutionFilter executionFilter1 = new ExecutionFilter();
        assertTrue(executionFilter1.equals(executionFilter1));

        // different object type
        assertFalse(executionFilter1.equals(new Object()));

        ExecutionFilter executionFilter2 = new ExecutionFilter();
        // different clientId
        assertFalse(executionFilter1.equals(executionFilter2));

        executionFilter2.m_clientId = executionFilter1.m_clientId;
        // same clientId
        assertEquals(executionFilter1, executionFilter2);

        executionFilter2.m_acctCode = "ACC1234";
        // different acctCode
        assertFalse(executionFilter1.equals(executionFilter2));

        executionFilter2.m_acctCode = executionFilter1.m_acctCode;
        // same acctCode
        assertEquals(executionFilter1, executionFilter2);

        executionFilter2.m_time = "2021-01-01";
        // different time
        assertFalse(executionFilter1.equals(executionFilter2));

        executionFilter2.m_time = executionFilter1.m_time;
        // same time
        assertEquals(executionFilter1, executionFilter2);

        executionFilter2.m_symbol = "AAPL";
        // different symbol
        assertFalse(executionFilter1.equals(executionFilter2));

        executionFilter2.m_symbol = executionFilter1.m_symbol;
        // same symbol
        assertEquals(executionFilter1, executionFilter2);

        executionFilter2.m_secType = "STOCK";
        // different secType
        assertFalse(executionFilter1.equals(executionFilter2));

        executionFilter2.m_secType = executionFilter1.m_secType;
        // same secType
        assertEquals(executionFilter1, executionFilter2);

        executionFilter2.m_exchange = "NYSE";
        // different exchange
        assertFalse(executionFilter1.equals(executionFilter2));

        executionFilter2.m_exchange = executionFilter1.m_exchange;
        // same exchange
        assertEquals(executionFilter1, executionFilter2);

        executionFilter2.m_side = "BUY";
        // different side
        assertFalse(executionFilter1.equals(executionFilter2));

        executionFilter2.m_side = executionFilter1.m_side;
        // same side
        assertEquals(executionFilter1, executionFilter2);
    }

    @Test
    public void testEqualsWithNull() {
        ExecutionFilter executionFilter1 = new ExecutionFilter();

        // comparing with null
        assertFalse(executionFilter1.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        ExecutionFilter executionFilter1 = new ExecutionFilter();
        assertFalse(executionFilter1.equals("ExecutionFilter"));
    }

    @Test
    public void testEqualsWithDifferentClientId() {
        ExecutionFilter executionFilter1 = new ExecutionFilter();
        executionFilter1.m_clientId = 1;

        ExecutionFilter executionFilter2 = new ExecutionFilter();
        executionFilter2.m_clientId = 2;

        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testEqualsWithDifferentAcctCode() {
        ExecutionFilter executionFilter1 = new ExecutionFilter();
        executionFilter1.m_acctCode = "ACC1234";

        ExecutionFilter executionFilter2 = new ExecutionFilter();
        executionFilter2.m_acctCode = "ACC5678";

        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testEqualsWithDifferentTime() {
        ExecutionFilter executionFilter1 = new ExecutionFilter();
        executionFilter1.m_time = "2021-01-01";

        ExecutionFilter executionFilter2 = new ExecutionFilter();
        executionFilter2.m_time = "2022-01-01";

        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testEqualsWithDifferentSymbol() {
        ExecutionFilter executionFilter1 = new ExecutionFilter();
        executionFilter1.m_symbol = "AAPL";

        ExecutionFilter executionFilter2 = new ExecutionFilter();
        executionFilter2.m_symbol = "GOOG";

        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testEqualsWithDifferentSecType() {
        ExecutionFilter executionFilter1 = new ExecutionFilter();
        executionFilter1.m_secType = "STOCK";

        ExecutionFilter executionFilter2 = new ExecutionFilter();
        executionFilter2.m_secType = "FUTURE";

        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testEqualsWithDifferentExchange() {
        ExecutionFilter executionFilter1 = new ExecutionFilter();
        executionFilter1.m_exchange = "NYSE";

        ExecutionFilter executionFilter2 = new ExecutionFilter();
        executionFilter2.m_exchange = "NASDAQ";

        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testEqualsWithDifferentSide() {
        ExecutionFilter executionFilter1 = new ExecutionFilter();
        executionFilter1.m_side = "BUY";

        ExecutionFilter executionFilter2 = new ExecutionFilter();
        executionFilter2.m_side = "SELL";

        assertFalse(executionFilter1.equals(executionFilter2));
    }
}