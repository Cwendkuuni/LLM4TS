import static org.junit.Assert.*;
import org.junit.Test;

public class ExecutionFilterTest {
    
    @Test
    public void testEquals_NullInput_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = null;
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:00", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertTrue(filter.equals(filter));
    }
    
    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:00", "AAPL", "STOCK", "NYSE", "BUY");
        String otherObject = "This is a string";
        
        assertFalse(filter.equals(otherObject));
    }
    
    @Test
    public void testEquals_EqualAttributes_ReturnsTrue() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:00", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertTrue(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_DifferentClientId_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT1", "2022-01-01 12:00", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_DifferentAcctCode_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT2", "2022-01-01 12:00", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_DifferentTime_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-02 12:00", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_DifferentSymbol_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:00", "GOOG", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_DifferentSecType_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:00", "AAPL", "OPTION", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_DifferentExchange_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_DifferentSide_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2022-01-01 12:00", "AAPL", "STOCK", "NYSE", "SELL");
        
        assertFalse(filter1.equals(filter2));
    }
}