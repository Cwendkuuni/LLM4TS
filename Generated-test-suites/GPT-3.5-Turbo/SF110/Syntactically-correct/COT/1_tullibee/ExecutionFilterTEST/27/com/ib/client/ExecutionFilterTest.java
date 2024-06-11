import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

    //Basic test cases for each method
    
    @Test
    public void testConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        Assert.assertEquals(0, filter.m_clientId);
        Assert.assertNull(filter.m_acctCode);
        Assert.assertNull(filter.m_time);
        Assert.assertNull(filter.m_symbol);
        Assert.assertNull(filter.m_secType);
        Assert.assertNull(filter.m_exchange);
        Assert.assertNull(filter.m_side);
    }
    
    @Test
    public void testParametrizedConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "test", "09:00", "AAPL", "STK", "NYSE", "BUY");
        Assert.assertEquals(1, filter.m_clientId);
        Assert.assertEquals("test", filter.m_acctCode);
        Assert.assertEquals("09:00", filter.m_time);
        Assert.assertEquals("AAPL", filter.m_symbol);
        Assert.assertEquals("STK", filter.m_secType);
        Assert.assertEquals("NYSE", filter.m_exchange);
        Assert.assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "test", "09:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "test", "09:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "test", "10:00", "AAPL", "STK", "NYSE", "SELL");
        
        Assert.assertEquals(filter1, filter2);
        Assert.assertNotEquals(filter1, filter3);
    }
    
    
    //Edge cases and exception handling scenarios
    
    @Test
    public void testEquals_Null() {
        ExecutionFilter filter = new ExecutionFilter(1, "test", "09:00", "AAPL", "STK", "NYSE", "BUY");
        Assert.assertNotEquals(filter, null);
    }
    
    @Test
    public void testEquals_SameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "test", "09:00", "AAPL", "STK", "NYSE", "BUY");
        Assert.assertEquals(filter, filter);
    }
    
    @Test
    public void testEquals_DifferentClass() {
        ExecutionFilter filter = new ExecutionFilter(1, "test", "09:00", "AAPL", "STK", "NYSE", "BUY");
        String string = "test";
        Assert.assertNotEquals(filter, string);
    }
    
    @Test
    public void testEquals_DifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "test", "09:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "test", "09:00", "AAPL", "STK", "NYSE", "BUY");
        Assert.assertNotEquals(filter1, filter2);
    }
    
    @Test
    public void testEquals_DifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "test", "09:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "test2", "09:00", "AAPL", "STK", "NYSE", "BUY");
        Assert.assertNotEquals(filter1, filter2);
    }
    
    @Test
    public void testEquals_DifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "test", "09:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "test", "10:00", "AAPL", "STK", "NYSE", "BUY");
        Assert.assertNotEquals(filter1, filter2);
    }
    
    @Test
    public void testEquals_DifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "test", "09:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "test", "09:00", "IBM", "STK", "NYSE", "BUY");
        Assert.assertNotEquals(filter1, filter2);
    }
    
    @Test
    public void testEquals_DifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "test", "09:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "test", "09:00", "AAPL", "OPT", "NYSE", "BUY");
        Assert.assertNotEquals(filter1, filter2);
    }
    
    @Test
    public void testEquals_DifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "test", "09:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "test", "09:00", "AAPL", "STK", "NASDAQ", "BUY");
        Assert.assertNotEquals(filter1, filter2);
    }
    
    @Test
    public void testEquals_DifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "test", "09:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "test", "09:00", "AAPL", "STK", "NYSE", "SELL");
        Assert.assertNotEquals(filter1, filter2);
    }
    
}