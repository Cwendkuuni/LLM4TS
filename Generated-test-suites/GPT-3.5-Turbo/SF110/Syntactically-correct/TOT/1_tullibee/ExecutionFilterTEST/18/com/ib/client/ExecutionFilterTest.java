import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    private ExecutionFilter filter;

    @Before
    public void setUp() {
        filter = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
    }

    @Test
    public void testDefaultConstructor() {
        ExecutionFilter defaultFilter = new ExecutionFilter();
        assertEquals(0, defaultFilter.m_clientId);
        assertNull(defaultFilter.m_acctCode);
        assertNull(defaultFilter.m_time);
        assertNull(defaultFilter.m_symbol);
        assertNull(defaultFilter.m_secType);
        assertNull(defaultFilter.m_exchange);
        assertNull(defaultFilter.m_side);
    }

    @Test
    public void testParameterizedConstructor() {
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT123", filter.m_acctCode);
        assertEquals("2020-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(filter.equals(filter));
    }

    @Test
    public void testEqualsNullObject() {
        assertFalse(filter.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(filter.equals(""));
    }

    @Test
    public void testEqualsDifferentClientId() {
        ExecutionFilter diffFilter = new ExecutionFilter(2, "ACCT123", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter.equals(diffFilter));
    }

    @Test
    public void testEqualsIgnoreCaseAccCode() {
        ExecutionFilter diffFilter = new ExecutionFilter(1, "acct123", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter.equals(diffFilter));
    }

    @Test
    public void testEqualsIgnoreCaseTime() {
        ExecutionFilter diffFilter = new ExecutionFilter(1, "ACCT123", "2020-01-02", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter.equals(diffFilter));
    }

    @Test
    public void testEqualsIgnoreCaseSymbol() {
        ExecutionFilter diffFilter = new ExecutionFilter(1, "ACCT123", "2020-01-01", "aapl", "STOCK", "NYSE", "BUY");
        assertTrue(filter.equals(diffFilter));
    }
    
    @Test
    public void testEqualsIgnoreCaseSecType() {
        ExecutionFilter diffFilter = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "stock", "NYSE", "BUY");
        assertTrue(filter.equals(diffFilter));
    }
    
    @Test
    public void testEqualsIgnoreCaseExchange() {
        ExecutionFilter diffFilter = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "STOCK", "nyse", "BUY");
        assertTrue(filter.equals(diffFilter));
    }
    
    @Test
    public void testEqualsIgnoreCaseSide() {
        ExecutionFilter diffFilter = new ExecutionFilter(1, "ACCT123", "2020-01-01", "AAPL", "STOCK", "NYSE", "buy");
        assertTrue(filter.equals(diffFilter));
    }

}