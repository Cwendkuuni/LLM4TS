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
    public void testDefaultConstructor() {
        assertEquals(0, executionFilter.m_clientId);
        assertNull(executionFilter.m_acctCode);
        assertNull(executionFilter.m_time);
        assertNull(executionFilter.m_symbol);
        assertNull(executionFilter.m_secType);
        assertNull(executionFilter.m_exchange);
        assertNull(executionFilter.m_side);
    }
    
    @Test
    public void testParameterizedConstructor() {
        int clientId = 1;
        String acctCode = "ACC123";
        String time = "2021-10-10 12:00:00";
        String symbol = "AAPL";
        String secType = "STK";
        String exchange = "NASDAQ";
        String side = "BUY";
        
        executionFilter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
        
        assertEquals(clientId, executionFilter.m_clientId);
        assertEquals(acctCode, executionFilter.m_acctCode);
        assertEquals(time, executionFilter.m_time);
        assertEquals(symbol, executionFilter.m_symbol);
        assertEquals(secType, executionFilter.m_secType);
        assertEquals(exchange, executionFilter.m_exchange);
        assertEquals(side, executionFilter.m_side);
    }
    
    @Test
    public void testEqualsWithNullObject() {
        ExecutionFilter other = null;
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEqualsWithSameObject() {
        assertTrue(executionFilter.equals(executionFilter));
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        Object other = new Object();
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEqualsWithDifferentClientId() {
        ExecutionFilter other = new ExecutionFilter(1, null, null, null, null, null, null);
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEqualsWithDifferentAcctCode() {
        ExecutionFilter other = new ExecutionFilter(0, "ACC123", null, null, null, null, null);
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEqualsWithDifferentTime() {
        ExecutionFilter other = new ExecutionFilter(0, null, "2021-10-10 12:00:00", null, null, null, null);
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEqualsWithDifferentSymbol() {
        ExecutionFilter other = new ExecutionFilter(0, null, null, "AAPL", null, null, null);
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEqualsWithDifferentSecType() {
        ExecutionFilter other = new ExecutionFilter(0, null, null, null, "STK", null, null);
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEqualsWithDifferentExchange() {
        ExecutionFilter other = new ExecutionFilter(0, null, null, null, null, "NASDAQ", null);
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEqualsWithDifferentSide() {
        ExecutionFilter other = new ExecutionFilter(0, null, null, null, null, null, "BUY");
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEqualsWithEqualParameters() {
        ExecutionFilter other = new ExecutionFilter(0, null, null, null, null, null, null);
        assertTrue(executionFilter.equals(other));
    }
    
    @Test
    public void testEqualsWithIgnoreCaseStrings() {
        executionFilter = new ExecutionFilter(0, "ACc123", "2021-10-10 12:00:00", "AapL", "stk", "nASDAq", "bUy");
        ExecutionFilter other = new ExecutionFilter(0, "acc123", "2021-10-10 12:00:00", "aapl", "STK", "NASDAQ", "BUY");
        assertTrue(executionFilter.equals(other));
    }
}