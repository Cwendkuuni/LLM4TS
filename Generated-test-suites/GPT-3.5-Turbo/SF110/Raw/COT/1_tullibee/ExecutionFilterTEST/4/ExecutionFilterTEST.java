import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = new ExecutionFilter();
        ExecutionFilter filter3 = new ExecutionFilter(1, "ACCT", "TIME", "SYMBOL", "SEC_TYPE", "EXCHANGE", "SIDE");

        // Test equal objects
        Assert.assertEquals(filter1, filter2);

        // Test not equal objects
        Assert.assertNotEquals(filter1, filter3);
        Assert.assertNotEquals(filter2, filter3);

        // Test same object
        Assert.assertEquals(filter1, filter1);

        // Test null object
        Assert.assertNotEquals(filter1, null);

        // Test null values in constructor
        ExecutionFilter filter4 = new ExecutionFilter(1, null, null, null, null, null, null);
        Assert.assertNotEquals(filter3, filter4);

        // Test case-insensitivity
        ExecutionFilter filter5 = new ExecutionFilter(1, "acct", "time", "symbol", "sec_type", "exchange", "side");
        Assert.assertEquals(filter3, filter5);
    }
}