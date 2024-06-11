
import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testExecutionFilterConstructor() {
        int clientId = 1;
        String acctCode = "ACCT-001";
        String time = "2022-01-01";
        String symbol = "AAPL";
        String secType = "STOCK";
        String exchange = "NASDAQ";
        String side = "BUY";
        
        ExecutionFilter executionFilter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
        
        Assert.assertEquals(clientId, executionFilter.m_clientId);
        Assert.assertEquals(acctCode, executionFilter.m_acctCode);
        Assert.assertEquals(time, executionFilter.m_time);
        Assert.assertEquals(symbol, executionFilter.m_symbol);
        Assert.assertEquals(secType, executionFilter.m_secType);
        Assert.assertEquals(exchange, executionFilter.m_exchange);
        Assert.assertEquals(side, executionFilter.m_side);
    }
    
    @Test
    public void testEquals() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT-001", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT-001", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter executionFilter3 = new ExecutionFilter(2, "ACCT-001", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        Assert.assertTrue(executionFilter1.equals(executionFilter2));
        Assert.assertFalse(executionFilter1.equals(executionFilter3));
    }
}
