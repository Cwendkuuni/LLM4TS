import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterDefaultConstructor() {
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
    public void testExecutionFilterParameterizedConstructor() {
        int clientId = 1;
        String acctCode = "ACCT123";
        String time = "2021-01-01";
        String symbol = "AAPL";
        String secType = "STK";
        String exchange = "NASDAQ";
        String side = "BUY";

        ExecutionFilter filter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);

        assertEquals(clientId, filter.m_clientId);
        assertEquals(acctCode, filter.m_acctCode);
        assertEquals(time, filter.m_time);
        assertEquals(symbol, filter.m_symbol);
        assertEquals(secType, filter.m_secType);
        assertEquals(exchange, filter.m_exchange);
        assertEquals(side, filter.m_side);
    }

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT123", "2021-01-01", "AAPL", "STK", "NASDAQ", "BUY");

        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
        assertFalse(filter1.equals(null));
    }
}