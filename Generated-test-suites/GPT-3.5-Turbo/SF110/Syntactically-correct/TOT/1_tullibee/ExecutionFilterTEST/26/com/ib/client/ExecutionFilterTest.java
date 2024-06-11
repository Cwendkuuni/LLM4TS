import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterEmptyConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
        assertNull(filter.m_acctCode);
        assertNull(filter.m_time);
        assertNull(filter.m_symbol);
        assertNull(filter.m_secType);
        assertNull(filter.m_exchange);
        assertNull(filter.m_side);
    }

    @Test
    public void testExecutionFilterParamConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT1", filter.m_acctCode);
        assertEquals("2021-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testEqualsSameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(filter, filter);
    }

    @Test
    public void testEqualsEqualObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT1", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT2", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2021-01-01", "GOOG", "STOCK", "NYSE", "BUY");
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "OPTION", "NYSE", "BUY");
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STOCK", "NYSE", "SELL");
        assertNotEquals(filter1, filter2);
    }

    @Test
    public void testEqualsNullObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertNotEquals(filter, null);
    }

    @Test
    public void testEqualsDifferentClass() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertNotEquals(filter, "ACCT1");
    }
}