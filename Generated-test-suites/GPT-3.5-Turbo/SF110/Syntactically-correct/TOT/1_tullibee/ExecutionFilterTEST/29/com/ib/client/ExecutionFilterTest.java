import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testConstructor() {
        ExecutionFilter ef = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        
        assertEquals(1, ef.m_clientId);
        assertEquals("ACCT", ef.m_acctCode);
        assertEquals("2020-01-01", ef.m_time);
        assertEquals("AAPL", ef.m_symbol);
        assertEquals("STK", ef.m_secType);
        assertEquals("NYSE", ef.m_exchange);
        assertEquals("BUY", ef.m_side);
    }
    
    @Test
    public void testEqualsSameObject() {
        ExecutionFilter ef = new ExecutionFilter();
        
        assertTrue(ef.equals(ef));
    }
    
    @Test
    public void testEqualsNullObject() {
        ExecutionFilter ef1 = new ExecutionFilter();
        
        assertFalse(ef1.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        ExecutionFilter ef1 = new ExecutionFilter();
        String str = "Not ExecutionFilter";
        
        assertFalse(ef1.equals(str));
    }
    
    @Test
    public void testEqualsDifferentClientId() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(2, "ACCT", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        
        assertFalse(ef1.equals(ef2));
    }
    
    @Test
    public void testEqualsDifferentAcctCode() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(1, "OTHER", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        
        assertFalse(ef1.equals(ef2));
    }
    
    @Test
    public void testEqualsDifferentTime() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(1, "ACCT", "2020-01-02", "AAPL", "STK", "NYSE", "BUY");
        
        assertFalse(ef1.equals(ef2));
    }
    
    @Test
    public void testEqualsDifferentSymbol() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(1, "ACCT", "2020-01-01", "GOOG", "STK", "NYSE", "BUY");
        
        assertFalse(ef1.equals(ef2));
    }
    
    @Test
    public void testEqualsDifferentSecType() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "OPT", "NYSE", "BUY");
        
        assertFalse(ef1.equals(ef2));
    }
    
    @Test
    public void testEqualsDifferentExchange() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        
        assertFalse(ef1.equals(ef2));
    }
    
    @Test
    public void testEqualsDifferentSide() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "NYSE", "SELL");
        
        assertFalse(ef1.equals(ef2));
    }
    
    @Test
    public void testEqualsSameAttributes() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        
        assertTrue(ef1.equals(ef2));
    }
}