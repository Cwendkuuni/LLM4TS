import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    // Test case for ExecutionFilter()
    @Test
    public void testConstructor_DefaultValues() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        assertEquals(0, executionFilter.m_clientId);
        assertNull(executionFilter.m_acctCode);
        assertNull(executionFilter.m_time);
        assertNull(executionFilter.m_symbol);
        assertNull(executionFilter.m_secType);
        assertNull(executionFilter.m_exchange);
        assertNull(executionFilter.m_side);
    }

    // Test case for ExecutionFilter(int, String, String, String, String, String, String)
    @Test
    public void testConstructor_WithValues() {
        int clientId = 1234;
        String acctCode = "ACC001";
        String time = "2021-06-01";
        String symbol = "AAPL";
        String secType = "STOCK";
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

    // Test case for equals(Object)
    @Test
    public void testEquals_EqualObjects() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1234, "ACC001", "2021-06-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1234, "ACC001", "2021-06-01", "AAPL", "STOCK", "NASDAQ", "BUY");

        assertTrue(executionFilter1.equals(executionFilter2));
    }

    // Test case for equals(Object)
    @Test
    public void testEquals_DifferentClientId() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1234, "ACC001", "2021-06-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(5678, "ACC001", "2021-06-01", "AAPL", "STOCK", "NASDAQ", "BUY");

        assertFalse(executionFilter1.equals(executionFilter2));
    }

    // Test case for equals(Object)
    @Test
    public void testEquals_DifferentNullFields() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1234, "ACC001", "2021-06-01", null, "STOCK", "NASDAQ", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1234, "ACC001", "2021-06-01", null, "STOCK", "NASDAQ", "BUY");

        assertTrue(executionFilter1.equals(executionFilter2));
    }

    // Test case for equals(Object)
    @Test
    public void testEquals_DifferentSymbol() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1234, "ACC001", "2021-06-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1234, "ACC001", "2021-06-01", "GOOG", "STOCK", "NASDAQ", "BUY");

        assertFalse(executionFilter1.equals(executionFilter2));
    }

    // Test case for equals(Object)
    @Test
    public void testEquals_DifferentTimeCase() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1234, "ACC001", "2021-06-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1234, "ACC001", "2021-06-01", "AAPL", "STOCK", "NASDAQ", "BUY");

        assertTrue(executionFilter1.equals(executionFilter2));
    }

    // Test case for equals(Object)
    @Test
    public void testEquals_DifferentExchangeCase() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1234, "ACC001", "2021-06-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1234, "ACC001", "2021-06-01", "AAPL", "STOCK", "nasdaq", "BUY");

        assertFalse(executionFilter1.equals(executionFilter2));
    }

    // Test case for equals(Object)
    @Test
    public void testEquals_NotEqualObjects() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1234, "ACC001", "2021-06-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(5678, "ACC001", "2021-06-01", "AAPL", "STOCK", "NASDAQ", "SELL");

        assertFalse(executionFilter1.equals(executionFilter2));
    }

}