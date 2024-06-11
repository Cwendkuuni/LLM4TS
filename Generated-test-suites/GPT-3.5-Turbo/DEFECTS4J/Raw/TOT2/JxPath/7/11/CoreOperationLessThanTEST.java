import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext(null);
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);

        operation = new CoreOperationLessThan(new Constant(10), new Constant(5));
        context = new EvalContext(null);
        result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(5), new Constant(10));
        String symbol = operation.getSymbol();
        assertEquals("<", symbol);
    }
}