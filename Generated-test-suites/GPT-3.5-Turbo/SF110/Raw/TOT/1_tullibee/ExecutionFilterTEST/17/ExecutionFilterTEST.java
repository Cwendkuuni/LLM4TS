import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testDefaultConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertSame(0, filter.m_clientId);
        assertNull(filter.m_acctCode);
        assertNull(filter.m_time);
        assertNull(filter.m_symbol);
        assertNull(filter.m_secType);
        assertNull(filter.m_exchange);
        assertNull(filter.m_side);
    }

    @Test
    public void testParameterizedConstructor() {
        int clientId = 123;
        String acctCode = "ACCT123";
        String time = "12:00";
        String symbol = "ABC";
        String secType = "STOCK";
        String exchange = "NYSE";
        String side = "BUY";
        ExecutionFilter filter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
        assertSame(clientId, filter.m_clientId);
        assertSame(acctCode, filter.m_acctCode);
        assertSame(time, filter.m_time);
        assertSame(symbol, filter.m_symbol);
        assertSame(secType, filter.m_secType);
        assertSame(exchange, filter.m_exchange);
        assertSame(side, filter.m_side);
    }

    @Test
    public void testEqualsSameObject() {
        ExecutionFilter filter = new ExecutionFilter();
        assertTrue(filter.equals(filter));
    }

    @Test
    public void testEqualsNullObject() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals("Not a filter object"));
    }

    @Test
    public void testEqualsEqualFilterObject() {
        ExecutionFilter filter1 = new ExecutionFilter(123, "ACCT123", "12:00", "ABC", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(123, "ACCT123", "12:00", "ABC", "STOCK", "NYSE", "BUY");
        assertTrue(filter1.equals(filter2));
        assertTrue(filter2.equals(filter1));
    }

    @Test
    public void testEqualsDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(123, "ACCT123", "12:00", "ABC", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(456, "ACCT123", "12:00", "ABC", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
        assertFalse(filter2.equals(filter1));
    }

    @Test
    public void testEqualsDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(123, "ACCT123", "12:00", "ABC", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(123, "ACCT456", "12:00", "ABC", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
        assertFalse(filter2.equals(filter1));
    }

    @Test
    public void testEqualsDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(123, "ACCT123", "12:00", "ABC", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(123, "ACCT123", "13:00", "ABC", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
        assertFalse(filter2.equals(filter1));
    }

    @Test
    public void testEqualsDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(123, "ACCT123", "12:00", "ABC", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(123, "ACCT123", "12:00", "XYZ", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
        assertFalse(filter2.equals(filter1));
    }

    @Test
    public void testEqualsDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(123, "ACCT123", "12:00", "ABC", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(123, "ACCT123", "12:00", "ABC", "OPTION", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
        assertFalse(filter2.equals(filter1));
    }

    @Test
    public void testEqualsDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(123, "ACCT123", "12:00", "ABC", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(123, "ACCT123", "12:00", "ABC", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
        assertFalse(filter2.equals(filter1));
    }

    @Test
    public void testEqualsDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(123, "ACCT123", "12:00", "ABC", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(123, "ACCT123", "12:00", "ABC", "STOCK", "NYSE", "SELL");
        assertFalse(filter1.equals(filter2));
        assertFalse(filter2.equals(filter1));
    }

}