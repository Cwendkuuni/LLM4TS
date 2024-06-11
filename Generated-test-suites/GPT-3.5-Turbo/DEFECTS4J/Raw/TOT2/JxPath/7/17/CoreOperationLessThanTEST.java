import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue_lessThan() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((boolean) result);
    }

    @Test
    public void testComputeValue_notLessThan() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(10), new Constant(5));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertFalse((boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(5), new Constant(10));
        assertEquals("<", operation.getSymbol());
    }

    @Test
    public void testComputeValue_edgeCaseNegativeNumbers() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(-5), new Constant(-10));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertFalse((boolean) result);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValue_nullContext() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(5), new Constant(10));
        operation.computeValue(null);
    }

}