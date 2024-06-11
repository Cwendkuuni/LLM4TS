import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExecutionFilterTest {

    @Test
    public void testConstructorWithParameters() {
        ExecutionFilter ef = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertEquals(1, ef.m_clientId);
        assertEquals("ACCT001", ef.m_acctCode);
        assertEquals("2022-01-01", ef.m_time);
        assertEquals("AAPL", ef.m_symbol);
        assertEquals("STK", ef.m_secType);
        assertEquals("NASDAQ", ef.m_exchange);
        assertEquals("BUY", ef.m_side);
    }

    @Test
    public void testConstructorWithoutParameters() {
        ExecutionFilter ef = new ExecutionFilter();
        assertEquals(0, ef.m_clientId);
    }

    @Test
    public void testEqualsWithNull() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter ef2 = null;
        assertEquals(false, ef1.equals(ef2));
    }

    @Test
    public void testEqualsWithSameObject() {
        ExecutionFilter ef = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertEquals(true, ef.equals(ef));
    }

    @Test
    public void testEqualsWithDifferentType() {
        ExecutionFilter ef = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        String other = "Not an ExecutionFilter object";
        assertEquals(false, ef.equals(other));
    }

    @Test
    public void testEqualsWithDifferentClientId() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(2, "ACCT001", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertEquals(false, ef1.equals(ef2));
    }

    @Test
    public void testEqualsWithDifferentAccountCode() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(1, "ACCT002", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertEquals(false, ef1.equals(ef2));
    }

    @Test
    public void testEqualsWithDifferentTime() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(1, "ACCT001", "2022-01-02", "AAPL", "STK", "NASDAQ", "BUY");
        assertEquals(false, ef1.equals(ef2));
    }

    @Test
    public void testEqualsWithDifferentSymbol() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "IBM", "STK", "NASDAQ", "BUY");
        assertEquals(false, ef1.equals(ef2));
    }

    @Test
    public void testEqualsWithDifferentSecType() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "OPT", "NASDAQ", "BUY");
        assertEquals(false, ef1.equals(ef2));
    }

    @Test
    public void testEqualsWithDifferentExchange() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STK", "NYSE", "BUY");
        assertEquals(false, ef1.equals(ef2));
    }

    @Test
    public void testEqualsWithDifferentSide() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STK", "NASDAQ", "SELL");
        assertEquals(false, ef1.equals(ef2));
    }

    @Test
    public void testEqualsWithEqualParameters() {
        ExecutionFilter ef1 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter ef2 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertEquals(true, ef1.equals(ef2));
    }
}