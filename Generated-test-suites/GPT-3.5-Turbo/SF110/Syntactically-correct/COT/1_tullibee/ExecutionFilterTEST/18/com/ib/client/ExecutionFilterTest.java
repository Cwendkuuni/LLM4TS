import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExecutionFilterTest {

    private ExecutionFilter executionFilter;

    @Before
    public void setup() {
        executionFilter = new ExecutionFilter();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(0, executionFilter.m_clientId);
    }

    @Test
    public void testParameterizedConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        assertEquals(1, executionFilter.m_clientId);
        assertEquals("ACCT123", executionFilter.m_acctCode);
        assertEquals("2020-01-01", executionFilter.m_time);
        assertEquals("AAPL", executionFilter.m_symbol);
        assertEquals("STK", executionFilter.m_secType);
        assertEquals("NYSE", executionFilter.m_exchange);
        assertEquals("BUY", executionFilter.m_side);
    }

    @Test
    public void testEqualsWithNullObject() {
        assertFalse(executionFilter.equals(null));
    }

    @Test
    public void testEqualsWithSameObject() {
        assertTrue(executionFilter.equals(executionFilter));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(executionFilter.equals(new Object()));
    }

    @Test
    public void testEqualsWithDifferentClientId() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(2, "ACCT123", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testEqualsWithDifferentAcctCode() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT456", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testEqualsWithDifferentTime() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT123", "2020-01-02", "AAPL", "STK", "NYSE", "BUY");
        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testEqualsWithDifferentSymbol() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT123", "2020-01-01", "MSFT", "STK", "NYSE", "BUY");
        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testEqualsWithDifferentSecType() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "OPT", "NYSE", "BUY");
        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testEqualsWithDifferentExchange() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testEqualsWithDifferentSide() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "STK", "NYSE", "SELL");
        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testEqualsWithDifferentValues() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(2, "ACCT456", "2020-01-02", "MSFT", "OPT", "NASDAQ", "SELL");
        assertFalse(executionFilter1.equals(executionFilter2));
    }

    @Test
    public void testEqualsWithSameValues() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        assertTrue(executionFilter1.equals(executionFilter2));
    }
    
}