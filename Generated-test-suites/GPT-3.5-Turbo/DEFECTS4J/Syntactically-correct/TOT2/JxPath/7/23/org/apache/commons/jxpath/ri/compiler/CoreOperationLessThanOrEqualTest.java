import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(2), new Constant(3));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);

        operation = new CoreOperationLessThanOrEqual(new Constant(3), new Constant(2));
        result = operation.computeValue(context);
        assertFalse((Boolean) result);

        operation = new CoreOperationLessThanOrEqual(new Constant(2), new Constant(2));
        result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(2), new Constant(3));
        assertEquals("<=", operation.getSymbol());
    }

    @Test(expected = Exception.class)
    public void testComputeValueWithNullArg() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(null, new Constant(3));
        EvalContext context = new EvalContext();
        // This should throw an exception
        operation.computeValue(context);
    }

}