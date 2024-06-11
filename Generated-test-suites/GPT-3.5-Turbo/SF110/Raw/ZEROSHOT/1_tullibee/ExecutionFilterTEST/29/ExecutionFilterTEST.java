import org.junit.Test;

import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testEquals_nullObject_ReturnsFalse() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(null));
    }

    @Test
    public void testEquals_sameObject_ReturnsTrue() {
        ExecutionFilter filter = new ExecutionFilter();
        assertTrue(filter.equals(filter));
    }

    @Test
    public void testEquals_differentClass_ReturnsFalse() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(new Object()));
    }

    @Test
    public void testEquals_equalValues_ReturnsTrue() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "time", "symbol", "secType", "exchange", "side");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testEquals_differentValues_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT2", "time2", "symbol2", "secType2", "exchange2", "side2");
        assertFalse(filter1.equals(filter2));
    }
}