import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext(null);
        assertEquals(true, lessThan.computeValue(context)); // 5 < 10 should return true
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(5), new Constant(10));
        assertEquals("<", lessThan.getSymbol());
    }

    @Test
    public void testComputeValueEdgeCase() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(10), new Constant(5));
        EvalContext context = new EvalContext(null);
        assertEquals(false, lessThan.computeValue(context)); // 10 < 5 should return false
    }

    @Test
    public void testComputeValueEqualValues() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(5), new Constant(5));
        EvalContext context = new EvalContext(null);
        assertEquals(false, lessThan.computeValue(context)); // 5 < 5 should return false
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueNullContext() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Constant(5), new Constant(10));
        lessThan.computeValue(null); // Passing null context should throw NullPointerException
    }

}