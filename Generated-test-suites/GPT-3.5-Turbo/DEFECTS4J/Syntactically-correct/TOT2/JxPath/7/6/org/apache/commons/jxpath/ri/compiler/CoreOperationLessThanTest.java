import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        // Basic test case
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, lessThan.computeValue(context));

        // Edge case: negative numbers
        CoreOperationLessThan lessThan2 = new CoreOperationLessThan(new Constant(-15), new Constant(-10));
        assertEquals(Boolean.TRUE, lessThan2.computeValue(context));

        // Edge case: floating point numbers
        CoreOperationLessThan lessThan3 = new CoreOperationLessThan(new Constant(5.5), new Constant(3.6));
        assertEquals(Boolean.FALSE, lessThan3.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(5), new Constant(10));
        assertEquals("<", lessThan.getSymbol());
    }

    @Test
    public void testConstructor() {
        // To test the constructor, we can try passing null values for arguments
        CoreOperationLessThan lessThan = new CoreOperationLessThan(null, null);
        assertNotNull(lessThan);
    }
}