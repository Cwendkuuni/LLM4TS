import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExecutionFilterTest {
    
    @Test
    public void testConstructorWithParameters() {
        ExecutionFilter filter = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        
        assertEquals(1, filter.m_clientId);
        assertEquals("acctCode", filter.m_acctCode);
        assertEquals("time", filter.m_time);
        assertEquals("symbol", filter.m_symbol);
        assertEquals("secType", filter.m_secType);
        assertEquals("exchange", filter.m_exchange);
        assertEquals("side", filter.m_side);
    }
    
    @Test
    public void testConstructorWithoutParameters() {
        ExecutionFilter filter = new ExecutionFilter();
        
        assertEquals(0, filter.m_clientId);
    }
    
    @Test
    public void testEqualsForEqualObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        
        assertEquals(true, filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsForDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter2 = new ExecutionFilter(2, "acctCode", "time", "symbol", "secType", "exchange", "side");
        
        assertEquals(false, filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsForDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter2 = new ExecutionFilter(1, "differentAcctCode", "time", "symbol", "secType", "exchange", "side");
        
        assertEquals(false, filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsForDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode", "differentTime", "symbol", "secType", "exchange", "side");
        
        assertEquals(false, filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsForDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode", "time", "differentSymbol", "secType", "exchange", "side");
        
        assertEquals(false, filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsForDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode", "time", "symbol", "differentSecType", "exchange", "side");
        
        assertEquals(false, filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsForDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "differentExchange", "side");
        
        assertEquals(false, filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsForDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "differentSide");
        
        assertEquals(false, filter1.equals(filter2));
    }
}