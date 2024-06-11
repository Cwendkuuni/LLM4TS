import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    private ExecutionFilter executionFilter;

    @Before
    public void setUp() {
        executionFilter = new ExecutionFilter(1, "ACCT001", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
    }

    @Test
    public void testDefaultConstructor() {
        ExecutionFilter ef = new ExecutionFilter();
        assertEquals(0, ef.m_clientId);
        assertNull(ef.m_acctCode);
        assertNull(ef.m_time);
        assertNull(ef.m_symbol);
        assertNull(ef.m_secType);
        assertNull(ef.m_exchange);
        assertNull(ef.m_side);
    }

    @Test
    public void testParameterizedConstructor() {
        assertEquals(1, executionFilter.m_clientId);
        assertEquals("ACCT001", executionFilter.m_acctCode);
        assertEquals("09:00", executionFilter.m_time);
        assertEquals("AAPL", executionFilter.m_symbol);
        assertEquals("STOCK", executionFilter.m_secType);
        assertEquals("NYSE", executionFilter.m_exchange);
        assertEquals("BUY", executionFilter.m_side);
    }

    @Test
    public void testEqualsWithEqualObjects() {
        ExecutionFilter other = new ExecutionFilter(1, "ACCT001", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(executionFilter.equals(other));
    }

    @Test
    public void testEqualsWithDifferentClientId() {
        ExecutionFilter other = new ExecutionFilter(2, "ACCT001", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(executionFilter.equals(other));
    }

    @Test
    public void testEqualsWithDifferentAcctCode() {
        ExecutionFilter other = new ExecutionFilter(1, "ACCT002", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(executionFilter.equals(other));
    }

    @Test
    public void testEqualsWithDifferentTime() {
        ExecutionFilter other = new ExecutionFilter(1, "ACCT001", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(executionFilter.equals(other));
    }

    @Test
    public void testEqualsWithDifferentSymbol() {
        ExecutionFilter other = new ExecutionFilter(1, "ACCT001", "09:00", "GOOG", "STOCK", "NYSE", "BUY");
        assertFalse(executionFilter.equals(other));
    }

    @Test
    public void testEqualsWithDifferentSecType() {
        ExecutionFilter other = new ExecutionFilter(1, "ACCT001", "09:00", "AAPL", "ETF", "NYSE", "BUY");
        assertFalse(executionFilter.equals(other));
    }

    @Test
    public void testEqualsWithDifferentExchange() {
        ExecutionFilter other = new ExecutionFilter(1, "ACCT001", "09:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertFalse(executionFilter.equals(other));
    }

    @Test
    public void testEqualsWithDifferentSide() {
        ExecutionFilter other = new ExecutionFilter(1, "ACCT001", "09:00", "AAPL", "STOCK", "NYSE", "SELL");
        assertFalse(executionFilter.equals(other));
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
    public void testEqualsWithDifferentObjectType() {
        assertFalse(executionFilter.equals(new Object()));
    }
}