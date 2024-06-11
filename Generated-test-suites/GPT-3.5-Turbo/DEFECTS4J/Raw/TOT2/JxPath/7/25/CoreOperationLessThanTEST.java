import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValueLessThan() {
        CoreOperationLessThan coreOperation = new CoreOperationLessThan(new Literal(5), new Literal(10));
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueNotLessThan() {
        CoreOperationLessThan coreOperation = new CoreOperationLessThan(new Literal(10), new Literal(5));
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan coreOperation = new CoreOperationLessThan(new Literal(5), new Literal(10));
        assertEquals("<", coreOperation.getSymbol());
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueNullContext() {
        CoreOperationLessThan coreOperation = new CoreOperationLessThan(new Literal(5), new Literal(10));
        coreOperation.computeValue(null);
    }

    @Test(expected = ArithmeticException.class)
    public void testComputeValueInvalidArguments() {
        CoreOperationLessThan coreOperation = new CoreOperationLessThan(new Literal("a"), new Literal("b"));
        EvalContext context = new EvalContext();
        coreOperation.computeValue(context);
    }
}