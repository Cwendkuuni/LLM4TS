import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
    }
    
    @Test
    public void testExecutionFilterConstructorWithParameters() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT123", filter.m_acctCode);
        assertEquals("2022-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STK", filter.m_secType);
        assertEquals("NASDAQ", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testEqualsWithEqualObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertTrue(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT123", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT456", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01 12:00:00", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2022-01-02 12:00:00", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "GOOG", "STK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "OPT", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NASDAQ", "SELL");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsWithNull() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(filter.equals(null));
    }
    
    @Test
    public void testEqualsWithSameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertTrue(filter.equals(filter));
    }
    
    @Test
    public void testEqualsWithDifferentClassObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(filter.equals("Test"));
    }
}