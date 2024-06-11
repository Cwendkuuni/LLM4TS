import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructorWithParameters() {
        // Typical use case
        ExecutionFilter executionFilter = new ExecutionFilter(10, "acct123", "2021-01-01", "AAPL", "STK", "NYSE", "BUY");
        
        Assert.assertEquals(10, executionFilter.m_clientId);
        Assert.assertEquals("acct123", executionFilter.m_acctCode);
        Assert.assertEquals("2021-01-01", executionFilter.m_time);
        Assert.assertEquals("AAPL", executionFilter.m_symbol);
        Assert.assertEquals("STK", executionFilter.m_secType);
        Assert.assertEquals("NYSE", executionFilter.m_exchange);
        Assert.assertEquals("BUY", executionFilter.m_side);
    }

    @Test
    public void testExecutionFilterConstructorWithoutParameters() {
        // Typical use case
        ExecutionFilter executionFilter = new ExecutionFilter();
        
        Assert.assertEquals(0, executionFilter.m_clientId);
        Assert.assertNull(executionFilter.m_acctCode);
        Assert.assertNull(executionFilter.m_time);
        Assert.assertNull(executionFilter.m_symbol);
        Assert.assertNull(executionFilter.m_secType);
        Assert.assertNull(executionFilter.m_exchange);
        Assert.assertNull(executionFilter.m_side);
    }

    @Test
    public void testEquals() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(10, "acct123", "2021-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(10, "acct123", "2021-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter executionFilter3 = new ExecutionFilter(20, "acct456", "2021-01-02", "GOOGL", "STK", "NASDAQ", "SELL");
        
        // Typical use case
        Assert.assertTrue(executionFilter1.equals(executionFilter2));
        Assert.assertFalse(executionFilter1.equals(executionFilter3));
        
        // Edge case
        Assert.assertTrue(executionFilter1.equals(executionFilter1));
        
        // Error scenarios
        Assert.assertFalse(executionFilter1.equals(null));
        Assert.assertFalse(executionFilter1.equals("Not an ExecutionFilter object"));
    }
}