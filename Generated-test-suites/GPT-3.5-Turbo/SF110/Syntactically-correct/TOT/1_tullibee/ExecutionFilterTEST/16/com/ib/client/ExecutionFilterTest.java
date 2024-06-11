import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    private ExecutionFilter executionFilter;

    @Before
    public void setUp() {
        executionFilter = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
    }

    @After
    public void tearDown() {
        executionFilter = null;
    }

    @Test
    public void testExecutionFilterConstructor() {
        assertEquals(1, executionFilter.m_clientId);
        assertEquals("ACCT1", executionFilter.m_acctCode);
        assertEquals("2020-01-01", executionFilter.m_time);
        assertEquals("AAPL", executionFilter.m_symbol);
        assertEquals("STOCK", executionFilter.m_secType);
        assertEquals("NYSE", executionFilter.m_exchange);
        assertEquals("BUY", executionFilter.m_side);
    }

    @Test
    public void testExecutionFilterDefaultConstructor() {
        ExecutionFilter defaultExecutionFilter = new ExecutionFilter();
        assertEquals(0, defaultExecutionFilter.m_clientId);
        assertNull(defaultExecutionFilter.m_acctCode);
        assertNull(defaultExecutionFilter.m_time);
        assertNull(defaultExecutionFilter.m_symbol);
        assertNull(defaultExecutionFilter.m_secType);
        assertNull(defaultExecutionFilter.m_exchange);
        assertNull(defaultExecutionFilter.m_side);
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(executionFilter.equals(executionFilter));
    }

    @Test
    public void testEqualsNullObject() {
        assertFalse(executionFilter.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(executionFilter.equals("string"));
    }

    @Test
    public void testEqualsDifferentClientId() {
        ExecutionFilter differentClientId = new ExecutionFilter(2, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(executionFilter.equals(differentClientId));
    }

    @Test
    public void testEqualsDifferentAcctCode() {
        ExecutionFilter differentAcctCode = new ExecutionFilter(1, "ACCT2", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(executionFilter.equals(differentAcctCode));
    }

    @Test
    public void testEqualsDifferentTime() {
        ExecutionFilter differentTime = new ExecutionFilter(1, "ACCT1", "2020-01-02", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(executionFilter.equals(differentTime));
    }

    @Test
    public void testEqualsDifferentSymbol() {
        ExecutionFilter differentSymbol = new ExecutionFilter(1, "ACCT1", "2020-01-01", "GOOG", "STOCK", "NYSE", "BUY");
        assertFalse(executionFilter.equals(differentSymbol));
    }

    @Test
    public void testEqualsDifferentSecType() {
        ExecutionFilter differentSecType = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "OPTION", "NYSE", "BUY");
        assertFalse(executionFilter.equals(differentSecType));
    }

    @Test
    public void testEqualsDifferentExchange() {
        ExecutionFilter differentExchange = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertFalse(executionFilter.equals(differentExchange));
    }

    @Test
    public void testEqualsDifferentSide() {
        ExecutionFilter differentSide = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NYSE", "SELL");
        assertFalse(executionFilter.equals(differentSide));
    }

    @Test
    public void testEqualsAllDifferent() {
        ExecutionFilter differentFilter = new ExecutionFilter(2, "ACCT2", "2020-01-02", "GOOG", "OPTION", "NASDAQ", "SELL");
        assertFalse(executionFilter.equals(differentFilter));
    }

    @Test
    public void testEqualsSameAttributesDifferentObject() {
        ExecutionFilter sameAttributesFilter = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(executionFilter.equals(sameAttributesFilter));
    }
}