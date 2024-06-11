import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    ExecutionFilter executionFilter;

    //Constructor test
    @Test
    public void testExecutionFilterConstructor() {
        executionFilter = new ExecutionFilter();
        assertNotNull(executionFilter);
        assertEquals(0, executionFilter.m_clientId);
    }

    //Constructor test with parameters
    @Test
    public void testExecutionFilterConstructorWithParams() {
        int clientId = 1;
        String acctCode = "ACC001";
        String time = "2021-01-01";
        String symbol = "AAPL";
        String secType = "STOCK";
        String exchange = "NYSE";
        String side = "BUY";

        executionFilter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
        assertNotNull(executionFilter);
        assertEquals(clientId, executionFilter.m_clientId);
        assertEquals(acctCode, executionFilter.m_acctCode);
        assertEquals(time, executionFilter.m_time);
        assertEquals(symbol, executionFilter.m_symbol);
        assertEquals(secType, executionFilter.m_secType);
        assertEquals(exchange, executionFilter.m_exchange);
        assertEquals(side, executionFilter.m_side);
    }

    //equals() method test
    @Test
    public void testEquals() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACC001", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(1, "ACC001", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter ef3 = new ExecutionFilter(2, "ACC002", "2021-02-02", "GOOG", "OPTION", "NASDAQ", "SELL");

        assertTrue(ef1.equals(ef1));
        assertTrue(ef1.equals(ef2));
        assertFalse(ef1.equals(null));
        assertFalse(ef1.equals(1234));
        assertFalse(ef1.equals(ef3));
    }

}