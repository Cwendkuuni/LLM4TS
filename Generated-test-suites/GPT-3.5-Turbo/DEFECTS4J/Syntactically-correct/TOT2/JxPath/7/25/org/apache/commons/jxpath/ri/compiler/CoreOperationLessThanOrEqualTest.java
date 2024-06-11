import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);

        assertNotNull(operation);
    }

    @Test
    public void testComputeValueLessThanOrEqual() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContextImpl();

        Object result = operation.computeValue(context);

        assertEquals(true, result);
    }

    @Test
    public void testComputeValueNotLessThanOrEqual() {
        Expression arg1 = new Constant(10);
        Expression arg2 = new Constant(5);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContextImpl();

        Object result = operation.computeValue(context);

        assertEquals(false, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(null, null);

        String symbol = operation.getSymbol();

        assertEquals("<=", symbol);
    }
}