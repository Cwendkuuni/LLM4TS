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
    public void testExecutionFilterDefaultConstructor() {
        assertEquals(0, executionFilter.m_clientId);
        assertNull(executionFilter.m_acctCode);
        assertNull(executionFilter.m_time);
        assertNull(executionFilter.m_symbol);
        assertNull(executionFilter.m_secType);
        assertNull(executionFilter.m_exchange);
        assertNull(executionFilter.m_side);
    }
    
    @Test
    public void testExecutionFilterParameterizedConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "ACC001", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");

        assertEquals(1, executionFilter.m_clientId);
        assertEquals("ACC001", executionFilter.m_acctCode);
        assertEquals("2021-01-01", executionFilter.m_time);
        assertEquals("AAPL", executionFilter.m_symbol);
        assertEquals("STOCK", executionFilter.m_secType);
        assertEquals("NYSE", executionFilter.m_exchange);
        assertEquals("BUY", executionFilter.m_side);
    }
    
    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACC001", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACC001", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter executionFilter3 = new ExecutionFilter(2, "ACC002", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "SELL");
        
        assertTrue(executionFilter1.equals(executionFilter2));
        assertFalse(executionFilter1.equals(executionFilter3));
    }
}