import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    private ExecutionFilter filter;

    /**
     * Test setup, executed before all tests.
     */
    @Before
    public void setup() {
        filter = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
    }

    /**
     * Test case for the default constructor.
     * Verify that the default client ID is set to zero.
     */
    @Test
    public void testDefaultConstructor() {
        ExecutionFilter defaultFilter = new ExecutionFilter();
        assertEquals(0, defaultFilter.m_clientId);
    }

    /**
     * Test case for the parameterized constructor.
     * Verify that the object is correctly initialized with provided values.
     */
    @Test
    public void testParameterizedConstructor() {
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT123", filter.m_acctCode);
        assertEquals("2022-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NASDAQ", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    /**
     * Test case for the equals() method.
     * Verify that two ExecutionFilter objects with the same values are considered equal.
     */
    @Test
    public void testEquals() {
        ExecutionFilter sameFilter = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter differentFilter = new ExecutionFilter(2, "ACCT456", "2022-02-02", "GOOGL", "STOCK", "NYSE", "SELL");

        assertTrue(filter.equals(sameFilter));
        assertFalse(filter.equals(differentFilter));
    }

    /**
     * Test case for the equals() method.
     * Verify that an ExecutionFilter object is not equal to null or another type of object.
     */
    @Test
    public void testEquals_NullAndDifferentType() {
        assertFalse(filter.equals(null));
        assertFalse(filter.equals("not_exec_filter"));
    }

}