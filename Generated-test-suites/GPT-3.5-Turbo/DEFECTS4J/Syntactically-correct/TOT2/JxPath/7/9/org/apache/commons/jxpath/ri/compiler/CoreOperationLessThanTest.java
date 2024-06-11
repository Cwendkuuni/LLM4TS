import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        // Typical case
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Edge case
        CoreOperationLessThan operation2 = new CoreOperationLessThan(new Constant(10), new Constant(5));
        assertEquals(Boolean.FALSE, operation2.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        // Typical case
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(5), new Constant(10));
        assertEquals("<", operation.getSymbol());

        // Edge case
        CoreOperationLessThan operation2 = new CoreOperationLessThan(new Constant(10), new Constant(5));
        assertEquals("<", operation2.getSymbol());
    }
}