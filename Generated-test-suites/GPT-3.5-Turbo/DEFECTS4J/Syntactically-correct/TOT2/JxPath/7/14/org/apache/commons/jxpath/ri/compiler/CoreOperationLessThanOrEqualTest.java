import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(new Constant(5), new Constant(7));
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(new Constant(5), new Constant(5));
        assertEquals("<=", coreOperation.getSymbol());
    }
}