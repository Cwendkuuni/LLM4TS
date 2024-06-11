import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(null, null);

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());

        // Test with left value less than right value
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Boolean result = operation.computeValue(context);
        assertEquals(false, result);

        // Test with left value equal to right value
        arg1 = new Constant(10);
        arg2 = new Constant(10);
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertEquals(true, result);

        // Test with left value greater than right value
        arg1 = new Constant(15);
        arg2 = new Constant(10);
        operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertEquals(true, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        assertEquals("<=", symbol);
    }
}